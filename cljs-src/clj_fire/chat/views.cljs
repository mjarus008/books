(ns clj-fire.chat.views
  (:require [clj-fire.auth.views :as auth.views]
            [clj-fire.views :refer [panels]]))

(defn message
  ([content]
   (message "me" content))
  ([sender content]
   ;; TODO(Lilitha): should sanitize the /"sender"/ perhaps the /"content"/ too?
   [:p.message [:span.message__sender (str sender ":")] content]))

(defn main-panel []
  [auth.views/auth-wrapper
   [:section.chat
    [:h1.chat__header "Chat room"]
    [:div.chat__conversation
     [message "Hello there!"]
     [message "Sean" "Hello back!"]]]])

(defmethod panels :chat
  [_]
  [main-panel])