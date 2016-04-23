# maven-release-properties
[![Build Status](https://travis-ci.org/fripoli/maven-release-properties.svg?branch=master)](https://travis-ci.org/fripoli/maven-release-properties)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/br.com.ripoli.xico/release-properties/badge.svg)](https://maven-badges.herokuapp.com/maven-central/br.com.ripoli.xico/release-properties)

A maven plugin to list all properties on RELEASE or LATEST

## Goal ##
This plugin only have one goal

    * **mvn release-properties:parse** will read all properties of the pom file and make available to the project:
        * propertiesOnRelease - a comma separated value of all properties on RELEASE
        * propertiesOnLatest - a comma separated value of all properties on LATEST
        * propertiesOnReleaseAndLatest - a comma separated value of all properties on RELEASE and on LATEST

## Usage ##

This plugin's main usage is combining with the versions:update-properties mojo:

    * **mvn release-properties:parse versions:update-properties -DincludeProperties='${propertiesOnRelease}'**