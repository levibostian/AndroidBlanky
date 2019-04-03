require 'trent'

ci = Trent.new(:color => :light_blue, :local => true)

ci.sh('./gradlew :app:assembleDevelopmentDebugUnitTest')
ci.sh('./gradlew :app:assembleDevelopmentDebug -PdisablePreDex')
ci.sh('./gradlew :app:assembleDevelopmentDebugAndroidTest -PdisablePreDex')

# Run unit tests locally because they are fast to run.
ci.sh('./gradlew testDevelopmentDebugUnitTest')