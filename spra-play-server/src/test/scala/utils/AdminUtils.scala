package utils

import net.wiringbits.spra.api.AdminDataExplorerApiClient
import net.wiringbits.spra.api.models.AdminCreateTable

import scala.concurrent.{ExecutionContext, Future}

trait AdminUtils {
  case class User(userId: String, name: String, email: String)

  case class UserLog(userLogId: String, userId: String, message: String)

  def createUser(using ec: ExecutionContext, client: AdminDataExplorerApiClient): Future[User] = {
    val name = "wiringbits"
    val email = "test@wiringbits.net"
    val password = "wiringbits"
    val request = AdminCreateTable.Request(Map("name" -> name, "email" -> email, "password" -> password))
    client.createItem("users", request).map(response => User(response.id, name, email))
  }

  def createUserLog(userId: String)(using ec: ExecutionContext, client: AdminDataExplorerApiClient): Future[UserLog] = {
    val message = "test"
    val request = AdminCreateTable.Request(Map("user_id" -> userId, "message" -> message))
    client.createItem("user_logs", request).map(response => UserLog(response.id, userId, message))
  }
}
