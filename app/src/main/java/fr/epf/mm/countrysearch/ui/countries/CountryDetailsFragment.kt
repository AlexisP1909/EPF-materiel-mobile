package fr.epf.mm.countrysearch.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.epf.mm.countrysearch.databinding.FragmentCountryDetailsBinding
import fr.epf.mm.countrysearch.models.Country

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

        // Load the flag image using the same method you used in the CountryAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}