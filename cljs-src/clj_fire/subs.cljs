(ns clj-fire.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::route
 (fn [db _]
   (:routes db)))

(rf/reg-sub
 ::handler
 :<- [::route]
 (fn [route _]
   (:handler route)))
