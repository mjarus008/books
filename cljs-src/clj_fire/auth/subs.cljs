(ns clj-fire.auth.subs
  (:require [re-frame.core :as re-frame]))

;; Firebase user

(re-frame/reg-sub
 ::fb-user
 (fn [db _]
   (:fb-user db)))

(re-frame/reg-sub
 ::fb-display-name
 :<- [::fb-user]
 (fn [user _]
   (:displayName user)))

(re-frame/reg-sub
 ::fb-uid
 :<- [::fb-user]
 (fn [user _]
   (:uid user)))

(re-frame/reg-sub
 ::fb-logged-in?
 :<- [::fb-user]
 (fn [fb-user _]
   (boolean (seq fb-user))))

(re-frame/reg-sub
 ::user-data
 (fn [db _]
   (:user-data db)))

(comment
  @(re-frame/subscribe [::fb-user])
  @(re-frame/subscribe [::user-data]))
