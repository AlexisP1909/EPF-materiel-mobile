package fr.epf.mm.countrysearch.adapters

import android.graphics.Picture
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVG
import fr.epf.mm.countrysearch.R
import fr.epf.mm.countrysearch.models.Country
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
}