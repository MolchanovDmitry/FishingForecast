package dmitry.molchanov.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class DriverFactory(private val context: Context) {

    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "test.db")
    }
}