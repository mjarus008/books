(ns ^:figwheel-hooks clj-fire.core
  (:require [clj-fire.firebase :as fb]
            clj-fire.home.views
            clj-fire.profile.views
            [clj-fire.routes :as routes]
            [clj-fire.views :as views]
            day8.re-frame.http-fx
            [clj-fire.auth.events :as fb-auth]
            [re-frame.core :as re-frame]
            [reagent.dom :as rdom]))

(defn ^:after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main] root-el)))

(defn init []
  (fb/on-auth-state-changed [::fb-auth/auth-state-change])
  (routes/start!)
  (mount-root))

(defonce _initial (init))
