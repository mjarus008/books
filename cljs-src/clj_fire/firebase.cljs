(ns clj-fire.firebase
  (:require ["firebase/app" :as fire]
            ["firebase/compat/app$default" :as firebase]
            ["firebase/auth" :as auth]
            firebaseui
            [promesa.core :as p]
            [re-frame.core :as re-frame]))

(def ^:private fire-config #js {:apiKey "AIzaSyD2enijFZ1ovNxt8PAO_0GxKcUoClujrR4",
                                :authDomain "moloweni.firebaseapp.com",
                                :projectId "moloweni",
                                :storageBucket "moloweni.appspot.com",
                                :messagingSenderId "811773534703",
                                :appId "1:811773534703:web:23dfb3d8a6e7973f70d61a",
                                :measurementId "G-CZD23Y8S7S"})

(def fb-login-options
  {:signInOptions
   [{:provider firebase.auth.EmailAuthProvider.PROVIDER_ID
     :requireDisplayName false}
    firebase.auth.GoogleAuthProvider.PROVIDER_ID]
   :callbacks {:signInSuccessWithAuthResult
               (fn [auth-result _redirect-url]
                 (print "auth result: " auth-result)
                 true)
               :uiShown
               (fn []
                 (print "UI shown."))}
   :signInSuccessUrl "http://localhost:9500/profile"})

(defn- initialise-app [& _]
  (.initializeApp firebase fire-config))

(defonce app (initialise-app))

(defonce auth (auth/getAuth app))

(defonce ui (firebaseui.auth.AuthUI. (firebase.auth)))

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
