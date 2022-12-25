(ns clj-fire.profile.views
  (:require [clj-fire.auth.events :as auth.events]
            [clj-fire.auth.subs :as auth.subs]
            [clj-fire.auth.views :as auth.views]
            [clj-fire.routes :as routes]
            [clj-fire.utils :as utils]
            [clj-fire.views :refer [panels]]
            [re-frame.core :as re-frame]))

(defn content []
  (let [fb-user @(re-frame/subscribe [::auth.subs/fb-user])]
    [:section
     [:h1 "Profile"]
     [:button.sign-out {:on-click #(re-frame/dispatch [::auth.events/logout])} "logout"]
     [:pre.code-block (utils/pretty-str (select-keys fb-user [:display-name :providerData]))]
     [:br]
     [:a {:href (routes/form-url :thread {:thread-id "my-convo-id"})} "go to thread"]]))

(defn main-panel []
  [auth.views/auth-wrapper [content]])

(defmethod panels :profile
  [_]
  [main-panel])

