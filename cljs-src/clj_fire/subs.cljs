(ns clj-fire.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::route
 (fn [db _]
   (:routes db)))

(re-frame/reg-sub
 ::handler
 :<- [::route]
 (fn [route _]
   (:handler route)))
