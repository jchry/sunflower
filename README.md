# Introduction

Java high performance distributed system monitoring.

- Error

- Distributed trace

- HTTP

- JVM

- CPU

- IO

- MySQL

- Redis

- Alarm

# Model description

## sunflower-agent(client)

Responsible for monitoring data collection and reporting.

## sunflower-server(server)

Responsible for monitoring data processing, storage, display, configuration, etc.

## sunflower-remoting(rpc)

The basic communication components are implemented based on netty.

## sunflower-logging(logger)

Inner logger.

## sunflower-common(common)

Common tools.

## sunflower-example(example)

Test demo of agent application , communication, monitoring, etc.
