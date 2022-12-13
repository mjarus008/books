(ns clj-fire.chat.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.chat.events :as chat.events]
            [clj-fire.chat.subs :as chat.subs]
            [clj-fire.views :refer [panels]]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]))

(defn message
  ([content]
   (message "me" content))
  ([sender content]
   ;; TODO(Lilitha): should sanitize the /"sender"/ perhaps the /"content"/ too?
   [:p.message [:span.message__sender (str sender ":")] content]))

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
  ;; TODO(Lilitha): should unsubscribe to Firebase when component is distroyed
  (let [_ (re-frame/dispatch [::chat.events/subscribe-to-thread "my-convo-id"])]
    (fn []
      (let [messages @(re-frame/subscribe [::chat.subs/messages "my-convo-id"])]
        [auth.views/auth-wrapper
         [:section.chat
          [:h1.chat__header "Chat room"]
          [:div.chat__conversation
           (into [:<>]
                 (map (fn [{:keys [content sender]}]
                        [message sender content]) messages))
           [:div.chat__container
            [message-input]]]]]))))

(defmethod panels :chat
  [_]
  [main-panel])