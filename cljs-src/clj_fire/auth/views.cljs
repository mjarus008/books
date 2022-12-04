(ns clj-fire.auth.views
  (:require [clj-fire.auth.events :as events]
            [clj-fire.auth.subs :as auth.subs]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(defn login-panel []
  (let [id "firebase-login"]
    (reagent/create-class
     {:display-name "firebase-login"
      :component-did-mount
      (fn [_this]
        (re-frame/dispatch [::events/init-fb-login-panel {:id id}]))
      :reagent-render
      (fn []
        [:div {:id id}])})))

(defn auth-wrapper
  ([comp] [auth-wrapper {} comp])
  ([_opts comp]
   (let [logged-in? @(re-frame/subscribe [::auth.subs/fb-logged-in?])]
     (if logged-in?
       comp
       [:section
        [:h1.login-header "Please login to continue"]
        [login-panel]]))))
