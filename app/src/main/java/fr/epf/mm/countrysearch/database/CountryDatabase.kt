package fr.epf.mm.countrysearch.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.epf.mm.countrysearch.dao.CountryDao
import fr.epf.mm.countrysearch.models.CountryEntity

@Database(entities = [CountryEntity::class], version = 2)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}