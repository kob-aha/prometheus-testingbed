# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "centos65-x86_64-20140116"

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  config.vm.network "forwarded_port", guest: 9090, host: 9090
  config.vm.network "forwarded_port", guest: 9100, host: 9100
  config.vm.network "forwarded_port", guest: 9001, host: 9001
  config.vm.network "forwarded_port", guest: 3000, host: 3000

  config.vm.provision "shell", inline: <<-SHELL
    export EXPORTER_VERSION=0.16.0
    export PROMETHEUS_VERSION=2.3.2

    yum install -y docker git glibc-static golang wget
    service start docker

    # Build mongoDB exporter
    pushd /vagrant/mongodb_exporter
    docker build -t=exporter_builder:latest .
    bash run.sh
    popd

    # Build node exporter (can't build it using docker so we'll run it in the machine)
    go get -v github.com/prometheus/node_exporter
    pushd ${GOPATH-$HOME/go}/src/github.com/prometheus/node_exporter
    make && cp node_exporter /vagrant/node_exporter
    popd
    
    wget -O - https://github.com/prometheus/node_exporter/releases/download/v${EXPORTER_VERSION}/node_exporter-${EXPORTER_VERSION}.linux-amd64.tar.gz \
      | tar -xvz --strip-components=1 -C /vagrant/node_exporter/
    
    mkdir -p /opt/prometheus \
      && wget -O - https://github.com/prometheus/prometheus/releases/download/v${PROMETHEUS_VERSION}/prometheus-${PROMETHEUS_VERSION}.linux-amd64.tar.gz \
      | tar -xvz --strip-components=1 -C /opt/prometheus
  SHELL

  config.vm.provision "shell", path: "provisions/grafana.sh"
  config.vm.provision "shell", path: "provisions/prometheus.sh", run: "always"
end