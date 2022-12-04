(ns clj-fire.profile.views
  (:require [clj-fire.auth.subs :as auth.subs]
            [clj-fire.auth.views :as auth.views]
            [clj-fire.utils :as utils]
            [clj-fire.views :refer [panels]]
            [clojure.pprint :as pretty]
            [re-frame.core :as re-frame]))

(defn content []
  (let [fb-user @(re-frame/subscribe [::auth.subs/fb-user])]
    [:section
     [:h1 "Profile"]
     [:pre.code-block (utils/pretty-str (select-keys fb-user [:display-name :providerData]))]]))

(defn main-panel []
  [auth.views/auth-wrapper [content]])

(defmethod panels :profile
  [_]
  [main-panel])

