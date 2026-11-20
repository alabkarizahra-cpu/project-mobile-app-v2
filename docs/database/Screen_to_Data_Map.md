
# LocalLens — Screen-to-Data Map & UI Validation

**Milestone:** 3-A — Database Design  
**Section:** 5 — Screen-to-Data Map  
**Database:** Firebase Realtime Database  
**Authentication:** Firebase Authentication

## 1. Screen-to-Data Map

| Screen | Reads (Entities / Fields) | Writes (Insert / Update / Delete) |
|---|---|---|
| Splash | Firebase Auth session state | None |
| Onboarding | None | None |
| Login | Firebase Authentication | Sign in via Firebase Authentication |
| Sign Up | Firebase Authentication | Create account in Firebase Auth; Insert User (userId, fullName, email) |
| Home | User (fullName); Experience (experienceId, name, category, imageUrl, location) | None |
| Explore | Experience (experienceId, name, description, location, category, imageUrl); SavedPlace (userId, experienceId) | Insert/Delete SavedPlace |
| Map | Experience (experienceId, name, latitude, longitude, category) | None |
| Experience Details | Experience (experienceId, name, description, location, imageUrl); Review (rating, comment, createdAt); SavedPlace (userId, experienceId) | Insert/Delete SavedPlace; Insert Review |
| Saved Places | SavedPlace (userId, experienceId, savedAt); Experience (name, location, imageUrl) | Delete SavedPlace |
| Build My Day | UserInterest (interestName); Experience (experienceId, name, category, latitude, longitude) | Insert Itinerary (durationHours, startTime, startingPoint); Insert ItineraryStop |
| Itinerary Result | Itinerary (itineraryId, title, date, durationHours, startTime, startingPoint); ItineraryStop (stopOrder, plannedTime, experienceId); Experience (name, location) | Update Itinerary/ItineraryStop when edited |
| Community & Trips | Community (communityId, name, description, imageUrl); Trip (tripId, title, destination, startDate, endDate) | None |
| Community Details | Community (communityId, name, description, imageUrl); CommunityMember (userId, communityId, joinedAt); Trip (tripId, communityId, title) | Insert/Delete CommunityMember |
| Trip Details | Trip (tripId, title, description, destination, startDate, endDate, meetingPoint, meetingTime); TripParticipant (userId, tripId, status) | Insert/Delete TripParticipant; Update TripParticipant.status |
| Profile | User (fullName, email, profileImage); UserInterest (interestName); SavedPlace; TripParticipant; JournalEntry | Update User; Insert/Delete UserInterest |
| Travel Journal | JournalEntry (entryId, userId, experienceId, title, content, imageUrl, createdAt); Experience (name) when linked | Insert/Update/Delete JournalEntry; experienceId is optional |

## 2. UI Validation

The screen-to-data mapping was reviewed against the LocalLens Milestone 2 screens and the Milestone 3-A ER Diagram.

### Validated Data Requirements

- Experience includes latitude and longitude for the interactive map.
- Itinerary includes durationHours, startTime, and startingPoint for Build My Day.
- Trip includes startDate, endDate, meetingPoint, and meetingTime for group trip details.
- JournalEntry supports an optional experienceId, allowing users to create travel journal entries without linking them to an existing experience.
- Firebase Authentication handles user registration and login separately from the User profile data.

### Scope Notes

- Check-in requires additional data storage if implemented as a persistent feature.
- Community posts require a separate data structure if included in the implemented scope.
- Read and write operations describe planned data interactions, not completed Android implementation.

## 3. Consistency Checks

- Entity names are aligned with the ER Diagram.
- Field names should match the final Relational Schema and Data Dictionary.
- All 16 screens have been reviewed for their data requirements.
- Any subsequent schema changes must also be reflected in this mapping.
