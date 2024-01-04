#!/bin/bash
# The purpose of this script is to sequentially execute the different steps from the release
# process one after the other, with the intent of partially automating the given process.

VERSION_NAME="$1"


# Step 1
echo "Creating release branch from the latest 'develop'"
./tools/release/release-step1.sh "$VERSION_NAME"
# Step 2
echo "Updating app's build.gradle.kts file"
./tools/release/release-step2.sh "$VERSION_NAME"
# Step 3
echo "Updating both 'develop' & 'master' branches with release branch"
./tools/release/release-step3.sh "$VERSION_NAME"
# Step 4
echo "Pushing all changes up to the 'origin'"
./tools/release/release-step4.sh "$VERSION_NAME"