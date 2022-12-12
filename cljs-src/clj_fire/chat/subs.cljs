(ns clj-fire.chat.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::messages-raw
 (fn [db [_ thread-id]]
   (get-in db [:threads thread-id])))

(re-frame/reg-sub
 ::messages
 (fn [[_ thread-id]]
   [(re-frame/subscribe [::messages-raw thread-id])])
 (fn [[messages-raw] [_ _thread-id]]
   (when (seq messages-raw)
     (vals messages-raw))))
