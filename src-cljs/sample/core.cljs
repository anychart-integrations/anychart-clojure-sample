(ns sample.core
  (:require [reagent.core :as r]
            [cljs.core.async :as async :refer [chan close!]]
            [goog.net.XhrIo :as xhr]
            [goog.Uri.QueryData :as gquery-data]
            [goog.structs :as gstruct]
            [sample.charts :as charts])
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]))

(enable-console-print!)

(defn- GET [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseJson)]
                  (go (>! ch (js->clj res :keywordize-keys true))
                      (close! ch)))))
    ch))

(defn- is-selected [item]
  (or (not (contains? item :selected))
      (:selected item)))

(defn- toggle-item [items item]
  (map #(if (= % item)
          (assoc % :selected (not (is-selected %)))
          %)
       items))

(defn- to-query-param [items]
  (str "("
       (clojure.string/join ","
                            (map :id
                                 (filter is-selected items)))
       ")"))

(defn- update-charts [state]
  (let [query-string (-> {:years (to-query-param (:years @state))
                          :quarters (to-query-param (:quarters @state))
                          :products (to-query-param (:products @state))
                          :regions (to-query-param (:regions @state))
                          :industries (to-query-param (:industries @state))
                          :sales-reps (to-query-param (:sales-reps @state))}
                         clj->js
                         gstruct/Map.
                         gquery-data/createFromMap
                         .toString)]
    (go
      (charts/update-charts (<! (GET (str "/data?" query-string)))))))

(defn- group-selector
  ([label state key is-disabled]
   [:div.form-group
    [:label.col-sm-1.control-label label]
    [:div
     (doall (for [item (get @state key)]
       ^{:key (:id item)} [:label.checkbox-inline
                           [:input {:type :checkbox
                                    :checked (and (is-selected item)
                                                  (not (is-disabled state item)))
                                    :disabled (is-disabled state item)
                                    :value (:id item)
                                    :on-change (fn []
                                                 (swap! state assoc key
                                                        (toggle-item (get @state key) item))
                                                 (update-charts state))}
                            (:name item)]]))]])
  ([label state key]
   (group-selector label state key (fn [_] false))))

(defn- years-selector [state]
  [group-selector "Years" state :years])

(defn- quarters-selector [state]
  [group-selector "Quarters" state :quarters])

(defn- industries-selector [state]
  [group-selector "Industries" state :industries])

(defn- product-selector [state]
  [group-selector "Products" state :products
   (fn [state item]
     (let [industry-id (:industry_id item)]
       (-> (filter #(= industry-id (:id %)) (get @state :industries))
            first
            is-selected
            not)))])

(defn- regions-selector [state]
  [group-selector "Regions" state :regions])

(defn- sales-reps-selector [state]
  [group-selector "Sales Reps" state :sales-reps])

(def state (r/atom {}))

(defn ^:export init []
  (go
    (let [res (<! (GET "/init"))]
      (reset! state res)
      (update-charts state)))
  
  (r/render-component [years-selector state]
                      (js/document.getElementById "years-selector"))

  (r/render-component [quarters-selector state]
                      (js/document.getElementById "quarters-selector"))

  (r/render-component [industries-selector state]
                      (js/document.getElementById "industries-selector"))

  (r/render-component [product-selector state]
                      (js/document.getElementById "products-selector"))

  (r/render-component [regions-selector state]
                      (js/document.getElementById "regions-selector"))

  (r/render-component [sales-reps-selector state]
                      (js/document.getElementById "sales-reps-selector"))

  (charts/create))
