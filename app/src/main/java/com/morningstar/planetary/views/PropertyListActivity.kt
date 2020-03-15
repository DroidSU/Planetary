package com.morningstar.planetary.views

import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.morningstar.planetary.R
import com.morningstar.planetary.adapters.PropertyListRecyclerAdapter
import com.morningstar.planetary.models.DummyModel
import com.morningstar.planetary.models.SearchPropertyResponse
import kotlinx.android.synthetic.main.activity_property_list.*

class PropertyListActivity : AppCompatActivity() {

    private lateinit var propertyArrayList: ArrayList<SearchPropertyResponse>
    private lateinit var dummyDataArrayList: ArrayList<DummyModel>

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_list)

        toolbar = findViewById(R.id.property_list_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Properties"

//        getValuesFromIntent()

        initDummyData()
        setupRecycler()
    }

    private fun initDummyData() {
        dummyDataArrayList = ArrayList()

        var model1 = DummyModel("https://planetmlsstore.blob.core.windows.net/mlslisting/81776961/thumbphoto_1.jpg",
            "12500000", "Country Club Parkway, San Jose",
            "5 Beds, 4 Full Baths, 1 Partial Bath, 6081 sq. ft, 0.41 acres, Built in 1995", "")

        var model2 = DummyModel("https://planetmlsstore.blob.core.windows.net/mlslisting/81750546/thumbphoto_1.jpg",
            "5350000", "Pleasant Knoll Court, San Jose", "5 Beds, 5 Full Baths, 1 Partial Bath, 8256 sq. ft, 12.69 acres, Built in 2019",
                "")

        var model3 = DummyModel("https://planetmlsstore.blob.core.windows.net/mlslisting/81785543/thumbphoto_1.jpg",
            "4798000", "Dry Creek Road, San Jose", "5 Beds, 4 Full Baths, 1 Partial Bath, 5227 sq. ft, 0.49 acres, Built in 2016",
            "")

        dummyDataArrayList.add(model1)
        dummyDataArrayList.add(model2)
        dummyDataArrayList.add(model3)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getValuesFromIntent() {
        val intent = intent
        propertyArrayList = ArrayList()
        if (intent != null)
            propertyArrayList = intent.getSerializableExtra("model") as ArrayList<SearchPropertyResponse>
    }

    private fun setupRecycler() {
        val adapter = PropertyListRecyclerAdapter(this, dummyDataArrayList)
        property_list_recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        property_list_recycler.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
