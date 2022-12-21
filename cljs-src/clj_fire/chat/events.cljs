(ns clj-fire.chat.events
  (:require [clj-fire.firebase :as fb]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::send-message
 (fn [{:keys [db]} [_ conversation-id message]]
   (let [sender (get-in db [:fb-user :displayName])]
     {:fx [(when message [:chat/send-message {:sender (or sender "anonymous")
                                              :conversation-id conversation-id
                                              :message message}])]})))

(re-frame/reg-fx
 :chat/send-message
 (fn [{:keys [sender conversation-id  message]}]
   (fb/new-message sender conversation-id message)))

(re-frame/reg-event-db
 :chat/add-message
 (fn [db [_ thread-id message]]
   (update-in db [:threads thread-id] (fnil conj []) message)))

(re-frame/reg-event-fx
 ::subscribe-to-thread
 (fn [{:keys [db]} [_ thread-id]]
   {:db (update-in db [:threads] dissoc thread-id)
    :fx [[:fb/subscribe-thread
          {:thread-id thread-id
           :on-value [:chat/add-message thread-id]}]]}))

(re-frame/reg-event-fx
 ::unsubscribe-to-thread
 (fn [{:keys [db]} [_ thread-id]]
   {:db (update-in db [:threads] dissoc thread-id)
    :fx [[:fb/unsubscribe-to-thread
          {:thread-id thread-id}]]}))
