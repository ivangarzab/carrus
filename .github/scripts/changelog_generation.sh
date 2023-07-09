#!/bin/bash
# This script wil automatically create a change log text file and locate it
#  in the path carrus/app/build/outputs/changelog/changelog.txt, for all commits that happened
#  between the $CURRENT_TAG and the $LAST_TAG.
#
# This script assumes that the following two things are true for a successful execution:
#  1.  There app/build directory exists.
#  2.  The script is being ran in the root directory of the repository.

LAST_TAG=$(git describe --abbrev=0 --tags "$(git rev-list --tags --skip=1 --max-count=1)")
CURRENT_TAG=$(git describe --tags --abbrev=0)
echo "Generating Change Log for tag $CURRENT_TAG"
GIT_HISTORY=$(git log "$LAST_TAG".."$CURRENT_TAG" --oneline --no-merges --pretty=format:'- %s')

# TODO: Conditional check to ensure we need to create a new directory; skip next line otherwise
mkdir ./app/build/outputs/changelog

printf 'Change Log for release v%s:\n%s' "$CURRENT_TAG" "$GIT_HISTORY" > ./app/build/outputs/changelog/changelog.txt
echo "Change Log generated"
