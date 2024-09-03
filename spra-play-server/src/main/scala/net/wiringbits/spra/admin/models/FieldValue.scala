package net.wiringbits.spra.admin.models

trait FieldValue[T] extends Serializable {
  val value: T
}

case class StringValue(value: String) extends FieldValue[String]
case class ByteArrayValue(value: Array[Byte]) extends FieldValue[Array[Byte]]
