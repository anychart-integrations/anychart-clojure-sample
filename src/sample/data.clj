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

(defn- vectorize [items]
  (map vals items))

(defn revenue-by-industry [jdbc years quarters products regions industries sales-reps]
  (vectorize (query jdbc (-> (select [:industry.name :name]
                                     [:%sum.sales.total :revenue])
                             (from :industry :sales :product)
                             (where [:= :sales.product_id :product.id]
                                    [:= :product.industry_id :industry.id]
                                    [:in :sales.region_id regions]
                                    [:in :sales.rep_id sales-reps]
                                    [:in :industry.id industries]
                                    [:in :product.id products]
                                    [:in (sql/call :date_part "year" :sales.date)
                                     years]
                                    [:in (sql/call :date_part "quarter" :sales.date)
                                     quarters])
                             (group :industry.id)
                             (order-by (sql/raw "1"))))))

(defn revenue-by-sales-reps [jdbc years quarters products regions sales-reps]
  (vectorize (query jdbc (-> (select [:sales_reps.name :name]
                                     [:%sum.sales.total :revenue])
                             (from :sales_reps :sales)
                             (where [:= :sales.rep_id :sales_reps.id]
                                    [:in :sales_reps.id sales-reps]
                                    [:in :sales.region_id regions]
                                    [:in :sales.product_id products]
                                    [:in (sql/call :date_part "year" :sales.date)
                                     years]
                                    [:in (sql/call :date_part "quarter" :sales.date)
                                     quarters])
                             (group :sales_reps.id)
                             (order-by (sql/raw "1"))))))

(defn revenue-by-product [jdbc years quarters products regions sales-reps]
  (vectorize (query jdbc (-> (select [:product.name :name]
                                     [:%sum.sales.total :revenue])
                             (from :product :sales)
                             (where [:= :sales.product_id :product.id]
                                    [:in :product.id products]
                                    [:in :sales.region_id regions]
                                    [:in :sales.rep_id sales-reps]
                                    [:in (sql/call :date_part "year" :sales.date)
                                     years]
                                    [:in (sql/call :date_part "quarter" :sales.date)
                                     quarters])
                             (dates-filter years quarters)
                             (group :product.id)
                             (order-by (sql/raw "1"))))))

(defn revenue-by-quarter [jdbc years quarters products regions sales-reps]
  (let [sel (reduce (fn [res quarter]
                      (merge-select
                       res
                       [(sql/raw (str "SUM(total) FILTER (WHERE date_part('quarter', \"date\") = " (int quarter) ")")) (str quarter)]))
                    (select [(sql/call :date_part "year" :sales.date) :year])
                    quarters)]
    (map vals (query jdbc (-> sel
                              (from :sales)
                              (where [:in :sales.region_id regions]
                                     [:in :sales.product_id products]
                                     [:in :sales.rep_id sales-reps]
                                     [:in (sql/call :date_part "year" :sales.date)
                                      years]
                                     [:in (sql/call :date_part "quarter" :sales.date)
                                      quarters])
                              (group (sql/raw "1"))
                              (order-by (sql/raw "1")))))))
