package com.example.wetravel.components

import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wetravel.R
import com.example.wetravel.models.Destination

// Font and theme used for Voting Results Page
val dmSansFamily = FontFamily(
    Font(
        resId = R.font.dmsans_semibold, FontWeight(600)
    )
)


// The header of the page
@Composable
fun VotingResultsHeader(tripName: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "your itinerary for",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = dmSansFamily
                )
            )
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = tripName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 45.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = dmSansFamily
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Voting results",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = dmSansFamily
                )
            )
        }
    }
}

// Destination Entry component to display the card for the final destinations
@Composable
fun DestinationEntry(destination: Destination) {
    // Could use Card instead if we don't want the elevation
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1FAEE),
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 110.dp)

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column (
                modifier = Modifier.width(250.dp)
            ){
                Text(
                    text = destination.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RatingBar(rating = destination.rating.toFloat()) // Assuming you have a custom RatingBar composable
                    Image(
                        painter = painterResource(id = R.drawable.destination_pin),
                        contentDescription = "Location pin widget",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${destination.address}",
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RatingBar(rating = destination.rating.toFloat()) // Assuming you have a custom RatingBar composable
                    Image(
                        painter = painterResource(id = R.drawable.rating_star),
                        contentDescription = "Location pin widget",
                        modifier = Modifier.size(16.dp)
                    )
                    if (destination.reviewCount > 0) {
                        Text(
                            text = " ${destination.rating} "
                        )
                        Text(
                            text = "(${destination.reviewCount})",
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            text = " No Reviews"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = destination.type.replace('_', ' ').split(' ').joinToString(" ") { it.capitalize() }
                    )
                }
            }


//            Spacer(modifier = Modifier.weight(1f))

            // Destination image
            if (destination.imageBitmap !== null) {
                Image(
                    bitmap = destination.imageBitmap.asImageBitmap(),
                    contentDescription = "${destination.name} image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

// Wrapper of Destination Entry, that adds the label on the left to it and the
@Composable
fun VotingResultItineraryListComponent(index: Int, votingListItem: Destination) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                color = Color.White,
                shape = CircleShape,
                border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(45.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = index.toString(),
                        color = Color(0xFF1D3557),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dmSansFamily
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.voting_coin),
                    contentDescription = "Location pin widget",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${votingListItem.totalVotes}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    color = Color.Black,
                    fontFamily = dmSansFamily
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        DestinationEntry(destination = votingListItem)
    }
}

// The text footer between the final destinations list and map
@Composable
fun VotingListTextFooter() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "Map + Best Travel Path",
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = dmSansFamily
            )
        )
    }
}


// The Map widget
// TODO: This function will need a lot of changes once we implement the Maps API
@Composable
fun Map() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_placeholder),
            contentDescription = "Place-holder map for google maps integration",
            modifier = Modifier
                .fillMaxWidth()
                .size(540.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

// A simple Destination Entry component displayed below the map to show the path
@Composable
fun DestinationEntrySimple(destination: Destination) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1FAEE),
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = destination.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RatingBar(rating = destination.rating.toFloat()) // Assuming you have a custom RatingBar composable
                    Image(
                        painter = painterResource(id = R.drawable.destination_pin),
                        contentDescription = "Location pin widget",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${destination.address}",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// A list of simple destination entry components that represents the path you're suppose to take
@Composable
fun VotingResultsPathList(idx: Int, votingListItem: Destination, n: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column {
            DestinationEntrySimple(destination = votingListItem)
            if (idx != n - 1) {
                Image(
                    painter = painterResource(id = R.drawable.path_list_connector),
                    contentDescription = "path list connector line",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(30.dp)
                        .padding(vertical = 5.dp)
                )
            }
        }
    }
}


// The bottom footer that includes the save and send itinerary button
@Composable
fun VotingResultsFooter() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.save_icon),
                    contentDescription = "save icon"
                )
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = "send icon"
                )
            }
        }
    }
}


