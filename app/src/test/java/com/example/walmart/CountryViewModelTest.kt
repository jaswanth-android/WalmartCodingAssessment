package com.example.walmart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.walmart.model.Country
import com.example.walmart.repository.CountryRepository
import com.example.walmart.viewmodel.CountryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CountryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: CountryRepository

    private lateinit var viewModel: CountryViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchCountries success returns countries`() = runTest {
        // Given
        val mockCountries = listOf(
            Country("United States", "NA", "US", "Washington"),
            Country("Canada", "NA", "CA", "Ottawa")
        )

        `when`(repository.getCountries()).thenReturn(CountryRepository.Result.Success(mockCountries))

        // When
        viewModel = CountryViewModel()
        viewModel.fetchCountries()

        // Then
        assertEquals(mockCountries, viewModel.countries.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `fetchCountries error returns error message`() = runTest {
        // Given
        val errorMessage = "Network error"
        `when`(repository.getCountries()).thenReturn(
            CountryRepository.Result.Error(Exception(errorMessage))
        )

        // When
        viewModel = CountryViewModel()
        viewModel.fetchCountries()

        // Then
        assertNotNull(viewModel.countries.value)
        assertEquals(0, viewModel.countries.value?.size)
        assertEquals(errorMessage, viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }
}