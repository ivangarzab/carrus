#!/bin/bash
# The purpose of this script is to execute the 4th step in our release process.
# This consists of finishing the branch update process for both 'master' & 'develop',
# followed by simply pushing all changes up to the 'origin,' including the newly created tag.

VERSION_NAME="$1"


echo "Merging master >> develop"
# Finish updating 'develop' and push to 'origin'
git checkout develop
git merge --no-ff master
echo "Pushing develop branch"
git push
# Checkout 'master' and push to 'origin'
echo "Pushing master branch"
git checkout master
git push
# Push new tag as well
echo "Pushing all tags"
git push origin "$VERSION_NAME"
echo "All changes have been pushed to origin"