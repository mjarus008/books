(ns clj-fire.profile.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.auth.subs :as auth.subs]
            [clj-fire.views :refer [panels]]
            [re-frame.core :as re-frame]))

(defn content []
  (let [fb-user @(re-frame/subscribe [::auth.subs/fb-user])]
    [:section
     [:h1 "Profile"]
     [:pre fb-user]]))

(defn main-panel []
  [auth.views/auth-wrapper [content]])

(defmethod panels :profile
  [_]
  [main-panel])

