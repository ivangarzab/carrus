#!/bin/bash
# The purpose of this script is to execute the 1st step in our release process.
# This consists of checking our develop, updating it, and then creating the release/ branch
# that we'll be using for the rest of the release.

VERSION_NAME="$1"
SED_OPTION=
if [[ "$OSTYPE" == "darwin"* ]]; then
  SED_OPTION="-i ''"
else
  SED_OPTION="-i"
fi


git checkout develop
git fetch
git status
git checkout -b release/"$VERSION_NAME"