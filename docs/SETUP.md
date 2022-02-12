# Setup

This document is meant to be a document you only need to use one time. It's the document that you use to take the [AndroidBlanky](https://github.com/levibostian/androidblanky/) project and turn it into your own project. 

Follow the instructions below. 

* Run these commands

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

> This project uses [cici](https://github.com/levibostian/cici/) to maintain sensitive information. Check out the project to learn how to use it.

* Delete the LICENSE file if you're writing closed source: `rm LICENSE`
* Go into `README.md` and delete the following sections:
  * Contribute
  * What are the goals of AndroidBlanky?
  * The very top of the file where you see the name *AndroidBlanky* and a few other sentences saying the project is boilerplate, delete all of that and rename it to the name of your app. 

You can keep the rest of the docs the way they are. They are written in a way that future developers of your project can read it and be able to get up and running. 

* Time to try and run your app! Compile it in Android Studio and see if it builds. 

Done! Well, with getting your app project renamed and built. Now comes configuration of all the various tools.

Continue reading this doc to learn about next steps to do to take full advantage of this project.

# Next steps

Beyond getting your project to build, there are many more steps to get your project working with all of the various projects and get it deployed for others to use.

Below, you will find a todo list of tasks to complete. This list is meant to be followed in order from top to bottom as some tasks depend on other tasks.

- [ ] If you wish to deploy your app to the public, start the process of creating a new [Google Play developer account](https://play.google.com/apps/publish/). This step is optional, however. You can skip this step for now or skip it entirely if you wish to deploy your app in another way.

After you create your developer account, manually create a new Android app in the [Play Console](https://play.google.com/apps/publish). For Fastlane to push an app, it must exist first in your Play Console account.

- [ ] Open up Android Studio. Whe you get to the main menu, select Import project > open the root directory of your project.

There are many environment variables within the `./github/workflows/` files that you will need to set to get things like deployment working. In the steps below when we say to *set an environment variable on the CI server*, [this is what we mean](https://docs.github.com/en/actions/security-guides/encrypted-secrets#creating-encrypted-secrets-for-a-repository).

- [ ] This project uses the tool [cici](https://github.com/levibostian/cici/) to store secret files in the source code of this project. The `.cici.yml` file is already setup for you to have a testing (aka staging) environment and a production environment.

Read up on this tool so you can use it in this project.

- [ ] GitHub Actions is setup to perform some automated tasks for you such as building and deploying your app for you, automatically. In order to do that, you need to create a GitHub bot and configure it to your repo. A bot account is simply another GitHub account that you create that is not your personal GitHub account. 

Have a private GitHub repo? 
1. Create 1 new GitHub account for your bot. 
2. Add the bot account as a collaborator to your repo so that the bot has write access to the repo. 
3. [Create a new GitHub personal access token](https://github.com/settings/tokens/new) under your bot account. Set the scope to `repo`. Set the environment variables `WRITE_ACCESS_BOT_TOKEN` and `READ_ONLY_BOT_TOKEN` to this access token that you just generated. 

Have a public GitHub repo? 
1. Create 2 new GitHub accounts for bots. 1 bot will have write access to your repository and the other will have read-only access. I like to name 1 bot as my "deployment bot" and the other as my "OSS bot". 
2. Add the deployment bot account as a collaborator to your repo so that the bot has write access to the repo. Do *not* add the OSS bot as a collaborator to the repository! 
3. [Create a new GitHub personal access token](https://github.com/settings/tokens/new) under your deployment bot account. Set the scope to `public_repo`. Set the environment variables `WRITE_ACCESS_BOT_TOKEN` to this access token that you just generated. 
4. [Create a new GitHub personal access token](https://github.com/settings/tokens/new) under your OSS bot account. Set the scope to `public_repo`. Set the environment variables `READ_ONLY_BOT_TOKEN` to this access token that you just generated. 

- [ ] Now to setup running UI tests inside of Firebase Test Lab.

We are using [this fastlane plugin](https://github.com/pink-room/fastlane-plugin-run_tests_firebase_testlab) to run tests in test lab.

* You need to create a Service Account on Google Cloud to authenticate into your Google Cloud project. To do this, Firebase project > Settings > Service accounts > Click on "X service accounts from Google Cloud Platform. It opens up Google Cloud Platform webpage for you. Your Firebase project should be selected as the Google Cloud project when this webpage is opened. This is because when you have a Firebase project, a Google Cloud project gets created for you without you even knowing it.
* If you have not already created a service account *just for firebase test lab* then let's create one. Click "Create service account" > for the name enter "firebase test lab" > for roles/permissions select "Project - Editor" > Create.
* Once you created the service account you need to then create a key file for this account to authenticate with this service account. Do this by clicking "Add key" > JSON under the service account you just created. Save this file to `_secrets/_common/fastlane/firebase_testlab_google_service_account.json` for cici to encrypt for you.
* While in the Google Cloud developer console, you need to go into the APIs section of the site and enable these 2 APIs:
  * Cloud Testing API
  * Cloud Tool Results API
* You need to now tell Firebase what device you want to run tests against. Install `gcloud` CLI on your machine with `brew cask install google-cloud-sdk`. Then run `gcloud beta firebase test android models list` to get a table list of all the devices you have available for you.
* Set `FIREBASE_PROJECT_ID` on your CI server and you're ready to go! `fastlane ui_test` is all setup for you in the Fastfile.

- [ ] Try to compile your app in Android Studio! Build > Clean Project. Build > Rebuild Project.

You are ready for development at this point. You can build your app, write tests, run tests on your computer or CI server, and perform QA testing using Firebase App Distribution to run builds of your app on a select number of devices. However, to go beyond development, follow these steps...

- [ ] We use the tool [fastlane](http://fastlane.tools/) to deploy the app to Google on the CI server. In order for the CI server to do this, you need to do some setup. Follow [the instructions](https://docs.fastlane.tools/getting-started/android/setup/#setting-up-supply) for collecting your Google credentials. After you follow these steps, you will have a `.json` file downloaded. Save the json file exported with your keys to the path: `_secrets/fastlane/fastlane_google_service_account.json` for cici to encrypt it for you.

- [ ] You need to [create a keystore file](keystores/README.md) for releasing your app.
