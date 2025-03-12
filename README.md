Project Structure:
<pre>
app/ 
├── build.gradle
├── src/
    ├── main/
    │   ├── java/com/example/walmart/
    │   │   ├── MainActivity.kt
    │   │   ├── CountryAdapter.kt
    │   │   ├── repository/
    │   │   │   ├── CountryRepository.kt
    │   │   │   └── NetworkService.kt
    │   │   ├── model/
    │   │   │   └── Country.kt
    │   │   └── viewmodel/
    │   │       └── CountryViewModel.kt
    │   └── res/
    │       ├── layout/
    │       │   ├── activity_main.xml
    │       │   └── item_country.xml
    │       └── values/
    │           └── strings.xml
    └── test/
        ├── java/com/example/walmart/
        │   ├── CountryViewModelTest.kt
        │   └── CountryRepositoryTest.kt
</pre>

I have completed the test as per the instructions mentioned in the assessment. I have used views, handled errors and edge cases, written test cases, and ensured the list remains consistent through device rotation.
