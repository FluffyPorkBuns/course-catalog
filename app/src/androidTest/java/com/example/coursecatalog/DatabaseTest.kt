package com.example.coursecatalog

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.database.TermEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var catalogDatabaseDao: CatalogDatabaseDao
    private lateinit var catalogDatabase: CatalogDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        catalogDatabase = Room.inMemoryDatabaseBuilder(context, CatalogDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        catalogDatabaseDao = catalogDatabase.catalogDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        if (::catalogDatabase.isInitialized) {
            catalogDatabase.close()
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTerm() {
        val term = TermEntity(1, "sample term", Date(0), Date(0))
        catalogDatabaseDao.insert(term)
        val newTermEntity = catalogDatabaseDao.getNewestTerm()
        assertEquals(newTermEntity?.termTitle, "sample term")
    }

}