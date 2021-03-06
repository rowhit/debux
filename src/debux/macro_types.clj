(ns debux.macro-types
  (:require [clojure.set :as set]
            [debux.common.util :as ut] ))

;;; macro management
(def macro-types*
  (atom {:def-type `#{def defonce}
         :defn-type `#{defn defn-}
         :fn-type `#{fn fn*}

         :let-type
         `#{let binding dotimes if-let if-some loop when-first when-let
            when-some with-in-str with-local-vars with-open with-out-str
            with-redefs}
         :letfn-type `#{letfn}
         
         :for-type `#{for doseq}
         :case-type `#{case}

         :skip-arg-1-type `#{set! with-precision}
         :skip-arg-2-type `#{as->}
         :skip-arg-1-2-type `#{}
         :skip-arg-1-3-type `#{defmethod}
         :skip-arg-2-3-type `#{amap areduce}
         :skip-form-itself-type
         `#{catch comment declare definline definterface defmacro defmulti
            defprotocol defrecord defstruct deftype extend-protocol
            extend-type finally gen-class gen-interface import memfn
            new ns proxy proxy-super quote refer-clojure reify sync
            var throw}

         :expand-type
         `#{clojure.core/.. -> ->> doto cond-> cond->> condp import 
            some-> some->>}
         :dot-type `#{.} }))

(defn- merge-symbols [old-symbols new-symbols]
  (->> (map #(ut/ns-symbol %) new-symbols)
       set
       (set/union old-symbols) ))

(defn register-macros! [macro-type new-symbols]
  (swap! macro-types* update macro-type
         #(merge-symbols % new-symbols)))

(defn show-macros
  ([] @macro-types*)
  ([macro-type] (select-keys @macro-types* [macro-type])))

