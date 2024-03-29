package net.wiringbits.spra.admin

import net.wiringbits.spra.admin.repositories.models.TableColumn
import net.wiringbits.spra.admin.utils.QueryBuilder
import org.scalatest.matchers.must.Matchers.{be, must}
import org.scalatest.wordspec.AnyWordSpec

class QueryBuilderSpec extends AnyWordSpec {

  "create" should {
    "create a complete SQL query" in {
      val expected =
        """
          |INSERT INTO users
          |  (user_id, email, name)
          |VALUES (
          |  ?, ?::citext, ?::text
          |)
          |RETURNING user_id::TEXT
          |""".stripMargin
      val tableName = "users"
      val body =
        Map(TableColumn("email", "citext") -> "wiringbits@wiringbits.net", TableColumn("name", "text") -> "wiringbits")
      val primaryKeyField = "user_id"

      val response = QueryBuilder.create(tableName, body, primaryKeyField)
      println(response)
      println(expected)
      response must be(expected)
    }

    "insert a empty body" in {
      val expected =
        """
          |INSERT INTO users
          |  (user_id)
          |VALUES (
          |  ?
          |)
          |RETURNING user_id::TEXT
          |""".stripMargin
      val tableName = "users"
      val body = Map.empty[TableColumn, String]
      val primaryKeyField = "user_id"

      val response = QueryBuilder.create(tableName, body, primaryKeyField)
      response must be(expected)
    }
  }

  "update" should {
    "build a SQL query" in {
      val expected =
        s"""
      |UPDATE users
      |SET email = ?::citext, name = ?::text
      |WHERE user_id = ?
      |""".stripMargin
      val tableName = "users"
      val body = Map(
        TableColumn("email", "citext") -> "wiringbits@wiringbits.net",
        TableColumn("name", "text") -> "wiringbits@wiringbits.net"
      )
      val primaryKeyField = "user_id"

      val response = QueryBuilder.update(tableName, body, primaryKeyField)
      response must be(expected)
    }

    "build a SQL query with null fields" in {
      val expected =
        s"""
           |UPDATE users
           |SET email = ?::citext, name = ?::text, phone_number = NULL
           |WHERE user_id = ?
           |""".stripMargin
      val tableName = "users"
      val body = Map(
        TableColumn("email", "citext") -> "wiringbits@wiringbits.net",
        TableColumn("name", "text") -> "wiringbits@wiringbits.net",
        TableColumn("phone_number", "text") -> "null"
      )
      val primaryKeyField = "user_id"

      val response = QueryBuilder.update(tableName, body, primaryKeyField)
      response must be(expected)
    }
  }
}
