# AndroidBlanky

Boilerplate project for Android apps. The template that [I use for the apps that I build](https://curiosityio.com/).

Nodejs developer? I have [a boilerplate project for you!](https://github.com/levibostian/expressjsblanky)
iOS developer? I have [a boilerplate project for you!](https://github.com/levibostian/iosblanky)

# Overview of project

### What are the goals of AndroidBlanky?

AndroidBlanky has been modified over years of building Android apps. Through experience, I try to improve the way that I build Android apps as I encounter common bugs and annoyances. After each encounter, some engineering work is done to help remove that annoyance and prevent that bug from happening again.

AndroidBlanky comes equipped with the following goals:

- Get up and running building an app as fast as possible. This project is very opinionated. Development tools and Android libraries that I tend to use in every project are installed in this project. This allows me to get up and running, fast.
- Latest version of Kotlin and Android SDK. Support for older versions of Android so people can enjoy using their older hardware, for longer.
- Quick and easy setup. Should compile out of the box without having to configure the project. 
- Unit, integration, and UI testing. I love a good test suite! 
- Leverage a CI server to run tests, build the app, and deploy it. 

This project attempts to be as small as possible to help you get up and running, fast. To stay small, here are the specific tasks that the project attempts to complete. Try to be no more, no less. 

- Dependency injection setup. 
- JVM testing. Locally and on CI server. 
- Android instrumentation tests. Locally and on CI server. (Requires setup on CI server)
- JUnit test reporting when running tests so CI server can parse the results. 
- Linting of Android and Kotlin. 
- Deployment to Google Play Store via CI server. (Requires setup)
- Git hooks to perform certain tasks for you such as linting. 
- Code coverage generating if you choose to use that in some way. 
- Automated semantic versioning and git tag deployments via various CI server tools. 
- Various testing utilities:
  - Disable animations for Espresso testing. 
  - Setup mock web server to allow tests to ping a mock web server. 

Decisions made for this project
- Installed OkHttp to perform networking. Because Coil is installed, OkHttp is used for performing HTTP requests for this app. I want to challenge myself to have as little dependencies as possible in my apps for build size and maintenance. Not using a HTTP request library would be a nice challenge. However, the dependency Coil uses OkHttp in it so until I can find a good image loading library with zero HTTP request libraries in it, I'll use OkHttp. 
- Hilt is installed for dependency injection. I have considered doing dependency injection manually as my DI setup in my apps is simple: 1 graph with some dependencies singleton, most not singletons. I do not use scopes at this time for simplicity reasons. Hilt is handy because it generates my graph for me and I don't need to run tests against the graph. If my app compiles, I can feel confident that my graph will work at runtime. There are other DI tools out there but Hilt has great support and simply requires adding `@Inject` to classes. I like to avoid dependencies that deeply embed in a project and are difficult to replace at a later time if chosen. 
- Firebase Test Lab runs instrumentation tests. You can use other services to run tests against a device. 

### Services 

When I build apps, I like to use some external development services/tools to improve my developer experience and user experience. Here is a list of these services:

* [GitHub Actions](https://github.com/features/actions) - CI server provider. 

For privacy reasons, I like to avoid using 3rd party services for my apps. 

### Tools 

Below is a list of development tools that are used in this project. These are tools beyond the typical Android development tools that are common amongst Android developers such as Android Studio. Check out the app's `build.gradle` files to see what Android specific tools and libraries are installed in the app. 

* [fastlane](https://fastlane.tools/ - Tool used by the CI server to run tests and deploy the app, easily. 
* [semantic-release](https://github.com/semantic-release/semantic-release) - Tool used by the CI server to automatically deploy the app. All you need to do is follow a specific workflow in GitHub and the app will automatically deploy for you! See the [section in this doc on workflow](#workflow) to learn how to do this.

# Getting started

Follow the instructions below to get the project setup so you can begin development!

**But wait!...** If you are here looking to *build a brand new app*, follow the instructions in [this document](docs/SETUP.md) to do some initial setup of the project. If someone else on your development team has already setup the project for you, you can skip reading that document and proceed with the instructions below. 

After you clone the GitHub repository on your computer, follow these instructions:

* First, you need to install some development tools on your machine. 

1. [ktlint](https://ktlint.github.io/) used for linting Kotlin code. Run `brew install ktlint` on macOS to install. See [this doc](https://ktlint.github.io/#getting-started) to install on any other OS. You know you have bundler installed when you run `ktlint --version` on your computer and get an output like this: `X.X.X`
5. Install a newer version of Java on your machine. Android Studio comes with Java installed already. However, some of our Android development tools (such as Robolectric) requires a newer version of Java. Follow [these instructions](https://gist.github.com/levibostian/3c70bd7e78d76454c165a8f32f1ff6e9) to learn more about how to install newer versions of Java and manage them on your machine. Open `./.github/workflows/test.yml` and view the java version the CI server is using. That's the Java version you should use for local development. 

* Next, run these commands on your machine. 

```
./hooks/autohook.sh install  # Install git hooks
```

* Open Android Studio. Select `Import project (gradle)`. Then, select the root directory of this project on your computer. 

* You're all set! Compile the app within Android Studio. You can also execute unit, integration, and UI tests within Android Studio. [This document](https://developer.android.com/training/testing/unit-testing/local-unit-tests#run) tells you how to run tests within Android Studio. 

### Generate screenshots

This may not work at this time... I want to take a step back and reconsider the implementation. See if there is a better way. 

Need to take screenshots of the app for the app store? Screenshots are generated automatically for you through Espresso UI tests. To create screenshots, do the following steps:

* Add `@ScreenshotOnly` to an `androidTest` instrumentation test function in your Espresso tests to mark a test as one you want to have a screenshot taken for you.
* In the test function after you mock everything and setup the environment with pre-populated data you want to be shown in the screenshot, call `screenshotUtil.takeForStore("Home screen")` to have a screenshot taken for you.
* At this time, only English US screenshots are taken for you. If you would like to generate more locales, edit the locales array in `fastlane/Fastfile` to include the ones you need.
* Lastly, start up an emulator you want to use to take the screenshots from and run this command: `bundle exec fastlane create_screenshots`.

At this time, screenshots are meant to be created on your development machine and manually upload screenshots to the Play Console. 

## Author

* Levi Bostian - [GitHub](https://github.com/levibostian), [Website/blog](https://levi.earth)

![Levi Bostian image](https://gravatar.com/avatar/22355580305146b21508c74ff6b44bc5?s=250)

## Contribute

AndroidBlanky is not open for contributions at this time. This project showcases *my* preferred way to build an Android app. If you wish to make edits, by all means, fork the repo and make your edits in your own copy of the repo.

