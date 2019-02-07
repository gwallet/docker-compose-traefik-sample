#!/bin/sh

usage() {
cat >&1 <<!
  usage: $(basename $0) [start|status|stop] <TENANT>
!
}

CMD=$1
TENANT=$2

case $CMD in
  start)
    docker-compose -p $TENANT -f docker-compose.yml -f infra/$TENANT/docker-compose.yml up -d
    ;;
  stop)
    docker-compose -p $TENANT -f docker-compose.yml -f infra/$TENANT/docker-compose.yml down
    ;;
  status)
    docker-compose -p $TENANT -f docker-compose.yml -f infra/$TENANT/docker-compose.yml ps
    ;;
  *)
    usage
    exit 1
    ;;
esac
