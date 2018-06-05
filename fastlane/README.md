fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew cask install fastlane`

# Available Actions
## Android
### android test_all
```
fastlane android test_all
```
Runs all tests. UI, Unit, all of em.
### android test
```
fastlane android test
```
Runs all unit tests
### android android_test
```
fastlane android android_test
```
Run Android tests while also taking screenshots
### android install_dev
```
fastlane android install_dev
```
Install dev build to connected device
### android foo
```
fastlane android foo
```

### android beta
```
fastlane android beta
```
Submit beta build to the Play Store
### android production
```
fastlane android production
```
Submit production build to the Play Store
### android app_icon
```
fastlane android app_icon
```


----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
