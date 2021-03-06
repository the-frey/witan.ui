(ns witan.ui.schema
  (:require [schema.core :as s]))

(def uuid?
  (s/pred (fn [s]
            (and (string? s)
                 (re-find #"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$" s)))))

(def GroupSchema
  {:kixi.group/name   s/Str
   :kixi.group/type   s/Str
   :kixi.group/id     uuid?
   (s/optional-key :kixi.group/created) s/Str
   (s/optional-key :kixi.group/created-by) s/Str})

(def UserSchema
  {:kixi.user/name   (s/maybe s/Str)
   :kixi.user/username (s/maybe s/Str)
   :kixi.user/id     (s/maybe s/Str)
   :kixi.user/groups [(s/maybe s/Str)]
   :kixi.user/self-group (s/maybe s/Str)
   (s/optional-key :user/group-search-filtered) [GroupSchema]})

(def SchemaSchema
  {:schema/name s/Str
   :schema/id   uuid?
   :schema/author GroupSchema})

(def RequestRecipientSchema
  (merge GroupSchema
         {:kixi.data-acquisition.request-to-share.recipient/data-id (s/maybe uuid?)}))

(def RTSSchema
  {:kixi.data-acquisition.request-to-share/request-id uuid?
   :kixi.data-acquisition.request-to-share/requester-id uuid?
   :kixi.data-acquisition.request-to-share/schema SchemaSchema
   :kixi.data-acquisition.request-to-share/recipients [RequestRecipientSchema]
   :kixi.data-acquisition.request-to-share/destinations [GroupSchema]
   :kixi.data-acquisition.request-to-share/message s/Str})

(def FilePropertiesSchema
  {uuid? {(s/optional-key :flags) #{s/Keyword}
          (s/optional-key :update-errors) {s/Keyword s/Str}}})

(def ActivityLogSchema
  {:status (s/enum :completed :failed)
   :message s/Str
   :time s/Any
   :activity s/Keyword})

(def ActivityPendingSchema
  {:activity s/Keyword
   :state s/Any
   :reporters {:failed s/Any
               :completed s/Any}
   :command-id uuid?
   :id uuid?
   :context s/Any})

(def ListDisplayItem
  ;; TODO: partial metadata schema
  s/Any)

(def Search
  {(s/optional-key :query) {s/Keyword s/Any}
   (s/optional-key :size) s/Num
   (s/optional-key :from) s/Num
   (s/optional-key :fields) [(s/conditional coll? [s/Keyword]
                                            :else s/Keyword)]
   (s/optional-key :sort-by) [(s/conditional map? {s/Keyword (s/conditional map? {s/Keyword s/Keyword}
                                                                            :else s/Keyword)}
                                             :else s/Keyword)]})

(def Paging
  (s/maybe {:total s/Num
            :count s/Num
            :index s/Num}))

(def SearchResults
  {Search {:search Search
           :items [ListDisplayItem]
           :paging Paging}})

(def BundleAddData
  {:kixi.collect.request/receiving-groups #{uuid?}
   :kixi.datastore.metadatastore/id uuid?
   :kixi.collect.campaign/id uuid?
   :kixi.collect.request/id uuid?})

;; app state schema
(def AppStateSchema
  {:app/login {:login/pending? s/Bool
               :login/token (s/maybe {:auth-token s/Str
                                      :refresh-token s/Str})
               :login/message (s/maybe s/Str)
               :login/auth-expiry s/Num
               :login/refresh-expiry s/Num
               :login/reset-complete? s/Bool}
   :app/user UserSchema
   :app/route {:route/path (s/maybe s/Keyword)
               :route/params (s/maybe s/Any)
               :route/address s/Str
               :route/query (s/maybe {s/Keyword s/Any})}
   :app/workspace  {:workspace/temp-variables {s/Str s/Str}
                    :workspace/running? s/Bool
                    :workspace/pending? s/Bool
                    (s/optional-key :workspace/current) s/Any
                    (s/optional-key :workspace/current-viz) {:result/location s/Str}
                    (s/optional-key :workspace/model-list) [{s/Keyword s/Any}]}
   :app/workspace-dash {:wd/workspaces (s/maybe [s/Any])}
   :app/search {:ks/dashboard {(s/optional-key :ks/current-search) Search
                               :ks/search->result SearchResults}
                :ks/datapack-files {(s/optional-key :ks/current-search) Search
                                    :ks/search->result SearchResults}
                :ks/datapack-files-expand-in-progress s/Bool}

   :app/create-data {:cd/pending? s/Bool
                     (s/optional-key :cd/error) s/Keyword
                     (s/optional-key :cd/pending-data) s/Any
                     (s/optional-key :cd/pending-message) {:message s/Keyword
                                                           :progress s/Num}}
   :app/rts-dash {s/Keyword s/Any}
   :app/workspace-results [{:result/location s/Str
                            :result/key s/Keyword
                            :result/downloading? s/Bool
                            (s/optional-key :result/content) s/Any}]
   :app/panic-message (s/maybe s/Str)
   :app/create-rts {(s/optional-key :crts/message) s/Str
                    (s/optional-key :crts/pending-payload) {uuid? {s/Keyword s/Any}}
                    :crts/pending? s/Bool}
   :app/request-to-share {:rts/requests {uuid? RTSSchema}
                          :rts/current (s/maybe uuid?)
                          :rts/pending? s/Bool}
   :app/datastore {:ds/current (s/maybe uuid?)
                   :ds/pending? s/Bool
                   :ds/confirming-delete? s/Bool
                   :ds/file-metadata {uuid? s/Any}
                   :ds/file-metadata-editing s/Any ;; TODO: metadata schema
                   :ds/file-metadata-editing-command s/Any ;; TODO: metadata schema + updates
                   :ds/file-properties FilePropertiesSchema
                   :ds/data-view-subview-idx s/Num
                   (s/optional-key :ds/error) s/Keyword
                   (s/optional-key :ds/query-tries) s/Num}
   :app/create-datapack {:cdp/pending? s/Bool
                         (s/optional-key :cdp/pending-datapack) s/Any
                         (s/optional-key :cdp/error) s/Keyword}
   :app/collect {:collect/pending? s/Bool
                 :collect/failure-message (s/maybe s/Str)
                 :collect/success-message (s/maybe s/Str)}
   :app/bundle-add {:ba/pending? s/Bool
                    :ba/failure-message (s/maybe s/Str)
                    :ba/success-message (s/maybe s/Str)
                    :ba/data (s/maybe BundleAddData)}
   :app/activities {:activities/log [ActivityLogSchema]
                    :activities/pending {uuid? ActivityPendingSchema}}
   :app/group-cache {uuid? GroupSchema}})
