package com.morningstar.planetary.repositories

import com.morningstar.planetary.models.*
import com.morningstar.planetary.network.ApiService
import com.morningstar.planetary.network.ResponseException
import com.morningstar.planetary.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.IOException

class MainRepositoryImpl(private val appApiService: ApiService) : MainRepository {

    private lateinit var autoCompleteList: MutableList<String>
    private lateinit var suggestionsList: ArrayList<String>

    override suspend fun getDropDownData(): Result<MainModel> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async fetchDropDowns()
        }.await()
    }

    override suspend fun searchProperty(searchText: String): Result<ArrayList<SearchPropertyResponse>> {
        return try {
            val searchData = appApiService.getSearchResult(searchText).await()
            Result.Success(searchData)
        } catch (ex: ResponseException) {
            ex.printStackTrace()
            Result.Error(IOException(ex.errorMessage))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Error(IOException("Something went wrong"))
        }
    }

    override suspend fun getAutoCompleteList(string: String): Result<ArrayList<String>> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async fetchLoc(string)
        }.await()
    }

    override suspend fun getSchoolNames(string: String): Result<ArrayList<SchoolNameResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async fetchSchoolNames(string)
        }.await()
    }

    override suspend fun getSchoolNamesBoundary(id: String): Result<SchoolNameBoundaryResponse> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async fetchSchoolNamesBoundary(id)
        }.await()
    }

    override suspend fun getSchoolDistrict(input: String): Result<ArrayList<SchoolDistrictResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async fetchSchoolDistrict(input)
        }.await()
    }

    override suspend fun getSchoolDistrictBoundary(input: String): Result<SchoolDistrictBoundaryResponse> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async fetchSchoolDistrictBoundary(input)
        }.await()
    }

    suspend fun fetchSchoolDistrictBoundary(input: String): Result<SchoolDistrictBoundaryResponse> {
        return try {
            val schoolDistrictBoundaryResponse = appApiService.getSchoolDistrictBoundary(input, "2").await()
            Result.Success(schoolDistrictBoundaryResponse)
        }
        catch (ex : ResponseException){
            Result.Error(IOException(ex.errorMessage))
        }
        catch (ex : Exception){
            Result.Error(IOException(ex.message))
        }
    }

    suspend fun fetchSchoolDistrict(input: String): Result<ArrayList<SchoolDistrictResponse>> {
        return try {
            val schoolDistrictResponse = appApiService.getSchoolDistrict(input, "0").await()
            Result.Success(schoolDistrictResponse)
        }
        catch (ex : ResponseException){
            Result.Error(IOException(ex.errorMessage))
        }
        catch (ex : Exception){
            Result.Error(IOException(ex.message))
        }
    }

    suspend fun fetchSchoolNamesBoundary(id: String) : Result<SchoolNameBoundaryResponse>{
        return try {
            val schoolNameBoundary = appApiService.getSchoolNameBoundary(id, "1").await()
            Result.Success(schoolNameBoundary)
        }
        catch (ex : ResponseException){
            Result.Error(IOException(ex.errorMessage))
        }
        catch (ex : Exception){
            Result.Error(IOException(ex.message))
        }
    }

    suspend fun fetchSchoolNames(string: String): Result<ArrayList<SchoolNameResponse>> {
        return try {
            val schoolNamesList = appApiService.getSchoolNames(string, "0").await()
            Result.Success(schoolNamesList)
        }
        catch (ex : ResponseException){
            Result.Error(IOException(ex.errorMessage))
        }
        catch (ex : Exception){
            Result.Error(IOException("Something went wrong"))
        }
    }

    private fun fillAutoCompleteList() {
        autoCompleteList = mutableListOf()
        autoCompleteList.add("APN 068 232 320 Woodside CA 94062")
        autoCompleteList.add("12 Montecito Road Woodside CA 94062")
        autoCompleteList.add("370 Mountain Home Court Woodside CA 94062")
        autoCompleteList.add("1125 Spyglass Woods Drive Pebble Beach CA 93953")
        autoCompleteList.add("1066 Spyglass Woods Drive Pebble Beach CA 93953")
        autoCompleteList.add("1070 Spyglass Woods Drive Pebble Beach CA 93953")
        autoCompleteList.add("1062 Spyglass Woods Drive Pebble Beach CA 93953")
        autoCompleteList.add("204 Josselyn Lane Woodside CA 94062")
        autoCompleteList.add("13233 Woodstock Drive Nevada City CA 95959")
        autoCompleteList.add("176 Harcross Road Woodside CA 94062")
        autoCompleteList.add("309 Manuella Avenue Woodside CA 94062")
    }

    suspend fun fetchDropDowns(): Result<MainModel> {
        return try {
            val dropDownData = appApiService.getDropDowns().await()
            Result.Success(dropDownData)
        } catch (ex: ResponseException) {
            ex.printStackTrace()
            Result.Error(IOException(ex.errorMessage))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Error(IOException("Something went wrong"))
        }
    }

    suspend fun fetchLoc(searchLoc : String): Result<ArrayList<String>> {
        return try {
            val dropDownData = appApiService.getLocations(searchLoc).await()
            Result.Success(dropDownData)
        } catch (ex: ResponseException) {
            ex.printStackTrace()
            Result.Error(IOException(ex.errorMessage))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Error(IOException("Something went wrong"))
        }
    }
}