/*
 * Created by Sujoy Datta. Copyright (c) 2020. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.planetary.viewmodels

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.morningstar.planetary.network.NetworkCallback
import com.morningstar.planetary.network.Result
import com.morningstar.planetary.repositories.MainRepository
import org.json.JSONObject

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    suspend fun getDropDowns(networkCallback: NetworkCallback){
        networkCallback.callStarted()
        val dropDownResponse = mainRepository.getDropDownData()
        if (dropDownResponse is Result.Success){
            networkCallback.callSuccess(dropDownResponse.data)
        }
        else if (dropDownResponse is Result.Error){
            networkCallback.callFailed(dropDownResponse.exception.message!!)
        }
    }

    suspend fun searchProperty(string: String, networkCallback: NetworkCallback) {
        networkCallback.callStarted()
        val searchResponse = mainRepository.searchProperty(string)
        if (searchResponse is Result.Success){
            networkCallback.callSuccess(searchResponse.data)
        }
        else if (searchResponse is Result.Error){
            networkCallback.callFailed(searchResponse.exception.message!!)
        }
    }

    suspend  fun locationAutoComplete(string: String, networkCallback: NetworkCallback) {
       /* val locationResponse = mainRepository.getAutoCompleteList(string)
          networkCallback.callSuccess(locationResponse)
        */

        networkCallback.callStarted()
        val locationResponse = mainRepository.getAutoCompleteList(string)
        if (locationResponse is Result.Success){
            networkCallback.callSuccess(locationResponse.data)
        }
        else if (locationResponse is Result.Error){
            networkCallback.callFailed(locationResponse.exception.message!!)
        }
    }

    suspend fun getSchoolNames(string: String, networkCallback: NetworkCallback){
        networkCallback.callStarted()
        val schoolNameResponse = mainRepository.getSchoolNames(string)
        if (schoolNameResponse is Result.Success)
            networkCallback.callSuccess(schoolNameResponse.data)
        else if (schoolNameResponse is Result.Error)
            networkCallback.callFailed(schoolNameResponse.exception.message!!)
    }

    suspend fun getSchoolNameBoundary(id : String, networkCallback: NetworkCallback){
        networkCallback.callStarted()
        val schoolNameBoundaryResponse = mainRepository.getSchoolNamesBoundary(id)
        if (schoolNameBoundaryResponse is Result.Success)
            networkCallback.callSuccess(schoolNameBoundaryResponse.data)
        else if (schoolNameBoundaryResponse is Result.Error)
            networkCallback.callFailed(schoolNameBoundaryResponse.exception.message!!)
    }

    suspend fun getSchoolDistrict(input : String, networkCallback: NetworkCallback){
        networkCallback.callStarted()
        val schoolDistrictResponse = mainRepository.getSchoolDistrict(input)
        if (schoolDistrictResponse is Result.Success)
            networkCallback.callSuccess(schoolDistrictResponse.data)
        else if (schoolDistrictResponse is Result.Error)
            networkCallback.callFailed(schoolDistrictResponse.exception.message!!)
    }

    suspend fun getSchoolDistrictBoundary(id : String, networkCallback: NetworkCallback){
        networkCallback.callStarted()
        val schoolDistrictBoundaryResponse = mainRepository.getSchoolDistrictBoundary(id)
        if (schoolDistrictBoundaryResponse is Result.Success)
            networkCallback.callSuccess(schoolDistrictBoundaryResponse.data)
        else if(schoolDistrictBoundaryResponse is Result.Error)
            networkCallback.callFailed(schoolDistrictBoundaryResponse.exception.message!!)
    }

}