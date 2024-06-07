package fr.epf.mm.countrysearch.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.epf.mm.countrysearch.adapters.CountryAdapter
import fr.epf.mm.countrysearch.databinding.FragmentCountriesBinding
import fr.epf.mm.countrysearch.models.Country

class CountriesFragment : Fragment() {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryAdapter: CountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the RecyclerView and the adapter
        val countries = listOf<Country>() // Get the list of countries from your data source
        countryAdapter = CountryAdapter(countries)
        binding.countriesRecyclerView.adapter = countryAdapter

        // Add a TextWatcher to the SearchView to filter the list of countries
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = countries.filter { it.name.contains(newText ?: "", ignoreCase = true) }
                countryAdapter.filterList(filteredList)
                return false
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}