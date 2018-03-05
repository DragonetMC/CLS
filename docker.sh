#!/bin/bash

# extract project version
VERSION=$(mvn -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec -q)
echo "Project version : $VERSION"

# build Docker image
docker build -t dragonet/cls:$VERSION .