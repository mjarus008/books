(ns clj-fire.books.views
  (:require [clj-fire.views :refer [panels]]
            [clj-fire.books.events :as books.events]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [clj-fire.utils :as utils]
            react))

(extend-type js/FileList
  ISeqable
  (-seq [l]
    (->> (range (.-length l))
         (map (fn [i] (.item l i)))
         (seq))))

(defn upload []
  (reagent/with-let [*file (reagent/atom nil)
                     upload-ref (react/createRef)]
    (let [file @*file]
      [:section
       [:h2 "Upload books"]
       [:input.upload__nat-in {:ref upload-ref
                               :type "file"
                               :accept ".pdf"
                               :on-change (fn [evt]
                                            (let [files (-> evt .-target .-files)]
                                              (reset! *file (first files))))}]
       [:button.upload__upload-cta {:on-click (fn [_evt]
                                                (-> upload-ref .-current .click))} "Upload books"]
       [:br]
       (when (some? file)
         [:pre (utils/pretty-str
                {:file-name (.-name file)})])
       [:br]
       [:button {:on-click #(re-frame/dispatch [::books.events/upload-book file])
                 :disabled (nil? file)} "Upload Book"]])))

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