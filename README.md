## Explore with me

The main function of this application is to create events and participate in them. There are three main access levels. The user level allows to create events, participate in them and give them likes/dislikes. The administrative level allows to add/remove users, approve/reject events, and work with collections and event categories. The public level allows to search by events and categories.

Structurally, the application consists of 2 separate services deployed in docker-containers and 2 databases also deployed separately and connected to services. The first container contains the main service with all the business logic. The second container contains a statistics service that counts the number of event views.

Stack: Java 11, Spring Boot, JDBC, Hibernate, MapStruct, Lombock, PostgreSQL, H2.



