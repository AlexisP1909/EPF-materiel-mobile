package fr.epf.mm.countrysearch.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val flag: String,
    val name: String,
    val capital: String,
    val region: String,
    val population: Long,
    val languages: List<Language>,
    val currencies: List<Currency>
) : Parcelable {
    override fun describeContents() = 0
}

@Parcelize
data class Currency(
    val code: String,
    val name: String,
    val symbol: String
) : Parcelable

@Parcelize
data class Language(
    val iso639_1: String,
    val iso639_2: String,
    val name: String,
    val nativeName: String
) : Parcelable