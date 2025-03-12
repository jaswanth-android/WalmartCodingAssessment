package com.example.walmart

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walmart.viewmodel.CountryViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: CountryViewModel by viewModels()
    private lateinit var adapter: CountryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.countriesRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
    }

    private fun setupRecyclerView() {
        adapter = CountryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.countries.observe(this) { countries ->
            adapter.submitList(countries)

            // Show empty view if needed
            if (countries.isNullOrEmpty() && viewModel.error.value.isNullOrEmpty()) {
                errorTextView.text = getString(R.string.no_internet_connection)
                errorTextView.visibility = View.VISIBLE
            } else {
                errorTextView.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                errorTextView.text = errorMessage
                errorTextView.visibility = View.VISIBLE
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                errorTextView.visibility = View.GONE
            }
        }
    }
}