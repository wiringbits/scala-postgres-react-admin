package net.wiringbits.spra.ui.web.utils

import net.wiringbits.spra.api.models.AdminGetTables
import net.wiringbits.spra.ui.web.models.{Column, ColumnType}

object ResponseGuesser {
  def getTypesFromResponse(response: AdminGetTables.Response.DatabaseTable): List[Column] = {
    response.columns.map { column =>
      val fieldType = ColumnType.fromTableField(column)
      Column(name = column.name, `type` = fieldType, disabled = !column.editable, filterable = column.filterable)
    }
  }
}
