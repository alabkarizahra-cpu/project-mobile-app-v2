# LocalLens – Milestone 3-A
## Entities Identification and Data Dictionary

**Team:** Group 04  
**Application:** LocalLens – Discover Saudi Arabia  
**Database:** Firebase Realtime Database  
**Contributor:** Sara Khalid Almohammadi  
**GitHub Issue:** #13

## Section 1 – Entities at a Glance

| Entity | What it stores | Related to |
|---|---|---|
| User | User account and profile information | SavedPlace, Review, Itinerary, CommunityMember, TripParticipant, JournalEntry, UserInterest |
| Experience | Tourist destinations and activities | SavedPlace, Review, ItineraryStop, JournalEntry |
| SavedPlace | Experiences saved by users | User, Experience |
| Review | User ratings and reviews of experiences | User, Experience |
| Itinerary | Personalized daily travel plans | User, ItineraryStop |
| ItineraryStop | Ordered stops within each itinerary | Itinerary, Experience |
| Community | Groups of users with shared interests | CommunityMember, Trip |
| CommunityMember | User memberships in communities | User, Community |
| Trip | Group trips organized by communities | Community, TripParticipant |
| TripParticipant | Users registered for group trips | User, Trip |
| JournalEntry | Personal travel memories and notes | User, Experience |
| UserInterest | Interests selected by users | User |

## Section 3 – Data Dictionary (Assigned Tables)

### Table: User

| Field | Type | Key | Constraints | Description |
|---|---|---|---|---|
| userId | String | PK | NOT NULL, UNIQUE | Firebase Authentication UID |
| fullName | String | — | NOT NULL | User's full name |
| email | String | — | NOT NULL, UNIQUE | User's email address |
| profileImage | String | — | NULLABLE | Profile image URL |

### Table: Experience

| Field | Type | Key | Constraints | Description |
|---|---|---|---|---|
| experienceId | String | PK | NOT NULL, UNIQUE | Unique experience identifier |
| name | String | — | NOT NULL | Experience name |
| description | String | — | NOT NULL | Experience details |
| location | String | — | NOT NULL | Location of the experience |
| category | String | — | NOT NULL | Experience category |
| imageUrl | String | — | NULLABLE | Experience image URL |

### Table: SavedPlace

| Field | Type | Key | Constraints | Description |
|---|---|---|---|---|
| savedPlaceId | String | PK | NOT NULL, UNIQUE | Unique saved place identifier |
| userId | String | FK → User | NOT NULL | User who saved the experience |
| experienceId | String | FK → Experience | NOT NULL | Saved experience |
| savedAt | Long | — | NOT NULL | Date and time the experience was saved |

### Table: Review

| Field | Type | Key | Constraints | Description |
|---|---|---|---|---|
| reviewId | String | PK | NOT NULL, UNIQUE | Unique review identifier |
| userId | String | FK → User | NOT NULL | User who submitted the review |
| experienceId | String | FK → Experience | NOT NULL | Experience being reviewed |
| rating | Int | — | NOT NULL, 1–5 | Experience rating from 1 to 5 |
| comment | String | — | NULLABLE | Optional user review comment |
| createdAt | Long | — | NOT NULL | Review creation timestamp |

### Table: Itinerary

| Field | Type | Key | Constraints | Description |
|---|---|---|---|---|
| itineraryId | String | PK | NOT NULL, UNIQUE | Unique itinerary identifier |
| userId | String | FK → User | NOT NULL | User who created the itinerary |
| title | String | — | NOT NULL | Name of the travel plan |
| date | String | — | NOT NULL | Planned travel date (YYYY-MM-DD) |
| createdAt | Long | — | NOT NULL | Creation timestamp |

### Table: ItineraryStop

| Field | Type | Key | Constraints | Description |
|---|---|---|---|---|
| stopId | String | PK | NOT NULL, UNIQUE | Unique itinerary stop identifier |
| itineraryId | String | FK → Itinerary | NOT NULL | Itinerary containing this stop |
| experienceId | String | FK → Experience | NOT NULL | Experience included in the itinerary |
| stopOrder | Int | — | NOT NULL, >= 1 | Order of the stop within the itinerary |
| plannedTime | String | — | NULLABLE | Planned visit time (HH:mm) |

## Notes

- The schema is designed for Firebase Realtime Database.
- User authentication is managed through Firebase Authentication; passwords are not stored in the User table.
- Entity names and key references must remain consistent across the ER diagram, relational schema, and Firebase plan.
- Constraints represent design requirements to be enforced through application validation and Firebase Security Rules where applicable.
- The remaining six entity dictionaries are assigned to another team member.

## AI Use Log

**Tool:** ChatGPT (OpenAI)  
**Sections:** Section 1 and Section 3  
**Purpose:** Assistance with entity identification, relationships, field definitions, data types, and documentation formatting.  
**Student review:** Reviewed and entered the entity list, descriptions, and six data dictionaries in the shared submission document. Final consistency and team review are pending.
