import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Data class for Country
data class Country(val name: String, val region: String, val code: String, val capital: String)

// ViewModel for data fetching
class CountryViewModel : androidx.lifecycle.ViewModel() {
    val countryList = androidx.lifecycle.MutableLiveData<List<Country>>()
    val error = androidx.lifecycle.MutableLiveData<String?>()

    fun fetchCountries() {
        viewModelScope.launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
                .build()

            try {
                val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
                response.body?.let { body ->
                    val listType = object : TypeToken<List<Country>>() {}.type
                    val countries: List<Country> = Gson().fromJson(body.string(), listType)
                    countryList.postValue(countries)
                } ?: error.postValue("Failed to get response")
            } catch (e: IOException) {
                error.postValue("Network error: ${e.message}")
            }
        }
    }
}

// Adapter for RecyclerView
class CountryAdapter(private val countries: List<Country>) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CountryItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.binding.tvCountryName.text = "${country.name}, ${country.region}"
        holder.binding.tvCountryCode.text = country.code
        holder.binding.tvCountryCapital.text = country.capital
    }

    override fun getItemCount() = countries.size
}

// Activity
typealias CountryItemBinding = com.example.app.databinding.ItemCountryBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CountryViewModel
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[CountryViewModel::class.java]
        viewModel.countryList.observe(this) { countries ->
            adapter = CountryAdapter(countries)
            recyclerView.adapter = adapter
        }
        viewModel.error.observe(this) { errorMsg -> errorMsg?.let { showError(it) } }

        viewModel.fetchCountries()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
