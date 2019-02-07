#!/bin/sh

usage() {
cat >&1 <<!
  usage: $(basename $0) [start|status|stop]
!
}

case $1 in
  start)
    docker-compose -p infra -f infra/docker-compose.yml up -d
    ;;
  stop)
    docker-compose -p infra -f infra/docker-compose.yml down
    ;;
  status)
    docker-compose -p infra -f infra/docker-compose.yml ps
    ;;
  *)
    usage
    exit 1
    ;;
esac
