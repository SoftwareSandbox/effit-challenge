# Effit-challenge web app [![](https://travis-ci.org/SoftwareSandbox/effit-challenge.svg?branch=master)](https://travis-ci.org/SoftwareSandbox/effit-challenge)
Web application for the PHUCIT Challenge:  
A week filled with challenges to make your Snowcase holiday even more memorable.

## Goals of this hobby project

* Learn new stuff (about Kotlin, Ktor, Htmx, ...)
* Figure out if Htmx can provide a much simpler way of rendering a frontend.

## Functional stuff
Check out [the diagrams](docs/diagrams.md).

## Stack
A full Ktor application,
using Htmx to make the Kotlin HTML DSL rendered content more dynamic.
Backed by a Postgres database, which gets access with simple sql.
Bulma will make sure things look pretty enough.
I will be using authentication this time, using OAuth2 with Google as a Federated Login.

I'll try to follow a hexagonal approach to the package/module structure.

## A note on where this came from
The [CaptainsLog](CaptainsLog.md) is a tool for doing [Lightweight ADR's](https://adr.github.io/), and documents a lot about what happened in the past.

I'll just continue logging stuff in there for this rewrite in Ktor.

## Frontend requirements
Needs to run on mobile devices, no plans on compiling for native devices.
 
