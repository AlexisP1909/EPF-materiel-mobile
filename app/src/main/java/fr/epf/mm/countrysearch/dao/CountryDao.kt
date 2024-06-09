package fr.epf.mm.countrysearch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.epf.mm.countrysearch.models.CountryEntity

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    suspend fun getAll(): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: CountryEntity)

    @Query("SELECT COUNT(*) FROM countries WHERE name = :countryName")
    suspend fun countCountryByName(countryName: String): Int

    @Query("DELETE FROM countries WHERE name = :countryName")
    suspend fun deleteCountryByName(countryName: String)
}