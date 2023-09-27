package net.wiringbits.spra.admin.repositories.daos

import java.util.UUID
import scala.util.Try

object DAOUtils {
  def validateUUID(uuid: String): Option[UUID] = {
    Try(UUID.fromString(uuid)).toOption
  }
}
