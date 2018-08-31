FROM golang:1.8
MAINTAINER Koby Aharon <kobyahron@gmail.com>

# Install glide
RUN curl https://glide.sh/get | sh
COPY build-exporter.sh /tmp/
RUN chmod +x /tmp/build-exporter.sh