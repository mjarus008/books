(ns clj-fire.utils
  (:require [cljs.pprint :as pretty]))

(defn pretty-str [obj]
  (with-out-str (pretty/pprint obj)))