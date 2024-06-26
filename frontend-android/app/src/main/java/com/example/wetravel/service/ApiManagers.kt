package com.example.wetravel.service

import android.util.Log
import com.example.wetravel.models.AddDestinationRequest
import com.example.wetravel.models.AddDestinationResponse
import com.example.wetravel.models.Destination
import com.example.wetravel.models.Trip
import com.example.wetravel.models.TripUpdateRequest
import com.example.wetravel.models.User
import com.example.wetravel.models.UserCreationRequest
import com.example.wetravel.models.UserUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("trips")
    suspend fun createTrip(@Body tripConfiguration: Trip): Response<Trip>

    @POST("trips/{id}/destinationsList")
    suspend fun addDestination(@Path("id") tripID: String, @Body placeID: AddDestinationRequest) :Response<AddDestinationResponse>

    @POST("users")
    suspend fun createUser(@Body userRequestBody: UserCreationRequest): Response<User>

    @GET("trips/{id}")
    suspend fun getTrip(@Path("id") tripID: String): Response<Trip>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userID: String): Response<User>

    @PATCH("users/{id}")
    suspend fun updateUser(@Path("id") userID: String, @Body userRequestBody: UserUpdateRequest): Response<User>

    @GET("userTrips/{id}")
    suspend fun getAllTrips(@Path("id") userID: String): Response<List<Trip>>

    @POST("add-participant-to-trip/{id}")
    suspend fun addParticipantToTrip(@Path("id") tripID: String, @Body userRequestBody: UserCreationRequest): Response<Unit>

    @POST("trips/{id}/addVote/{placeId}/{userId}")
    suspend fun addVote(@Path("id") tripID: String, @Path("placeId") placeID: String, @Path("userId") userID: String): Response<Unit>

    @POST("trips/{id}/removeVote/{placeId}/{userId}")
    suspend fun removeVote(@Path("id") tripID: String, @Path("placeId") placeID: String, @Path("userId") userID: String): Response<Unit>


    @PATCH("trips/{id}")
    suspend fun updateTrip(@Path("id") tripID: String, @Body tripRequestBody: TripUpdateRequest): Response<Trip>

    @PATCH("updateFinalDestinations/{id}")
    suspend fun updateFinalDestinations(@Path("id") tripID: String): Response<Unit>
}


class TripRepository (private val apiService: ApiService) {
    suspend fun createTrip(tripConfiguration: Trip): Result<Trip> {
        return try {
            val response = apiService.createTrip(tripConfiguration)
            Log.d("Response from trips API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Create Trip API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun addDestination(tripID: String, placeID: String,) : Result<AddDestinationResponse> {
        return try {
            val requestBody = AddDestinationRequest(placeID)
            val response = apiService.addDestination(tripID, requestBody)
            Log.d("Response from Add Destinations API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            }
            else {
                // Return error
                Result.failure(RuntimeException("Add Destination API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTrips(userID: String): Result<List<Trip>> {
        return try {
            val response = apiService.getAllTrips(userID = userID)
            Log.d("Response from get all trips API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Get All Trip API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrip(tripID: String): Result<Trip> {
        return try {
            val response = apiService.getTrip(tripID = tripID)
            Log.d("Response from get trips API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Get Trip API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTrip(tripID: String, tripConfiguration: TripUpdateRequest): Result<Trip> {
        return try {
            val response = apiService.updateTrip(tripID, tripConfiguration)
            Log.d("Response from trips API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Update Trip API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addParticipant(tripID: String, userID: String): Result<Unit> {
        return try {
            val requestBody = UserCreationRequest(userID)
            val response = apiService.addParticipantToTrip(tripID = tripID, userRequestBody = requestBody)
            Log.d("addParticipant", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("AddParticipant to Trip API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addVote(tripID: String, userID: String, placeID: String): Result<Unit> {
        return try {
            // TODO
            val response = apiService.addVote(tripID = tripID, userID = userID, placeID = placeID)
            Log.d("Cast vote", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Casting vote to ${placeID} in ${tripID} failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeVote(tripID: String, userID: String, placeID: String): Result<Unit> {
        return try {
            val response = apiService.removeVote(tripID = tripID, userID = userID, placeID = placeID)
            Log.d("Remove vote", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Remove vote from ${placeID} in ${tripID} failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFinalDestinations(tripID: String): Result<Unit> {
        return try {
            val response = apiService.updateFinalDestinations(tripID = tripID)
            Log.d("updateFinalDestinations", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("updateFinalDestinations API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UserRepository (private val apiService: ApiService) {
    suspend fun createUser(userID: String): Result<User> {
        return try {
            val requestBody = UserCreationRequest(userID)
            val response = apiService.createUser(requestBody)
            Log.d("Response from API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Create User API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userID: String): Result<User> {
        return try {
            val response = apiService.getUser(userID)
            Log.d("Response from API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Get User API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(userID: String, tripID: String): Result<User> {
        return try {
            val requestBody = UserUpdateRequest(tripID)
            val response = apiService.updateUser(userID, requestBody)
            Log.d("Response from API", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Return error
                Result.failure(RuntimeException("Get User API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// TODO: Create more API Managers here (e.g. DatabaseAPIManager, GoogleMapsAPIManager, ...)



