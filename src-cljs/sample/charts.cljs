(ns sample.charts
  (:require [anychart]))

(def ^:private revenue-by-industry (anychart.data.set))
(def ^:private revenue-by-sales (anychart.data.set))
(def ^:private revenue-by-product (anychart.data.set))
(def ^:private revenue-by-quarter (anychart.data.set))

(defn create
  "Setup all charts"
  []
  (js/anychart.theme (clj->js {:defaultTooltip {:title {:enabled false}}}))
  
  (doto (anychart.bar revenue-by-industry)
    (.container "rev-by-industry")
    (.title "Revenue by industry")
    (.draw))

  (doto (anychart.column revenue-by-sales)
    (.container "rev-by-sales")
    (.title "Revenue by sales rep")
    (.draw))

  (doto (anychart.pie revenue-by-product)
    (.container "rev-by-product")
    (.title "Revenue by product")
    (.draw))

  (doto (anychart.line revenue-by-quarter)
    (.container "rev-by-quarter")
    (.title "Revenue by quarter")
    (.draw)))

(defn update-charts
  "Replace charts data"
  [data]
  (.data revenue-by-industry (clj->js (data :revenue-by-industry)))
  (.data revenue-by-sales (clj->js (data :revenue-by-sales-reps)))
  (.data revenue-by-product (clj->js (data :revenue-by-product)))
  (.data revenue-by-quarter (clj->js (data :revenue-by-quarter))))
