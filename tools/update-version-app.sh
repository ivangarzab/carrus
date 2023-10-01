#!/bin/bash

VERSION_NAME="$1"

SED_OPTION=
if [[ "$OSTYPE" == "darwin"* ]]; then
  SED_OPTION="-i ''"
else
  SED_OPTION="-i"
fi

# Update versionName
VERSION_NAME_PATTERN="versionName"
echo "Updating the app versionName to: $VERSION_NAME"
sed "$SED_OPTION" "/$VERSION_NAME_PATTERN/ s/\=.*\"$/\= \"$VERSION_NAME\"/" app/build.gradle.kts

# Update versionCode
VERSION_CODE_PATTERN="versionCode"
VERSION_CODE=$(grep "$VERSION_CODE_PATTERN" app/build.gradle.kts | tr -dc '0-9')
((VERSION_CODE=VERSION_CODE+1))
echo "Updating the app versionCode to: $VERSION_CODE"
sed "$SED_OPTION" "/$VERSION_CODE_PATTERN/ s/\=.*$/\= $VERSION_CODE/" app/build.gradle.kts

echo "App versionName & versionCode update completed"