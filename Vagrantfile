# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  #config.vm.box = "centos-7-minimal-packer"
  config.vm.box = "centos65-x86_64-20140116"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  config.vm.network "forwarded_port", guest: 9090, host: 9090
  config.vm.network "forwarded_port", guest: 9100, host: 9100

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine and only allow access
  # via 127.0.0.1 to disable public access
  # config.vm.network "forwarded_port", guest: 80, host: 8080, host_ip: "127.0.0.1"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  #config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
  #   vb.gui = true
  #
  #   # Customize the amount of memory on the VM:
  #   vb.memory = "1024"
  #end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  config.vm.provision "shell", inline: <<-SHELL
    export EXPORTER_VERSION=0.16.0
    export PROMETHEUS_VERSION=2.3.2

    yum install -y docker git glibc-static golang wget
    systemctl start docker

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

  config.vm.provision "shell", path: "run-prometheus.sh", run: "always"
end