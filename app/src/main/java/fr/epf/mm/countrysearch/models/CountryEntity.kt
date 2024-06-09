package fr.epf.mm.countrysearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val capital: String,
    val region: String,
    val flag: String,
    val population: Long,
    val language: String,
    val currency: String
) {
    fun toCountry(): Country {
        return Country(
            flag = this.flag,
            name = this.name,
            capital = this.capital,
            region = this.region,
            population = this.population,
            language = this.language,
            currency = this.currency
        )
    }
}