# LocalLens — Milestone 3-A, Section 4: Relationships

Group 04 | Firebase Realtime Database

| Relationship | Type | Implemented by (FK or junction table) | On delete |
|---|---|---|---|
| User → SavedPlace | 1:N | SavedPlace.userId → User.userId | CASCADE |
| Experience → SavedPlace | 1:N | SavedPlace.experienceId → Experience.experienceId | CASCADE |
| User → Review | 1:N | Review.userId → User.userId | CASCADE |
| Experience → Review | 1:N | Review.experienceId → Experience.experienceId | CASCADE |
| User → Itinerary | 1:N | Itinerary.userId → User.userId | CASCADE |
| Itinerary → ItineraryStop | 1:N | ItineraryStop.itineraryId → Itinerary.itineraryId | CASCADE |
| Experience → ItineraryStop | 1:N | ItineraryStop.experienceId → Experience.experienceId | CASCADE |
| User → CommunityMember | 1:N | CommunityMember.userId → User.userId | CASCADE |
| Community → CommunityMember | 1:N | CommunityMember.communityId → Community.communityId | CASCADE |
| Community → Trip | 1:N | Trip.communityId → Community.communityId | CASCADE |
| User → TripParticipant | 1:N | TripParticipant.userId → User.userId | CASCADE |
| Trip → TripParticipant | 1:N | TripParticipant.tripId → Trip.tripId | CASCADE |
| User → JournalEntry | 1:N | JournalEntry.userId → User.userId | CASCADE |
| Experience → JournalEntry | 1:N | JournalEntry.experienceId → Experience.experienceId | SET NULL |
| User → UserInterest | 1:N | UserInterest.userId → User.userId | CASCADE |

**Logical M:N relationships:** User–Experience through SavedPlace (saved items) and Review (ratings); User–Community through CommunityMember; User–Trip through TripParticipant; Itinerary–Experience through ItineraryStop.

**On-delete policy (proposed):** Firebase Realtime Database does not enforce SQL-style FK constraints or cascading deletes. Implement these actions in application code / controlled backend functions, using multi-location updates where appropriate. CASCADE means deleting related child records; SET NULL means retaining the journal entry while clearing its optional experienceId reference.
