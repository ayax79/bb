#!/bin/bash

export TC_HOME=/usr/local/terracotta
export JAVA_HOME=/usr/java/default

su -l -c "/usr/local/terracotta/bin/start-tc-server.sh -f $TC_HOME/tc-config.xml $@ &" terracotta

#/usr/local/terracotta/bin/start-tc-server.sh $@

