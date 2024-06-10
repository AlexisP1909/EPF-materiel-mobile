package fr.epf.mm.countrysearch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.epf.mm.countrysearch.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import android.widget.ImageView
import fr.epf.mm.countrysearch.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val welcomeTextView: TextView = binding.textWelcome
        homeViewModel.welcomeText.observe(viewLifecycleOwner) {
            welcomeTextView.text = it
        }

        val descriptionTextView: TextView = binding.textDescription
        homeViewModel.descriptionText.observe(viewLifecycleOwner) {
            descriptionTextView.text = it
        }


        Glide.with(this)
            .load(R.drawable.home_background)
            .override(1080, 1920)
            .into(binding.imageHome)
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}