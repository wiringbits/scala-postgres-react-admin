# scala-postgres-react-admin

SPRA exposes your Postgres database through a nice React-Admin UI

## Installation

Add the following dependency to your `build.sbt` file:

```scala
"net.wiringbits" %% "scala-postgres-react-admin" % "VERSION"
```

## How to run

- In your main Play Framework server, add an AbstractModule that provides a DataExplorerSettings, for example:

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

- Add the SPRA `AppRouter` routes to your `routes` file:

```scala
-> / net.wiringbits.spra.admin.AppRouter
```

- Run your Play Framework server application
- Run `sbt spra-dev` to start the SPRA web
