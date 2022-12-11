(ns clj-fire.log
  (:require [re-frame.core :as re-frame]))

(def warn prn)

(def error prn)

(def info prn)

(def trace prn)

(re-frame/reg-event-fx
 :log/error
 (fn [_ [_ args]]
   {:log/error args}))

(re-frame/reg-fx
 :log/error
 (fn [args]
   (error args)))

(re-frame/reg-event-fx
 :log/info
 (fn [_ [_ args]]
   {:log/info args}))

(re-frame/reg-fx
 :log/info
 (fn [args]
   (info args)))