(ns sample.charts)

(enable-console-print!)

(def ^:private revenue-by-industry (js/anychart.data.set))
(def ^:private revenue-by-sales (js/anychart.data.set))
(def ^:private revenue-by-product (js/anychart.data.set))
(def ^:private revenue-by-quarter (js/anychart.data.set))

(defn create []
  (js/anychart.theme (clj->js {:defaultTooltip {:title {:enabled false}}}))
  
  (doto (js/anychart.bar revenue-by-industry)
    (.container "rev-by-industry")
    (.title "Revenue by industry")
    (.draw))

  (doto (js/anychart.column revenue-by-sales)
    (.container "rev-by-sales")
    (.title "Revenue by sales rep")
    (.draw))

  (doto (js/anychart.pie revenue-by-product)
    (.container "rev-by-product")
    (.title "Revenue by product")
    (.draw))

  (doto (js/anychart.line revenue-by-quarter)
    (.container "rev-by-quarter")
    (.title "Revenue by quarter")
    (.draw)))

(defn update-charts [data]
  (.data revenue-by-industry (clj->js (data :revenue-by-industry)))
  (.data revenue-by-sales (clj->js (data :revenue-by-sales-reps)))
  (.data revenue-by-product (clj->js (data :revenue-by-product)))
  (.data revenue-by-quarter (clj->js (data :revenue-by-quarter)))
  (.log js/console (clj->js (data :revenue-by-quarter))))
