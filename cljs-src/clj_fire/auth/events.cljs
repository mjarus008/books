(ns clj-fire.auth.events
  (:require [clj-fire.firebase :as fb]
            [re-frame.core :as re-frame]))

(defn user-profile [user]
  (some-> user
          (.toJSON)
          (js->clj :keywordize-keys true)))

(re-frame/reg-event-db
 ::set-user
 (fn [db [_ user]]
   (assoc db :fb-user (user-profile user))))

;; Firebase login panel

(re-frame/reg-fx
 :fb/init-login-panel
 (fn [opts]
   (fb/init-login-panel!
    (merge fb/fb-login-options opts))))

(re-frame/reg-event-fx
 ::init-fb-login-panel
 (fn [_ [_ opts]]
   {:fb/init-login-panel opts}))