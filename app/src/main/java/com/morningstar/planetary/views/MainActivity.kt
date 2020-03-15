package com.morningstar.planetary.views

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.morningstar.planetary.R
import com.morningstar.planetary.adapters.BedsRecyclerAdapter
import com.morningstar.planetary.adapters.LifeStyleChoicesAdapter
import com.morningstar.planetary.models.*
import com.morningstar.planetary.network.NetworkCallback
import com.morningstar.planetary.viewmodels.MainViewModel
import com.morningstar.planetary.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(), KodeinAware, NetworkCallback {

    override val kodein by closestKodein()

    private lateinit var toolbar: Toolbar
    private val mainViewModelFactory: MainViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var itemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener

    private var min_price: String = "Min"
    private var min_price_value: String = "-1"
    private var max_price: String = "Max"
    private var max_price_value: String = "-1"
    private lateinit var bed_count: String
    private lateinit var bath_count: String

    private var selectedLocation: String = ""
    private var selectedSchoolName : String = ""
    private var selectedSchoolId : String = ""
    private var schoolParameterBoundary : String? = null
    private var schoolParameterAddress : String? = null
    private var schoolParameterLat : String? = null
    private var schoolParameterLong : String? = null


    /*
        0 - dropDown
        1 - locationSearch
        2 - searchProperty
        3 - auto complete school name
        4 - school name boundary
        5 - school district
     */
    private var currentApiCalled: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Search"

        layout_form.visibility = GONE
        layout_progress_bar.visibility = VISIBLE

        mainViewModel =
            ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        bottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_search -> {
                    searchProperty()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        getDropDowns()

        editText_location.apply {
            afterTextChanged {
                currentApiCalled = 1
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.locationAutoComplete(
                        string = it,
                        networkCallback = this@MainActivity
                    )
                }

            }
        }

        autoComplete_school_name.apply {
            afterTextChanged {
                currentApiCalled = 3
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getSchoolNames(string = it, networkCallback = this@MainActivity)
                }
            }
        }

        autoComplete_school_district.apply {
            afterTextChanged {
                currentApiCalled = 5
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getSchoolDistrict(input = it, networkCallback = this@MainActivity)
                }
            }
        }
    }

    private fun searchProperty() = CoroutineScope(Dispatchers.IO).launch {
        if (editText_location.text.toString().isNotEmpty()) {
            currentApiCalled = 2
            val requestString = createRequestJSON()

            mainViewModel.searchProperty(requestString, this@MainActivity)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@MainActivity, "Please select a locaiton", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun createRequestJSON(): String {
        var jsonObj = JSONObject();
        try {
            var jsonBuildingUnit = JSONObject()
            jsonBuildingUnit.put("MaxValue", max_price_value)
            jsonBuildingUnit.put("MinValue", min_price_value)

            jsonObj.put("BuildingUnits", jsonBuildingUnit)
            jsonObj.put("BuyRent", 0)
            jsonObj.put("Culture", "")
            jsonObj.put("DownPaymentData", "")
            jsonObj.put("EverydayConvenience", "")
            jsonObj.put("FamilyFriendly", "")
            jsonObj.put("ForeClosure", "-1")
            jsonObj.put("FunandHip", "")
            jsonObj.put("Golf", "")
            jsonObj.put("HealthandSafety", "");
            jsonObj.put("InrixPerson1Address", "");
            jsonObj.put("InrixPerson1Lat", "");
            jsonObj.put("InrixPerson1Long", "");
            jsonObj.put("InrixPerson2Address", "");
            jsonObj.put("InrixPerson2Lat", "");
            jsonObj.put("InrixPerson2Long", "");
            jsonObj.put("IsEmailOption", false);
            jsonObj.put("LifestyleChoices", "00000000")


            var jsonLivingrea = JSONObject()
            jsonLivingrea.put("MaxValue", "")
            jsonLivingrea.put("MinValue", "")
            jsonObj.put("LivingArea", jsonLivingrea);

            jsonObj.put("MOInsterestRate", "")
            jsonObj.put("OpenHouse", "-1")
            jsonObj.put("PageNumber", "-1")
            jsonObj.put("PetFriendly", "")
            jsonObj.put("PriceReductionWithin", "-1");


            var jsonPropertyAge = JSONObject()
            jsonPropertyAge.put("MaxValue", "")
            jsonPropertyAge.put("MinValue", "")
            jsonObj.put("PropertyAge", jsonPropertyAge);


            jsonObj.put("SearchText", "San Jose, CA");
            jsonObj.put("Shopping", "");
            jsonObj.put("ShortSale", "-1");
            jsonObj.put("TypeDollerOrPercent", "");
            jsonObj.put("_Baths", 2);
            jsonObj.put("_Beds", 1);
            jsonObj.put("_FirePlaceyn", "-1");
            jsonObj.put("_Includeview", "-1");
            jsonObj.put("_IsFeaturedlisting", "-1");
            jsonObj.put("_MaxPrice", "-1");
            jsonObj.put("_MinPrice", "-1");
            jsonObj.put("_PropertyType", "-1");
            jsonObj.put("_RentMax", "-1");
            jsonObj.put("_RentMin", "-1");
            jsonObj.put("_SeniorCommunityYN", "-1");
            jsonObj.put("_cooling", "-1");
            jsonObj.put("_foundation", "-1");
            jsonObj.put("_garage", "-1");
            jsonObj.put("_hasImage", "-1");
            jsonObj.put("_hasVirtualTour", "-1");
            jsonObj.put("_heating", "-1")
            jsonObj.put("_inrixArrivalTimePerson1", "-1");
            jsonObj.put("_inrixArrivalTimePerson2", "-1");
            jsonObj.put("_inrixDurationPerson1", "-1");
            jsonObj.put("_inrixDurationPerson2", "-1");
            jsonObj.put("_isFurnished", "-1");
            jsonObj.put("_lotSize", "0.11");
            jsonObj.put("_noofBuildingUnits", "-1");
            jsonObj.put("_noofgarage", "-1");
            jsonObj.put("_noofparking", "-1");
            jsonObj.put("_parkview", "-1");
            jsonObj.put("_pool", "-1");
            jsonObj.put("_schoolDistricts", "");
            jsonObj.put("_schoolDistrictsId", "");
            jsonObj.put("_schools", "");
            jsonObj.put("_schoolsId", "");
            jsonObj.put("_schoolsRating", "-1");
            jsonObj.put("_style", "-1");
            jsonObj.put("_waterSourse", "-1");
            jsonObj.put("_waterfront", "-1");
            jsonObj.put("_waterview", "-1");
            jsonObj.put("airportNoise", "-1");
            jsonObj.put("apiPath", "https://www.planetrecrm.biz/PlanetREAPI/api/");


            var jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("bedroomsonLowerFloor", jsonbedroomSoon);

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("bedroomsonMainFloor", jsonbedroomSoon);

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("bedroomsonUpperFloor", jsonbedroomSoon);


            jsonObj.put("communityid", "");
            jsonObj.put("communityname", "");

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("fullBathsonLowerFloor", jsonbedroomSoon);

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("fullBathsonMainFloor", jsonbedroomSoon);

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("fullBathsonUpperFloor", jsonbedroomSoon);


            jsonObj.put("getLocationType", "City");

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("halfBathsonLowerFloor", jsonbedroomSoon);

            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("halfBathsonMainFloor", jsonbedroomSoon);


            jsonbedroomSoon = JSONObject()
            jsonbedroomSoon.put("MaxValue", "")
            jsonbedroomSoon.put("MinValue", "")
            jsonObj.put("halfBathsonUpperFloor", jsonbedroomSoon);


            jsonObj.put("intersectionPoints", "");
            jsonObj.put("isIntersectionPresent", "0");
            jsonObj.put("isSaved", "");
            jsonObj.put("localNoise", "-1");
            jsonObj.put("maxDaysListed", "");
            jsonObj.put("maxPricebyMOP", "");
            jsonObj.put("minPricebyMOP", "");
            jsonObj.put("multipleCityorZip", "San Jose, CA");
            jsonObj.put("multipleMajorSubDivision", "");
            jsonObj.put("multipleMinorSubDivision", "");
            jsonObj.put("multipleSchools", "");
            jsonObj.put("multipleareas", "");
            jsonObj.put("multiplecounties", "");
            jsonObj.put("multiplemodels", "");
            jsonObj.put("overallNoise", "-1");
            jsonObj.put("polyPoints1", "");
            jsonObj.put("polyPoints2", "");
            jsonObj.put("savedSearchID", "");

            val schoolParam = JSONObject()
            schoolParam.put("schoolParameterAddress", "")
            schoolParam.put("schoolParameterAddressLat", "")
            schoolParam.put("schoolParameterAddressLng", "")
            schoolParam.put("schoolParameterBoundary", "")
            jsonObj.put("schoolParameters", schoolParam)


            jsonObj.put("selectItem_PropertyType", "5")
            jsonObj.put("selectItem_cooling", "-1")
            jsonObj.put("sortby", "1")
            jsonObj.put("status", "-1")
            jsonObj.put("trafficNoise", "-1")
            jsonObj.put("uservisitorid", "")


            var yearJson = JSONObject()
            yearJson.put("yearRenovatedFrom", "")
            yearJson.put("yearRenovatedTo", "")
            jsonObj.put("yearRenovated", yearJson)


        } catch (ex: Exception) {
        }
        return jsonObj.toString()
    }

    private fun getDropDowns() = CoroutineScope(Dispatchers.IO).launch {
        currentApiCalled = 0
        mainViewModel.getDropDowns(networkCallback = this@MainActivity)
    }

    override fun callStarted() {
    }

    override fun callFailed(errorMessage: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@MainActivity, "Failed $errorMessage", Toast.LENGTH_SHORT).show()
        }
    }

    override fun callSuccess(data: Any) {
        CoroutineScope(Dispatchers.Main).launch {
            when (currentApiCalled) {
                0 -> {
                    layout_progress_bar.visibility = GONE
                    layout_form.visibility = VISIBLE
                    val responseModel: MainModel = data as MainModel
                    setupUi(responseModel)
                }
                1 -> {

                    val autoCompleteArrayList = data as ArrayList<String>
                    /*@Suppress("UNCHECKED_CAST") val autoCompleteArrayList: ArrayList<String> =
                        data as ArrayList<String>*/
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1, autoCompleteArrayList
                    )
                    editText_location.setAdapter(adapter)

                    editText_location.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                selectedLocation = autoCompleteArrayList[p2]
                            }

                        }
                }
                2 -> {
                    @Suppress("UNCHECKED_CAST") val searchPropertyResponseModel: ArrayList<SearchPropertyResponse> =
                        data as ArrayList<SearchPropertyResponse>

                    val responseArrayList: ArrayList<SearchPropertyResponse> = data
                    startActivity(
                        Intent(
                            this@MainActivity,
                            PropertyListActivity::class.java
                        ).putExtra("model", responseArrayList)
                    )
                }
                3 -> {
                    val responseArrayList = data as ArrayList<SchoolNameResponse>

                    val schoolNameArrayList : ArrayList<String> = ArrayList()
                    val schoolIdsArrayList : ArrayList<String> = ArrayList()

                    for (value in responseArrayList){
                        schoolNameArrayList.add(value.m_Item1)
                        schoolIdsArrayList.add(value.m_Item2)
                    }
                    /*@Suppress("UNCHECKED_CAST") val autoCompleteArrayList: ArrayList<String> =
                        data as ArrayList<String>*/
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1, schoolNameArrayList
                    )
                    autoComplete_school_name.setAdapter(adapter)

                    autoComplete_school_name.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                selectedSchoolName = schoolNameArrayList[p2]
                                selectedSchoolId = schoolIdsArrayList[p2]

                                autoComplete_school_district.setText("")

                                CoroutineScope(Dispatchers.IO).launch {
                                    currentApiCalled = 4
                                    mainViewModel.getSchoolNameBoundary(selectedSchoolId, this@MainActivity)
                                }
                            }

                        }
                }
                4 -> {
                    val response = data as SchoolNameBoundaryResponse
                    schoolParameterBoundary = response.schoolParameterBoundary
                    schoolParameterAddress = response.schoolParameterAddress
                    schoolParameterLat = response.schoolParameterAddressLat
                    schoolParameterLong = response.schoolParameterAddressLng
                }
                5 -> {
                    val districtResponseArrayList = data as ArrayList<SchoolDistrictResponse>

                    val schoolNameArrayList : ArrayList<String> = ArrayList()
                    val schoolIdsArrayList : ArrayList<String> = ArrayList()

                    for (value in districtResponseArrayList){
                        schoolNameArrayList.add(value.m_Item1)
                        schoolIdsArrayList.add(value.m_Item2)
                    }
                    /*@Suppress("UNCHECKED_CAST") val autoCompleteArrayList: ArrayList<String> =
                        data as ArrayList<String>*/
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1, schoolNameArrayList
                    )
                    autoComplete_school_district.setAdapter(adapter)

                    autoComplete_school_district.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                selectedSchoolName = schoolNameArrayList[p2]
                                selectedSchoolId = schoolIdsArrayList[p2]

                                autoComplete_school_name.setText("")

                                CoroutineScope(Dispatchers.IO).launch {
                                    currentApiCalled = 6
                                    mainViewModel.getSchoolDistrictBoundary(selectedSchoolId, this@MainActivity)
                                }
                            }

                        }
                }
                6 -> {
                    val schoolDistrictBoundaryResponse = data as SchoolDistrictBoundaryResponse
                    schoolParameterBoundary = schoolDistrictBoundaryResponse.schoolParameterBoundary
                    schoolParameterAddress = schoolDistrictBoundaryResponse.schoolParameterAddress
                    schoolParameterLat = schoolDistrictBoundaryResponse.schoolParameterAddressLat
                    schoolParameterLong = schoolDistrictBoundaryResponse.schoolParameterAddressLng
                }
            }
        }
    }

    private fun setupUi(responseModel: MainModel) {
        fillMinPriceSpinner(responseModel)
        fillMaxPriceSpinner(responseModel)
        fillBeds(responseModel)
        fillBaths(responseModel)
        fillSchoolRatings(responseModel)
        fillLifeStyleChoice(responseModel)
        fillPropertyTypes(responseModel)
        fillStatus(responseModel)
        fillShortSale(responseModel)
        fillForeClosure(responseModel)
        fillLotSize(responseModel)
        fillOpenHouse(responseModel)
        fillStyle(responseModel)
        fillPoolData(responseModel)
        fillHeatingType(responseModel)
        fillCoolingType(responseModel)
        fillGarageFeature(responseModel)
        fillFoundation(responseModel)
        fillPriceReductionWithin(responseModel)
    }

    private fun fillPriceReductionWithin(responseModel: MainModel) {
        val priceReduction = mutableListOf<String>()
        for (value in responseModel.DropDownPriceReductionWithinDDl)
            priceReduction.add(value.Text)

        val priceReductionAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priceReduction)
        spinner_price_reduction_within.adapter = priceReductionAdapter
    }

    private fun fillFoundation(responseModel: MainModel) {
        val foundation = mutableListOf<String>()
        for (value in responseModel.foundation)
            foundation.add(value.Text)
        val foundationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, foundation)
        spinner_foundation.adapter = foundationAdapter
    }

    private fun fillGarageFeature(responseModel: MainModel) {
        val garageFeature = mutableListOf<String>()
        for (value in responseModel.garage)
            garageFeature.add(value.Text)
        val garageAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, garageFeature)
        spinner_garage_feature.adapter = garageAdapter
    }

    private fun fillCoolingType(responseModel: MainModel) {
        val coolingType = mutableListOf<String>()
        for (value in responseModel.cooling)
            coolingType.add(value.Text)
        val coolingAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, coolingType)
        spinner_cooling_type.adapter = coolingAdapter
    }

    private fun fillHeatingType(responseModel: MainModel) {
        val heatingType = mutableListOf<String>()
        for (value in responseModel.heating)
            heatingType.add(value.Text)
        val heatingAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, heatingType)
        spinner_heating_type.adapter = heatingAdapter
    }

    private fun fillPoolData(responseModel: MainModel) {
        val pool = mutableListOf<String>()
        for (value in responseModel.pool)
            pool.add(value.Text)
        val poolAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pool)
        spinner_pool.adapter = poolAdapter
    }

    private fun fillStyle(responseModel: MainModel) {
        val style = mutableListOf<String>()
        for (value in responseModel.style)
            style.add(value.Text)
        val styleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, style)
        spinner_style.adapter = styleAdapter
    }

    private fun fillOpenHouse(responseModel: MainModel) {
        val openHouse = mutableListOf<String>()
        for (value in responseModel.DropDownOpenHouseDDl)
            openHouse.add(value.Text)
        val openHouseAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, openHouse)
        spinner_open_house.adapter = openHouseAdapter
    }

    private fun fillLotSize(responseModel: MainModel) {
        val lotSize = mutableListOf<String>()
        for (value in responseModel.lotSize)
            lotSize.add(value.Text)

        val lotSizeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lotSize)
        spinner_lot_size.adapter = lotSizeAdapter
    }

    private fun fillForeClosure(responseModel: MainModel) {
        val foreClosure = mutableListOf<String>()
        for (value in responseModel.DropDownforeClosureDDl)
            foreClosure.add(value.Text)

        val foreclosureAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, foreClosure)
        spinner_foreclosure.adapter = foreclosureAdapter
    }

    private fun fillShortSale(responseModel: MainModel) {
        val shortSale = mutableListOf<String>()
        for (value in responseModel.DropDownShortSaleSDDl) {
            shortSale.add(value.Text)
        }
        val shortSaleAdapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item, shortSale
        )
        spinner_short_sale.adapter = shortSaleAdapter
    }

    private fun fillStatus(responseModel: MainModel) {
        val status = mutableListOf<String>()
        for (value in responseModel.DropDownstatus) {
            status.add(value.Text)
        }
        val statusAdapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item, status
        )
        spinner_status.adapter = statusAdapter
    }

    private fun fillPropertyTypes(responseModel: MainModel) {
        val propertyType = mutableListOf<String>()
        for (value in responseModel.PropertyType) {
            propertyType.add(value.Text)
        }
        val propertyTypeAdapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item, propertyType
        )
        spinner_prop_type.adapter = propertyTypeAdapter
    }

    private fun fillLifeStyleChoice(responseModel: MainModel) {
        val lifeStyleArrayList = ArrayList<String>()
        lifeStyleArrayList.add("Culturally Rich")
        lifeStyleArrayList.add("Everyday Commute")
        lifeStyleArrayList.add("fun and Help")
        lifeStyleArrayList.add("family Friendly")
        lifeStyleArrayList.add("Golfer's Paradise")
        lifeStyleArrayList.add("Health and Safety")
        lifeStyleArrayList.add("Pet Friendly")
        lifeStyleArrayList.add("Shopping")

        val lifeStyleImageArrayList = ArrayList<Drawable>()
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.lc1a))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.everyday_con))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.fun_and_hip))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.family))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.golfer))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.health))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.pet))
        lifeStyleImageArrayList.add(resources.getDrawable(R.drawable.shopping))

        val lifeStyleImageArrayListActivated = ArrayList<Drawable>()
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.lc1))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.everyday_con_s))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.fun_activated))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.family_activated))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.golfer_activated))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.health_activated))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.pet_small))
        lifeStyleImageArrayListActivated.add(resources.getDrawable(R.drawable.shopping_small))

        val adapter = LifeStyleChoicesAdapter(
            this,
            lifeStyleArrayList,
            lifeStyleImageArrayList,
            lifeStyleImageArrayListActivated
        )
        rv_lifestyle.layoutManager = GridLayoutManager(this, 2)
        rv_lifestyle.adapter = adapter
    }

    private fun fillSchoolRatings(responseModel: MainModel) {
        val ratingsArrayList = ArrayList<String>()

        ratingsArrayList.add("No Preference")

        for (count in 1..5)
            ratingsArrayList.add("$count+")

        val adapter = BedsRecyclerAdapter(this, ratingsArrayList)
        rv_school_rating.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_school_rating.adapter = adapter
    }

    private fun fillBaths(responseModel: MainModel) {
        val bathsArrayList = ArrayList<String>()

        for (bed in responseModel.Beds)
            bathsArrayList.add(bed.Text)

        val adapter = BedsRecyclerAdapter(this, bathsArrayList)
        rv_baths.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_baths.adapter = adapter
    }

    private fun fillBeds(responseModel: MainModel) {
        val bedsArrayList = ArrayList<String>()

        for (bed in responseModel.Beds)
            bedsArrayList.add(bed.Text)

        val adapter = BedsRecyclerAdapter(this, bedsArrayList)
        rv_beds.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_beds.adapter = adapter
    }

    private fun fillMaxPriceSpinner(responseModel: MainModel) {
        val maxPriceArray = mutableListOf<String>()
        for (value in responseModel.MaxPrice) {
            maxPriceArray.add(value.Text)
        }
        val maxPriceAdapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item, maxPriceArray
        )
        spinner_max_price.adapter = maxPriceAdapter

        spinner_max_price.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                max_price = responseModel.MaxPrice[p2].Text
                max_price_value = responseModel.MaxPrice[p2].Value
            }

        }
    }

    private fun fillMinPriceSpinner(responseModel: MainModel) {
        val minPriceArray = mutableListOf<String>()
        for (value in responseModel.MinPrice) {
            minPriceArray.add(value.Text)
        }
        val minPriceAdapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item, minPriceArray
        )
        spinner_min_price.adapter = minPriceAdapter

        spinner_min_price.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                min_price = responseModel.MinPrice[p2].Text
                min_price_value = responseModel.MinPrice[p2].Value
            }
        }
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
