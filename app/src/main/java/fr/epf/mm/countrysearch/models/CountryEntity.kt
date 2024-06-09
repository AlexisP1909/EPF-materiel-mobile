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
    val population: Long, // new field for population
    val language: String, // new field for language
    val currency: String  // new field for currency
) {
    fun toCountry(): Country {
        return Country(
            flag = this.flag,
            name = this.name,
            capital = this.capital,
            region = this.region,
            population = this.population, // map the new field
            language = this.language, // map the new field
            currency = this.currency  // map the new field
        )
    }
}