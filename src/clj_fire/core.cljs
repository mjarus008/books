(ns clj-fire.core
  (:require [clj-fire.home.views :as home.views]
            [goog.dom :as gdom]
            [reagent.dom :as rdom]))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [home.views/main-panel])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)
