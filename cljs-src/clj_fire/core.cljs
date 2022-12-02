(ns ^:figwheel-hooks clj-fire.core
  (:require clj-fire.home.views
            [clj-fire.routes :as routes]
            [clj-fire.views :as views]
            day8.re-frame.http-fx
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame]))

(defn ^:after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main] root-el)))

(defn init []
  (routes/start!)
  (mount-root))

(defonce _initial (init))
