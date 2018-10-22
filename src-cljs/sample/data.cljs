(ns sample.data
  (:require [cljs.core.async :as async :refer [chan close!]]
            [goog.net.XhrIo :as xhr]
            [goog.Uri.QueryData :as gquery-data]
            [goog.structs :as gstruct]
            [clojure.string])
  (:require-macros
    [cljs.core.async.macros :refer [go]]))


(defn is-selected [item]
  (or (not (contains? item :selected))
      (:selected item)))


(defn- to-query-param [items]
  (str "("
       (clojure.string/join ","
                            (map :id
                                 (filter is-selected items)))
       ")"))


(defn query-string [state]
  (-> {:years      (to-query-param (:years state))
       :quarters   (to-query-param (:quarters state))
       :products   (to-query-param (:products state))
       :regions    (to-query-param (:regions state))
       :industries (to-query-param (:industries state))
       :sales-reps (to-query-param (:sales-reps state))}
      clj->js
      gstruct/Map.
      gquery-data/createFromMap
      .toString))


(defn GET [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseJson)]
                  (go (>! ch (js->clj res :keywordize-keys true))
                      (close! ch)))))
    ch))
