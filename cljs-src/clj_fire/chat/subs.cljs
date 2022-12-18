(ns clj-fire.chat.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::messages
 (fn [db [_ thread-id]]
   (get-in db [:threads thread-id])))
