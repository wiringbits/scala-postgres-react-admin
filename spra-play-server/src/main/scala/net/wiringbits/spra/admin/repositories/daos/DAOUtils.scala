package net.wiringbits.spra.admin.repositories.daos

import java.util.UUID

object DAOUtils {
  def validateIsUUID(uuid: String): Option[UUID] = {
    val uuidRegex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$".r
    uuid match {
      case uuidRegex() => Some(UUID.fromString(uuid))
      case _ => None
    }
  }
}
