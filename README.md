# Mancala Game

This is a simple web application which is developed to represent
the game called Mancala.

## Game details

### Board Setup

Each of the two players has his six pits in front of him. To the right of the six pits, each player has a larger pit. At
the start of the game, there are six stones in each of the six round pits . Rules

### Game Play

The player who begins with the first move picks up all the stones in any of his own six pits, and sows the stones on to
the right, one in each of the following pits, including his own big pit. No stones are put in the opponents' big pit. If
the player's last stone lands in his own big pit, he gets another turn. This can be repeated several times before it's
the other player's turn.

### Capturing Stones

During the game the pits are emptied on both sides. Always when the last stone lands in an own empty pit, the player
captures his own stone and all stones in the opposite pit (the other player’s pit) and puts them in his own (big or
little?) pit.

### The Game Ends

The game is over as soon as one of the sides runs out of stones. The player who still has stones in his pits keeps them
and puts them in his big pit. The winner of the game is the player who has the most stones in his big pit.



You can try the demo using below,


## Getting started

### Start and access the backend services

1. Clone or Download the manacala-backend repository
2. Go to project home and run the command </br>"mvn spring-boot:run". </br>It will start and run the project
3. To check the services you can access using any rest client tools such as Postman via http://localhost:8081/mancala/newgame
4. If you want to access already started game, You can access using below url format,

   [http://localhost:8081/{gameId}/pits/{pitId}](http://localhost:8081/{gameId}/pits/{pitId})

### Start and access the frontend react services

1. Clone or Download the manacala-frontend repository
2. Go to project home and run the below  commands </br>
 "npm i". </br>
 "npm start" </br>
It will start and run the project
3. Can  access the application use the URL http://localhost:3000


## Installation

### Prerequisites

* Java 8 or higher
* Maven 3.6.3 or Higher
* Node (16.13.2 or higher) </br>
* NPM (8.1.2)
* Internet connectivity to download dependencies, node, npm and etc

## Developer Guide

### Technologies & Frameworks Used

* Java 8
* Spring Boot 2.6.3
* React 17.0.2
* Material UI 5.3.0

Java with Spring Boot framework is used to develop more extensible, maintainable and readable code within very short
development time.

React with Material UI is used for UI. It is much easy to implement simple UI like this without having prior knowledge
regarding UI technologies. Componentized nature of React gives lots of flexibility to component reuse and code reuse. So
using React with Material UI inbuilt components, help to write a simple UI within a very short development time.

### Steps - Running with Tests

1. Go to project home and run the command "mvn spring-boot:run". It will start and run the project.
2. You can access the UI via http://localhost:8080/
3. You can run test suit using the command "mvn clean install". Once it is completed, test reports are available under the
   following directories.

            target/site/jacoco/index.html

### Design Decisions

This application is developed based on the client-server architecture where we have simple React UI to represent the
Mancala Game UI and REST APIs to serve the requests coming from the ReactUI

We have segregated the backend and front end as separate applications to
facilitates the further expansion and enhancement of the project.

H2 In-Memory database is used to persist the game states. Because it provides flexibility to set up and run the entire
project without facing many difficulties like configuring the database and etc. But if we need to change the database,
you can change it to any other RDBMS adding the following configuration in application.properties file

            spring.datasource.url=
            spring.datasource.driverClassName=
            spring.datasource.username=
            spring.datasource.password=
            spring.jpa.database-platform=


### Diving Into Source Code

#### Under Mancala-backend project

This contains the complete backend for Mancala Game. It has three main layers as below,

1. Controller Layer - It has MancalaGameController. MancalaGameController exposes APIs to create, load
   and play the game.

2. Service Layer -The service layer has been segregated in to two parts.</br>
   &nbsp;&nbsp;&nbsp;&nbsp;a. Direct services which expose to the controller layer. It has the MancalaGameService that handles service related logic</br>
   &nbsp;&nbsp;&nbsp;&nbsp;b. Business related logic specific to the game rule. It has the MancalaGameEngine that handles the entire logic for the Mancala Game.</br>
In the project, we have used MancalaGame, MancalaSmallPit and MancalaPlayer classes to represent game entities as Objects in our source code.
   MancalaGameEngine is using the above-mentioned classes to implement the logic for the game.

3. Data Repository - This project uses inbuilt spring-data-JPA support to persist the Mancala Game states.

#### Under Mancala-front project

This contains all React components that are used to design the Game UI. Since React framework is a component-based
framework, we developed several UI components and reused them within the Game UI.

### Possible Future Improvements
#### GUI improvements
* Upgrade front ent with rich UI/UX.

#### Game Play Improvements
* Introduce an AI to play with
* Introduce user login base enhanced experience to the user
