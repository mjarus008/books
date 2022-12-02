(ns clj-fire.auth.events 
  (:require [re-frame.core :as re-frame]))

(defn user-profile [user]
  (some-> user
          (.toJSON)
          (js->clj :keywordize-keys true)))

(re-frame/reg-event-db
 ::set-user
 (fn [db [_ user]]
   (assoc db :fb-user (user-profile user))))
