require 'trent'

########## Edit the values below ##########

# You can copy this below value from `app/build.gradle` android -> defaultConfig -> testInstrumentationRunner
google_project_id = "project-id-here"
google_service_account = "firebase-test-lab@yourproject.iam.gserviceaccount.com"
test_runner_file_namespace = "com.levibostian.AndroidTestTestRunner"

###########################################

ci = Trent.new
ci.config_travis(ENV["TRAVIS_API_KEY"], true)
ci.config_github(ENV['DANGER_GITHUB_API_TOKEN'])
ci.path('gcloud', './google-cloud-sdk/bin/gcloud')
ci.travis.fail_if_pr_branch_build_failed()

# If you want to ignore tests with an annotation, add the following to the gcloud command where tests are being run
# --test-targets \"notAnnotation com.yourpackage.ScreenshotOnly\"
# docs: https://cloud.google.com/sdk/gcloud/reference/beta/firebase/test/android/run
#
# As of now, we are running all tests so that when we need to generate screenshots, it's guaranteed that everything will work.

# Versions found https://cloud.google.com/sdk/docs/downloads-versioned-archives
google_cloud_sdk_filename = "google-cloud-sdk-239.0.0-linux-x86_64.tar.gz"

ci.sh("wget https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/#{google_cloud_sdk_filename}")
ci.sh("tar -xzf #{google_cloud_sdk_filename}")
ci.sh("echo 'n' | ./google-cloud-sdk/install.sh")

ci.sh("gcloud config set project #{google_project_id}")
ci.sh('gcloud --quiet components update')
ci.sh('gcloud --quiet components install beta')
ci.sh("gcloud auth activate-service-account #{google_service_account} --key-file $(pwd)/app/firebase-test-lab-service-account.json")

ci.sh('./gradlew :app:assembleDevelopmentDebug -PdisablePreDex')
ci.sh('./gradlew :app:assembleDevelopmentDebugAndroidTest -PdisablePreDex')

test_results = ci.sh("echo 'y' | gcloud beta firebase test android run --app app/build/outputs/apk/development/debug/app-development-debug.apk --test app/build/outputs/apk/androidTest/development/debug/app-development-debug-androidTest.apk --test-runner-class \"#{test_runner_file_namespace}\" --device model=Pixel2,version=28,locale=en,orientation=portrait", fail_non_success: false)
test_output = test_results[:output]

#### Parse the output to send to GitHub comments.
# Help about parsing from: https://stackoverflow.com/a/19482947/1486374

# test_output at the very end, looks like this:
# ... test stuff here.
# Instrumentation testing complete.

# More details are available at [https://console.firebase.google.com/project/project-name/testlab/histories/23.9384932920239399/matrices/1010292938384747575].
# ┌─────────┬───────────────────────┬──────────────────────────────────────────┐
# │ OUTCOME │    TEST_AXIS_VALUE    │               TEST_DETAILS               │
# ├─────────┼───────────────────────┼──────────────────────────────────────────┤
# │ Failed  │ Pixel2-28-en-portrait │ 1 test cases failed, 1 passed, 5 skipped │
# └─────────┴───────────────────────┴──────────────────────────────────────────┘

test_results_table = test_output.split('More details are available at ')[1]

# Using regex, I want to get the URL from the text. Then, I remove the '[' and ']' characters from the text to just give me the result URL.
test_results_url = test_results_table.match(/(\[https:\/\/console.firebase.google.com\/project\/.+\/testlab\/histories\/.+\])/)[1].tr('[]', '')

failed_success_message = test_results[:result] ? "Success" : "Failed!"
ui_integration_test_message = "#{failed_success_message} - UI/integration test results [available to view](#{test_results_url})! They are hosted on Firebase and will be deleted in 90 days."

ci.github.comment(ui_integration_test_message)

# Exit with a true/false result to indicate if running of the unit tests failed/succeeded. We run exit *after* sending the github comment.
exit(test_results[:result])