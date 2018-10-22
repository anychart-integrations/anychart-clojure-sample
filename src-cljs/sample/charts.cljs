(ns sample.charts
  (:require [anychart]))


(def ^:private revenue-by-industry (anychart.data.set))
(def ^:private revenue-by-sales (anychart.data.set))
(def ^:private revenue-by-product (anychart.data.set))
(def ^:private revenue-by-quarter (atom nil))


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

  (reset! revenue-by-quarter (anychart.line))
  (doto @revenue-by-quarter
    (.container "rev-by-quarter")
    (.title "Revenue by quarter")
    (.draw)))


(defn update-charts
  "Replace charts data"
  [data]
  (.data revenue-by-industry (clj->js (data :revenue-by-industry)))
  (.data revenue-by-sales (clj->js (data :revenue-by-sales-reps)))
  (.data revenue-by-product (clj->js (data :revenue-by-product)))
  (.removeAllSeries @revenue-by-quarter)
  (.apply (.-addSeries @revenue-by-quarter)
          @revenue-by-quarter
          (anychart.data.mapAsTable (clj->js (data :revenue-by-quarter)))))
