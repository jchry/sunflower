# Preface

JDK8 develops enviroments.

# Introduction

Java high performance distributed system monitoring.

- Error

- Distributed trace

- HTTP

- JVM

- CPU

- IO

- MySql

- Redis

- Warn

- Custom

# Model description

## sunflower-agent(client)

Responsible for monitoring data collection and reporting.

## sunflower-server(server)

Responsible for monitoring data processing, storage, display, configurationg, etc.

## sunflower-remoting(rpc)

The basic communication components are implemented based on netty.

## sunflower-logging(logger)

Inner logger.

## sunflower-common(common)

Common tools.

## sunflower-example(example)

Test demo of agent application , communication, monitoring, etc.
