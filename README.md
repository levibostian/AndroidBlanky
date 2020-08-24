# AndroidBlanky

Boilerplate project for Android apps. The template that [I use for the apps that I build](https://curiosityio.com/).

Nodejs developer? I have [a boilerplate project for you!](https://github.com/levibostian/expressjsblanky)
iOS developer? I have [a boilerplate project for you!](https://github.com/levibostian/iosblanky)

# Overview of project

### What are the goals of AndroidBlanky?

AndroidBlanky has been modified over years of building Android apps. Through experience, I try to improve the way that I build Android apps as I encounter common bugs and annoyances. After each encounter, some engineering work is done to help remove that annoyance and prevent that bug from happening again.

AndroidBlanky comes equipped with the following goals:

- Get up and running building an app as fast as possible. This project is very opinionated. Development tools and Android libraries have already been installed and setup for you. This allows you to get up and running, fast. If you don't need a feature added to the boilerplate, just delete that code.   
- Latest version of Kotlin and Android SDK. Support for older versions of Android so people can enjoy using their older hardware, for longer.
- Full configuration of the application through environment variables as defined [in the 12factor app](https://12factor.net/config).
- Unit, integration, and UI testing.
- Leverage a CI server to run tests, build the app, and deploy it. 

### Services 

When I build apps, I like to use some external development services/tools to improve my developer experience and user experience. Here is a list of these services:

* [Travis CI](https://travis-ci.com/) - CI server provider. 
* [Firebase](https://firebase.google.com/) - Provides a few pieces of functionality to the app. 
  * [Crash reporting](https://firebase.google.com/products/crashlytics) - When the app crashes at runtime, we get notified so we can fix the issue. We get notified when crashes happen automatically.
  * [Performance monitoring](https://firebase.google.com/products/performance) - Finds places in your code where the app is slow. When the network is slow or when you are slowing down the UI thread of the app. 
  * [Test lab](https://firebase.google.com/products/test-lab) - Run UI or integration tests on a real device in the cloud. Running tests on an actual device is much easier and faster then alternatives such as running them on an emulator instance running on the CI server. The CI server communicates with Test Lab for you. 
  * [App distribution](https://firebase.google.com/products/app-distribution) - Allows people on your team to install builds of the app on their devices. This is used for QA testing of the app before we send it out to beta users. App distribution is not good for running public beta tests. It's great for using in your internal team. 
  * [Remote config](https://firebase.google.com/products/remote-config) - Allows us to make changes to the behavior of our app without needing to deploy a new version to the app store. We mostly use this service to change the text shown in the app. You can even run A/B tests on remote config values which allows you to A/B test colors, text, or features of your app. 
  * [Push notifications](https://firebase.google.com/products/cloud-messaging) - Send push notifications to the app users. You can manually send push notifications for marketing purposes using the Firebase website. We also may send automated push notifications from the API. We can send push notifications to specific devices, all devices for a user, or to groups of users at once. 
  * [Analytics](https://firebase.google.com/products/analytics) - Analytics to track user activity within our app. Allows us to see how the users are using our app so we can make changes in the future based off of user usage. 
  * [Dynamic links](https://firebase.google.com/products/dynamic-links) - When the user of our app opens a URL for our website, we can have our app open instead of having the URL open in the browser on the device. This is how we perform login to an account in our app. You enter your email address into the app, the API sends you a unique URL, you open the URL on your device and we automatically log you into the app when the app opens from the URL. 

### Tools 

Below is a list of development tools that are used in this project. These are tools beyond the typical Android development tools that are common amongst Android developers such as Android Studio. Check out the app's `build.gradle` files to see what Android specific tools and libraries are installed in the app. 

* [cici](https://github.com/levibostian/cici/) - Used to securely store secret files within the source code repository. If you have files in your project that contain secret passwords or API keys, use cici to store those files in your code base. Do not commit these secret files into your source code repository! cici is a great way to store your upload keystore files, share secrets with your development team, white label your app, prepare for your app environments (staging, QA, beta, production builds of your app). 
* [fastlane](https://fastlane.tools/ - Tool used by the CI server to run tests and deploy the app, easily. 
* [semantic-release](https://github.com/semantic-release/semantic-release) - Tool used by the CI server to automatically deploy the app. All you need to do is follow a specific workflow in GitHub and the app will automatically deploy for you! See the [section in this doc on workflow](#workflow) to learn how to do this.

# Getting started

Follow the instructions below to get the code compiling on your machine so you can begin development!

**But wait!...** If you are here looking to *build a brand new app*, follow the instructions in [this document](docs/SETUP.md) to do some initial setup of the project. If someone else on your development team has already setup the project for you, you can skip reading that document and proceed with the instructions below. 

After you clone the GitHub repository on your computer, follow these instructions:

* First, you need to install some development tools on your machine. 

1. [bundler](https://bundler.io/) - Run `gem install bundler` to install it on your computer. (`gem` is a tool that is installed on your machine after you install the Ruby programming language. macOS machines already have Ruby installed by default). You know you have bundler installed when you run `bundle -v` on your computer and get an output like this: `Bundler version X.X.X`
2. [pre-commit](https://pre-commit.com/) used for git hooks. Run `brew install pre-commit` on macOS to install. See [this doc](https://pre-commit.com/#install) to install on any other OS. You know you have pre-commit installed when you run `pre-commit --version` on your computer and get an output like this: `pre-commit X.X.X`
3. [ktlint](https://ktlint.github.io/) used for linting Kotlin code. Run `brew install ktlint` on macOS to install. See [this doc](https://ktlint.github.io/#getting-started) to install on any other OS. You know you have bundler installed when you run `ktlint --version` on your computer and get an output like this: `X.X.X`
4. [cici](https://github.com/levibostian/cici/#getting-started) used to store secret files in your source code. Run `gem install cici` to install on your computer. 
5. Install a newer version of Java on your machine. Android Studio comes with Java installed already. However, some of our Android development tools (such as Robolectric) requires a newer version of Java. Follow [these instructions](https://gist.github.com/levibostian/3c70bd7e78d76454c165a8f32f1ff6e9) to learn more about how to install newer versions of Java and manage them on your machine. You know everything is setup correctly when you run `jenv global` on your machine and a number is shown to you. Open the `.travis.yml` file and look for the term `openjdkXX` towards the top. `XX` is the version of Java to install on your machine. 

* Next, run these commands on your machine. 

```
bundle install               # Install development tools 
./hooks/autohook.sh install  # Install git hooks
cici decrypt                 # When running this command, it will ask you for a KEY and IV value. Your team members should have shared these passwords with you. Enter those at that time. 
```

* Open Android Studio. Select `Import project (gradle)`. Then, select the root directory of this project on your computer. 

* You're all set! Compile the app within Android Studio. You can also execute unit, integration, and UI tests within Android Studio. [This document](https://developer.android.com/training/testing/unit-testing/local-unit-tests#run) tells you how to run tests within Android Studio. 

# Workflow 

This project is setup to follow a strict development workflow. This workflow when followed will automatically (1) run all of your tests to make sure they are all passing, (2) build and deploy your app for easy QA testing, (3) build and deploy your app for the app store. All of this is done for you! All you need to do is write code! 

There are some rules that you must follow for the workflow to work:
1. Never make a commit to the branches: `master`, `beta`, or `alpha`. These branches can only be updated by making a GitHub pull request into these branches. If you make a mistake and make a commit on one of these branches, [follow these instructions](https://stackoverflow.com/a/1628584/1486374) to move your commits to another branch. 
2. When you make a commit, the commit message that you write must be written in a special way. Read [this document](https://gist.github.com/levibostian/71afa00ddc69688afebb215faab48fd7) which explains what I mean by that. 

With those rules out of the way, let's go over the workflow for you to follow when you write code. 

Let's say that you want to make a change to the code. Any change, it does not matter. Follow these steps:
1. Make a new git branch off of the `master` branch. Name this branch something that is not `master`, `beta`, or `alpha` as those are reserved for special purposes. 
2. Make your code changes. Make commits in this branch. 
3. When you are done with all of your changes you want to make, [create a GitHub pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request). 

When you create your pull request it's important for you to choose the correct branch to merge your code into. Choose one of the following branches to merge your code into:
* `master` - Your change is "production ready". This change can go out to public beta customers. It can be public. If you making a small change to your app, this is a good choice to choose. You will probably choose this option most of the time. 
* `beta` - Your change is not yet ready for production. Let's say that you and your team are preparing for a big change to your mobile app that will take 2 to 3 weeks to finish. This big change might require 10 separate pull requests to finish. This branch should be used for that purpose. Merge all 10 of the separate pull requests into the `beta` branch. When all 10 of your team's pull requests are merged in and your big changes to your app are done, make a new pull request from the `beta` branch into the `master` branch to make the public release. This branch is used less often then `master`, but can still come in handy every so often. 
* `alpha` - This branch is not used very often. It's mostly used for very early progress to an app. Maybe you and your team are making drastic changes to your mobile app but it's still early on in the project, use this branch. This branch is like the `beta` branch, but meant for code that is more conceptual, unstable, or could dramatically change. 

4. After you make your pull request, do not merge it until [the status checks](https://docs.github.com/en/github/administering-a-repository/about-required-status-checks) have passed. 

If you are on a software team that performs code reviews, you may need to also wait until someone from your team reads over your code and [submits a code review](https://docs.github.com/en/enterprise/2.13/user/articles/approving-a-pull-request-with-required-reviews). 

The CI server will automatically build your app and deploy the app to Firebase App Distribution for you to easily install the app on your device to test it out. This makes QA testing on your team super easy. Your team members do not need to clone your changes to their machine and manually build the app to test it out. The CI server will make a comment giving you information about the deployed app. All you need to do is open the App Distribution app on your test device, install the build you want, then run the app. 

After you QA test the app, all tests pass, and your code review has been completed, merge the pull request! 

5. After the code is merged you are all done for now. The CI server is going to automatically deploy your app for you! 

> Note: The CI server can always fail when it's trying to deploy your app. You know the deployment was successful if you see a comment on your pull request [similar to this one](https://github.com/levibostian/Swapper-iOS/pull/20#issuecomment-676499660). It may take a while for this comment to show up. Check the CI server logs if the task fails so you can fix it. 

6. The CI server will *only build your app and send it to the app store for you*. It will now deploy it to the public. That is a manual step that you need to do. Use the [Google Play developer console](https://play.google.com/apps/publish/) to publish the app to public beta or to the public. 

There are ways to make the CI server automatically deploy your app for you instead of requiring you to login. However, it has been decided to make it manual because...
* Future proof - If Google makes a change in the future that breaks your CI server build and deploy process, you need to do it manually anyway. 
* Reminder to check metadata - This gives you the opportunity to inspect the metadata and screenshots of your app store page before you deploy it. This gives you the chance to make changes before it's live. 
* Flexibility - When a build of the app is sent to the app store, you get to choose who to deploy it to and when. Run an internal beta test, public beta test, or launch to production with a couple clicks of a button for every build. 

It's recommended that you follow this deployment process:
* The CI server builds and sends it to the app store after you merge a pull request. 
* Login to the Google Play developer console and make a release of the app to public beta. Run this beta test for however long you feel is necessary. Watch app analytics and crash reporting to make a judgement if the build of the app is stable. 
  * If you determine the app is not stable, follow the workflow above to fix the issues and make a new public beta release. 
  * If the app is determined stable, log into the Google Developer developer console and make a new public release of the app from the build the CI server made for you. 

### Generate screenshots

Need to take screenshots of the app for the app store? Screenshots are generated automatically for you through Espresso UI tests. To create screenshots, do the following steps:

* Add `@ScreenshotOnly` to an `androidTest` instrumentation test function in your Espresso tests to mark a test as one you want to have a screenshot taken for you.
* In the test function after you mock everything and setup the environment with pre-populated data you want to be shown in the screenshot, call `screenshotUtil.takeForStore("Home screen")` to have a screenshot taken for you.
* At this time, only English US screenshots are taken for you. If you would like to generate more locales, edit the locales array in `fastlane/Fastfile` to include the ones you need.
* Lastly, start up an emulator you want to use to take the screenshots from and run this command: `bundle exec fastlane create_screenshots`.

At this time, screenshots are meant to be created on your development machine and manually upload screenshots to the Play Console. 

## Author

* Levi Bostian - [GitHub](https://github.com/levibostian), [Website/blog](http://levibostian.com)

![Levi Bostian image](https://gravatar.com/avatar/22355580305146b21508c74ff6b44bc5?s=250)

## Contribute

AndroidBlanky is not open for contributions at this time. This project showcases *my* preferred way to build an Android app. If you wish to make edits, by all means, fork the repo and make your edits in your own copy of the repo.

