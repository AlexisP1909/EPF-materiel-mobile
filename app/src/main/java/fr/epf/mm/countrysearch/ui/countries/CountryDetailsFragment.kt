package fr.epf.mm.countrysearch.ui.countries

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.caverock.androidsvg.SVG
import com.google.gson.Gson
import fr.epf.mm.countrysearch.R
import fr.epf.mm.countrysearch.database.CountryDatabase
import fr.epf.mm.countrysearch.database.MIGRATION_1_2
import fr.epf.mm.countrysearch.databinding.FragmentCountryDetailsBinding
import fr.epf.mm.countrysearch.models.Country
import fr.epf.mm.countrysearch.models.CountryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

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
        binding.languageTextView.text = country.languages.joinToString { it.name }
        binding.currencyTextView.text = country.currencies.joinToString { it.name }
        loadSvg(binding.flagImageView, country.flag)

        val saveCountryButton = root.findViewById<Button>(R.id.saveCountryButton)
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(
                requireContext(),
                CountryDatabase::class.java, getString(R.string.country_database)
            )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
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
                            languages = Gson().toJson(country.languages),
                            currencies = Gson().toJson(country.currencies)
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadSvg(imageView: ImageView, url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val svg = SVG.getFromInputStream(URL(url).openStream())
                val drawable = PictureDrawable(svg.renderToPicture())
                withContext(Dispatchers.Main) {
                    imageView.setImageDrawable(drawable)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}