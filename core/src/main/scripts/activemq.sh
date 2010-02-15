#!/bin/bash

export ACTIVEMQ_HOME=/usr/local/apache-activemq
export JAVA_HOME=/usr/java/default

#su -l -c "/usr/local/apache-activemq/bin/linux-x86-64/activemq $@" activemq

/usr/local/apache-activemq/bin/linux-x86-64/activemq $@

