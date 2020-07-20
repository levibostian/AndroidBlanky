#!/bin/bash

export PATH="$PATH:~/.rbenv/shims/"

dotenv-android --source app/src/main/java/com/app/ --package com.app
