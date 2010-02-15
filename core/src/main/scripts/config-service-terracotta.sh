#!/bin/bash

export TC_HOME=/usr/local/terracotta
export JAVA_HOME=/usr/java/default

$TC_HOME/bin/tim-get.sh install tim-jetty-6.1 1.3.0 org.terracotta.modules
$TC_HOME/bin/tim-get.sh install modules-base 1.0.1 org.terracotta.modules
$TC_HOME/bin/tim-get.sh install tim-annotations 1.3.2 org.terracotta.modules
$TC_HOME/bin/tim-get.sh install tim-concurrent-collections 1.1.2 org.terracotta.modules
$TC_HOME/bin/tim-get.sh install tim-map-evictor 1.1.2 org.terracotta.modules
