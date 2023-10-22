package dmitry.molchanov.db

import dmitry.molchanov.db.AppDatabase
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.db.SqlDriver

class DriverFactory(private val context: Context) {

    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "test.db")
    }
}