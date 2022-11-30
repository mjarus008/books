(ns clj-fire.auth.views
  (:require [reagent.core :as reagent]
            [clj-fire.firebase :as fb]
            ["firebase/compat/app$default" :as firebase]))

(defn login-panel []
  (let [id "firebase-login"]
    (reagent/create-class
     {:display-name "firebase-login"
      :component-did-mount
      (fn [_this]
        (fb/init-login-panel! 
         {:id id
          :signInOptions
          [{:provider firebase.auth.EmailAuthProvider.PROVIDER_ID
            :requireDisplayName false}
           firebase.auth.GoogleAuthProvider.PROVIDER_ID]
          :callbacks { :signInSuccessWithAuthResult
           (fn [auth-result redirect-url]
             (print "auth result: " auth-result)
             true)
           :uiShown
           (fn []
             (print "UI shown."))}
          :signInSuccessUrl "http://localhost:9500/"}))
      :reagent-render
      (fn []
        [:div {:id id}])})))

