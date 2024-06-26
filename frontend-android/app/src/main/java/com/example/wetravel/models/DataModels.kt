package com.example.wetravel.models

import android.graphics.Bitmap
import java.util.UUID


// This file contains the Data Models used by our Frontend
data class Destination(
    val placeId: String,
    val name: String,
    val address: String,
    val rating: Double,
    val reviewCount: Int,
    val type: String,
    val imageBitmap: Bitmap?,
    val totalVotes: Int,
    val userVotes : Int
)

// ENUM for voting phase
// TODO: Decide how to incorporate this into our DB Schema
enum class VotingPhase {
    ADD_DESTINATIONS,
    VOTING,
    RESULTS
}

data class AddDestinationResponse(
    val success: String,
    val message: String
)

data class AddDestinationRequest(
    val placeID: String
)

data class User(
    val userID: String = "",
    val tripIDs: List<String> = emptyList(),
)

data class UserCreationRequest(
    val userId: String
)

data class UserUpdateRequest(
    val tripID: String
)

data class TripUsers(
    val userID: String = "",
    val votes: Int = 0,
)

data class Trip(
    val tripID: String = "",
    val name: String = "",
    val city: String = "",
    val finalDestinationCount: Int = 0,
    val users: List<TripUsers> = emptyList(),
    val adminUserID: String = "",
    val votesPerPerson: Int = 0,
    val phase: String = "",
    val destinationsList: List<Destination> = emptyList(),
)

data class TripUpdateRequest(
    val tripID: String = "",
    val name: String = "",
    val city: String = "",
    val finalDestinationCount: Int = 0,
    val votesPerPerson: Int = 0,
    val phase: String = "Adding"
)

