(ns clj-fire.views
  (:require [clj-fire.subs :as subs]
            [re-frame.core :as rf]
            [clj-fire.utils :as utils]))

(defn- not-found [params]
  [:section
   [:h3 "The page you are looking for could not be found."]
   [:pre (utils/pretty-str params)]])

(defmulti panels :handler)
(defmethod panels :default
  [params]
  [not-found params])

(defn main []
  (let [route @(rf/subscribe [::subs/route])]
    [panels route]))
