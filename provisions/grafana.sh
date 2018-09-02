#!/bin/bash

GRAFANA_VERSION=5.1.4-1

echo "Installing grafana ${GRAFANA_VERSION}"

sudo yum install -y https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana-${GRAFANA_VERSION}.x86_64.rpm

cp /vagrant/reports/*.json /etc/grafana/provisioning/dashboards/

# Configure grafana to start on boot and start it manually
/sbin/chkconfig --add grafana-server
service grafana-server start

echo "Finished installing grafana"