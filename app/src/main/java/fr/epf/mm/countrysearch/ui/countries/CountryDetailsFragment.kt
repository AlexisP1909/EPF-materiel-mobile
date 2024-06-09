package fr.epf.mm.countrysearch.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.room.Room
import fr.epf.mm.countrysearch.R
import fr.epf.mm.countrysearch.database.CountryDatabase
import fr.epf.mm.countrysearch.database.MIGRATION_1_2
import fr.epf.mm.countrysearch.databinding.FragmentCountryDetailsBinding
import fr.epf.mm.countrysearch.models.Country
import fr.epf.mm.countrysearch.models.CountryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryDetailsFragment : Fragment() {

    private var _binding: FragmentCountryDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val country: Country = arguments?.getParcelable("country")!!

        binding.nameTextView.text = country.name
        binding.capitalTextView.text = country.capital
        binding.regionTextView.text = country.region
        binding.populationTextView.text = country.population.toString()
        binding.languageTextView.text = country.language
        binding.currencyTextView.text = country.currency

        val saveCountryButton = root.findViewById<Button>(R.id.saveCountryButton)
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(
                requireContext(),
                CountryDatabase::class.java, getString(R.string.country_database)
            )
                .addMigrations(MIGRATION_1_2)
                .build()
            val countryDao = db.countryDao()
            val count = countryDao.countCountryByName(country.name)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    saveCountryButton.text = getString(R.string.remove_from_favorites)
                    saveCountryButton.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            countryDao.deleteCountryByName(country.name)
                            withContext(Dispatchers.Main) {
                                saveCountryButton.text = getString(R.string.save_to_favorites)
                            }
                        }
                    }
                } else {
                    saveCountryButton.text = getString(R.string.save_to_favorites)
                    saveCountryButton.setOnClickListener {
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
                        CoroutineScope(Dispatchers.IO).launch {
                            countryDao.insert(countryEntity)
                            withContext(Dispatchers.Main) {
                                saveCountryButton.text = getString(R.string.remove_from_favorites)
                            }
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}