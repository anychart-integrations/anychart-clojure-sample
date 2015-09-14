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

(defn- dates-filter [sqlmap years quarters]
  (merge-where sqlmap
               [:in (sql/call :date_part "year"
                              (sql/qualify :sales.date))
                years]
               [:in (sql/call :date_part "quarter"
                              (sql/qualify :sales.date))
                quarters]))

(defn- vectorize [items]
  (map (fn [item]
         [(:name item) (:revenue item)])
       items))

(defn revenue-by-industry [jdbc years quarters products regions industries sales-reps]
  (vectorize (query jdbc (-> (select [:industry.name :name]
                                     [:%sum.sales.total :revenue])
                             (from :industry :sales :product)
                             (where [:in :industry.id industries]
                                    [:in :product.id products]
                                    [:= :sales.product_id :product.id]
                                    [:= :product.industry_id :industry.id]
                                    [:in :sales.region_id regions]
                                    [:in :sales.rep_id sales-reps])
                             (dates-filter years quarters)
                             (group :industry.id)))))

(defn revenue-by-sales-reps [jdbc years quarters products regions sales-reps]
  (vectorize (query jdbc (-> (select [:sales_reps.name :name]
                                     [:%sum.sales.total :revenue])
                             (from :sales_reps :sales)
                             (where [:in :sales_reps.id sales-reps]
                                    [:= :sales.rep_id :sales_reps.id]
                                    [:in :sales.region_id regions]
                                    [:in :sales.product_id products])
                             (dates-filter years quarters)
                             (group :sales_reps.id)))))

(defn revenue-by-product [jdbc years quarters products regions sales-reps]
  (vectorize (query jdbc (-> (select [:product.name :name]
                                     [:%sum.sales.total :revenue])
                             (from :product :sales)
                             (where [:in :product.id products]
                                    [:= :sales.product_id :product.id]
                                    [:in :sales.region_id regions]
                                    [:in :sales.rep_id sales-reps])
                             (dates-filter years quarters)
                             (group :product.id)))))

(defn revenue-by-quarter [jdbc years quarters])
