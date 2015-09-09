(ns sample.core)

(defn ^:export init []
  (doto (js/anychart.barChart)
    (.container "rev-by-industry")
    (.title "Revenue by industry")
    (.bar #js [#js ["P1"  "128.14"]
               #js ["P2"  "112.61"]
               #js ["P3"  "163.21"]
               #js ["P4"  "229.98"]
               #js ["P5"  "90.54"]])
    (.draw))

  (doto (js/anychart.columnChart)
    (.container "rev-by-sales")
    (.title "Revenue by sales rep")
    (.column #js [#js ["P1" "128.14"]
                  #js ["P2" "128.14"]
                  #js ["P3" "128.14"]
                  #js ["P4" "128.14"]
                  #js ["P5" "128.14"]])
    (.draw))

  (doto (js/anychart.pie #js [#js ["Department Stores" 6371664]
                              #js ["Discount Stores" 6371664]])
    (.container "rev-by-product")
    (.title "Revenue by product")
    (.draw)))
