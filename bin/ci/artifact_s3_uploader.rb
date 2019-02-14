require 'trent'

########## Edit the values below ##########

# the name you typed in when creating your new S3 bucket.
aws_bucketname = "app-test-results"
# To find the URL, open up AWS S3's website, select your bucket name from the list -> "Properties" tab -> "Static website hosting" -> copy the URL at the top called "Endpoint".
aws_bucket_url = "http://your-project-bucket.s3-website-us-east-1.amazonaws.com"

###########################################

ci = Trent.new
ci.config_github(ENV['DANGER_GITHUB_API_TOKEN'])

# Travis does not allow uploading artifacts on pull requests. I need to do that. So, I will do it myself.

unit_test_results = 'app/build/reports/tests/testDevelopmentDebugUnitTest/'
unit_test_subdir = "/#{ENV['TRAVIS_REPO_SLUG']}/#{ENV['TRAVIS_BUILD_NUMBER']}/#{ENV['TRAVIS_JOB_NUMBER']}/unit_tests"

def upload_dir(ci, bucket_name, directory_to_upload, bucket_path)
  ci.sh("docker run --rm \
                -e KEY=#{ENV['AWS_ACCESS_KEY']} \
                -e SECRET=#{ENV['AWS_SECRET_KEY']} \
               	-e REGION=us-east-1 \
                -e BUCKET=\"#{bucket_name}\" \
                -e BUCKET_PATH=\"#{bucket_path}\" \
                -v $(pwd)/#{directory_to_upload}:/data:ro \
                futurevision/aws-s3-sync now")
end

if ARGV[0] == 'unit_tests'
  puts 'Uploading unit test results to S3'
  upload_dir(ci, aws_bucketname, unit_test_results, unit_test_subdir)

  unit_test_path = "#{aws_bucket_url}#{unit_test_subdir}/index.html"
  unit_test_message = "Unit test results [available to view](#{unit_test_path})! Files will be deleted in 14 days."

  ci.github.comment(unit_test_message)
end