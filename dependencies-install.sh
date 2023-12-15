#!/bin/bash
cd ynthm-common/common-dependencies
mvn clean install -e -U -Dmaven.test.skip=true
cd ..