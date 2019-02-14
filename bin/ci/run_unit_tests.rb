require 'trent'

ci = Trent.new(color: :light_blue)

runUnitTestsResult = ci.sh('./gradlew testDevelopmentDebugUnitTest', fail_non_success: false)

ci.sh('ruby ./bin/ci/artifact_s3_uploader.rb unit_tests')

# Exit with a true/false result to indicate if running of the unit tests failed/succeeded. We run exit *after* uploading to S3 so I always have the results.
exit(runUnitTestsResult[:result])