LocalLens – Relational Schema & Data Dictionary
Project: LocalLens
Milestone: 3-A
Document: Relational Schema & Data Dictionary
1. Overview
This document describes the relational schema and data dictionary for the following six LocalLens tables:
Community
CommunityMember
Trip
TripParticipant
JournalEntry
UserInterest
Key Definitions
PK (Primary Key): Uniquely identifies each record.
FK (Foreign Key): References a primary key in another table.
NOT NULL: A value must be provided.
NULLABLE: A value may be omitted.
UNIQUE: A value must not be duplicated.
2. Data Dictionary
Table: Community
Field Data Type Key Constraints Description
communityId String PK NOT NULL, UNIQUE Unique community identifier.
name String — NOT NULL Community name.
description String — NOT NULL Community description.
imageUrl String — NULLABLE Community image URL.
createdAt Long — NOT NULL Community creation timestamp.
Table: CommunityMember
Field Data Type Key Constraints Description
membershipId String PK NOT NULL, UNIQUE Unique membership identifier.
userId String FK → User.userId NOT NULL User who joined the community.
communityId String FK → Community.communityId NOT NULL Community the user joined.
joinedAt Long — NOT NULL Membership creation timestamp.
Table: Trip
Field Data Type Key Constraints Description
tripId String PK NOT NULL, UNIQUE Unique trip identifier.
communityId String FK → Community.communityId NOT NULL Community organizing the trip.
title String — NOT NULL Trip title.
description String — NOT NULL Trip details.
destination String — NOT NULL Trip destination.
startDate String — NOT NULL Trip start date (YYYY-MM-DD).
endDate String — NOT NULL Trip end date (YYYY-MM-DD).
meetingTime String — NOT NULL Scheduled departure or gathering time (HH:MM).
meetingPoint String — NOT NULL Meeting location for trip participants.
Table: TripParticipant
Field Data Type Key Constraints Description
participationId String PK NOT NULL, UNIQUE Unique participation identifier.
userId String FK → User.userId NOT NULL User participating in the trip.
tripId String FK → Trip.tripId NOT NULL Trip joined by the user.
joinedAt Long — NOT NULL Participation timestamp.
status String — NOT NULL, DEFAULT 'JOINED' Participation status.
Table: JournalEntry
Field Data Type Key Constraints Description
entryId String PK NOT NULL, UNIQUE Unique journal entry identifier.
userId String FK → User.userId NOT NULL User who created the entry.
experienceId String FK → Experience.experienceId NULLABLE Related tourist experience, if applicable.
title String — NOT NULL Journal entry title.
content String — NOT NULL Travel memory or notes.
imageUrl String — NULLABLE Journal image URL.
createdAt Long — NOT NULL Entry creation timestamp.
Table: UserInterest
Field Data Type Key Constraints Description
userInterestId String PK NOT NULL, UNIQUE Unique user interest identifier.
userId String FK → User.userId NOT NULL User who selected the interest.
interestName String — NOT NULL Selected interest category.
3. Relationship Summary
Relationship Cardinality Description
Community – CommunityMember One-to-Many (1:N) A community can have multiple members; each membership belongs to one community.
Community – Trip One-to-Many (1:N) A community can organize multiple trips; each trip belongs to one community.
User – CommunityMember One-to-Many (1:N) A user can have multiple community memberships.
User – TripParticipant One-to-Many (1:N) A user can participate in multiple trips.
Trip – TripParticipant One-to-Many (1:N) A trip can have multiple participants.
User – JournalEntry One-to-Many (1:N) A user can create multiple journal entries.
Experience – JournalEntry One-to-Many (1:N) An experience can be associated with multiple journal entries; experienceId is nullable in JournalEntry.
User – UserInterest One-to-Many (1:N) A user can have multiple interests.
4. Notes
JournalEntry.experienceId is nullable, so a journal entry may be created without being linked to a specific experience.
LocalLens – Milestone 3-A
Relational Schema and Data Dictionary
Team: Group 04
Application: LocalLens – Discover Saudi Arabia
Database: Firebase Realtime Database
Contributor: [Your Name]
Section 1 – Data Dictionary
Table: Community
Field Type Key Constraints Description
communityId String PK NOT NULL, UNIQUE Unique community identifier
name String — NOT NULL Community name
description String — NOT NULL Community description
imageUrl String — NULLABLE Community image URL
createdAt Long — NOT NULL Community creation timestamp
Table: CommunityMember
Field Type Key Constraints Description
membershipId String PK NOT NULL, UNIQUE Unique membership identifier
userId String FK NOT NULL User who joined the community
communityId String FK NOT NULL Community the user joined
joinedAt Long — NOT NULL Membership creation timestamp
Table: Trip
Field Type Key Constraints Description
tripId String PK NOT NULL, UNIQUE Unique trip identifier
communityId String FK NOT NULL Community organizing the trip
title String — NOT NULL Trip title
description String — NOT NULL Trip details
destination String — NOT NULL Trip destination
startDate String — NOT NULL Trip start date
endDate String — NOT NULL Trip end date
meetingPoint String — NOT NULL Meeting location
meetingTime String — NOT NULL Scheduled meeting time
Table: TripParticipant
Field Type Key Constraints Description
participationId String PK NOT NULL, UNIQUE Unique participation identifier
userId String FK NOT NULL User participating in the trip
tripId String FK NOT NULL Trip joined by the user
joinedAt Long — NOT NULL Participation timestamp
status String — NOT NULL, DEFAULT 'JOINED' Participation status
Table: JournalEntry
Field Type Key Constraints Description
entryId String PK NOT NULL, UNIQUE Unique journal entry identifier
userId String FK NOT NULL User who created the entry
experienceId String FK NULLABLE Related tourist experience
title String — NOT NULL Journal entry title
content String — NOT NULL Travel memories and notes
imageUrl String — NULLABLE Journal image URL
createdAt Long — NOT NULL Entry creation timestamp
Table: UserInterest
Field Type Key Constraints Description
userInterestId String PK NOT NULL, UNIQUE Unique user interest identifier
userId String FK NOT NULL User who selected the interest
interestName String — NOT NULL Selected interest category
Section 2 – Relationships
Relationship Cardinality
User – CommunityMember 1:N
Community – CommunityMember 1:N
Community – Trip 1:N
User – TripParticipant 1:N
Trip – TripParticipant 1:N
User – JournalEntry 1:N
Experience – JournalEntry 1:N
User – UserInterest 1:N
Section 3 – Notes
JournalEntry.experienceId is nullable because a journal entry does not always need to be linked to an experience.
All primary keys must be unique.
Foreign keys maintain relationships between the database tables.
TripParticipant.status has a default value of 'JOINED'.
