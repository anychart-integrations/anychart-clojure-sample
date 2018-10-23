(ns sample.web.routes
  (:require [compojure.core :refer [routes defroutes GET]]
            [compojure.route :as route]
            [selmer.parser :refer [render-file]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response redirect]]
            [sample.db.data :as data]))


(defn- jdbc
  "Get jdbc from request"
  [request]
  (-> request :component :jdbc))


(defn- index-page
  "Index page"
  [request]
  (render-file "templates/index.selmer" {:debug (-> request :component :config :debug)}))


(defn- init
  "Get initial settings"
  [request]
  (response {:years      (map (fn [y] {:id y :name y}) (data/years (jdbc request)))
             :industries (data/industries (jdbc request))
             :products   (data/products (jdbc request))
             :regions    (data/regions (jdbc request))
             :sales-reps (data/sales-reps (jdbc request))
             :quarters   (map (fn [d] {:id d :name d}) (range 1 5))}))


(defn- charts-data
  "Get filtered charts data"
  [request]
  (let [years (-> request :params :years read-string)
        quarters (-> request :params :quarters read-string)
        industries (-> request :params :industries read-string)
        products (-> request :params :products read-string)
        sales-reps (-> request :params :sales-reps read-string)
        regions (-> request :params :regions read-string)]
    (response {:revenue-by-industry   (data/revenue-by-industry (jdbc request)
                                                                years
                                                                quarters
                                                                products
                                                                regions
                                                                industries
                                                                sales-reps)
               :revenue-by-sales-reps (data/revenue-by-sales-reps (jdbc request)
                                                                  years
                                                                  quarters
                                                                  products
                                                                  regions
                                                                  sales-reps)
               :revenue-by-product    (data/revenue-by-product (jdbc request)
                                                               years
                                                               quarters
                                                               products
                                                               regions
                                                               sales-reps)
               :revenue-by-quarter    (data/revenue-by-quarter (jdbc request)
                                                               years
                                                               quarters
                                                               products
                                                               regions
                                                               sales-reps)})))


(defroutes app-routes
           (route/resources "/")
           (GET "/" [] index-page)
           (GET "/init" [] init)
           (GET "/data" [] charts-data))


(def app (-> app-routes
             wrap-keyword-params
             wrap-params
             wrap-json-response))
