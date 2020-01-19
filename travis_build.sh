#!/bin/bash

set -ev
if [ "$TRAVIS_OS_NAME" == "osx" ]; then
	if [ "${TRAVIS_PULL_REQUEST}" != "false" ]; then
		echo "Build on MacOSX: Pull Request"
		./mvnw -f edelta.parent/pom.xml clean install $ADDITIONAL
	else
		echo "Skipping build on MacOSX for standard commit"
	fi
else
	echo "Build on Linux"
	./mvnw -f edelta.parent/pom.xml clean install $ADDITIONAL
fi 

#  -Dtycho.disableP2Mirrors=true
