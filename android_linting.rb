# This file exists because I don't want to spend the time learning how to make custom rules for Ktlint. 
# This file is meant to be run on a ci server or git hook. 

# I keep getting tests broken when a file has multiple `import kotlinx.android.synthetic.*` statements in it. Catch these!
Dir.glob('app/src/**/*.kt') do |kotlin_file|
  if File.readlines(kotlin_file).grep(/(import kotlinx.android.synthetic.).+/).size >= 2
    puts "Bug! File #{kotlin_file} contains 2+ lines of `import kotlinx.*` in it. Delete 1 to fix the issue."
    exit(1)
  end
end