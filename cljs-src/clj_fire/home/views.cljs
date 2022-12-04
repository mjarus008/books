(ns clj-fire.home.views
  (:require [clj-fire.auth.subs :as auth.subs]
            [clj-fire.auth.views :as auth.views]
            [clj-fire.routes :as routes]
            [clj-fire.views :refer [panels]]
            [re-frame.core :as re-frame]))

(defn main-panel []
  (let [logged-in? @(re-frame/subscribe [::auth.subs/fb-logged-in?])]
    [:div.intro
     [:h1.intro--header "Books"]
     [:p.intro--content "We have books for you to read, study with others online and benefit from others' notes."]
     [:p "Get access to tutors when you get stuck, at a low cost."]
     [:br]
     (if-not logged-in?
       [auth.views/login-panel]
       [:div
        [:a.view-profile {:href (routes/form-url :profile)} "View profile"]])]))

(defmethod panels :home
  [_]
  [main-panel])
