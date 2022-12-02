(ns clj-fire.profile.views 
  (:require [clj-fire.views :refer [panels]]))

(defn main-panel []
  [:section
   [:h1 "Profile"]])

(defmethod panels :profile
  [_]
  [main-panel])

