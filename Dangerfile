if github.branch_for_head == "staging" && github.branch_for_base != "beta"
  fail "You can only merge from the staging branch into beta."
end

if github.branch_for_head == "beta" && github.branch_for_base != "production"
  fail "You can only merge from the beta branch into production."
end

if github.branch_for_base == "staging" && (github.branch_for_head == "beta" || github.branch_for_head == "production")
  fail "You cannot merge from the beta or production branches into staging."
end

if github.branch_for_base == "development"
  message "Don't forget to generate screenshots if you changed the UI up at all."
end

if github.branch_for_base == "staging"
  android_version_change.assert_version_name_changed()

  if !git.diff_for_file("release_notes.txt")
    warn 'You did not update the contents of the release_notes.txt file with release notes for the app store.'
  end

  if !git.diff_for_file("CHANGELOG.md")
    fail 'You did not append to the CHANGELOG.md file with release notes.'
  end

  if !git.diff_for_file("demo_list.md")
    fail 'You did not create a demo yet. Create a demo and add it to the demo_list.md file. If this is a bug fixes release, make an entry in the demo_list.md file saying so.'
  end
end

# I keep getting tests broken when a file has multiple `import kotlinx.android.synthetic.*` statements in it. Catch these!
Dir.glob('app/src/**/*.kt') do |kotlin_file|
  if File.readlines(kotlin_file).grep(/(import kotlinx.android.synthetic.).+/).size >= 2
    fail 'Bug! File #{kotlin_file} contains 2+ lines of `import kotlinx.*` in it. Delete 1 to fix the issue.'
  end
end