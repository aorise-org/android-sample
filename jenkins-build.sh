#!/bin/sh

echo ======================Execute sonarqube======================
gradle sonarqube;
echo;

echo ======================jenkins-build finish======================
echo;