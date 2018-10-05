#!/bin/bash

if [ -f mongodb_exporter ]; then
    echo "MongoDB exporter file exists. No need to build again"
    exit 0
fi

CONTAINER_NAME="mongodb_exporter"

docker run --name ${CONTAINER_NAME} exporter_builder /tmp/build-exporter.sh
docker cp ${CONTAINER_NAME}:/go/src/github.com/dcu/mongodb_exporter/mongodb_exporter .
docker stop ${CONTAINER_NAME} && docker rm ${CONTAINER_NAME}