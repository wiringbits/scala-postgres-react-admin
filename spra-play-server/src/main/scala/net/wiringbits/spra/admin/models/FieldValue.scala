package net.wiringbits.spra.admin.models

trait FieldValue extends Serializable

case class StringValue(value: String) extends FieldValue
case class ByteArrayValue(value: Array[Byte]) extends FieldValue
