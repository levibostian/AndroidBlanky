#!/bin/bash

export PATH="$PATH:~/.rbenv/shims/"

dotenv-android --source app/src/main/java/com/levibostian/ --package com.levibostian
