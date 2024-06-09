package fr.epf.mm.countrysearch.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val flag: String,
    val name: String,
    val capital: String,
    val region: String,
    val population: Long, // new field for population
    val language: String, // new field for language
    val currency: String  // new field for currency
) : Parcelable {
    override fun describeContents() = 0
}