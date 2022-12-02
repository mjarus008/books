(ns clj-fire.profile.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.views :refer [panels]]))

(defn content []
  [:section
   [:h1 "Profile"]])

(defn main-panel []
  [auth.views/auth-wrapper [content]])

(defmethod panels :profile
  [_]
  [main-panel])

