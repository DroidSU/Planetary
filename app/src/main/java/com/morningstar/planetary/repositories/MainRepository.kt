package com.morningstar.planetary.repositories

import com.morningstar.planetary.models.*
import com.morningstar.planetary.network.Result

interface MainRepository {
    suspend fun getDropDownData(): Result<MainModel>
    suspend fun searchProperty(searchText: String): Result<ArrayList<SearchPropertyResponse>>

    // change this to a network called function
    suspend fun getAutoCompleteList(string: String): Result<ArrayList<String>>

    suspend fun getSchoolNames(string: String): Result<ArrayList<SchoolNameResponse>>

    suspend fun getSchoolNamesBoundary(id: String): Result<SchoolNameBoundaryResponse>

    suspend fun getSchoolDistrict(input : String) : Result<ArrayList<SchoolDistrictResponse>>

    suspend fun getSchoolDistrictBoundary(input : String) : Result<SchoolDistrictBoundaryResponse>
}