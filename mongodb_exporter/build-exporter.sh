#!/bin/bash

go get -v -d github.com/dcu/mongodb_exporter
cd /go/src/github.com/dcu/mongodb_exporter
make build

