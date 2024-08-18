#!/bin/bash
docker build -t hospital-appointments:latest . || {
    echo "Failed to build hospital-appointments project from Dockerfile"
    exit 1
}
docker tag hospital-appointments:latest cosiska/hospital-appointments:latest || {
    echo "Failed to tag local docker image of hospital-appointments to DockerHub image"
    exit 1
}
docker push cosiska/hospital-appointments:latest || {
    echo "Failed to push hospital-appointments docker image to DockerHub"
    exit 1
}
exit 0
