/*
 * Created by Sujoy Datta. Copyright (c) 2020. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.planetary.models

import java.io.Serializable

data class SearchPropertyResponse(
    val Address: String?,
    val AddressAllowed: Boolean,
    val AddressLoggedIn: String?,
    val Age: String?,
    val Bath: String?,
    val Beds: String?,
    val BuildingName: Any,
    val BuyRent: Int,
    val City: String?,
    val DaysOnMarket: Any,
    val ElementarySchool: Any,
    val ForeClosure: Boolean,
    val FullBaths: String?,
    val HighSchool: Any,
    val ImgUrl: String?,
    val Latitude: String?,
    val ListingCloseDate: Any,
    val ListingCourtesy: Any,
    val ListingagentId: Any,
    val Longitude: String?,
    val LotSize: String?,
    val MaxPrice: Any,
    val MiddleSchool: Any,
    val MinPrice: Any,
    val MlsNumber: String?,
    val MlsProvider: String?,
    val New: Boolean,
    val NoOfStories: Any,
    val OpenhouseActive: Boolean,
    val OriginalPrice: Any,
    val PartialBaths: String?,
    val Price: String?,
    val PriceDecrease: Any,
    val PriceIncrease: Any,
    val PriceReduction: Boolean,
    val PriceType: Any,
    val PropertySubType: Any,
    val PropertyURL: String?,
    val Property_SubDivisionName: Any,
    val SchoolName: Any,
    val ShortSale: Boolean,
    val SqFt: String?,
    val State: String?,
    val SystemId: String?,
    val TransactionType: Any,
    val Type: Any,
    val UnderContract: Boolean,
    val WalkScore: Any,
    val YearBuilt: String?,
    val Zip: String?,
    val considerOnCarousel: Any,
    val indexValue: Any,
    val isActive: Boolean,
    val isPocketListing: Boolean,
    val isPropertySaved: Any,
    val isSold: Boolean,
    val isUnderContract: Boolean,
    val listdate: Any,
    val openhousedate: Any,
    val propertyTypeId: Any,
    val stateAbbrivation: String?,
    val streetName: Any,
    val streetNumber: Any
) : Serializable