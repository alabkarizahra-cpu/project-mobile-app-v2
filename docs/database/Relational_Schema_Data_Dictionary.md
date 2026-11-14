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
