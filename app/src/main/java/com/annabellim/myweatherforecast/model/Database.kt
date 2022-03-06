package com.annabellim.myweatherforecast.model

import android.content.Context
import androidx.room.*

// Use Room to manage SQLite database

@Entity(tableName = "ForecastTable")
data class ForecastTable(
    @PrimaryKey val uid: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val country: String,
    @ColumnInfo val dt: Int,
    @ColumnInfo val id: Int,
    @ColumnInfo val main: String,
    @ColumnInfo val description: String,
    @ColumnInfo val sunrise: Int,
    @ColumnInfo val sunset: Int,
    @ColumnInfo val day: Double,
    @ColumnInfo val night: Double,
    @ColumnInfo val eve: Double,
    @ColumnInfo val morn: Double,
    @ColumnInfo val min: Double,
    @ColumnInfo val max: Double,
    @ColumnInfo val pressure: Int,
    @ColumnInfo val humidity: Int,
    @ColumnInfo val speed: Double,
    @ColumnInfo val deg: Int,
    @ColumnInfo val gust: Double,
    @ColumnInfo val clouds: Int
)

@Dao
interface ForecastDao {

    @Query("SELECT * FROM ForecastTable ORDER  BY dt ASC")
    fun getForecasts(): List<ForecastTable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(forecastTable: ForecastTable)

    @Query("DELETE FROM ForecastTable")
    fun deleteAll()

}

@Database(entities = [ForecastTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDatabase").allowMainThreadQueries().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
