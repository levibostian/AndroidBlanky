#!/bin/bash

adb -d root

adb -d pull /data/data/com.levibostian.androidblanky/databases/AndroidBlanky.db debug.db

adb -d unroot

#adb -d exec-out run-as com.levibostian.androidblanky cat databases/AndroidBlanky.db > debug.db