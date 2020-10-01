# Docs

Here you will find a collection of documentation you can read to better understand this project. They are here to help you get up and running fast with this project.

This documentation is written with these goals in mind:
* Explain design decision made for how this project is built. Hopefully I can answer all of your questions for, "Why was the app built this way?" or, "This seems complex. Why don't you just do it this way?"
* Teach you how to do some common tasks in this project like executing network requests, creating and running tests, and deploying your app. 
* Teach the basics behind the tools and services that this app uses. These docs do not teach Android development to you. They are meant for Android developers to learn new tools and services. It's a pain to learn new tools for every project you work with! 

If you are building a brand new app from this project, read [Setup](SETUP.md). However, if you or someone else on your team has gone through this document and performed the necessary setup instructions there is no need to read this document. 

# How to

* Perform HTTP network requests to an API. To learn how to do this, read the documents [libs/RETROFIT.md], [libs/MOSHI.md] and [libs/RXJAVA.md]. 
* Load an image from a URL (https://...) in the app. Open the file `RemoteImageView.kt` in this project and read the comment at the top. It will tell you how. 

# Design decisions 

* Does your team have a production network API and a staging network API? Are you using a service like Stripe and you want to quickly switch between the Live and Sandbox modes in the app? See the [Env](ENV.md) document to learn how to do that. 