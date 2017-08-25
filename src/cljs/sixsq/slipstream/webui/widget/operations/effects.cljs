(ns sixsq.slipstream.webui.widget.operations.effects
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<!]]
    [re-frame.core :refer [reg-fx dispatch]]
    [sixsq.slipstream.client.api.cimi :as cimi]
    [sixsq.slipstream.webui.panel.authn.utils :as au]
    [taoensso.timbre :as log]))

(reg-fx
  :fx.webui.op/add
  (fn [[client resource-type data]]
    (go
      (let [json-data (-> (.parse js/JSON (clj->js data) nil 2)
                          (js->clj :keywordize-keys true))]
        (log/error "DEBUG" "ADD" resource-type data json-data)
        (let [resp (<! (cimi/add client resource-type json-data))]
          (log/error "DEBUG" "RESPONSE" resp)
          (let [{:keys [status message] :as resp} resp
                state (if (= 201 status) "SUCCESS" "FAIL")]
            (dispatch [:message (str state ": " message "\n" resp)])))))))

(reg-fx
  :fx.webui.op/edit
  (fn [[client href data]]
    (log/error "DEBUG" "DELETE" href)
    (go
      (let [{:keys [status message] :as resp} (<! (cimi/edit client href data))
            state (if (= 200 status) "SUCCESS" "FAIL")]
        (dispatch [:message (str state ": editing " href "\n" message)])))))

(reg-fx
  :fx.webui.op/delete
  (fn [[client href]]
    (log/error "DEBUG" "DELETE" href)
    (go
      (let [{:keys [status message] :as resp} (<! (cimi/delete client href))
            state (if (= 200 status) "SUCCESS" "FAIL")]
        (dispatch [:message (str state ": deleting " href "\n" message)])))))


