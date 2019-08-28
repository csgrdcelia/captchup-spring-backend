# Spring Boot backend of Captch'up game
This project was part of ESGI's J2E Framework course.

### Application
We decided to use the concept of one of my previous projects : [Captch'up android](https://github.com/csgrdcelia/captchup-android) </br>
It is a game thas uses the Cloud Vision API : the player upload any picture from his gallery and after a call to the Vision API a level is created with the 3 most relevant predictions. Then the player can try to guess them :)

### Specs
- Project divided into 4 APIs : 
  - Level : Manage the game (creation, solving...)
  - Stats : Returns user specific & global stats 
  - User : Manages user (creation of accounts, following...)
  - Config : Allow to update configuration at any time : for instance, the number of uploads authorized in a day for a user (default is 5)
- The server application was hosted on AWS, with Elastic Beanstalk and communicated with a mysql server
- The images were stored in a S3 Bucket
- The CI/CD was hosted on a linux server at home (because less expensive) and used to test and deploy to our Elastic Beanstalk server
- To make our APIs stateless we used JWT Authentication with Spring Security 
- We tested with JUnit
- I made an [IOS frontend](https://github.com/csgrdcelia/captchup-ios)

### Authors
See contributors :)
