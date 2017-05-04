(ns sixsq.slipstream.webui.widget.history.utils
  (:require
    [goog.events :as events]
    [clojure.string :as str]
    [re-frame.core :refer [dispatch dispatch-sync]]
    [secretary.core :as secretary]
    [sixsq.slipstream.webui.utils :as utils])
  (:import
    [goog History]
    [goog.history Html5History EventType]
    [goog.history.Html5History TokenTransformer]))

(defn get-token
  "Creates the history token from the given location."
  [path-prefix location]
  (let [url (str (.-protocol location) "//" (.-host location) (.-pathname location) (.-search location))]
    (str/replace-first url path-prefix "")))

(defn create-transformer
  "Saves and restores the URL based on the token provided to the
   Html5History object.  The methods of this object are needed
   when not using fragment based routing. The tokens are simply
   the remaining parts of the URL after the path prefix."
  []
  (let [transformer (TokenTransformer.)]
    (set! (.. transformer -retrieveToken)
          (fn [path-prefix location]
            (get-token path-prefix location)))
    (set! (.. transformer -createUrl)
          (fn [token path-prefix location]
            (str path-prefix token)))
    transformer))

(def history
  (doto (Html5History. js/window (create-transformer))
    (events/listen EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
    (.setUseFragment false)
    (.setEnabled false)))

(defn initialize
  "Sets the path-prefix to use for the history object and enables
   the object to start sending events on token changes."
  [path-prefix]
  (doto history
    (.setPathPrefix path-prefix)
    (.setEnabled true)))

(defn start
  "Sets the starting point for the history. No history event will be
   generated when setting the first value, so this explicitly dispatches
   the value to the URL routing."
  [path-prefix]
  (let [token (get-token path-prefix (.-location js/window))]
    (.log js/console "start token: " token)
    (.setToken history token)
    (secretary/dispatch! token)))

(defn navigate
  "Navigates to the given internal URL (relative to the application root)
   by pushing the corresponding token onto the HTML5 history object.
   Actual rerendering will be triggered by the event generated by the
   history object itself."
  [url]
  (.setToken history (str "/" url)))
