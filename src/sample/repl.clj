(ns sample.repl
  (:require [figwheel-sidecar.auto-builder :as fig-auto]
            [figwheel-sidecar.core :as fig]
            [clojurescript-build.auto :as auto]))

;; start the figwheel server
(def figwheel-server
  (fig/start-server { :css-dirs ["resources/public/css"] }))

(def config {:builds [{ :id "example"
                        :output-to "resources/public/checkbuild.js"
                        :output-dir "resources/public/out"
                        :optimizations :none }]
             :figwheel-server figwheel-server })

;; start the watching and building process
;; this will not block and output will appear in the REPL
(def fig-builder (fig-auto/autobuild* config))
