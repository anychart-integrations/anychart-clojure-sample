# anychart clojure and clojurescript integration sample

## Project structure
* src/templates - selmer templates
* src/sample - sample clojure code
* src-cljs - clojurescript sources
* resources/public - static resources

## Requirenments
* JRE 7+
* Postgresql 9.3+
* Leiningen 2+

## Database setup
`psql anychart_sample < dump`

## Database connection config
Edit base-config in src/sample/core.clj

## Rebuilding clojurescript
`lein cljsbuild once prod` for production, `lein cljsbuild once dev` for development.

## Running locally
After `lein run` project will be available at http://localhost:9197

## Deploying
You can use uberjar for deploy: `lein uberjar`
