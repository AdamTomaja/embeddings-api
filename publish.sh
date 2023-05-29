#!/bin/sh
version=$1

echo "Publishing with version $version"
echo "Building docker image"
docker build -t vault:5556/embeddings-api:$version .

echo "Pushing image to vault"
docker push vault:5556/embeddings-api:$version