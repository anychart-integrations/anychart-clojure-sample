(ns sample.core
  (:require [sample.charts :as charts]
            [sample.ui :as ui]
            [sample.data :as data]
            [reagent.core :as r]
            [cljs.core.async :as async :refer [chan]])
  (:require-macros
   [cljs.core.async.macros :refer [go]]))

(def state (r/atom {}))

(defn ^:export init []
  
  (go
    (let [res (<! (data/GET "/init"))]
      (reset! state res)
      (charts/update-charts (<! (data/GET (str "/data?" (data/query-string @state)))))))

  (let [update (chan)]
    (ui/create state update)
    (go (while true
          (charts/update-charts
           (<! (data/GET (str "/data?" (data/query-string @(<! update)))))))))
  
  (charts/create))
