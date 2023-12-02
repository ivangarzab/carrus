#!/bin/bash
# The purpose of this script is to execute the 3rd step in our release process.
# This consists of pushing the release branch up into the 'origin', and then merging that
# same branch into both 'master' & 'develop'.

VERSION_NAME="$1"
SED_OPTION=
if [[ "$OSTYPE" == "darwin"* ]]; then
  SED_OPTION="-i ''"
else
  SED_OPTION="-i"
fi


# Publish release branch into 'origin'
BRANCH_NAME=release/"$VERSION_NAME"
git push -u origin "$BRANCH_NAME"
# Merge release branch into 'master'
git checkout master
git merge --no-ff "$BRANCH_NAME"
DATE_TODAY='date +"%Y-%m-%d"'
# Tag the release
git tag -a "$VERSION_NAME" -m "Tagging release v$VERSION_NAME on $DATE_TODAY"
# Merge release branch into 'develop'
git checkout develop
git merge --no-ff "$BRANCH_NAME"