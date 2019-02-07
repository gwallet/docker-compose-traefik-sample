#!/bin/sh

[ "$(ping -c 1 tenant1.dockerhost)" ] || echo "No name defined for tenant1.dockerhost, please add it to /etc/hosts ;)"
[ "$(curl -s http://tenant1.dockerhost/ping)" = "pong" ] || echo "Can not join tenant1.dockerhost, is it still running ? (./ctltenant.sh status tenant1)"
[ "$(ping -c 1 tenant2.dockerhost)" ] || echo "No name defined for tenant2.dockerhost, please add it to /etc/hosts ;)"
[ "$(curl -s http://tenant2.dockerhost/ping)" = "pong" ] || echo "Can not join tenant2.dockerhost, is it still running ? (./ctltenant.sh status tenant2)"

echo "Testing tenant1"
curl -s http://tenant1.dockerhost/ipa | jq .
echo "Testing tenant2"
curl -s http://tenant2.dockerhost/ipa | jq .
