(ns clj-fire.home.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.views :refer [panels]]))

(defn main-panel []
  [:div.intro
   [:h1.intro--header "Books"]
   [:p.intro--content "We have books for you to read, study with others online and benefit from others' notes."]
   [:p "Get access to tutors when you get stuck, at a low cost.xyz"]
   [:br]
   [auth.views/login-panel]])

(defmethod panels :home
  [_]
  [main-panel])
