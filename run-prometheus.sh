#!/bin/bash -x

echo "Start running node exporter"

# Run node exporter
/vagrant/node_exporter/node_exporter &

echo "Start running prometheus"

# Start prometheus
/opt/prometheus/prometheus --config.file=/vagrant/prometheus.yml &