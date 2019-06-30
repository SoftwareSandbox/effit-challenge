LiveCoding sessions left until Snowcase 2020: **37**

# Effit-challenge web app [![](https://travis-ci.org/SoftwareSandbox/effit-challenge.svg?branch=master)](https://travis-ci.org/SoftwareSandbox/effit-challenge)
Web application for the PHUCIT Challenge:  
A week filled with challenges to make your Snowcase holiday even more memorable.

I will mostly be live-coding my working on this project. You can follow along and rewatch recent past broadcast over at [my Twitch channel](https://twitch.tv/sch3lpsc2).

## Goals of this hobby project

* Learn new stuff (about Kotlin, Vue, other testing frameworks maybe, ...)
* Prove to myself that I can incrementally _architect_ from a monolith to hexagonal (because I have the tendency to over design architectures)

## Stack
Backend will be a SpringBoot web application built in Kotlin.

Frontend will be a VueJS app.

SpringBoot web app both serves the REST API, **and** the VueJS app.

## A note on incremental architecture
We'll start off by building a monolith (with an attempt at keeping it modular).

See [CaptainsLog](CaptainsLog.md) for more [Lightweight ADR's](https://adr.github.io/)

## Frontend requirements
Needs to run on mobile devices, no plans on compiling for native devices.
 