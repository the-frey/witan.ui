(ns witan.ui.strings
  (:require [clojure.string])
  (:require-macros
   [cljs-log.core :as log]))

(def strings
  {:witan                          "Witan"
   :witan-tagline                  "Make more sense of your city"
   :witan-title                    "Witan for London"
   :forecast                       "Projection"
   :new-forecast                   "Create New Projection"
   :new-forecast-name-placeholder  "Enter a name for this projection"
   :new-forecast-desc-placeholder  "What should this projection be used for? Who should use it?"
   :filter                         "Filter"
   :search                         "Search"
   :model                          "Model"
   :properties                     "Properties"
   :forecast-name                  "Name"
   :forecast-type                  "Type"
   :forecast-owner                 "Owner"
   :forecast-version               "Version"
   :forecast-lastmodified          "Last Modified"
   :forecast-desc                  "Description"
   :forecast-public?               "Public?"
   :forecast-public?-explain       "Tick this box to make the Projection visible to everyone"
   :model-publisher                "Publisher"
   :optional                       "(optional)"
   :sign-in                        "Sign In"
   :email                          "Email"
   :password                       "Password"
   :forgotten-question             "forgotten your password?"
   :forgotten-password             "Forgotten Password"
   :forgotten-instruction          "Please enter your email address. If it matches one in our system we'll send you reset instructions."
   :reset-submitted                "Thanks. Your password reset request has been received."
   :reset-password                 "Reset Password"
   :back                           "Back"
   :thanks                         "Thanks"
   :signing-in                     "Signing in..."
   :sign-in-failure                "There was a problem with your details. Please try again."
   :api-failure                    "Sorry, we're having a problem with the service. Please try again. If the problem persists, please contact us at witan@mastodonc.com" ;; TODO add link?
   :create                         "Create"
   :created                        "Created"
   :logout                         "Log Out"
   :no-model-properties            "This model has no properties to configure"
   :please-wait                    "Please Wait..."
   :create-new-forecast            "Run this projection"
   :revert-forecast                "Revert changes"
   :in-progress                    "In-Progress"
   :changed                        "Changed"
   :refresh-now                    "Refresh"
   :new                            "New"
   :upload                         "Upload"
   :upload-new-data                "Upload new data"
   :forecast-changes-text          "Okay, we've recorded your changes. When you're ready, hit 'Run this projection' to generate your new outputs for download."
   :forecast-in-progress-text      "We're currently building a new version of this projection for you. You can download your data from the 'Output' section when this message disappears."
   :input                          "Input"
   :input-intro                    "To generate your projection, choose which data should go into your model from the options below. You can upload your own data, or choose from any datasets already in the system."
   :model-intro                    "Your projection feeds your chosen input data into the model to below to generate your data. Download it at the next step."
   :output                         "Output"
   :output-intro                   "Once the model's calculations have been completed and your output data is available, you will be able to download it here."
   :no-input-specified             "No data input specified."
   :please-select-data-input       "Please select a data input for this category."
   :default-brackets               "(default)"
   :browser-choose-data            "Choose from existing data or upload your own new data"
   :browser-upload-completes       "Please wait whilst your upload completes..."
   :browser-no-file-selected       "No file selected..."
   :browser-upload-option-existing "This is an updated version of an existing data item"
   :browser-upload-option-new      "This is a brand new data item"
   :browser-upload-select-existing "Please select the existing data item that you want to update"
   :browser-upload-select-new      "Please enter a name for the new data item"
   :browser-upload-error           "An error occurred whilst trying to upload the file. Please try again and if this problem persists, contact us." ;; TODO wants email
   :downloads                      "Downloads"
   :download                       "Download"
   :new-version-no-downloads       "Once you have run your first projection, your data will be available here. Start this at the 'Inputs' section"
   :in-progress-no-downloads       "Sorry, we're still running the projection to generate your data. This can take several minutes so please check again shortly."
   :data-items                     "data items"
   :upload-success                 "Upload successful"
   :creating-forecast              "Please wait whilst we update this projection..."
   :today                          "Today"
   :yesterday                      "Yesterday"
   :missing-required-inputs        "Some inputs are still missing data. Before you can save this projection, please select or upload appropriate data for the corresponding inputs."
   :pw-input-brief                 "Choose your input data"
   :pw-model-brief                 "See how the model works"
   :pw-output-brief                "Download your data"
   :no-description-provided        "(No description has been provided.)"
   :view-edit-forecast             "View/Edit this projection"
   :superseded                     "Superseded"
   :use-data-item                  "Use"
   :public                         "Public"
   :upload-data-public-explain     "Tick this box to make the data visible to everyone (public)"
   :upload-data-public-warning     "As this is a public projection, any data you upload will also become public."
   :about-model                    "About this model"})



(defn get-string
  ""
  [keywd & add]
  (if (contains? strings keywd)
    (if add
      (str (keywd strings) (first add) (clojure.string/join " " (concat " " (rest add))))
      (keywd strings))
    (do
      (log/severe "Failed to find string " (str keywd))
      "## ERROR ##")))
