package fr.epf.mm.countrysearch.ui.home

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _welcomeText = MutableLiveData<String>().apply {
        value = "Welcome to Country Search!"
    }
    val welcomeText: LiveData<String> = _welcomeText

    private val _descriptionText = MutableLiveData<String>().apply {
        value = "This is a site where you can find countries you love and share them with others using QR codes."
    }
    val descriptionText: LiveData<String> = _descriptionText
}