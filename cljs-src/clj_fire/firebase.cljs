(ns clj-fire.firebase
  (:require ["firebase/auth" :as auth]
            ["firebase/compat/app$default" :as firebase]
            ["firebase/database" :as database]
            ["firebase/storage" :as storage]
            [clj-fire.log :as log]
            [clojure.string :as str]
            firebaseui
            [promesa.core :as p]
            [re-frame.core :as re-frame])
  (:refer-clojure :exclude [assoc-in get-in update-in]))

(def ^:private fire-config #js {:apiKey "AIzaSyD2enijFZ1ovNxt8PAO_0GxKcUoClujrR4",
                                :authDomain "moloweni.firebaseapp.com",
                                :projectId "moloweni",
                                :messagingSenderId "811773534703",
                                :appId "1:811773534703:web:23dfb3d8a6e7973f70d61a",
                                :measurementId "G-CZD23Y8S7S"
                                :databaseURL "https://moloweni-default-rtdb.firebaseio.com"
                                :storageBucket "gs://moloweni.appspot.com/"})

(def fb-login-options
  {:signInOptions
   [{:provider firebase.auth.EmailAuthProvider.PROVIDER_ID
     :requireDisplayName false}
    firebase.auth.GoogleAuthProvider.PROVIDER_ID]
   :callbacks {:signInSuccessWithAuthResult
               (fn [auth-result _redirect-url]
                 (log/info "Sign in was successful.")
                 (log/info "auth result: " auth-result)
                 true)
               :uiShown
               (fn []
                 (log/info "UI shown."))}
   :signInSuccessUrl "http://localhost:9500/profile"})

(defn- initialise-app [& _]
  (.initializeApp firebase fire-config))

(defonce app (initialise-app))

(defonce auth (auth/getAuth app))

(defonce ui (firebaseui.auth.AuthUI. (firebase.auth)))

(defonce dbase (database/getDatabase app))

(defn- path->path-str [path]
  (->> path
       (map name)
       (str/join "/")))

(defn assoc-in [path new-value]
  (let [str-path (path->path-str path)]
    (database/set
     (database/ref dbase str-path)
     (clj->js new-value))))

(comment
  (assoc-in [:foo]  {:msg "Molweni wolrd!"
                     :something "hey"
                     :nothing nil})
  (assoc-in [:foo :age] 26)

  (assoc-in [:users "hfyg6OMuJoNoYDcjeX7J6QHH9ks1"] {:age 27
                                                     :subscription :free
                                                     :books "book-id"}))

(defn get-in [{:keys [path on-success on-failiure]}]
  (let [str-path (path->path-str path)
        db-ref (database/ref dbase)]
    (-> (database/get
         (database/child db-ref str-path))
        (p/then (fn [snapshot]
                  (when (.exists snapshot)
                    (let [value (-> (.val snapshot)
                                    (js->clj :keywordize-keys true))]
                      (if (vector? on-success)
                        (re-frame/dispatch
                         (conj on-success value))
                        (on-success value))))))
        (p/catch (fn [error]
                   (log/error "There was an error reading the data at " path ". error: " error)
                   (if (vector? on-failiure)
                     (re-frame/dispatch
                      (conj on-failiure error))
                     (on-failiure error)))))))

(comment
  ;; Can't block in Clojurescript, so need to use callbacks
  (get-in {:path [:foo]
           :on-success [:log/info]})

  (get-in {:path [:foo :age]
           :on-success prn}))

(defn update-in [path f & args]
  (get-in {:path path
           :on-success
           (fn [v] (assoc-in path (apply f v args)))}))

(comment
  (update-in [:foo :age] inc)

  ;; my uid (Google login)

  (update-in [:users "hfyg6OMuJoNoYDcjeX7J6QHH9ks1" :age] inc))

(defn subscribe [{:keys [path on-value]}]
  (let [path-str (path->path-str path)
        ref (database/ref dbase path-str)]
    (database/onValue ref (fn [snapshot]
                            (let [value (when (.exists snapshot)
                                          (-> (.val snapshot)
                                              (js->clj :keywordize-keys true)))]
                              (re-frame/dispatch (conj on-value value)))))))

(defn new-message [sender uid conversation-id message]
  (let [path [:messages conversation-id]
        ref (database/ref dbase (path->path-str path))
        message-ref (database/push ref)]
    (database/set message-ref
                  #js {:content message
                       :sender sender
                       :sender_uid uid})))

(re-frame/reg-fx
 :fb/subscribe-thread
 (fn [{:keys [on-delete on-value]
       ?thread-id :thread-id}]
   (when-some [thread-id ?thread-id]
     (let [thread-ref (database/ref dbase (str "messages/" thread-id))]
       (when (some? on-value)
         (database/onChildAdded thread-ref (fn [message-snapshot]
                                             (let [message (.val message-snapshot)
                                                   message-key (.-key message-snapshot)]
                                               (re-frame/dispatch (conj on-value
                                                                        (-> message
                                                                            (js->clj :keywordize-keys true)
                                                                            (assoc :key message-key))))))))
       (when (some? on-delete)
         (database/onChildRemoved thread-ref (fn [message-snapshot]
                                               (let [message-key (.-key message-snapshot)]
                                                 (re-frame/dispatch (conj on-delete message-key))))))))))

(re-frame/reg-fx
 :fb/unsubscribe-to-thread
 (fn [{?thread-id :thread-id}]
   (when-some [thread-id ?thread-id]
     (let [thread-ref (database/ref dbase (str "messages/" thread-id))]
       (database/off thread-ref)))))

(comment
  (let [st (storage/getStorage)
        reference (storage/ref st "books/book1.pdf")]))

(defn on-auth-state-changed [evt]
  (auth/onAuthStateChanged auth (fn [user] (-> (conj evt user)
                                               (re-frame/dispatch)))))

(defn sign-out [{:keys [on-success on-failiure]}]
  (-> (auth/signOut auth)
      (p/then #(re-frame/dispatch on-success))
      (p/catch #(re-frame/dispatch on-failiure))))

(defn init-login-panel! [{:keys [id] :as opts}]
  (let [pointer (str "#" id)]
    (.start ui pointer (clj->js (dissoc opts :id)))))
