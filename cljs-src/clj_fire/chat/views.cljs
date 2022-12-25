(ns clj-fire.chat.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.chat.events :as chat.events]
            [clj-fire.auth.subs :as auth.subs]
            [clj-fire.chat.subs :as chat.subs]
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
       [:input.chat__input {:type "text"
                            :placeholder "Enter your message"
                            :value @*message
                            :on-change (fn [e]
                                         (reset! *message (-> e .-target .-value)))}]
       [:button {:on-click (fn [_evt]
                             (re-frame/dispatch [::chat.events/send-message "my-convo-id" @*message])
                             (reset! *message ""))} "Send"]])))

(defn main-panel []
  (reagent/with-let [_ (re-frame/dispatch [::chat.events/subscribe-to-thread "my-convo-id"])]
    (let [messages @(re-frame/subscribe [::chat.subs/messages "my-convo-id"])
          uid @(re-frame/subscribe [::auth.subs/fb-uid])]
      [auth.views/auth-wrapper
       [:section.chat
        [:h1.chat__header "Chat room"]
        [:div.chat__conversation
         (into [:<>]
               (for [{:keys [content sender_uid sender key]} messages
                     :let [sender-name (when
                                        (not= sender_uid uid)
                                         sender)]]
                 ^{:key key}
                 [message sender-name content]))
         [:div.chat__container
          [message-input]]]]])
    (finally
      (re-frame/dispatch [::chat.events/unsubscribe-to-thread]))))

(defmethod panels :chat
  [_]
  [main-panel])