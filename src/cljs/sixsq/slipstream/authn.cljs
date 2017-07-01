(ns sixsq.slipstream.authn
  (:require
    [clojure.string :as str]
    [reagent.core :as reagent]
    [re-frame.core :refer [dispatch dispatch-sync]]
    [taoensso.timbre :as log]

    [sixsq.slipstream.webui.routes]
    [sixsq.slipstream.webui.utils :as utils]

    ;; must include these to ensure that they are not elided
    [sixsq.slipstream.webui.panel.authn.events]
    [sixsq.slipstream.authn.main.events]

    [sixsq.slipstream.webui.main.events]
    [sixsq.slipstream.webui.main.subs]
    [sixsq.slipstream.webui.main.views]

    [sixsq.slipstream.authn.main.events]
    [sixsq.slipstream.authn.main.views]

    [sixsq.slipstream.webui.widget.history.events]))

;;
;; This option is not compatible with other platforms, notably nodejs.
;; Use instead the logging calls to provide console output.
;;
(enable-console-print!)

;;
;; debugging log level
;;
;; Set the value like this:
;;
;; {:compiler-options {:closure-defines {'sixsq.slipstream.authn/LOGGING_LEVEL "info"}}
;;
(goog-define LOGGING_LEVEL "info")
(log/set-level! (keyword LOGGING_LEVEL))

;;
;; determine the host url
;;
;; Set a fixed SlipStream endpoint (useful for development) with:
;;
;; {:compiler-options {:closure-defines {'sixsq.slipstream.webui/HOST_URL "https://nuv.la"}}
;;
;; NOTE: When using an endpoint other than the one serving the javascript code
;; you MUST turn off the XSS protections of the browser.
;;
(goog-define HOST_URL "")
(def SLIPSTREAM_URL (delay (if-not (str/blank? HOST_URL) HOST_URL (utils/host-url))))

;;
;; determine the web application prefix
;;
;; The default is to concatenate '/webui' to the end of the SLIPSTREAM_URL.
;; If the application is mounted elsewhere, you can change the default with:
;;
;; {:compiler-options {:closure-defines {'sixsq.slipstream.webui/CONTEXT ""}}
;;
(goog-define CONTEXT "/authn")
(def PATH_PREFIX (delay (str (utils/host-url) CONTEXT)))

;;
;; hook to initialize the web application
;;
(defn ^:export init
  []
  (log/info "using slipstream server:" @SLIPSTREAM_URL)
  (log/info "using path prefix:" @PATH_PREFIX)
  (dispatch-sync [:evt.webui.main/initialize-db])
  (dispatch-sync [:evt.webui.main/initialize-client @SLIPSTREAM_URL])
  (dispatch-sync [:fetch-cloud-entry-point])
  (dispatch-sync [:evt.webui.history/initialize @PATH_PREFIX])
  (dispatch-sync [:evt.webui.authn/initialize])
  (dispatch-sync [:evt.webui.authn/set-redirect-uri "/authn/login"])
  (dispatch [:evt.webui.authn/check-session])
  (when-let [container-element (.getElementById js/document "webui-container")]
    (reagent/render [sixsq.slipstream.authn.main.views/app] container-element)))