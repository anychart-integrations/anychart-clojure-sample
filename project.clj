(defproject sample "0.1.0"
  :description "AnyChart Clojure/ClojureScript/PostgreSQL sample"
  :url "https://github.com/anychart-integrations/anychart-clojure-sample"
  :license {:name "Apache License"
            :url  "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"]
                 [clj-time "0.15.0"]
                 [com.stuartsierra/component "0.3.2"]
                 ;; cljs
                 [org.clojure/clojurescript "1.10.339"]
                 [reagent "0.8.1"]
                 ;; web
                 [compojure "1.6.1"]
                 [ring/ring-json "0.4.0"]
                 [http-kit "2.3.0"]
                 ;; templates
                 [selmer "1.12.2"]
                 ;; logging
                 [com.taoensso/timbre "4.10.0"]
                 ;; db
                 [org.clojure/java.jdbc "0.7.8"]
                 [postgresql/postgresql "9.3-1102.jdbc41"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.3"]
                 [honeysql "0.9.4"]]
  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-ancient "0.6.15"]]
  :main sample.core
  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src-cljs"]
                        :compiler     {:output-to     "resources/public/js/main.js"
                                       :optimizations :whitespace
                                       :foreign-libs  [{:file     "https://cdn.anychart.com/releases/v8/js/anychart-bundle.min.js"
                                                        :provides ["anychart"]}]
                                       :pretty-print  true}}
                       {:id           "prod"
                        :source-paths ["src-cljs"]
                        :compiler     {:output-to     "resources/public/js/main.min.js"
                                       :optimizations :advanced
                                       :externs       ["src-cljs/externs.js"]
                                       :foreign-libs  [{:file     "https://cdn.anychart.com/releases/v8/js/anychart-bundle.min.js"
                                                        :provides ["anychart"]}]
                                       :pretty-print  false}}]})
