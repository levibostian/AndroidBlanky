# AndroidBlanky
Android boilerplate project I use for all the Android apps that I build. Clone the repo, edit some configurations, and get off to building your next awesome app!

iOS developer? I have [a boilerplate project for you!](https://github.com/levibostian/iosblanky)
Nodejs API developer? I have [a boilerplate project for you!](https://github.com/levibostian/expressjsblanky)

# What is AndroidBlanky?

AndroidBlanky is an Android app. It's a very simple Android app that includes a collection of libraries, configurations, and architecture decisions for all of the apps that I build. So whenever I want to build a new Android, instead of creating a new Android Studio project and spending a bunch of time configuring it to include all my libraries, configurations, and architecture decisions in it that I want, I simply clone this repo, do some editing to the project, and I am off to building my app!

# Why use AndroidBlanky?

You know the feeling when you go to begin a new project. The day you begin a project is spent on just setting up your project, configuring it, and getting your environment all setup. It takes hours to days to complete!

AndroidBlanky works to avoid that. By having a blank Android app already created, I am able to copy it and I am ready to jump right into developing my new app. AndroidBlanky works to get you to building your app within minutes instead of hours or days.

# What is included in AndroidBlanky?

### Language:

* [Kotlin](https://kotlinlang.org/) -  Yeah, Kotlin is awesome. It's Java but better.

### Libraries:

* AppCompat
* Android Design support library
* RecyclerView
* [Teller](https://github.com/levibostian/teller-android) - To help build offline-first mobile apps by caching retrieved data for offline viewing.
* [Wendy](https://github.com/levibostian/wendy-android) - To help build offline-first mobile apps by pushing data to my API in the background.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) Architecture components - Great way to work with data and the Android life cycle.
* [Room](https://developer.android.com/topic/libraries/architecture/room) - Easier way to work with SQLite on Android.
* [Dagger 2](https://github.com/google/dagger) - Easier dependency management for Android to make testing easier.
* [RxJava2](https://github.com/ReactiveX/RxJava), [RxAndroid](https://github.com/ReactiveX/RxAndroid) - Make data observable in my apps to update the UI of my apps easily.
* [RxPreferences](https://github.com/f2prateek/rx-preferences) - RxJava2 for SharedPreferences.
* [Moshi](https://github.com/square/moshi) - Deserialize JSON into models.
* [Retrofit](https://github.com/square/retrofit) - Easy way to perform network calls.
* [OkHttp logging interceptor](https://github.com/square/okhttp) - Log network calls to the Logcat during development for debugging.
* [Eventbus](https://github.com/greenrobot/EventBus) - An event bus to communicate in my app.
* [Timber](https://github.com/JakeWharton/timber/) - Unified logging to the Logcat and to Crashlytics within my app.
* [Android-License-Fragment](https://github.com/first087/Android-License-Fragment) - To easily create an open source licenses list.

##### For testing...

* [Truth](https://github.com/google/truth/) - Assertions library used by Google.
* [Mockito](https://github.com/mockito/mockito) - Mocking objects for testing.
* [Android testing support library](https://developer.android.com/training/testing/) - To run unit, integration, and UI tests for Android.
* [Espresso](https://developer.android.com/training/testing/espresso/) - Create UI tests on Android.
* [Fastlane screengrab](https://docs.fastlane.tools/actions/screengrab/) - Take screenshots of app during UI tests to help debug UI tests and to use for creating screenshots for the Play Store.

### Tools and services:

* [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics/) - Crash reporting.
Configure: After you follow the directions in [Getting started](#getting-started) for setting up Firebase, this is done for you.

* [Firebase Analytics](https://firebase.google.com/docs/analytics/) - Mobile analytics.
Configure: After you follow the directions in [Getting started](#getting-started) for setting up Firebase, this is done for you.

* [Fastlane](https://fastlane.tools/) - Distributing the app to the Play Store. Pretty much automates tasks for me.
Configure: Edit the files in the `fastlane/` directory to configure it for your project.

* [Travis CI](https://travis-ci.com/) - CI server to run tests and distribute apps to the Play Store. Pretty much all automation that I can.
Configure: Create a [Travis](https://travis-ci.com/) account, and enable your GitHub repo for your Android project in your Travis profile page.

* [Danger](http://danger.systems/ruby/) - Bot that looks over my pull requests and make sure I do not forget to complete certain tasks.
Configure: [Here are instructions](http://danger.systems/guides/getting_started.html#creating-a-bot-account-for-danger-to-use) for adding a Danger bot to your repo. This depends on if your app is an open source or closed source project.

# Getting started

The instructions below assume you are on a macOS machine and are comfortable with the command line. Are you on Windows or want to use a GUI? Feel free to make a pull request with the instructions!

```
git clone https://github.com/levibostian/AndroidBlanky.git NameOfYourNewApp
cd NameOfYourNewApp
rm -rf .git/
git init
git config user.email "you@example.com"
git config user.name "First Last"
git add .; git commit -m "Initial commit. Created project from levibostian/AndroidBlanky boilerplate.";
bundle install # This is a ruby tool, [Bundler](https://bundler.io/). This is for running [Fastlane](https://fastlane.tools/).
```

Now, open up the Android project in Android Studio. Everything should Gradle sync and build successfully.

* Open the `app/build.gradle` file. Find and replace this line: `applicationId "com.levibostian.androidblanky"` with your own namespace for your app.
* Edit the package names of your source code to your namespace as well. [This is how to do that](https://stackoverflow.com/a/29092698/1486374)

Now it's time to setup Firebase.

* [Create a new Firebase application via a web browser and setup Firebase in app](https://firebase.google.com/docs/android/setup). The big thing to do here is to download a `google-services.json` file and save that to `app/google-services.json` replacing the existing one there already.
* Select `Set up Crashlytics` [in the Firebase console](https://console.firebase.google.com/project/_/crashlytics) for the project you just created (if `Set up Crashlytics` is not visible, simply [follow these directions](https://firebase.google.com/docs/crashlytics/get-started) to enable Crashlytics in your project).
* Build > Clean Project. Build > Rebuild Project.

### Things to edit beyond setup

* In production/strings.xml it says AndroidBlanky. Change name your app name.
* If calling an API, then go into `src/main/java/service/` and edit the name of `GitHubApi.java` to be your API name. If not calling an API, go into the manifest file and delete the INTERNET permission request.
* Edit the theme in the `app/src/main/res/values/styles.xml`.
* Edit `fastlane/Fastfile` to work for your app.
* Edit `DatabaseManager`'s DATABASE_NAME.
* Edit 'res/values/strings.xml' file to edit the strings put in place.

## Generate app icons

* Create a 128x128px icon and put into `fastlane/metadata/icons/app_icon.png`. Then run `fastlane app_icon` to generate them.

# Release app to Play Store

AndroidBlanky uses [Fastlane](https://fastlane.tools/) to distribute apps to the Play Store for beta testing and production releases.

When creating build to release to Play Store:

* [Create a keystore file](keystores/README.md).
* Edit your `app/build.gradle` file editing the `signingConfig` section with the passwords and location to your keystore file.
* Manually create a new Android app in the [Play Console](https://play.google.com/apps/publish). For Fastlane to push an app, it must exist first in your Play Console account.
* Edit the `prod_deploy` section of `fastlane/Fastfile` to set it up for your account.

# Tests

* Run JUnit unit tests:

`fastlane test`

* Run Android Espresso tests and take screenshots:

`fastlane android_test`

## Author

* Levi Bostian - [GitHub](https://github.com/levibostian), [Twitter](https://twitter.com/levibostian), [Website/blog](http://levibostian.com)

![Levi Bostian image](https://gravatar.com/avatar/22355580305146b21508c74ff6b44bc5?s=250)

## Contribute

AndroidBlanky is not open for contributions at this time. This project showcases *my* preferred way to build an Android app. If you wish to make edits, by all means, fork the repo and make your edits in your own copy of the repo.

I do make some exceptions for contributions. I allow the following:

* Edits to this `README.md` document.