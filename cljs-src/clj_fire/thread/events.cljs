(ns clj-fire.thread.events
  (:require [clj-fire.firebase :as fb]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::send-message
 (fn [{:keys [db]} [_ conversation-id message]]
   (let [sender (get-in db [:fb-user :displayName])
         uid (get-in db [:fb-user :uid])]
     {:fx [(when message [:thread/send-message {:sender (or sender "anonymous")
                                                :conversation-id conversation-id
                                                :message message
                                                :uid uid}])]})))

(re-frame/reg-fx
 :thread/send-message
 (fn [{:keys [conversation-id message sender uid]}]
   (fb/new-message sender uid conversation-id message)))

(re-frame/reg-event-db
 :thread/add-message
 (fn [db [_ thread-id message]]
   (update-in db [:threads thread-id] (fnil conj []) message)))

(re-frame/reg-event-db
 :thread/delete-message
 (fn [db [_ thread-id message-id]]
   (-> db
       (update-in [:threads thread-id] #(filterv (comp (partial not= message-id) :key) %)))))

(re-frame/reg-event-fx
 ::subscribe-to-thread
 (fn [{:keys [db]} [_ thread-id]]
   {:db (update-in db [:threads] dissoc thread-id)
    :fx [[:fb/subscribe-thread
          {:thread-id thread-id
           :on-value [:thread/add-message thread-id]
           :on-delete [:thread/delete-message thread-id]}]]}))

(re-frame/reg-event-fx
 ::unsubscribe-to-thread
 (fn [{:keys [db]} [_ thread-id]]
   {:db (update-in db [:threads] dissoc thread-id)
    :fx [[:fb/unsubscribe-to-thread
          {:thread-id thread-id}]]}))
