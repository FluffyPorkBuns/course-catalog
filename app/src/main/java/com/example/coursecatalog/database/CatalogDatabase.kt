package com.example.coursecatalog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// database that stores information for the course catalog app
@Database(entities = [TermEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CatalogDatabase: RoomDatabase() {

    // connect database to DAO class
    abstract val catalogDatabaseDao: CatalogDatabaseDao

    // companion object allows the addition of functions to database class
    companion object {

        // volatile declaration helps make sure database is only initialized once
        @Volatile
        private var INSTANCE: CatalogDatabase? = null

        /**
         * threadsafe singleton pattern for ensuring only one instance of
         * the database is created and used
         */
        fun getInstance(context: Context): CatalogDatabase {
            synchronized(this) {

                // local variable of INSTANCE
                var instance = INSTANCE

                // creates a database if there is no existing database created
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CatalogDatabase::class.java,
                        "course_catalog_database"
                    )
                        /**
                         * erases and rebuilds database whenever a new version
                         * is made
                         */
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()

                    // assign instance of database to INSTANCE
                    INSTANCE = instance
                }
                // return instance of database
                return instance
            }
        }
    }
}