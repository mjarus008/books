(ns clj-fire.thread.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.subs :as subs]
            [clj-fire.thread.events :as thread.events]
            [clj-fire.auth.subs :as auth.subs]
            [clj-fire.thread.subs :as thread.subs]
            [clj-fire.views :refer [panels]]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]))

(defn message
  ([content]
   (message nil content))
  ([sender content]
   ;; TODO(Lilitha): should sanitize the /"sender"/ perhaps the /"content"/ too?
   [:p.message [:span.message__sender (str (or sender "me") ":")] content]))

(defn message-input []
  (let [*message (reagent/atom "")]
    (fn []
      [:<>
       [:input.thread__input {:type "text"
                              :placeholder "Enter your message"
                              :value @*message
                              :on-change (fn [e]
                                           (reset! *message (-> e .-target .-value)))}]
       [:button {:on-click (fn [_evt]
                             (re-frame/dispatch [::thread.events/send-message "my-convo-id" @*message])
                             (reset! *message ""))} "Send"]])))

(defn thread-panel [thread-id]
  (reagent/with-let [_ (re-frame/dispatch [::thread.events/subscribe-to-thread thread-id])]
    (let [messages @(re-frame/subscribe [::thread.subs/messages thread-id])
          uid @(re-frame/subscribe [::auth.subs/fb-uid])]
      [auth.views/auth-wrapper
       [:section.thread
        [:h1.thread__header "Chat room"]
        [:div.thread__conversation
         (into [:<>]
               (for [{:keys [content sender_uid sender key]} messages
                     :let [sender-name (when
                                        (not= sender_uid uid)
                                         sender)]]
                 ^{:key key}
                 [message sender-name content]))
         [:div.thread__container
          [message-input]]]]])
    (finally
      (re-frame/dispatch [::thread.events/unsubscribe-to-thread]))))

(defmethod panels :thread
  [{{:keys [thread-id]} :route-params}]
  [thread-panel thread-id])

(defn threads-panel []
  [:section
   [:h1 "Threads"]
   [:div "TODO(Lilitha)"]])

(defmethod panels :threads
  [_]
  [threads-panel])