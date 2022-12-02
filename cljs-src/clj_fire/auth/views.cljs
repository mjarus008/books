(ns clj-fire.auth.views
  (:require [clj-fire.auth.events :as events]
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
