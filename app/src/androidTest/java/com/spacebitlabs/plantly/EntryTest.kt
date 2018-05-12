package com.spacebitlabs.plantly

import androidx.sqlite.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.spacebitlabs.plantly.data.EntryType
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.entities.Entry
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.source.EntryDao
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.OffsetDateTime

/**
 * Created by afzal_najam on 2018-03-05.
 */
@RunWith(AndroidJUnit4::class)
class EntryTest {
    private lateinit var db: PlantDatabase
    private lateinit var entryDao: EntryDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java).build()
        entryDao = db.entryDao()

        db.plantDao().insert(Plant(id = 1, name = "Test"))
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun createEntry() {
        val time = OffsetDateTime.now()
        val entry = Entry(type = EntryType.BIRTH, time = time, plantId = 1)
        entryDao.insert(entry)

        val plants = entryDao.getEvents(1)
        val expectedEntry = Entry(id = 1, type = EntryType.BIRTH, time = time, plantId = 1)

        Assert.assertThat(plants[0], Matchers.equalTo(expectedEntry))
    }
}