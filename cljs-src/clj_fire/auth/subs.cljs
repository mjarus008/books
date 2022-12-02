(ns clj-fire.auth.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::fb-user
 (fn [db _]
   (:fb-user db)))

(re-frame/reg-sub
 ::fb-logged-in?
 :<- [::fb-user]
 (fn [fb-user _]
   (boolean (seq fb-user))))
