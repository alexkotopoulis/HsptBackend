# School Vocabulary Quiz Backend

This project contains the backend portion of a web application to train and test for school vocabulary, in particular the High School Placement Test (HSPT) vocabulary 
questions in the Reading Comprehension section. 
The backend portion consists of a SpringBoot-based REST API server that needs to be paired with the [Angular-based web frontend](https://github.com/alexkotopoulis/HsptWeb).

The frontend and backend use [Google Sign-In](https://developers.google.com/identity/sign-in/web) for authentication of users. In order to deploy the web application you will need to sign up for Google Sign-In and create a Client ID. The id needs to be stored in /src/main/resources/application-dev.properties (application-prod.properties for production context). Please see /src/main/resources/application-dev.properties.example for details.
