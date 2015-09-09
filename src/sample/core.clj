(ns sample.core
  (:require [sample.components.jdbc :as jdbc]
            [sample.components.web :as web]
            [com.stuartsierra.component :as component]))

(def base-config {:web {:port 9197}
                  :jdbc {:subprotocol "postgresql"
                         :subname "//localhost:5432/anychart_sample"
                         :classname "org.postgresql.Driver"
                         :user "alex"
                         :password ""}})

(defn app-system [config]
  (component/system-map
   :jdbc  (jdbc/new-jdbc (:jdbc config))
   :web   (component/using (web/new-web (:web config)) [:jdbc])))

(def dev (app-system base-config))

(defn start []
  (alter-var-root #'dev component/start))

(defn stop []
  (alter-var-root #'dev component/stop))

(defn -main []
  (println "starting server http://localhost:9197/")
  (component/start (app-system base-config)))
