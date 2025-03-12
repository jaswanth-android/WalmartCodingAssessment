package com.example.walmart

import com.example.walmart.model.Country
import com.example.walmart.repository.CountryApiService
import com.example.walmart.repository.CountryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class CountryRepositoryTest {

    @Mock
    private lateinit var apiService: CountryApiService

    private lateinit var repository: CountryRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CountryRepository()
    }

    @Test
    fun `getCountries success returns countries`() = runTest {
        // Given
        val mockCountries = listOf(
            Country("United States", "NA", "US", "Washington"),
            Country("Canada", "NA", "CA", "Ottawa")
        )

        val mockResponse = Response.success(mockCountries)
        `when`(apiService.getCountries()).thenReturn(mockResponse)

        // When
        val result = repository.getCountries()

        // Then
        assertTrue(result is CountryRepository.Result.Success)
        assertEquals(mockCountries, (result as CountryRepository.Result.Success).countries)
    }

    @Test
    fun `getCountries failure returns error`() = runTest {
        // Given
        `when`(apiService.getCountries()).thenThrow(IOException("Network error"))

        // When
        val result = repository.getCountries()

        // Then
        assertTrue(result is CountryRepository.Result.Error)
        assertEquals("Network error", (result as CountryRepository.Result.Error).exception.message)
    }

    @Test
    fun `getCountries empty body returns empty list`() = runTest {
        // Given
        val mockResponse = Response.success<List<Country>>(null)
        `when`(apiService.getCountries()).thenReturn(mockResponse)

        // When
        val result = repository.getCountries()

        // Then
        assertTrue(result is CountryRepository.Result.Success)
        assertEquals(0, (result as CountryRepository.Result.Success).countries.size)
    }
}