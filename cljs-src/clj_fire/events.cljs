(ns clj-fire.events
  (:require [re-frame.core :as re-frame]))

(defn navigate-to [handler params]
  (let [route (->> params
                   seq
                   flatten
                   (cons handler))]
    {:navigate route}))

(re-frame/reg-event-fx
 ::navigate-to
 (fn [_ [_ handler params]]
   (navigate-to handler params)))

(comment
  (let [handler :stats
        params {:id "x" :section "y"}]
    (->> params
         seq
         flatten
         (cons handler))))

(re-frame/reg-event-db
 ::set-route
 (fn [db [_ params]]
   (assoc db :routes params)))