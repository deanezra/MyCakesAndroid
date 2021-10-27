# MyCakesAndroid
Demonstration Android app that shows a list of Cakes. You can click on any cake and it will show the cakes description.

# Bug in data returned from Web API:
As of 27th October 2021: Two of the cakes image urls returned by the api are not valid (http not allowed and a 404 for 'Banana cake' and 'Birthday cake'). I have worked around this by specifying a placeholder vector image to be shown via Picasso image loader. As I feel showing image loading errors is not nice UX.

API address of cakes is:
https://gist.githubusercontent.com/t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client

# Architecture
This app uses the MVVM architecture. This is amongst other things to simplify testing and also separate concerns.
Tech used in the app:

- Livedata: The app also uses the Android X Livedata to implement the reactive parts of MVVM.
- Retrofit: For Web api network calls.
- JUnit and MockK : Unit tests and mocking
- Picasso: Simplify loading up images from URLs into the UI.

# Android studio notes

## Java 11 - If you get a compile error stating you need to be using Java 11, install Java 11 JDK on your mac and select it under:

Android Studio -> Preferences -> Build Execute and Deployment -> Built tools -> Gradle -> Gradle JDK.

# Attribution of icons used in this demo app.
The placeholder cake image used when the real cake image urls fail to load is free for commercial use without attribution here: https://uxwing.com/cake-slice-icon/
