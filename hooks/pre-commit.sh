#!/bin/bash

set -e

RED='\033[0;31m'

if ! [ -x "$(command -v pre-commit)" ]; then
    echo -e "${RED}You need to install the program 'pre-commit' on your machine to continue."
    echo ""
    echo -e "${RED}The easiest way is 'brew install pre-commit'. If you're not on macOS, check out other instructions for installing: https://pre-commit.com/#install"
    exit 1
fi

pre-commit run || true # pre-commit will fail if it formats any files. It's annoying when it fails and you need to run `git commit` again. So, always let it succeed. We will see in PRs if you ran pre-commit or not. 