# Prometheus Testingbed
Sample project to check prometheus and grafana. 
You can use this project to load a VM instance containing prometheus, grafana and 2 exporters:
* [Node exporter](https://github.com/prometheus/node_exporter)
* [MongoDB exporter](https://github.com/dcu/mongodb_exporter)

# Running
We use Vagrant in order to automate VM creation. You can simply run the VM by issuing:

```vagrant up```

from the root clone folder.

# Assumption
There should be a running MongoDB instance which can be accessed using the following URI: mongodb://root:root123@[HOST_IP]:27017. 
See below for configuration instructions.

# Configuration

### Mongo URI
Mongo URI is defined in "provisions/prometheus.sh". Simply update the file and reload VM using Vagrant.
