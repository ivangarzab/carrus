#!/bin/bash
# The purpose of this script is to execute the 2nd step in our release process.
# This consist of running the `update-version-app.sh` command from the tools directory, and
# committing the changes that this script generates directly into our release branch.

VERSION_NAME="$1"
SED_OPTION=
if [[ "$OSTYPE" == "darwin"* ]]; then
  SED_OPTION="-i ''"
else
  SED_OPTION="-i"
fi


chmod +x tools/update-version-app.sh
./tools/update-version-app.sh "$VERSION_NAME"
git commit -am "Update versionCode & versionName for release v$VERSION_NAME"