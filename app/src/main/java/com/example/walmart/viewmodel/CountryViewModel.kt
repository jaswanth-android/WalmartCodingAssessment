package com.example.walmart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walmart.model.Country
import com.example.walmart.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {
    private val repository = CountryRepository()

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchCountries()
    }

    fun fetchCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getCountries()) {
                is CountryRepository.Result.Success -> {
                    _countries.value = result.countries
                    _error.value = null
                }
                is CountryRepository.Result.Error -> {
                    _error.value = result.exception.message ?: "Unknown error occurred"
                    if (_countries.value.isNullOrEmpty()) {
                        // Only set empty list if we don't have any data
                        _countries.value = emptyList()
                    }
                }
            }
            _isLoading.value = false
        }
    }
}