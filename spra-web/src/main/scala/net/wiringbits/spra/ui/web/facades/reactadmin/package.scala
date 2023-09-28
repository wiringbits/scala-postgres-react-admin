package net.wiringbits.spra.ui.web.facades

import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

package object reactadmin {
  @JSImport("react-admin", JSImport.Namespace)
  @js.native
  object ReactAdmin extends js.Object {
    def useEditContext(): js.Dictionary[js.Any] = js.native

    def required(): js.Any = js.native

    val Admin, Resource, EditGuesser, ListGuesser, TextInput, ImageField, NumberInput, DateTimeInput, ReferenceInput,
        SelectInput, Button, DeleteButton, SaveButton, TopToolbar, Toolbar, Edit, SimpleForm, DateField, TextField,
        EmailField, NumberField, ReferenceField, DateInput, FilterButton, ExportButton, List, Datagrid, Create,
        CreateButton: js.Object =
      js.native
  }

  def temp(ls: Seq[ReactElement]): Seq[ReactElement] = ls flatMap {
    case i: Seq[ReactElement] => temp(i)
    case x => Seq(x)
  }
}
