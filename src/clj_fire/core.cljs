(ns clj-fire.core
  (:require
   [goog.dom :as gdom]
   [reagent.dom :as rdom]
   [clj-fire.auth.views :as auth.views]))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:h1 "Molweni App"]
   [auth.views/login-panel]])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)
