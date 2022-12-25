(ns clj-fire.books.views
  (:require [clj-fire.views :refer [panels]]
            [clj-fire.subs :as subs]
            [re-frame.core :as re-frame]))

(defn main-panel []
  [:section
   [:h1 "Books"]
   [:dl
    [:dt "Book 1"]
    [:dd "This is a great book"]]])

(defmethod panels :book
  [_]
  (let [{{:keys [book-id]} :route-params} @(re-frame/subscribe [::subs/route])]
    [main-panel book-id]))