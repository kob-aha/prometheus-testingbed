#!/bin/bash

GRAFANA_VERSION=5.1.4-1

echo "Installing grafana ${GRAFANA_VERSION}"

sudo yum install -y https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana-${GRAFANA_VERSION}.x86_64.rpm

# Configure grafana to use prometheus datasource and configured dashboards
sed -i -e 's/;provisioning.*/provisioning = \/vagrant\/conf\/grafana/' /etc/grafana/grafana.ini

# Configure grafana to start on boot and start it manually
/sbin/chkconfig --add grafana-server
service grafana-server start

echo "Finished installing grafana"