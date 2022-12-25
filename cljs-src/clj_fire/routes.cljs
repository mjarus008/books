(ns clj-fire.routes
  (:require
   [bidi.bidi :as bidi]
   [clj-fire.events :as events]
   [pushy.core :as pushy]
   [re-frame.core :as rf]))

(def routes
  ["/" {""      :home
        "home" :home
        "settings" :settings
        "profile" :profile
        ["threads/" :thread-id] :thread}])

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

(defn form-url
  ([handler] (form-url handler nil))
  ([handler params]
   (apply bidi/path-for routes handler (mapcat identity params))))

(comment
  (form-url :thread {:thread-id "m"})
  (bidi/path-for routes :profile {:thread-id "m"}))

(comment (bidi/path-for routes :profile))

(defn start!
  []
  (pushy/start! history))

(rf/reg-fx
 :navigate
 (fn [route]
   (apply navigate! route)))
