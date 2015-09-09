(ns sample.web.routes
  (:require [compojure.core :refer [routes defroutes GET]]
            [compojure.route :as route]
            [selmer.parser :refer [render-file]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :refer [response redirect]]
            [sample.data :as data]))

(defn jdbc [request]
  (-> request :component :jdbc))

(defn- index-page [request]
  (render-file "templates/index.selmer" {:years (data/years (jdbc request))
                                         :industries (data/industries (jdbc request))
                                         :products (data/products (jdbc request))
                                         :regions (data/regions (jdbc request))
                                         :sales-reps (data/sales-reps (jdbc request))}))

(defroutes app-routes
  (route/resources "/")
  (GET "/" [] index-page))

(def app (-> app-routes
             wrap-keyword-params
             wrap-params))
