package fr.epf.mm.countrysearch.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val flag: String,
    val name: String,
    val capital: String,
    val region: String
) : Parcelable {
    override fun describeContents() = 0
}