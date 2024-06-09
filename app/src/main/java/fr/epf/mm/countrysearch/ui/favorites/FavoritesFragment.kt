package fr.epf.mm.countrysearch.ui.favorites

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.zxing.integration.android.IntentIntegrator
import fr.epf.mm.countrysearch.R
import fr.epf.mm.countrysearch.adapters.CountryAdapter
import fr.epf.mm.countrysearch.database.CountryDatabase
import fr.epf.mm.countrysearch.database.MIGRATION_1_2
import fr.epf.mm.countrysearch.databinding.FragmentFavoritesBinding
import fr.epf.mm.countrysearch.models.CountryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private lateinit var countryAdapter: CountryAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(
                requireContext(),
                CountryDatabase::class.java, "country-database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
            val countryEntities = db.countryDao().getAll()
            val countries = countryEntities.map { it.toCountry() }
            withContext(Dispatchers.Main) {
                recyclerView.adapter = CountryAdapter(countries)
            }
        }

        binding.generateQRCodeButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val db = Room.databaseBuilder(
                    requireContext(),
                    CountryDatabase::class.java, getString(R.string.country_database)
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                val countryDao = db.countryDao()
                val countries = countryDao.getAll()
                val qrCode = countryAdapter.generateQRCode(countries)
                withContext(Dispatchers.Main) {
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.dialog_qr_code)
                    val imageView = dialog.findViewById<ImageView>(R.id.qrCodeImageView)
                    imageView.setImageBitmap(qrCode)
                    val closeButton = dialog.findViewById<Button>(R.id.closeButton)
                    closeButton.setOnClickListener { dialog.dismiss() }
                    dialog.show()
                }
            }
        }

        binding.scanQRCodeButton.setOnClickListener {
            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setPrompt("Scan a QR code")
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setBarcodeImageEnabled(true)
            intentIntegrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
    if (result != null) {
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val countryDataList = result.contents.split(";")
            CoroutineScope(Dispatchers.IO).launch {
                val db = Room.databaseBuilder(
                    requireContext(),
                    CountryDatabase::class.java, getString(R.string.country_database)
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                val countryDao = db.countryDao()
                for (countryData in countryDataList) {
                    val fields = countryData.split(",")
                    val countryName = fields[0]
                    val count = countryDao.countCountryByName(countryName)
                    if (count == 0) {
                        val countryEntity = CountryEntity(
                            id = 0,
                            name = countryName,
                            capital = fields[1],
                            region = fields[2],
                            flag = fields[3],
                            population = fields[4].toLong(),
                            language = fields[5],
                            currency = fields[6]
                        )
                        countryDao.insert(countryEntity)
                    }
                }
            }
        }
    } else {
        super.onActivityResult(requestCode, resultCode, data)
    }
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}