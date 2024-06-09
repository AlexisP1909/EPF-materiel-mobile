package fr.epf.mm.countrysearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val capital: String,
    val region: String,
    val flag: String
) {
    fun toCountry(): Country {
        return Country(
            name = this.name,
            capital = this.capital,
            region = this.region,
            flag = this.flag
        )
    }
}