(ns sample.ui
  (:require [reagent.core :as r]
            [sample.data :as data]
            [cljs.core.async :refer [put!]]))


(defn- toggle-item [items item]
  (map #(if (= % item)
          (assoc % :selected (not (data/is-selected %)))
          %)
       items))


(defn- group-selector
  ([label state key is-disabled update-chan]
   [:div.form-group
    [:label.col-sm-1.control-label label]
    [:div
     (doall (for [item (get @state key)]
              ^{:key (:id item)} [:label.checkbox-inline
                                  [:input {:type      :checkbox
                                           :checked   (and (data/is-selected item)
                                                           (not (is-disabled state item)))
                                           :disabled  (is-disabled state item)
                                           :value     (:id item)
                                           :on-change (fn []
                                                        (swap! state assoc key
                                                               (toggle-item (get @state key) item))
                                                        (put! update-chan state))}]
                                  (:name item)]))]])
  ([label state key update-chan]
   (group-selector label state key (constantly false) update-chan)))


(defn- years-selector [state update-chan]
  [group-selector "Years" state :years update-chan])


(defn- quarters-selector [state update-chan]
  [group-selector "Quarters" state :quarters update-chan])


(defn- industries-selector [state update-chan]
  [group-selector "Industries" state :industries update-chan])


(defn- product-selector [state update-chan]
  [group-selector "Products" state :products
   (fn [state item]
     (let [industry-id (:industry_id item)]
       (-> (filter #(= industry-id (:id %)) (get @state :industries))
           first
           data/is-selected
           not)))
   update-chan])


(defn- regions-selector [state update-chan]
  [group-selector "Regions" state :regions update-chan])


(defn- sales-reps-selector [state update-chan]
  [group-selector "Sales Reps" state :sales-reps update-chan])


(defn create [state update-chan]
  (r/render-component [years-selector state update-chan]
                      (js/document.getElementById "years-selector"))

  (r/render-component [quarters-selector state update-chan]
                      (js/document.getElementById "quarters-selector"))

  (r/render-component [industries-selector state update-chan]
                      (js/document.getElementById "industries-selector"))

  (r/render-component [product-selector state update-chan]
                      (js/document.getElementById "products-selector"))

  (r/render-component [regions-selector state update-chan]
                      (js/document.getElementById "regions-selector"))

  (r/render-component [sales-reps-selector state update-chan]
                      (js/document.getElementById "sales-reps-selector")))
