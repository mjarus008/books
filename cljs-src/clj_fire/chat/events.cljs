(ns clj-fire.chat.events
  (:require [clj-fire.firebase :as fb]
            [clj-fire.log :as log]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::send-message
 (fn [{:keys [db]} [_ conversation-id message]]
   (let [sender (get-in db [:fb-user :displayName])]
     {:fx [(when message [:chat/add-message {:sender (or sender "anonymous")
                                             :conversation-id conversation-id
                                             :message message}])]})))

(re-frame/reg-fx
 :chat/add-message
 (fn [{:keys [sender conversation-id  message]}]
   (fb/new-message sender conversation-id message)))

(re-frame/reg-event-fx
 ::subscribe-to-thread
 (fn [{:keys [db]} [_ thread-id]]
   {:fx [[:fb/subscribe
          {:path [:messages thread-id]
           :on-value [::message-on-value thread-id]}]]}))

(re-frame/reg-event-db
 ::message-on-value
 (fn [db [_ thread-id messages]]
   (assoc-in db [:threads thread-id] messages)))
