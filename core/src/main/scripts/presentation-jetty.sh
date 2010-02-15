#!/bin/bash

export JAVA_HOME=/usr/java/default
export JETTY_LOGS=$JETTY_HOME/logs
export JETTY_HOME=/usr/local/presentation-jetty
export JETTY_USER=jetty

export TC_HOME=/usr/local/terracotta
export TC_CONFIG=" -Dtc.config=$JETTY_HOME/presentation-terracotta-config.xml "
export JETTY_JAVA_OPTIONS="  -Dconfig.home=$JETTY_HOME "

. "${TC_HOME}/bin/dso-env.sh"

export JAVA_OPTIONS=" ${TC_JAVA_OPTS} $TC_CONFIG $JETTY_JAVA_OPTIONS "

/usr/local/presentation-jetty/bin/jetty.sh $@
#su -l -c "/usr/local/presentation-jetty/bin/jetty.sh $@" jetty
