package net.wiringbits.spra.admin.models

sealed trait FieldValue[T] {
  val value: T
}

case class StringValue(value: String) extends FieldValue[String]
case class ByteArrayValue(value: Array[Byte]) extends FieldValue[Array[Byte]]
