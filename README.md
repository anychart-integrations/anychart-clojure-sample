# AnyChart Clojure and Clojurescript Integration Sample

## Overview

This sample shows a sample dashboard done with [AnyChart JavaScript Charting Framework](http://www.anychart.com/) and, [Cloоurescript](https://github.com/clojure/clojurescript) frontend, [Clojure](http://clojure.org/) backend and [Postgresql](http://www.postgresql.org/) Database.

The sample is a basic sales dashboard, with data stored in several tables and shown on a website as several JavaScript bar, line and pie charts, along with ability to filter data and update charts on the fly.

## Project Structure
* src/templates - selmer templates
* src/sample - clojure sample code
* src-cljs - clojurescript sources
* resources/public - static resources

## Requirements
* [JRE 7+](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)
* [Postgresql 9.3+](http://www.postgresql.org/download/)
* [Leiningen 2+](http://leiningen.org/)

## Database Setup
`psql anychart_sample < dump`

## Database Connection Configiration
Edit base-config in src/sample/core.clj

## Rebuilding clojurescript
`lein cljsbuild once prod` for production, `lein cljsbuild once dev` for development.

## Running locally
After `lein run` project will be available at http://localhost:9197

## Deploying
You can use uberjar for deploy: `lein uberjar`
