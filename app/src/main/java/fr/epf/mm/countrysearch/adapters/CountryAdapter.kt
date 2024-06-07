package fr.epf.mm.countrysearch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.countrysearch.R
import fr.epf.mm.countrysearch.models.Country

class CountryAdapter(private var countries: List<Country>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
    val country = countries[position]
    // Assuming you have TextViews with the ids nameTextView, capitalTextView, continentTextView, and flagTextView in your country_item layout
    holder.view.findViewById<TextView>(R.id.name_text_view).text = country.name
    holder.view.findViewById<TextView>(R.id.capital_text_view).text = country.capital
    holder.view.findViewById<TextView>(R.id.continent_text_view).text = country.region
    // For flag, if it's a URL to an image, you might need to use an image loading library like Glide or Picasso
}

    override fun getItemCount() = countries.size

    fun filterList(filteredList: List<Country>) {
        countries = filteredList
        notifyDataSetChanged()
    }
}