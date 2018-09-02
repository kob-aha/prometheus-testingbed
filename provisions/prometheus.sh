#!/bin/bash -x

echo "Start running node exporter"
/vagrant/node_exporter/node_exporter &

echo "Start mongoDB exporter"
/vagrant/mongodb_exporter/mongodb_exporter -mongodb.uri mongodb://root:root123@192.168.42.1:27017 &

echo "Start running prometheus"

# Start prometheus
/opt/prometheus/prometheus --config.file=/vagrant/prometheus.yml &

# Make sure to sleep a little so that prometheus can start
sleep 5