package net.wiringbits.spra.admin.repositories.models

import net.wiringbits.spra.admin.models.ColumnType

case class TableColumn(
    name: String,
    `type`: ColumnType
)
