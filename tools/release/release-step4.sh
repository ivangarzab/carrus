#!/bin/bash
# The purpose of this script is to execute the 4th step in our release process.
# This consists of finishing the branch update process for both 'master' & 'develop',
# followed by simply pushing all changes up to the 'origin,' including the newly created tag.

VERSION_NAME="$1"
SED_OPTION=
if [[ "$OSTYPE" == "darwin"* ]]; then
  SED_OPTION="-i ''"
else
  SED_OPTION="-i"
fi


# Finish updating 'develop' and push to 'origin'
git checkout develop
git merge --no-ff master
git push
# Checkout 'master' and push to 'origin'
git checkout master
git push
# Push new tag as well
git push origin "$VERSION_NAME"