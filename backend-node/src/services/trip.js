const { FieldValue } = require("firebase-admin").firestore;
const db = require("../database/database");

class TripService {
  async createTrip(tripData) {
    try {
      const docRef = db.collection('trips').doc(tripData.tripID);
      tripData.users.push({ userID: tripData.adminUserID, votes: tripData.votesPerPerson })
      tripData.destinationsList = {};
      await docRef.set(tripData);
      return { id: docRef.id, ...tripData };
    } catch (error) {
      console.error("Error creating trip:", error);
      throw new Error("Failed to create trip");
    }
  }

  async updateTrip(updates, tripId) {
    try {
      const docRef = db.collection("trips").doc(tripId);
      const doc = await docRef.get()
      if (!doc.exists) {
        console.log("No such trip!");
        return { success: false, message: "Trip does not exist" };
      }

      await docRef.update(updates);
      let trip = await docRef.get();
      return {
        success: true,
        id: doc.id,
        ...trip.data()
      };
    } catch (error) {
      console.error("Error updating trip: ", error);
      throw new Error("Failed to update trip");
    }
  }

  async loadTrip(tripId) {
    try {
      const docRef = db.collection("trips").doc(tripId);
      const doc = await docRef.get();

      if (!doc.exists) {
        console.log("No such trip!");
        return { success: false, message: "Trip does not exist" };
      } else {
        return { success: true, ...doc.data() };
      }
    } catch (error) {
      console.error("Error getting trip:", error);
      throw new Error("Failed to get trip");
    }
  }

  async getAllTrips(userId) {
    try {
      // Step 1: Retrieve the user document by userId
      const userDoc = await db.collection('users').doc(userId).get();
      
      if (!userDoc.exists) {
        console.log('No such user!');
        return {success: false, data: []};
      }
      
      const userData = userDoc.data();
      const tripIds = userData.tripIds || [];
      
      // Step 2: Fetch trips using tripIds
      const tripsPromises = tripIds.map(tripId => db.collection('trips').doc(tripId).get());
      const tripsDocs = await Promise.all(tripsPromises);
      
      // Step 3: Extract the trip data from documents
      const tripsData = tripsDocs.map(doc => {
        if (!doc.exists) {
          console.log('A trip document does not exist');
          return null;
        }
        return { ...doc.data() };
      }).filter(trip => trip !== null); // Filter out any null values (non-existing trip documents)
      console.log(tripsData)
      if (tripsData) {
        return {success: true, data: tripsData}
      } else {
        return {success: false, data: []}
      }

    } catch (error) {
      console.error("Error fetching user trips:", error);
      throw error; // Rethrow or handle as needed
    }
  }

  async addDestination(tripId, newDestinationID) {
    try {
      const docRef = db.collection("trips").doc(tripId);
      const doc = await docRef.get();

      if (!doc.exists) {
        console.log("No such trip!");
        return {
          success: false,
          message: "Trip does not exist",
        };
      }

      const trip = doc.data();

      const destinations = trip.destinationsList || {};

      // Check if the destination already exists in destinationsList
      if (!destinations[newDestinationID]) {
        console.log(newDestinationID);
        // Initialize new destination with 0 totalVotes and empty userVotes
        destinations[newDestinationID] = {
          totalVotes: 0,
          userVotes: {}
        };
        
        // Update the destinationsList map in Firestore
        await docRef.update({ destinationsList: destinations });

        console.log("Destination added successfully");
        return {
          success: true,
          message: "Destination added successfully",
        };
      } else {
        console.log("Destination already exists in trip");
        return {
          success: false,
          message: "Destination already exists in trip",
        };
      }

      
    } catch (error) {
      console.error("Error  adding destination:", error);
      throw new Error("Error adding destination to the trip");
    }
  }

  async addParticipantToTrip(userId, tripId) {
    try {
      const tripRef = db.collection("trips").doc(tripId);
      const trip = await tripRef.get()
      if (!trip.exists) {
        console.log("No such trip!");
        return {
          success: false,
          message: "Trip does not exist",
        };  
      }
      const numOfVotes = trip.data().finalDestinationCount;

      await tripRef.update({
        users: FieldValue.arrayUnion({ userID: userId, votes: numOfVotes }),
      });

      return {
        success: true,
        message: "Participant added successfully to the trip",
      };
    } catch (error) {
      console.error("Error adding participant: ", error);
      throw new Error("Failed to add participant");
    }
  }

  async removeParticipantFromTrip(userId, tripId) {
    const docRef = db.collection("trips").doc(tripId);
    try {
      const doc = await docRef.get();
      if (!doc.exists) {
        console.log("No such document!");
      } else {
        let data = doc.data();
        // Filter out the user with the given userID
        const updatedUsers = data.users.filter(
          (user) => user.userID !== userId
        );

        // Update the document with the new users array
        await docRef.update({
          users: updatedUsers,
        });
        return {
          success: true,
          message: "Participant removed successfully from the trip",
        };
      }
    } catch (error) {
      console.error("Error removing participant: ", error);
      throw new Error("Failed to remove participant");
    }
  }

  async getUserVotes(tripID, userID) {
    const trip = db.collection("trips").doc(tripID);
    try {
      const doc = await trip.get();
      if (!doc.exists) {
        console.log("No such trip!");
        return {
          success: false,
          message: "Trip does not exist",
        }
      } else {
        const userData = doc.data().users.find(user => user.userID === userID);
        if (!userData) {
          console.log('User not in trip');
          return {
              success: false,
              message: "User not in trip",
          };
        }
        return {
          success: true,
          votes: userData?.votes || -1
        };
      }
    } catch (error) {
      console.error("Error getting user votes: ", error);
      throw new Error("Failed to get user votes");
    }
  }

  async addVote(tripId, userId, placeId) {

    try {
      const tripRef = db.collection('trips').doc(tripId);
      const doc = await tripRef.get();

      if (!doc.exists) {
        console.log("No such trip!");
        return {
          success: false,
          message: "Trip does not exist",
        };
      }

      const destinations = doc.data().destinationsList;
      
      const userList = doc.data().users;

      // validates that a user still has votes left 
      const userIndex = userList.findIndex(u => u.userID === userId);
      if (userList[userIndex].votes < 1) {
        console.log("No more votes available!");
        return {
          success: false,
          message: "No more votes available for this trip",
        };
      }

      if (!destinations[placeId]) {
        console.log("No such destination!");
        return {
          success: false,
          message: "Destination does not exist",
        };
      }

      // Update userVotes and totalVotes for the destination
      const destinationDetails = destinations[placeId];
      destinationDetails.totalVotes = destinationDetails.totalVotes + 1;
      destinationDetails.userVotes[userId] = (destinationDetails.userVotes[userId] || 0) + 1;   

  
      // Update userList to remove a vote available from the current user
      userList[userIndex].votes = userList[userIndex].votes - 1 || 1;
      
      
   
      await tripRef.update({
        destinationsList: destinations,
        users: userList
    });
  
      return { success: true, message: 'Vote added successfully to destination' };
  
    } catch (error) {
      console.error('Error adding vote: ', error)
      throw new Error('Failed to add vote');
    }
  }



  async removeVote(tripId, userId, placeId) {

    try {
      const tripRef = db.collection('trips').doc(tripId);
      const doc = await tripRef.get();

      if (!doc.exists) {
        console.log("No such trip!");
        return {
          success: false,
          message: "Trip does not exist",
        };
      }

      const destinations = doc.data().destinationsList;
      
      const userList = doc.data().users;


      if (!destinations[placeId]) {
        console.log("No such destination!");
        return {
          success: false,
          message: "Destination does not exist",
        };
      }

      // Update userVotes and totalVotes for the destination
      const destinationDetails = destinations[placeId];

      // check for verifying that a user already voted for a trip they want to remove a vote from 
      const currentUserVotesForGivenPlace = destinationDetails.userVotes[userId] 
      if (currentUserVotesForGivenPlace === undefined || currentUserVotesForGivenPlace === 0) {
        console.log("Can't remove a vote from a location you didn't already vote for");
        return {
          success: false,
          message: "Can't remove a vote from a location you didn't already vote for",
        };
      }

      destinationDetails.totalVotes = destinationDetails.totalVotes - 1;
      destinationDetails.userVotes[userId] = (destinationDetails.userVotes[userId] || 0) - 1;   
  
      // Update userList to add back a vote available from the current user
      const userIndex = userList.findIndex(u => u.userID === userId);
      userList[userIndex].votes = userList[userIndex].votes + 1 || 1;
      
      
   
      await tripRef.update({
        destinationsList: destinations,
        users: userList
    });
  
      return { success: true, message: 'Vote removed successfully to destination' };
  
    } catch (error) {
      console.error('Error removing vote: ', error)
      throw new Error('Failed to remove vote');
    }
  }

  async createUser(userId) {
    try {
      const docRef = db.collection('users').doc(userId);
      const doc = await docRef.get();
      if (doc.exists) {
        console.log('User already exists');
        return {
          success: false,
          message: 'User already exists'
        };
      }
      await docRef.set({ tripIds: [] });
      return {
        success: true,
        id: doc.id,
        tripIds: []
      };
    } catch (error) {
      console.error('Error adding user:', error);
      throw new Error('Failed to add user');
    }
  }

  async getUser(userId) {
    try {
      const docRef = db.collection('users').doc(userId);
      const doc = await docRef.get();
      if (!doc.exists) {
        console.log('User does not exist');
        return {
          success: false,
          message: 'User does not exist'
        };
      }
      console.log('User Found');
      return {
        success: true,
        id: doc.id,
        ...doc.data()
      };
    } catch (error) {
      console.error('Error getting user:', error);
      throw new Error('Failed to get user');
    }
  }

  async updateUser(userID, tripID) {
    try {
      const docRef = db.collection('users').doc(userID);
      let user = await docRef.get();
      if (!user.exists) {
        console.log('User does not exist');
        return {
          success: false,
          message: 'User does not exist'
        };
      }

      let tripIDs = user.data().tripIds || [];

      tripIDs.push(tripID);
      await docRef.update({
        tripIds: tripIDs
      });
      console.log("TripID added to User");
      user = await docRef.get();
      return {
        success: true,
        id: user.id,
        ...user.data()
      };
    } catch (error) {
      console.error('Error getting user:', error);
      throw new Error('Failed to get user');
    }
  }

  async updateFinalDestinations(tripID) {

    try {
      const docRef = db.collection('trips').doc(tripID);
      const doc = await docRef.get();

      if (!doc.exists) {
        return {
          success: false,
          message: "Document not found"
        };
      }

      const data = doc.data()
      const finalDestinationCount = data.finalDestinationCount;
      let destinationsList = data.destinationsList;

      // Convert to array and sort 
      let destinationsArray = Object.keys(destinationsList).map(key => {
        return { id: key, ...destinationsList[key] };
      });

      destinationsArray.sort((a, b) => b.totalVotes - a.totalVotes);

      // Keep top finalDestinationCount destinations
      if (destinationsArray.length > finalDestinationCount) {
        destinationsArray = destinationsArray.slice(0, finalDestinationCount);
      }

      // Convert it back to a map
      const updatedDestinationsList = {}
      destinationsArray.forEach(destination => {
        updatedDestinationsList[destination.id] = {
          totalVotes: destination.totalVotes,
          userVotes: destination.userVotes
        };
      });

      await docRef.update({ destinationsList: updatedDestinationsList }); // Update firebase
      console.log("Final Destinations List successfully updated")
      return { success: true, message: "Destinations list updated successfully"}
    } catch (error) {
      console.log('Error updating to final destinations list')
      return {
        success: false,
        message: "Internal server error. Check Firebase Status"
      };
    }
  }
}

module.exports = TripService;
