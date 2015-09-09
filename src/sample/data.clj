(ns sample.data
  (:require [honeysql.helpers :refer :all]
            [honeysql.core :as sql]
            [sample.components.jdbc :refer [query]])
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(defn years [jdbc]
  (map #(-> % :year int)
       (query jdbc (-> (select [(sql/call :extract (sql/raw "YEAR FROM sales.date"))
                                :year])
                       (from :sales)
                       (group :year)
                       (order-by [:year :desc])))))

(defn industries [jdbc]
  (query jdbc (-> (select :id :name)
                  (from :industry)
                  (order-by :name))))

(defn regions [jdbc]
  (query jdbc (-> (select :id :name)
                  (from :region)
                  (order-by :name))))

(defn products [jdbc]
  (query jdbc (-> (select :id :name :industry_id)
                  (from :product)
                  (order-by :name))))

(defn sales-reps [jdbc]
  (query jdbc (-> (select :id :name)
                  (from :sales_reps)
                  (order-by :name))))

(defn revenue-by-industry [jdbc years quarters regions products sales-reps]
  (query jdbc (-> (select [:industry.name :industry-name]
                          [:%summ.sales.total :revenue])
                  (from :industry :sales :product)
                  (where [:in :product.id products]
                         [:in :sales.region_id regions]
                         [:in :sales.product_id :product.id]
                         [:in :industry.id :product.industry_id])
                  (group :industry.id))))
