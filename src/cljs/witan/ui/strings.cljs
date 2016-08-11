(ns witan.ui.strings
  (:require [clojure.string])
  (:require-macros
   [cljs-log.core :as log]))

(def strings
  {:string/name                           "Name"
   :string/forecast-in-progress-text      "We're currently building a new version of this projection for you. You can download your data from the 'Output' section when this message disappears."
   :string/sign-in-failure                "There was a problem with your details. Please try again."
   :string/upload-new-data                "Upload new data"
   :string/forgotten-instruction          "Please enter your email address. If it matches one in our system we'll send you reset instructions."
   :string/browser-upload-select-existing "Please select the existing data item that you want to update"
   :string/use-data-item                  "Use"
   :string/pw-input-brief                 "Choose your input data"
   :string/confirm-email                  "Confirm email"
   :string/input-intro                    "To generate your projection, choose which data should go into your model from the options below. You can upload your own data, or choose from any datasets already in the system."
   :string/witan                          "Witan"
   :string/view-edit-forecast             "View/Edit this projection"
   :string/yesterday                      "Yesterday"
   :string/model                          "Model"
   :string/reset-submitted                "Thanks. Your password reset request has been received."
   :string/back                           "Back"
   :string/revert-forecast                "Revert changes"
   :string/please-wait                    "Please Wait..."
   :string/error                          "Error"
   :string/choose-file                    "Choose file"
   :string/public-only                    "Public only"
   :string/upload-data-public-warning     "As this is a public projection, any data you upload will also become public."
   :string/optional                       "(optional)"
   :string/no-model-properties            "This model has no properties to configure"
   :string/forecast-name                  "Name"
   :string/changed                        "Changed"
   :string/model-publisher                "Publisher"
   :string/processing-account             "Creating account..."
   :string/filter                         "Filter"
   :string/forgotten-password             "Forgotten Password"
   :string/browser-no-file-selected       "No file selected..."
   :string/witan-title                    "Witan for London"
   :string/help                           "Help"
   :string/no-input-specified             "No data input specified."
   :string/download                       "Download"
   :string/forecast-changes-text          "Okay, we've recorded your changes. When you're ready, hit 'Run this projection' to generate your new outputs for download."
   :string/sign-up-failure                "There was a problem signing-up with the provided details. Please check your invite token and try again."
   :string/output-extra-info              "The high level of precision of the figures in this data should not be mistaken for a declaration of accuracy. Users should be aware that significant levels of uncertainty exist in all of the outputs of these models. When publishing data for public use, it is recommended that numbers should be rounded and age groups should be aggregated."
   :string/forecast-lastmodified          "Last Modified"
   :string/please-select-data-input       "Please select a data input for this category."
   :string/create-new-forecast            "Run this projection"
   :string/new-forecast-desc-placeholder  "What should this projection be used for? Who should use it?"
   :string/browser-upload-option-existing "This is an updated version of an existing data item"
   :string/pw-output-brief                "Download your data"
   :string/input                          "Input"
   :string/output-intro                   "Once the model's calculations have been completed and your output data is available, you will be able to download it here. "
   :string/properties                     "Properties"
   :string/create-account-header          "Need an account?"
   :string/forecast-version               "Version"
   :string/view                           "View"
   :string/api-failure                    "Sorry, we're having a problem with the service. Please try again. If the problem persists, please contact us at witan@mastodonc.com"
   :string/password-no-match              "The provided passwords do not match."
   :string/forecast-type                  "Type"
   :string/refresh-now                    "Refresh"
   :string/model-intro                    "Your projection feeds your chosen input data into the model to below to generate your data. Download it at the next step."
   :string/thanks                         "Thanks"
   :string/forecast-public?               "Public?"
   :string/upload                         "Upload"
   :string/downloads                      "Downloads"
   :string/new-forecast                   "Create New Projection"
   :string/create-account                 "Create Account"
   :string/email                          "Email"
   :string/missing-required-inputs        "Some inputs are still missing data. Before you can save this projection, please select or upload appropriate data for the corresponding inputs."
   :string/email-no-match                 "The provided email addresses do not match."
   :string/creating-forecast              "Please wait whilst we update this projection..."
   :string/new-forecast-name-placeholder  "Enter a name for this projection"
   :string/search                         "Search"
   :string/sign-up-token                  "Invite code"
   :string/forecast-owner                 "Owner"
   :string/forecast-public?-explain       "Tick this box to make the Projection visible to everyone"
   :string/no-description-provided        "(No description has been provided.)"
   :string/upload-success                 "Upload successful"
   :string/in-progress                    "In-Progress"
   :string/forgotten-question             "forgotten your password?"
   :string/witan-tagline                  "Make more sense of your city"
   :string/browser-upload-option-new      "This is a brand new data item"
   :string/browser-upload-select-new      "Please enter a name for the new data item"
   :string/forecast-desc                  "Description"
   :string/sign-in                        "Sign In"
   :string/new                            "New"
   :string/create                         "Create"
   :string/in-progress-no-downloads       "Sorry, we're still running the projection to generate your data. This can take several minutes so please check again shortly."
   :string/password-under-length          "The provided password is too short. Please make passwords at least 8 characters long."
   :string/upload-data-public-explain     "Tick this box to make the data visible to everyone (public)"
   :string/password                       "Password"
   :string/created                        "Created"
   :string/confirm-password               "Confirm password"
   :string/browser-upload-error           "An error occurred whilst trying to upload the file. Please try again and if this problem persists, contact us."
   :string/today                          "Today"
   :string/reset-password                 "Reset Password"
   :string/logout                         "Log Out"
   :string/data-items                     "data items"
   :string/default-brackets               "(default)"
   :string/signing-in                     "Signing in..."
   :string/settings                       "Settings"
   :string/forecast                       "Projection"
   :string/create-account-info            "If you have an invite code you can create your account below:"
   :string/public                         "Public"
   :string/pw-model-brief                 "See how the model works"
   :string/output                         "Output"
   :string/browser-choose-data            "Choose from existing data or upload your own new data"
   :string/new-version-no-downloads       "Once you have run your first projection, your data will be available here. Start this at the 'Inputs' section"
   :string/about-model                    "About this model"
   :string/browser-upload-completes       "Please wait whilst your upload completes..."
   :string/superseded                     "Superseded"
   :string/tooltip-workspace              "Browse your workspaces"
   :string/tooltip-data                   "Browse your data sets"
   :string/tooltip-logout                 "Log out from your account"
   :string/tooltip-help                   "Get help"
   :string/workspace-dash-title           "Workspaces"
   :string/data-dash-title                "Data Sets"
   :string/workspace-dash-filter          "Filter your workspaces"
   :string/data-dash-filter               "Filter your data sets"
   :string/workspace-data-view            "Data"
   :string/workspace-config-view          "Configuration"
   :string/workspace-history-view         "History"
   :string/create-workspace-title         "Create a new workspace"
   :string/create-workspace-subtitle      "A workspace contains models and visualisations, configured how you want them"
   :string/create-workspace-name          "Workspace name"
   :string/create-workspace-name-ph       "Enter a name for this workspace"
   :string/create-workspace-desc          "Description"
   :string/create-workspace-desc-ph       "What will this workspace be used for?"
   :string/create-workspace-error         "An error occurred whilst trying to create this workspace. Please try again."
   :string/workspace-404-error            "Unable to find a workspace at this address."
   :string/workspace-empty                "This workspace is empty!"
   :string/workspace-add-model            "Add Model"
   :string/workspace-select-a-model       "Please start by selecting a model:"
   :string/data-empty-catalog             "No data inputs required."
   :string/config-empty-catalog           "No configuration required."
   :string/run                            "Run"
   :string/running                        "Running"})

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
