(ns sample.core
  (:require [sample.components.jdbc :as jdbc]
            [sample.components.web :as web]
            [com.stuartsierra.component :as component])
  (:gen-class :main :true))

(def base-config {:web {:port 9197 :debug true}
                  :jdbc {:subprotocol "postgresql"
                         :subname "//localhost:5432/anychart_sample"
                         :classname "org.postgresql.Driver"
                         :user "anychart_user"
                         :password "anychart_pass"}})

(def prod-config (assoc-in base-config [:web :debug] false))

(defn app-system
  "App system with jdbc and http-kit web server"
  [config]
  (component/system-map
   :jdbc  (jdbc/new-jdbc (:jdbc config))
   :web   (component/using (web/new-web (:web config)) [:jdbc])))

(def dev (app-system base-config))

(defn start
  "Start dev system"
  []
  (alter-var-root #'dev component/start))

(defn stop
  "Stop dev system"
  []
  (alter-var-root #'dev component/stop))

(defn -main
  "Start prod system"
  []
  (println "starting server http://localhost:9197/")
  (component/start (app-system prod-config)))
