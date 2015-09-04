(ns witan.ui.controllers.api
  (:require [ajax.core :as ajax]
            [om.core :as om :include-macros true]
            [cognitect.transit :as t]
            [witan.ui.nav :as nav]
            [witan.ui.util :as util]
            [witan.ui.data :refer [get-string]]))

(defn local-endpoint
  [method]
  (str "http://localhost:3000" method))

(defmulti api-request
  (fn [event args cursor] event))

(defmulti api-response
  (fn [event status cursor response] [event status]))

(defn- handle-response
  [status event cursor response]
  (api-response event status cursor (clojure.walk/keywordize-keys response)))

(defn POST
  [event cursor method params]
  (ajax/POST (local-endpoint method)
             {:params params
              :handler (partial handle-response :success event cursor)
              :error-handler (partial handle-response :failure event cursor)
              :format :json}))

(defn handler
  [[event args] cursor]
  (api-request event args cursor))

(defmethod api-request
  :api/login
  [event {:keys [email pass]} cursor]
  (POST event cursor "/login" {:username email :password pass}))

(defmethod api-response
  [:api/login :success]
  [event status cursor response]
  (let [token (:token response)]
    (if token
      (do
        (println "Login success.")
        (om/transact! cursor :login-state #(assoc % :token token))
        (om/transact! cursor :login-state #(assoc % :is-logged-in? true))
        (nav/restart-app))
      (do
        (println "Login failed.")
        (om/transact! cursor :login-state #(assoc % :message (get-string :sign-in-failure)))
        (om/transact! cursor :login-state #(assoc % :phase :prompt))))))

(defmethod api-response
  [:api/login :failure]
  [event status cursor response]
  (om/transact! cursor :login-state #(assoc % :message (get-string :api-failure)))
  (om/transact! cursor :login-state #(assoc % :phase :prompt)))