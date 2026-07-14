(ns merkle-sum.cljs-runner
  "Run the portable suite under a real ClojureScript host (cljs.main
  --target node) — the fleet runtime priority (cljs before JVM compat).
    clojure -Sdeps '{:paths [\"src\" \"test\"]}' -M:cljs \\
      -m cljs.main --target node -m merkle-sum.cljs-runner"
  (:require [clojure.test :as t :refer [run-tests]]
            [merkle-sum.core-test]))

#?(:cljs
   (defmethod t/report [:cljs.test/default :end-run-tests] [m]
     (when-not (t/successful? m)
       (set! (.-exitCode js/process) 1))))

(defn -main [] (run-tests 'merkle-sum.core-test))
