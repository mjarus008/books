(ns clj-fire.views
  (:require [clj-fire.events :as events]
            [clj-fire.subs :as subs]
            [re-frame.core :as rf]))

(defn- not-found []
  [:p "The page you are looking for could not be found."])

(defmulti panels identity)
(defmethod panels :default
  [_]
  [not-found])

(defn main []
  (let [handler @(rf/subscribe [::subs/handler])]
    [panels handler]))
