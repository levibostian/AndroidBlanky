# AndroidBlanky

Boilerplate project for Android apps. The template that [I use for the apps that I build](https://curiosityio.com/).

Nodejs developer? I have [a boilerplate project for you!](https://github.com/levibostian/expressjsblanky)
iOS developer? I have [a boilerplate project for you!](https://github.com/levibostian/iosblanky)

# What are the goals of AndroidBlanky?

AndroidBlanky has been modified over years of building Android apps. Through experience, you are guaranteed to find many annoyances and bugs. After each encounter, some engineering work is done to help remove that annoyance and prevent that bug from happening again.

AndroidBlanky comes equipped with the following goals:

- Get up and running building an app as fast as possible. Pre-configure this project so you don't have to. Install set of dependencies and configure them assuming they will be used.
  - Make some assumptions about what this app will probably use such as push notifications, dynamic links, SQLite, etc. If you don't need something, just delete that code.
- Latest version of Kotlin and Android SDK.
- Support for older versions of Android so people can enjoy using their older hardware, for longer.
- Full configuration of the application through environment variables as defined [in the 12factor app](https://12factor.net/config).
- Unit, integration, and UI testing.
- Easily create mocks and work with sample data in tests.
- Use a CI server to run lint, tests on each commit of the code.
- Deployment of the app with CI server.
- Offline-first functionality through [Teller](https://github.com/levibostian/teller-android/) and [Wendy](https://github.com/levibostian/wendy-android/)

# What is included in AndroidBlanky?

Go ahead and explore the source code! No need to include _all_ of the details here, but here is a gist of the major components of this project:

- Project uses Kotlin as the programming language and Android Studio as the editor.
- Firebase setup for push notifications and some other services such as dynamic links, analytics, crash reporting.
- Room used as the database to store user data.
- Travis CI setup as the CI server to build, test, and deploy app.

# Getting started

* Before you begin, you need to install some development tools:
1. [bundler](https://bundler.io/) used to install some development tools
2. [pre-commit](https://pre-commit.com/) used for git hooks. `brew install pre-commit` works easy.
3. [ktlint](https://ktlint.github.io/) used for linting Kotlin code. `brew install ktlint` works easy.

* Now, run these commands below.

```
git clone https://github.com/levibostian/AndroidBlanky.git NameOfYourNewApp
cd NameOfYourNewApp
rm -rf .git/
git init
git config user.email "you@example.com"
git config user.name "First Last"
git add .; git commit -m "Initial commit. Created project from levibostian/AndroidBlanky boilerplate.";
bundle install

# install git hooks
./hooks/autohook.sh install
```

* Lastly, to get the app to compile you need to create some files that are hidden by default because they may contain sensitive information.

```
cp .env.example .env
cp app/src/main/res/values/env.xml.example app/src/main/res/values/env.xml
cp app/google-services.json.example app/google-services.json
```

> This project uses [cici](https://github.com/levibostian/cici/) to maintain sensitive information. Check out the project to learn how to use it.

* Delete the LICENSE file if you're writing closed source: `rm LICENSE`
* Time to try and run your app! Compile it in Android Studio and see if it builds.

Done! Well, with getting your app project renamed and built. Now comes configuration of all the various tools.

Continue reading this doc to learn about next steps to do to take full advantage of this project.

# Environments

This project assumes that you have 2 environments for your app. Production and Testing.

Production:
* Used for the public when they download the app from the App Store.
* Used for public beta testing through TestFlight.

Testing:
* Used during QA testing. QA testing at this time is only for the development team.
* Used for internal team during internal testing. This is meant for internal team to have the app on their devices to test out new features that are coming out. They can test out things that are very fresh.

Sure, you may also have a development environment. But that is unique to everyone. So, I recommend that you set the environment to the testing environment, then just edit the `.env` to edit things like the API endpoint to a development server. Try to keep as much as possible to the original testing environment.

# Next steps

Beyond getting your project to build, there are many more steps to get your project working with all of the various projects and get it deployed for others to use.

Below, you will find a todo list of tasks to complete. This list is meant to be followed in order from top to bottom as some tasks depend on other tasks.

- [ ] If you wish to deploy your app to the public, start the process of creating a new [Google Play developer account](https://play.google.com/apps/publish/). This step is optional, however. You can skip this step for now or skip it entirely if you wish to deploy your app in another way.

After you create your developer account, manually create a new Android app in the [Play Console](https://play.google.com/apps/publish). For Fastlane to push an app, it must exist first in your Play Console account.

- [ ] Open up Android Studio. Whe you get to the main menu, select Import project > open the root directory of your project.

There are many environment variables within the `.travis.yml` file that you will need to set to get things like deployment working. In the steps below when we say to *set an environment variable on the CI server*, [this is what we mean](https://docs.travis-ci.com/user/environment-variables/).

- [ ] This project uses the tool [cici](https://github.com/levibostian/cici/) to store secret files in the source code of this project. The `.cici.yml` file is already setup for you to have a testing (aka staging) environment and a production environment.

Read up on this tool so you can use it in this project.

- [ ] Create a new Firebase project. Do this by going to the [Firebase console](https://console.firebase.google.com/) and selecting "Add project". Then, you need to [create a new Android app](https://firebase.google.com/docs/android/setup) under the new Firebase project. I like to create 2 Android apps: 1 for production and 1 for the testing environment.

After you have added the Firebase apps, you need to download the `google-services.json` file that Firebase provides to you. Download this file and save it to `app/google-services.json` in your project.

> Don't forget: You will also want to store your new `google-services.json` file inside of the `_secrets` directory for cici to encrypt the file for you as well.

- [ ] Select *Set up Crashlytics* [in the Firebase console](https://console.firebase.google.com/project/_/crashlytics) for the project you just created.

- [ ] Enable Travis-CI for your GitHub repository. The `.travis.yml` file is already made and fully configured for you. You should just need to [enable Travis](https://docs.travis-ci.com/user/tutorial/) and you're good to go!

- [ ] Travis CI is setup to perform some automated tasks for you such as building and deploying your app for you, automatically. In order to do that, you need to create a GitHub bot and configure it to your repo. A bot account is simply another GitHub account that you create that is not your personal GitHub account. Once you create this account, you need to add this GitHub username to your GitHub repo. Make sure to give that username write permissions. For public projects this usually just means adding them as a contributor. For private projects, make this bot account an admin as it's required in order for the bot account to skip your branch management checks.

Then, [create a new github token](https://github.com/settings/tokens/new) for this GitHub bot account. For public projects, do not select any of the scopes. For private projects, select `repo` to give full access to private repos. Set the environment variable `GITHUB_TOKEN` in Travis CI where the value is the token that was just generated.

- [ ] Now to setup running UI tests inside of Firebase Test Lab.

We are using [this fastlane plugin](https://github.com/pink-room/fastlane-plugin-run_tests_firebase_testlab) to run tests in test lab.

* You need to create a Service Account on Google Cloud to authenticate into your Google Cloud project. To do this, Firebase project > Settings > Service accounts > Click on "X service accounts from Google Cloud Platform. It opens up Google Cloud Platform webpage for you. Your Firebase project should be selected as the Google Cloud project when this webpage is opened. This is because when you have a Firebase project, a Google Cloud project gets created for you without you even knowing it.
* If you have not already created a service account *just for firebase test lab* then let's create one. Click "Create service account" > for the name enter "firebase test lab" > for roles/permissions select "Project - Editor" > Create.
* Once you created the service account you need to then create a key file for this account to authenticate with this service account. Do this by clicking "Add key" > JSON under the service account you just created. Save this file to `_secrets/_common/fastlane/firebase_testlab_google_service_account.json` for cici to encrypt for you.
* You need to now tell Firebase what device you want to run tests against. Install `gcloud` CLI on your machine with `brew cask install google-cloud-sdk`. Then run `gcloud beta firebase test android models list` to get a table list of all the devices you have available for you.
* Set `FIREBASE_PROJECT_ID` on your CI server and you're ready to go! `fastlane ui_test` is all setup for you in the Fastfile.

- [ ] Try to compile your app in Android Studio! Build > Clean Project. Build > Rebuild Project.

You are ready for development at this point. You can build your app, write tests, run tests on your computer or CI server, and perform QA testing using Firebase App Distribution to run builds of your app on a select number of devices. However, to go beyond development, follow these steps...

- [ ] We use the tool [fastlane](http://fastlane.tools/) to deploy the app to Google on the CI server. In order for the CI server to do this, you need to do some setup. Follow [the instructions](https://docs.fastlane.tools/getting-started/android/setup/#setting-up-supply) for collecting your Google credentials. After you follow these steps, you will have a `.json` file downloaded. Save the json file exported with your keys to the path: `_secrets/fastlane/fastlane_google_service_account.json` for cici to encrypt it for you.

- [ ] You need to [create a keystore file](keystores/README.md) for releasing your app.

# Generate screenshots

Screenshots are generated automatically for you through Espresso UI tests. To create screenshots, do the following steps:

* Add `@ScreenshotOnly` to an `androidTest` instrumentation test function in your Espresso tests to mark a test as one you want to have a screenshot taken for you.
* In the test function after you mock everything and setup the environment with pre-populated data you want to be shown in the screenshot, call `screenshotUtil.takeForStore("Home screen")` to have a screenshot taken for you.
* At this time, only English US screenshots are taken for you. If you would like to generate more locales, edit the locales array in `fastlane/Fastfile` to include the ones you need.
* Lastly, start up an emulator you want to use to take the screenshots from and run this command: `bundle exec fastlane create_screenshots`.

At this time, screenshots are meant to be created on your development machine and manually upload screenshots to the Play Console.

# Deployment of your app

The CI server is setup to do this automatically! All you need to do is:
1. Create a pull request on GitHub into the `master` branch
2. Merge the pull request.

Your CI server will automatically build your app and push it to the Play Store. All thanks for [semantic-release](https://github.com/semantic-release/semantic-release) and [fastlane](http://fastlane.tools/). Awesome tools!

# Tests

Tests are setup to run automatically on the CI server for you. However, unit and instrumentation tests can be executed manually inside of Android Studio.

## Author

* Levi Bostian - [GitHub](https://github.com/levibostian), [Twitter](https://twitter.com/levibostian), [Website/blog](http://levibostian.com)

![Levi Bostian image](https://gravatar.com/avatar/22355580305146b21508c74ff6b44bc5?s=250)

## Contribute

AndroidBlanky is not open for contributions at this time. This project showcases *my* preferred way to build an Android app. If you wish to make edits, by all means, fork the repo and make your edits in your own copy of the repo.

