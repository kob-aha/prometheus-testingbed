#!/bin/bash -x

read mongoUrl

export MONGODB_URL=${mongoUrl}

/vagrant/mongodb_exporter/mongodb_exporter
