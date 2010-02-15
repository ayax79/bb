#!/bin/bash

export JETTY_HOME=/usr/local/server-jetty
export JETTY_LOGS=$JETTY_HOME/logs
export JAVA_HOME=/usr/java/default
export JETTY_USER=jetty

export TC_HOME=/usr/local/terracotta
export TC_CONFIG=" -Dtc.config=$JETTY_HOME/server-terracotta-config.xml "
export JETTY_JAVA_OPTIONS=" -Xmx512m -XX:MaxPermSize=256m -Dconfig.home=$JETTY_HOME "

. "${TC_HOME}/bin/dso-env.sh"

export JAVA_OPTIONS=" ${TC_JAVA_OPTS} $TC_CONFIG $JETTY_JAVA_OPTIONS "

/usr/local/server-jetty/bin/jetty.sh $@

#su -l -c "/usr/local/server-jetty/bin/jetty.sh $@" jetty
