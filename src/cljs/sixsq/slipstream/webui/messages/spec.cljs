(ns sixsq.slipstream.webui.messages.spec
  (:require-macros [sixsq.slipstream.webui.utils.spec :refer [only-keys]])
  (:require
    [clojure.spec.alpha :as s]))


(s/def :message/header string?)

(s/def :message/content string?)

(s/def :message/type #{:success :info :error})

(s/def :message/timestamp any?)

(s/def ::message (only-keys :req-un [:message/header
                                     :message/content
                                     :message/type
                                     :message/timestamp]))

(s/def ::messages (s/coll-of ::message))

(s/def ::alert-message (s/nilable ::message))

(s/def ::db (s/keys :req [::messages
                          ::alert-message]))

(def defaults {::messages      []
               ::alert-message nil})