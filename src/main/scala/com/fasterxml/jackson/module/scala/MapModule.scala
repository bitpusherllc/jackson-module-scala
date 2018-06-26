package com.fasterxml.jackson.module.scala

import com.fasterxml.jackson.module.scala.deser.{SortedMapDeserializerModule, UnsortedMapDeserializerModule}
import com.fasterxml.jackson.module.scala.ser.MapSerializerModule

trait MapModule extends MapSerializerModule with SortedMapDeserializerModule with UnsortedMapDeserializerModule
