package com.fasterxml.jackson.module.scala.deser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.deser.{Deserializers, std}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala.JacksonModule

import scala.languageFeature.postfixOps

object UntypedObjectDeserializer {
  lazy val SEQ = new TypeReference[collection.Seq[Any]] {}
  lazy val MAP = new TypeReference[collection.Map[String,Any]] {}
}

private class UntypedObjectDeserializer extends std.UntypedObjectDeserializer(null, null) {

  private var _mapDeser: JsonDeserializer[AnyRef] = _
  private var _listDeser: JsonDeserializer[AnyRef] = _

  override def resolve(ctxt: DeserializationContext): Unit = {
    super.resolve(ctxt)
    val anyRef = ctxt.constructType(classOf[AnyRef])
    val string = ctxt.constructType(classOf[String])
    val tf = ctxt.getTypeFactory
    _mapDeser = ctxt.findRootValueDeserializer(
      ctxt.getConfig.mapAbstractType(
        tf.constructMapLikeType(classOf[collection.Map[_,_]], string, anyRef))).asInstanceOf[JsonDeserializer[AnyRef]]
    _listDeser = ctxt.findRootValueDeserializer(
      ctxt.getConfig.mapAbstractType(
        tf.constructCollectionLikeType(classOf[collection.Seq[_]], anyRef))).asInstanceOf[JsonDeserializer[AnyRef]]
  }

  override def mapArray(jp: JsonParser, ctxt: DeserializationContext): AnyRef = {
    if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
      mapArrayToArray(jp, ctxt)
    }
    else {
      _listDeser.deserialize(jp, ctxt)
    }
  }

  override def mapObject(jp: JsonParser, ctxt: DeserializationContext): AnyRef = {
    _mapDeser.deserialize(jp, ctxt)
  }
}


private object UntypedObjectDeserializerResolver extends Deserializers.Base {

  private val objectClass = classOf[AnyRef]

  override def findBeanDeserializer(javaType: JavaType,
                                    config: DeserializationConfig,
                                    beanDesc: BeanDescription) =
    if (!objectClass.equals(javaType.getRawClass)) null
    else new UntypedObjectDeserializer
}

trait UntypedObjectDeserializerModule extends JacksonModule {
  this += (_ addDeserializers UntypedObjectDeserializerResolver)
}
