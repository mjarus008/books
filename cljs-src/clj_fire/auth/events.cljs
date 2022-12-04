(ns clj-fire.auth.events
  (:require [clj-fire.firebase :as fb]
            [re-frame.core :as re-frame]
            [clj-fire.utils :as utils]))

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

(re-frame/reg-fx
 :fb/logout
 (fn [args]
   (fb/sign-out args)))

(re-frame/reg-event-db
 ::clear-fb-user
 (fn [db _]
   (print "logged out")
   (dissoc db :fb-user)))

(re-frame/reg-event-fx
 ::error
 (fn [_ {:keys [message]}]
   (utils/pretty-str message)))

(re-frame/reg-event-fx
 ::logout
 (fn [{:keys [db]} _]
   {:fb/logout
    {:on-success [::clear-fb-user]
     :on-failure [::error {:msg "logout failed."}]}}))
