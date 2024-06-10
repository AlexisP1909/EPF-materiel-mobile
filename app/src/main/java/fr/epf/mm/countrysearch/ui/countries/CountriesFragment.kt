package fr.epf.mm.countrysearch.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.mm.countrysearch.adapters.CountryAdapter
import fr.epf.mm.countrysearch.databinding.FragmentCountriesBinding
import fr.epf.mm.countrysearch.models.Country
import fr.epf.mm.countrysearch.models.Currency
import fr.epf.mm.countrysearch.models.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class CountriesFragment : Fragment() {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryAdapter: CountryAdapter
    private var fullCountryList = listOf<Country>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
        binding.countriesRecyclerView.layoutManager = LinearLayoutManager(context)
        val root: View = binding.root

        // Initialize the RecyclerView and the adapter
        val countries = listOf<Country>()
        countryAdapter = CountryAdapter(countries)
        binding.countriesRecyclerView.adapter = countryAdapter

        // Add a TextWatcher to the SearchView to filter the list of countries
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = fullCountryList.filter { it.name.contains(newText ?: "", ignoreCase = true) }
                countryAdapter.filterList(filteredList)
                return false
            }
        })
        return root
    }

    private fun fetchCountries() {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = "https://www.apicountries.com/countries"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }

                val countriesJsonArray = JSONArray(response)
                val countries = mutableListOf<Country>()
                for (i in 0 until countriesJsonArray.length()) {
                    val countryJsonObject = countriesJsonArray.getJSONObject(i)
                    val country = Country(
                        name = countryJsonObject.getString("name"),
                        capital = if (countryJsonObject.isNull("capital")) "No information" else countryJsonObject.getString("capital"),
                        region = countryJsonObject.getString("region"),
                        flag= countryJsonObject.getString("flag"),
                        population = countryJsonObject.getLong("population"),
                        languages = if (countryJsonObject.isNull("languages")) listOf() else {
                            val languagesJsonArray = countryJsonObject.getJSONArray("languages")
                            val languages = mutableListOf<Language>()
                            for (j in 0 until languagesJsonArray.length()) {
                                val languageJsonObject = languagesJsonArray.getJSONObject(j)
                                val language = Language(
                                    iso639_1 = languageJsonObject.optString("iso639_1", ""),
                                    iso639_2 = languageJsonObject.optString("iso639_2", ""),
                                    name = languageJsonObject.getString("name"),
                                    nativeName = languageJsonObject.optString("nativeName", "name")
                                )
                                languages.add(language)
                            }
                            languages
                        },
                        currencies = if (countryJsonObject.isNull("currencies")) listOf() else {
                            val currenciesJsonArray = countryJsonObject.getJSONArray("currencies")
                            val currencies = mutableListOf<Currency>()
                            for (j in 0 until currenciesJsonArray.length()) {
                                val currencyJsonObject = currenciesJsonArray.getJSONObject(j)
                                val currency = Currency(
                                    code = currencyJsonObject.getString("code"),
                                    name = currencyJsonObject.getString("name"),
                                    symbol = currencyJsonObject.getString("symbol")
                                )
                                currencies.add(currency)
                            }
                            currencies
                        }
                    )
                    countries.add(country)
                }
                fullCountryList = countries
                countryAdapter = CountryAdapter(countries)
                withContext(Dispatchers.Main) {
                    binding.countriesRecyclerView.adapter = countryAdapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fetchCountries()
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}