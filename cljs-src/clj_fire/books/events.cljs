(ns clj-fire.books.events 
  (:require [re-frame.core :as re-frame]
            [clj-fire.log :as log]))


(re-frame/reg-event-fx
 ::booked-uploaded
 (fn [_ [_ book-id]]
   (log/info book-id "book successfully uploaded")))

(re-frame/reg-event-fx
 ::upload-book
 (fn [_ [_ file-blob]]
   {:fx [[:fb/upload-file 
          [file-blob
           {:path [:books "book1.pdf"]
            :on-success [::booked-uploaded "book1"]}]]]}))