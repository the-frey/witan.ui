(ns witan.ui.schema
  (:require [schema.core :as s]))

(defn uuid?
  [s]
  (and (string? s)
       (re-find #"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$" s)))

(def GroupSchema
  {:kixi.group/name   s/Str
   :kixi.group/type   s/Keyword
   :kixi.group/id     uuid?
   :kixi.group/emails [s/Str]})

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

;; app state schema
(def AppStateSchema
  {:app/side {:side/upper [[s/Keyword]]
              :side/lower [[s/Keyword]]}
   :app/login {:login/token (s/maybe s/Str)
               :login/message (s/maybe s/Str)}
   :app/user {:user/name (s/maybe s/Str)
              :user/id (s/maybe s/Str)
              (s/optional-key :user/group-search-results) [GroupSchema]}
   :app/route {:route/path (s/maybe s/Keyword)
               :route/params (s/maybe s/Any)
               :route/query (s/maybe {s/Keyword s/Any})}
   :app/workspace  {:workspace/temp-variables {s/Str s/Str}
                    :workspace/running? s/Bool
                    :workspace/pending? s/Bool
                    (s/optional-key :workspace/current) s/Any
                    (s/optional-key :workspace/current-viz) {:result/location s/Str}
                    (s/optional-key :workspace/model-list) [{s/Keyword s/Any}]}
   :app/workspace-dash {:wd/workspaces (s/maybe [s/Any])}
   :app/data-dash (s/maybe s/Any)
   :app/rts-dash (s/maybe s/Any)
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
   :app/datastore {(s/optional-key :schema/search-results) [SchemaSchema]}})
