#!/bin/bash

# We are going to modify files that are staged and ready for commit. 
# But once you modify it, that file is no longer staged in that commit so you will need to:
# `git add` files, run this script to modify files, then you will need to `git add` files again that were modified. 
# 
# But instead, we will stash unstaged files, script to modify files, `git add` all files, then unstage files. 
# Thanks, https://stackoverflow.com/a/26911078/1486374 and https://codeinthehole.com/tips/tips-for-using-a-git-pre-commit-hook/

# Stash unstaged changes
STASH_NAME="pre-commit-hook-$(date +%s)"
git stash save -q --keep-index $STASH_NAME
echo "[GIT HOOK] created stash for unstaged files: $STASH_NAME"

########### All scripts here ##################
echo "[GIT HOOK] running hook scripts"
./gradlew formatKotlin
RESULT=$?
##############################################

# Stage updated files
git add .

# Re-apply original unstaged changes
git stash pop -q $STASH_NAME
echo "[GIT HOOK] popping stash $STASH_NAME"

[ $RESULT -ne 0 ] && exit 1
exit 0