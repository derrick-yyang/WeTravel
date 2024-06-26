const express = require('express');
const router = express.Router();
const tripController = require('../controller/controller');

// #region User Routes

// create a user
router.post('/users/', tripController.createUser);

// get a user by id
router.get('/users/:id', tripController.getUser);

// update a user by id
router.patch('/users/:id', tripController.updateUser);

// #endregion

// #region Trip Routes

// create a trip
router.post('/trips', tripController.createTrip);
// get a trip by id (access code)
router.get('/trips/:id', tripController.loadTrip);
router.post('/trips/:id/destinationsList', tripController.addDestination);
router.post('/trips/:id/addVote/:placeId/:userId', tripController.addVote);
router.post('/trips/:id/removeVote/:placeId/:userId', tripController.removeVote);
router.get('/userTrips/:id', tripController.getAllTrips);

// Update a trip based on a specific ID
router.patch('/trips/:id', tripController.updateTrip);

// add participant to an existing trip route
router.post('/add-participant-to-trip/:id', tripController.addParticipantToTrip)

// remove participant from existing trip route
router.post('/remove-participant-from-trip/:id', tripController.removeParticipantFromTrip)

// get user votes in a trip
router.get('/trips/:tripID/userVotes/:userID', tripController.getUserVotes)

router.patch('/updateFinalDestinations/:id', tripController.updateFinalDestinations)

// #endregion

module.exports = router;
