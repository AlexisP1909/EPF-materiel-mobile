package fr.epf.mm.countrysearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val capital: String,
    val region: String,
    val flag: String,
    val population: Long,
    val languages: String,
    val currencies: String
) {
    fun toCountry(): Country {
        val gson = Gson()
        val languagesListType = object : TypeToken<List<Language>>() {}.type
        val languagesList = gson.fromJson<List<Language>>(languages, languagesListType)
        val currenciesListType = object : TypeToken<List<Currency>>() {}.type
        val currenciesList = gson.fromJson<List<Currency>>(currencies, currenciesListType)
        return Country(
            flag = this.flag,
            name = this.name,
            capital = this.capital,
            region = this.region,
            population = this.population,
            languages = languagesList,
            currencies = currenciesList
        )
    }
}