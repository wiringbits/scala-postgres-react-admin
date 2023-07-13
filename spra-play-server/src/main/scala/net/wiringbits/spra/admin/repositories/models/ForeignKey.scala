package net.wiringbits.spra.admin.repositories.models

case class ForeignKey(foreignTable: String, primaryTable: String, foreignColumnName: String)
