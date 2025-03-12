package com.example.walmart.repository

import com.example.walmart.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository {
    private val apiService = NetworkService.countryApiService

    sealed class Result {
        data class Success(val countries: List<Country>) : Result()
        data class Error(val exception: Exception) : Result()
    }

    suspend fun getCountries(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCountries()
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Result.Success(countries)
                } else {
                    Result.Error(Exception("Failed to fetch countries: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}