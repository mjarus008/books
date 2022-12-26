(ns clj-fire.books.views
  (:require [clj-fire.views :refer [panels]]))

(defn books-panel []
  [:section
   [:h1 "Books"]
   [:dl
    [:dt "Book 1"]
    [:dd "This is a great book"]]])

(defn book-panel [book-id]
  [:section
   [:h1 (str "book with book-id: " book-id)]
   [:div "TODO(Lilitha)"]])

(defmethod panels :books
  [_]
  [books-panel])

(defmethod panels :book
  [{{:keys [book-id]} :route-params}]
  [book-panel book-id])