#!/bin/bash
# This script wil automatically create a change log text file, from the last release on, 
# and locate it in carrus/build/outputs/changelog/changelog.txt.

echo "Generating Change Log..."
LAST_TAG=$(git describe --tags --abbrev=0)
GIT_HISTORY=$(git log "$LAST_TAG"..HEAD --oneline --no-merges --pretty=format:\"%s\")
mkdir ./app/build/outputs/changelog
printf 'v%s Change Log:\n%s' "$LAST_TAG" "$GIT_HISTORY" > ./app/build/outputs/changelog/changelog.txt
echo "Change Log generated."
