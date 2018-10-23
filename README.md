[<img src="https://cdn.anychart.com/images/logo-transparent-segoe.png?2" width="234px" alt="AnyChart - Robust JavaScript/HTML5 Chart library for any project">](https://anychart.com)
# AnyChart Clojure and ClojureScript Integration Sample
The sample is a basic sales dashboard, with data stored in several tables and shown on a website as several JavaScript bar, line and pie charts, along with ability to filter data and update charts on the fly.

## Overview

This sample shows a sample dashboard done with [AnyChart JavaScript Charting Framework](http://www.anychart.com/) and, [ClojureScript](https://github.com/clojure/clojurescript) frontend, [Clojure](http://clojure.org/) backend and [Postgresql](http://www.postgresql.org/) Database.

## Requirements
* [JRE 7+](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)
* [Postgresql 9.3+](http://www.postgresql.org/download/)
* [Leiningen 2+](http://leiningen.org/)


## Database Setup
```
# login to PostgreSQL
sudo -u postgres psql

# CREATE user and database
CREATE USER anychart_user WITH PASSWORD 'anychart_pass';
CREATE DATABASE anychart_sample;
GRANT ALL PRIVILEGES ON DATABASE anychart_sample TO anychart_user;

# exit psql
\q

# run dump
psql anychart_sample < dump
```


## Database Connection Configiration
Edit base-config in src/sample/core.clj

## Rebuilding ClojureScript
`lein cljsbuild once prod` for production, `lein cljsbuild once dev` for development.

## Running locally
After `lein run` project will be available at http://localhost:9197

## Deploying
You can use uberjar for deploy: `lein uberjar`


## Project Structure
```
anychart-clojure-sample/
    resources/
        public/
            js/
                main.js             # generated JS code
                main.min.js         # optimized generated JS code
    src/                            # Backend Clojure code
        sample/
            db/
                core.clj            # DB component
                data.clj            # DB data functions
            web/
                core.clj            # Web component
                routes.clj          # Compojure routes and handlers
            core.clj
        templates/
            index.selmer            # HTML template
    src-cljs/                       # Frontend ClojureScript code
        sample/
            charts.cljs             # AnyChart charts creation      
            core.cljs               # Initalization
            data.cljs               # Function for work with data
            ui.cljs                 # Top control panel
        exnters.js
    dump.sql                        # PostgreSQL dump
    project.clj                     # Main project settings

```


## Technologies
Language - [Clojure](https://clojure.org) / [ClojureScript](https://clojurescript.org/)<br />
Build tool - [Leiningen](https://leiningen.org/)<br />
Database - [PostgreSQL](http://www.postgresql.org/)<br />
HTTP server - [HTTP Kit](http://www.http-kit.org/)<br />
Template Engine - [Selmer](https://github.com/yogthos/Selmer)<br />
UI library - [Reagent](https://reagent-project.github.io/)


## Further Learning
* [Documentation](https://docs.anychart.com)
* [JavaScript API Reference](https://api.anychart.com)
* [Code Playground](https://playground.anychart.com)
* [Technical Support](https://anychart.com/support)

## License
[© AnyChart.com - JavaScript charts](http://www.anychart.com). Released under the [Apache 2.0 License](https://github.com/anychart-integrations/anychart-clojure-sample/blob/master/LICENSE).