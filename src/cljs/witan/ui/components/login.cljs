(ns witan.ui.components.login
  (:require [reagent.core :as r]
            ;;
            [witan.ui.components.icons :as icons]
            [witan.ui.strings :refer [get-string]]
            [witan.ui.route :refer [swap-query-string!]]
            ;;
            [witan.ui.controller :as controller]
            [witan.ui.data :as data]
            [goog.string :as gstr])
  (:require-macros [cljs-log.core :as log]
                   [devcards.core :as dc :refer [defcard]]))

(def password-validation
  {:pattern "(?=.*\\d.*)(?=.*[a-z].*)(?=.*[A-Z].*).{8,}"
   :title "8 characters minimum, at least one number, lowercase letter, and uppercase letter"})

(defn get-form-payload
  []
  {:invite-code (.-value (.querySelector js/document ".sign-up-form #token"))
   :name (.-value (.querySelector js/document ".sign-up-form #name"))
   :usernames [(.-value (.querySelector js/document ".sign-up-form #login-email"))
               (.-value (.querySelector js/document ".sign-up-form #confirm-email"))]
   :passwords [(.-value (.querySelector js/document ".sign-up-form #password"))
               (.-value (.querySelector js/document ".sign-up-form #confirm-password"))]})

(defn populate-branding-strap [branding-config]
  [:div (for [item branding-config]
          (cond (contains? item :branding/strap-img)
                [:img.branding-strap-item
                 {:key (:branding/strap-img item)
                  :src (str "../img/branding/" (:branding/strap-img item))}]
                (contains? item :branding/strap-text)
                [:span.branding-strap-item
                 {:key (:branding/strap-text item)}
                 (:branding/strap-text item)]))])

(defn photo-credit [{:keys [link name licence-link licence-name] :as credit-map}]
  (when (not-empty credit-map)
    [:span#bg-attribution.trans-bg
     "Photo by "
     [:a {:href link :target "_blank" :key "photo-attr1"} name] " - "
     [:a {:href licence-link :target "_blank" :key "photo-attr2"} licence-name]]))

(defmulti login-state-view
  (fn [phase data] phase))

(defmethod
  login-state-view
  :sign-up
  [_ {:keys [set-phase-fn params message]}]
  [:div.sub-page-div
   [:h3 (get-string :string/create-account)]
   (when message
     [:span#error-message (get-string message)])
   [:form {:class "sign-up-form pure-form pure-form-stacked"
           :key "sign-up"
           :on-submit (fn [e]
                        (controller/raise! :user/signup (get-form-payload))
                        (.preventDefault e))}
    [:input {:tab-index 1
             :ref "token"
             :type "text"
             :id "token"
             :value (:ic params)
             :read-only (:ic params)
             :placeholder (get-string :string/sign-up-token)
             :required :required}]
    [:input {:tab-index 2
             :ref "email"
             :type "email"
             :value (:un params)
             :read-only (:un params)
             :id "login-email"
             :placeholder (get-string :string/email)
             :required :required}]
    [:input {:tab-index 3
             :ref "confirm-email"
             :type "email"
             :id "confirm-email"
             :placeholder (get-string :string/confirm-email)
             :required :required}]
    [:input {:tab-index 4
             :ref "name"
             :type "text"
             :id "name"
             :placeholder (get-string :string/full-name)
             :required :required}]
    [:input (merge password-validation
                   {:tab-index 5
                    :ref "password"
                    :type "password"
                    :id "password"
                    :placeholder (get-string :string/password)
                    :required :required})]
    [:input (merge password-validation
                   {:tab-index 6
                    :ref "confirm-password"
                    :type "password"
                    :id "confirm-password"
                    :placeholder (get-string :string/confirm-password)
                    :require :required})]
    [:div [:button {:tab-index 7
                    :type "submit"
                    :class "pure-button pure-button-primary"} (get-string :string/create-account)]
     [:button {:id "back-button"
               :class "pure-button"
               :on-click (fn [e]
                           (set-phase-fn :prompt)
                           (.preventDefault e))} (get-string :string/back)]]]])

(defmethod
  login-state-view
  :reset
  [_ {:keys [set-phase-fn]}]
  [:div.sub-page-div
   [:h3 (get-string :string/forgotten-password)]
   [:p
    [:span {:id "reset-instructions"} (get-string :string/forgotten-instruction)]]
   [:form {:class "pure-form"
           :on-submit (fn [e]
                        (set! (.-innerText (. js/document (getElementById "reset-instructions"))) (get-string :string/reset-submitted))
                        (set! (.-innerText (. js/document (getElementById "reset-button"))) (get-string :string/thanks))
                        (set! (.-disabled (. js/document (getElementById "reset-button"))) true)
                        (set! (.-disabled (. js/document (getElementById "reset-input"))) true)
                        (controller/raise! :user/reset-password (.-value (. js/document (getElementById "reset-input"))))
                        (.preventDefault e))}
    [:input {:tab-index 1
             :ref "reset-email"
             :id "reset-input"
             :type "email"
             :placeholder (get-string :string/email)
             :required :required}]
    [:div
     [:button {:tab-index 2
               :id "reset-button"
               :class "pure-button pure-button-primary"} (get-string :string/reset-password)]
     [:button {:id "back-button"
               :class "pure-button"
               :on-click (fn [e]
                           (set-phase-fn :prompt)
                           (.preventDefault e))} (get-string :string/back)]]]])

(defmethod
  login-state-view
  :reset-complete
  [_ {:keys [set-phase-fn params message pending?]}]
  (let [reset-complete? (data/get-in-app-state :app/login :login/reset-complete?)]
    [:div.sub-page-div
     [:h3 (get-string :string/reset-your-password)]
     (when message
       [:span#error-message (get-string message)])
     [:p
      [:span {:id "reset-instructions"} (get-string :string/reset-your-password-instructions)]]
     [:form {:class "pure-form"
             :disabled reset-complete?
             :on-submit (fn [e]
                          (let [rc (.-value (. js/document (getElementById "reset-password-code")))
                                un (.-value (. js/document (getElementById "reset-password-email")))
                                pass (.-value (. js/document (getElementById "reset-password-new")))
                                pass-c (.-value (. js/document (getElementById "reset-password-new-confirm")))]
                            (controller/raise! :user/reset-password-complete
                                               {:reset-code rc
                                                :username un
                                                :passwords [pass pass-c]}))
                          (.preventDefault e))}
      [:input {:tab-index 1
               :id "reset-password-code"
               :type "email"
               :value (:rc params)
               :read-only (:rc params)
               :disabled (or pending? reset-complete?)
               :placeholder (get-string :string/reset-code)
               :required :required}]
      [:input {:tab-index 2
               :id "reset-password-email"
               :type "email"
               :value (:un params)
               :read-only (:un params)
               :disabled (or pending? reset-complete?)
               :placeholder (get-string :string/email)
               :required :required}]
      [:input (merge password-validation
                     {:tab-index 3
                      :type "password"
                      :id "reset-password-new"
                      :disabled (or pending? reset-complete?)
                      :placeholder (get-string :string/password)
                      :required :required})]
      [:input (merge password-validation
                     {:tab-index 4
                      :type "password"
                      :id "reset-password-new-confirm"
                      :disabled (or pending? reset-complete?)
                      :placeholder (get-string :string/confirm-password)
                      :required :required})]
      (when reset-complete?
        [:p
         [:span (get-string :string/reset-password-completion)]])
      [:div
       [:button {:tab-index 2
                 :id "reset-button"
                 :disabled (or pending? reset-complete?)
                 :class "pure-button pure-button-primary"} (get-string :string/reset-password)]
       [:button {:id "back-button"
                 :class "pure-button"
                 :on-click (fn [e]
                             (swap-query-string! (constantly {}))
                             (set-phase-fn :prompt)
                             (.preventDefault e))} (get-string :string/back)]]]]))
(defmethod
  login-state-view
  :prompt
  [_ {:keys [message set-phase-fn pending?]}]
  [:div
   [:h3 (get-string :string/sign-in)]
   (when message
     [:span#error-message (get-string message)])
   [:form {:class "pure-form pure-form-stacked"
           :key "prompt"
           :on-submit (fn [e]
                        (controller/raise! :user/login {:email (.-value (. js/document (getElementById "login-email")))
                                                        :pass (.-value (. js/document (getElementById "login-password")))})
                        (.preventDefault e))}
    [:input {:tab-index 1
             :auto-complete "username"
             :ref "email"
             :type "email"
             :id "login-email"
             :placeholder (get-string :string/email)
             :required :required}]
    [:input (merge password-validation
                   {:tab-index 2
                    :auto-complete "current-password"
                    :ref "password"
                    :type "password"
                    :id "login-password"
                    :placeholder (get-string :string/password)
                    :require :required
                    })]
    [:button {:tab-index 3
              :disabled pending?
              :type "submit"
              :class "pure-button pure-button-primary"} (get-string :string/sign-in)]
    [:a.forgotten-link
     {:id "forgotten-link"
      :disabled pending?
      :on-click (fn [e]
                  (set-phase-fn :reset)
                  (.preventDefault e))} (str "(" (get-string :string/forgotten-question) ")")]]
   [:h3 (get-string :string/create-account-header)]
   [:p
    [:span.text-white (get-string :string/create-account-info)]]
   [:button.pure-button.pure-button-success
    {:disabled pending?
     :on-click (fn [e]
                 (set-phase-fn :sign-up)
                 (.preventDefault e))} (get-string :string/create-account)]])

(defn root-view
  []
  (fn []
    (let [route (data/get-app-state :app/route)
          start-phase (cond
                        (gstr/startsWith (:route/address route) "/reset") :reset-complete
                        (gstr/startsWith (:route/address route) "/invite") :sign-up
                        :else :prompt)
          phase (r/atom start-phase)
          phase-fn (fn [ph]
                     (controller/raise! :user/reset-message)
                     (reset! phase ph))]
      (r/create-class
       {:reagent-render
        (fn []
          (let [{:keys [login/message login/pending?]} (data/get-app-state :app/login)]
            [:div
             [:div {:key "login-bg"
                    :class (str "login-bg " (get @data/config :branding/login-bg-class))}
              (if (get @data/config :branding/photo-credit)
                (photo-credit (get @data/config :branding/photo-credit))
                (photo-credit {:link "https://www.flickr.com/photos/fico86/"
                               :name "Binayak Dasgupta"
                               :licence-name "CC BY 2.0"
                               :licence-link "https://creativecommons.org/licenses/by/2.0/"}))]
             [:div#content-container
              {:key "login-content"
               :class (get @data/config :branding/login-content-class)}
              [:div#relative-container
               [:div.login-title.trans-bg {:key "login-title"}
                [:h1 {:key "login-title-main"} (or (get @data/config :branding/title) (get-string :string/witan)) ]
                [:h2 {:key "login-title-sub"} (or (get @data/config :branding/subtitle) (get-string :string/witan-tagline))]]
               [:div.witan-login.trans-bg {:key "login-state"}
                (login-state-view @phase {:message message :set-phase-fn phase-fn :pending? pending?
                                          :params (:route/query route)})]]]
             [:div.branding-strap (populate-branding-strap (get @data/config :branding/login-strap))]]))}))))
