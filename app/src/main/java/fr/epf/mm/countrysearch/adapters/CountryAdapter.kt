package fr.epf.mm.countrysearch.adapters

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Picture
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.caverock.androidsvg.SVG
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import fr.epf.mm.countrysearch.R
import fr.epf.mm.countrysearch.database.CountryDatabase
import fr.epf.mm.countrysearch.database.MIGRATION_1_2
import fr.epf.mm.countrysearch.models.Country
import fr.epf.mm.countrysearch.models.CountryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class CountryAdapter(private var countries: List<Country>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.view.findViewById<TextView>(R.id.name_text_view).text = country.name
        holder.view.findViewById<TextView>(R.id.capital_text_view).text = country.capital
        holder.view.findViewById<TextView>(R.id.continent_text_view).text = country.region

        val flagImageView = holder.view.findViewById<ImageView>(R.id.flag_image_view)
        loadSvg(flagImageView, country.flag)


        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("country", country)
            }
            it.findNavController().navigate(R.id.action_navigation_countries_to_countryDetailsFragment, bundle)
        }

        holder.view.findViewById<Button>(R.id.save_country_button).apply {
            CoroutineScope(Dispatchers.IO).launch {
                val db = Room.databaseBuilder(
                    holder.view.context,
                    CountryDatabase::class.java, context.getString(R.string.country_database)
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                val count = db.countryDao().countCountryByName(country.name)
                withContext(Dispatchers.Main) {
                    if (count > 0) {
                        visibility = View.GONE
                    } else {
                        visibility = View.VISIBLE
                        setOnClickListener {
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
                                db.countryDao().insert(countryEntity)
                            }
                        }
                    }
                }
            }
        }
    }

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

    override fun getItemCount() = countries.size

    fun filterList(filteredList: List<Country>) {
        countries = filteredList
        notifyDataSetChanged()
    }

    fun generateQRCode(countries: List<CountryEntity>): Bitmap {
        val writer = QRCodeWriter()
        val countryData = countries.joinToString(separator = ";") {
            "${it.name},${it.capital},${it.region},${it.flag},${it.population},${it.language},${it.currency}"
        }
        val bitMatrix = writer.encode(countryData, BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }


}