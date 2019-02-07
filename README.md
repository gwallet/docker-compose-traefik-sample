Infrastructure FTW!
===================

Investigation on how to deal with too many sub networks on a Docker host.

Sample App
----------

The app is divided into 2 pieces maintained altogether with Docker Compose

1. the API which looks for IP address of each network interfaces
1. the Web frontend, doing the same thing and include the response from the API as a summary

> Why ?
>
> In order to check if the Web instance is talking to the good API instance in a multi tenant deployment model, if it works :)

Building
--------

First, compile the sample apps :
```bash
$ mvn package
```

That's it!

Running
-------

· First: run the infra structure related stuff, ie [Traefik](https://traefik.io/)

```bash
$ ./ctlinfra.sh start

```

· Then: run the first tenant instance

```bash
$ ./ctltenant.sh start tenant1
```

· And: run the second tenant instance

```bash
$ ./ctltenant.sh start tenant2
```

· Finally: run the test case

```bash
$ ./test.sh
```
