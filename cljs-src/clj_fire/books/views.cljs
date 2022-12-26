(ns clj-fire.books.views
  (:require [clj-fire.views :refer [panels]]
            [clj-fire.books.events :as books.events]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [clj-fire.utils :as utils]))

(extend-type js/FileList
  ISeqable
  (-seq [l]
    (->> (range (.-length l))
         (map (fn [i] (.item l i)))
         (seq))))

(defn upload []
  (reagent/with-let [*file (reagent/atom nil)]
    [:section
     [:h2 "Upload books"]
     [:input {:type "file"
              :accept ".pdf"
              :on-change (fn [evt]
                           (let [files (-> evt .-target .-files)]
                             (reset! *file (first files))))}]
     [:br]
     [:pre (utils/pretty-str @*file)]
     [:button {:on-click #(re-frame/dispatch [::books.events/upload-book @*file])
               :disabled (nil? @*file)} "Upload book"]]))

(defn books-panel []
  [:section
   [:h1 "Books"]
   [:dl
    [:dt "Book 1"]
    [:dd "This is a great book"]]
   [upload]])

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