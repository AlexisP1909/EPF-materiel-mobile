package fr.epf.mm.countrysearch.ui.countries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.epf.mm.countrysearch.database.CountryDatabase
import fr.epf.mm.countrysearch.models.Country
import fr.epf.mm.countrysearch.models.CountryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Create a ViewModel that will handle the logic of the CountryDetailsFragment

class CountryDetailsViewModel(private val database: CountryDatabase) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun checkIfCountryIsFavorite(country: Country) {
        viewModelScope.launch {
            val count = database.countryDao().countCountryByName(country.name)
            _isFavorite.postValue(count > 0)
        }
    }

    fun toggleFavoriteStatus(country: Country) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                database.countryDao().deleteCountryByName(country.name)
            } else {
                val countryEntity = CountryEntity(
                    id = 0, // Room will generate the id
                    name = country.name,
                    capital = country.capital,
                    region = country.region,
                    flag = country.flag,
                    population = country.population,
                    language = country.language,
                    currency = country.currency
                )
                database.countryDao().insert(countryEntity)
            }
            checkIfCountryIsFavorite(country)
        }
    }
}