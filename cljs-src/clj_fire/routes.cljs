(ns clj-fire.routes
  (:require
   [bidi.bidi :as bidi]
   [clj-fire.events :as events]
   [pushy.core :as pushy]
   [re-frame.core :as rf]))

(def routes
  ["/" {""      :home
        "stats" :stats
        "settings" :settings}])

(defn parse
  [url]
  (bidi/match-route routes url))

(defn dispatch
  [route]
  (rf/dispatch [::events/set-route route]))

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate!
  [handler & params]
  (pushy/set-token!
   history
   (apply bidi/path-for routes handler params)))

(comment (bidi/path-for routes :stats))

(defn start!
  []
  (pushy/start! history))

(rf/reg-fx
 :navigate
 (fn [route]
   (apply navigate! route)))
