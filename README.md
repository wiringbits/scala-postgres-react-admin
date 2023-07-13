# scala-postgres-react-admin
SPRA exposes your Postgres database through a nice React-Admin UI

## How to run
- Add an AbstractModule that provides a DataExplorerSettings to your PlayFramework application, for example:
```scala
class DataExplorerModule extends AbstractModule {

  @Provides()
  def dataExplorerSettings: DataExplorerSettings = DataExplorerSettings(settings)

  val settings = List(
    TableSettings(
      tableName = "users",
      primaryKeyField = "user_id",
      hiddenColumns = List("password", "email"),
      nonEditableColumns = List("user_id", "email", "created_at", "verified_on", "name"),
      canBeDeleted = false,
      filterableColumns = List("name", "last_name")
    )
  )
} 
```
- Add the `AppRouter` routes to your `routes` file:
```scala
-> / net.wiringbits.webapp.utils.admin.AppRouter
```
- Run the PlayFramework application
- Run `sbt spra-dev` to start the SPRA web
