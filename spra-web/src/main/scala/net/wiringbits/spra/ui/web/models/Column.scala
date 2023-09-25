package net.wiringbits.spra.ui.web.models

case class Column(
    name: String,
    `type`: ColumnType,
    disabled: Boolean,
    filterable: Boolean,
    isVisible: Boolean,
    isRequiredOnCreate: Option[Boolean]
)
