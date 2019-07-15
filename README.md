**35** LiveCoding sessions left until Snowcase 2020 

# Effit-challenge web app [![](https://travis-ci.org/SoftwareSandbox/effit-challenge.svg?branch=master)](https://travis-ci.org/SoftwareSandbox/effit-challenge)
Web application for the PHUCIT Challenge:  
A week filled with challenges to make your Snowcase holiday even more memorable.

I will mostly be live-coding my working on this project. You can follow along and rewatch recent past broadcast over at [my Twitch channel](https://twitch.tv/sch3lpsc2).

## Goals of this hobby project

* Learn new stuff (about Kotlin, Vue, other testing frameworks maybe, ...)
* Prove to myself that I can incrementally _architect_ from a monolith to hexagonal (because I have the tendency to over design architectures)

## Functional stuff
Check out [the diagrams](docs/diagrams.md).

## Stack
Backend will be a SpringBoot web application built in Kotlin.

Frontend will be a VueJS app.

SpringBoot web app both serves the REST API, **and** the VueJS app.

## A note on incremental architecture
We'll start off by building a monolith (with an attempt at keeping it modular).

See [CaptainsLog](CaptainsLog.md) for more [Lightweight ADR's](https://adr.github.io/)

## Frontend requirements
Needs to run on mobile devices, no plans on compiling for native devices.
 
## _CI/CD Deployment pipeline_
In quotes because it's not exactly a proper pipeline, I guess.

After successful build:
1) `git merge prod`
1) `git checkout prod`
1) `git merge master`
1) `git push`

This will trigger a TravisCI build for the `prod` branch, which will then 

1) create a docker image, 
1) push it to the heroku docker registry,
1) and execute heroku's release phase via the _Heroku API_ (aka a curl command)

### Property passing is a thing

All Heroku properties are accessible as environment variables in the docker container.

* `$DATABASE_URL`: supplied by Heroku when you use the postgres add-on. Contains weird looking url (which isn't a jdbc url). So we parse it (in [execJava.sh](ops/webapp/execJava.sh))
* `$PORT`: you **need** to have the springboot app use this variable, otherwise Heroku won't be able to forward http requests to it (and it'll actually crash).

## Locally checking Heroku server logs
`heroku logs -a effit-challenge --tail`
