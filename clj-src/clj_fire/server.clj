(ns clj-fire.server
  (:require [ring.util.response :refer [resource-response content-type not-found content-type]]
            #_[bidi.ring :as br]
            [reitit.ring :as ring]))

(defn- default-handler [_]
  (some-> (resource-response "index.html" {:root "public"})
          (content-type "text/html; charset=utf-8")))

#_(def ^:private routes
    ["/" {"public" identity
          true default-handler}])

(def ^:private file-not-found
  (constantly
   (-> "Sorry, the file you were looking for could not be found."
       (not-found)
       (content-type "text/plan"))))

(def handler
  (ring/ring-handler
   (ring/router
    ["/resources/public/*"
     (ring/create-resource-handler {:root "public"
                                    :not-found-handler file-not-found})])
   (ring/routes
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler
     {:not-found default-handler}))))

(comment
  (handler {:uri "/resources/public/" :request-method :get}))