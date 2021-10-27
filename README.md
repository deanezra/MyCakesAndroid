# MyCakesAndroid
Demonstration Android app that shows a list of Cakes. You can click on any cake and it will show the cakes description.

# Architecture
This app uses the MVVM architecture. This is amongst other things to simplify testing and also separate concerns.
Tech used in the app:

- Livedata: The app also uses the Android X Livedata to implement the reactive parts of MVVM.
- Retrofit: For Web api network calls.
- JUnit and Mockito: Unit tests and mocking
- Glide: Simplify loading up images from URLs into the UI.

# Android studio notes

## Java 11 - If you get a compile error stating you need to be using Java 11, install Java 11 JDK on your mac and select it under:

Android Studio -> Preferences -> Build Execute and Deployment -> Built tools -> Gradle -> Gradle JDK.
