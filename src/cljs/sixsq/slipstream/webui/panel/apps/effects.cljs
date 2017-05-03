(ns sixsq.slipstream.webui.panel.apps.effects
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<!]]
    [re-frame.core :refer [reg-fx dispatch]]
    [sixsq.slipstream.client.api.modules :as modules]))

;; usage: (dispatch [:modules-search client])
;; queries the given resource
(reg-fx
  :modules/search
  (fn [[client url]]
    (go
      (let [results (<! (modules/get-children client url))]
        (dispatch [:set-modules-data results])))))
