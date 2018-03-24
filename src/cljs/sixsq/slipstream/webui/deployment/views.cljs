(ns sixsq.slipstream.webui.deployment.views
  (:require
    [clojure.string :as str]
    [re-frame.core :refer [subscribe dispatch]]
    [reagent.core :as reagent]

    [sixsq.slipstream.webui.panel :as panel]

    [sixsq.slipstream.webui.deployment.events :as deployment-events]
    [sixsq.slipstream.webui.deployment.subs :as deployment-subs]
    [sixsq.slipstream.webui.i18n.subs :as i18n-subs]

    [sixsq.slipstream.webui.utils.component :as ui-utils]

    [sixsq.slipstream.webui.history.events :as history-events]

    [sixsq.slipstream.webui.utils.semantic-ui :as ui]

    [sixsq.slipstream.webui.main.subs :as main-subs]

    [sixsq.slipstream.webui.deployment-detail.events :as deployment-detail-events]
    [sixsq.slipstream.webui.deployment-detail.views :as deployment-detail-views]
    [sixsq.slipstream.webui.utils.collapsible-card :as cc]))


(defn bool->int [bool]
  (if bool 1 0))

(defn runs-control []
  (let [tr (subscribe [::i18n-subs/tr])
        query-params (subscribe [::deployment-subs/query-params])
        offset-value (reagent/atom (:offset @query-params))
        limit-value (reagent/atom (:limit @query-params))
        cloud-value (reagent/atom (:cloud @query-params))
        activeOnly-value (reagent/atom (-> @query-params :activeOnly js/parseInt zero? not))]
    (fn []
      (let [{:keys [offset limit cloud activeOnly]} @query-params]
        (reset! offset-value offset)
        (reset! limit-value limit)
        (reset! cloud-value cloud)
        (reset! activeOnly-value (-> activeOnly js/parseInt zero? not)))
      [ui/Form
       [ui/FormGroup
        [ui/FormField
         [ui/Input {:type      "number"
                    :min       0
                    :label     (@tr [:offset])
                    :value     @offset-value
                    :on-change (ui-utils/callback :value
                                                  (fn [v]
                                                    (reset! offset-value v)
                                                    (dispatch [::deployment-events/set-query-params {:offset v}])))}]]

        [ui/FormField
         [ui/Input {:type      "number"
                    :min       0
                    :label     (@tr [:limit])
                    :value     @limit-value
                    :on-change (ui-utils/callback :value
                                                  (fn [v]
                                                    (reset! limit-value v)
                                                    (dispatch [::deployment-events/set-query-params {:limit v}])))}]]]

       [ui/FormGroup
        [ui/FormField
         [ui/Input {:type      "text"
                    :label     (@tr [:cloud])
                    :value     @cloud-value
                    :on-change (ui-utils/callback :value
                                                  (fn [v]
                                                    (reset! cloud-value v)
                                                    (dispatch [::deployment-events/set-query-params {:cloud v}])))}]]
        [ui/FormField
         [ui/Checkbox {:checked   @activeOnly-value
                       :slider    true
                       :fitted    true
                       :label     (@tr [:active?])
                       :on-change (ui-utils/callback :checked
                                                     (fn [v]
                                                       (let [flag (bool->int v)]
                                                         (reset! activeOnly-value flag)
                                                         (dispatch [::deployment-events/set-query-params {:activeOnly flag}]))))}]]]])))


(defn menu-bar
  []
  (let [tr (subscribe [::i18n-subs/tr])
        loading? (subscribe [::deployment-subs/loading?])
        filter-visible? (subscribe [::deployment-subs/filter-visible?])]
    (fn []
      [:div
       [ui/Menu {:attached   (if @filter-visible? "top" false)
                 :borderless true}
        [ui/MenuItem {:name     "refresh"
                      :on-click #(dispatch [::deployment-events/get-deployments])}
         [ui/Icon {:name    "refresh"
                   :loading @loading?}]
         (@tr [:refresh])]
        [ui/MenuMenu {:position "right"}
         [ui/MenuItem {:name     "filter"
                       :on-click #(dispatch [::deployment-events/toggle-filter])}
          [ui/IconGroup
           [ui/Icon {:name "filter"}]
           [ui/Icon {:name   (if @filter-visible? "chevron down" "chevron right")
                     :corner true}]]
          (str "\u00a0" (@tr [:filter]))]]]

       (when @filter-visible?
         [ui/Segment {:attached "bottom"}
          [runs-control]])])))


(defn service-url
  [url status]
  [:span
   (if (and (= status "Ready") (not (str/blank? url)))
     [:a
      {:href   url
       :target "_blank"}
      [:i {:class (str "zmdi zmdi-hc-fw-rc zmdi-mail-reply")}]]
     "\u00a0")])


(defn format-module
  [module]
  (let [tag (second (reverse (str/split module #"/")))]
    (fn []
      [:span tag])))


(defn format-uuid
  [uuid]
  (let [tag (.substring uuid 0 8)
        on-click #(dispatch [::history-events/navigate (str "deployment/" uuid)])]
    [:a {:style {:cursor "pointer"} :on-click on-click} tag]))


(defn row-fn [entry]
  [ui/TableRow
   [ui/TableCell [format-uuid (:uuid entry)]]
   [ui/TableCell (:status entry)]
   [ui/TableCell (:activeVm entry)]
   [ui/TableCell [service-url (:serviceUrl entry) (:status entry)]]
   [ui/TableCell [format-module (:moduleResourceUri entry)]]
   [ui/TableCell (:startTime entry)]
   [ui/TableCell (:cloudServiceNames entry)]
   [ui/TableCell (:tags entry)]
   [ui/TableCell (:username entry)]])


(defn vertical-data-table
  [entries]
  (let [tr (subscribe [::i18n-subs/tr])]
    (fn [entries]
      [ui/Table
       {:compact     true
        :single-line true
        :padded      false}
       [ui/TableHeader
        [ui/TableRow
         [ui/TableHeaderCell (@tr [:id])]
         [ui/TableHeaderCell (@tr [:status])]
         [ui/TableHeaderCell (@tr [:vms])]
         [ui/TableHeaderCell (@tr [:url])]
         [ui/TableHeaderCell (@tr [:module])]
         [ui/TableHeaderCell (@tr [:start])]
         [ui/TableHeaderCell (@tr [:cloud])]
         [ui/TableHeaderCell (@tr [:tags])]
         [ui/TableHeaderCell (@tr [:username])]]]
       (vec (concat [ui/TableBody]
                    (map row-fn entries)))])))


(defn runs-display
  []
  (let [tr (subscribe [::i18n-subs/tr])
        loading? (subscribe [::deployment-subs/loading?])
        deployments (subscribe [::deployment-subs/deployments])]
    (fn []
      [:div {:class-name "webui-x-autoscroll"}
       (when-not @loading?
         (when-let [{:keys [runs]} @deployments]
           (let [{:keys [count totalCount]} runs]
             [ui/MenuItem
              [ui/Statistic {:size :mini}
               [ui/StatisticValue (str count "/" totalCount)]
               [ui/StatisticLabel (@tr [:results])]]])))
       (when-not @loading?
         (when-let [{:keys [runs]} @deployments]
           (let [{:keys [item]} runs]
             [vertical-data-table item])))])))


(defn deployments
  []
  (let [tr (subscribe [::i18n-subs/tr])]
    [ui/Container {:fluid true}
     [menu-bar]
     [cc/collapsible-card
      (@tr [:results])
      [runs-display]]]))


(defn deployment-resource
  []
  (let [path (subscribe [::main-subs/nav-path])
        query-params (subscribe [::main-subs/nav-query-params])]
    (fn []
      (let [[_ resource-id] @path]
        (dispatch [::deployment-detail-events/set-runUUID resource-id])
        (when @query-params
          (dispatch [::deployment-events/set-query-params @query-params])))
      (let [n (count @path)
            children (case n
                       1 [[deployments]]
                       2 [[deployment-detail-views/deployment-detail]]
                       [[deployments]])]
        (vec (concat [:div] children))))))


(defmethod panel/render :deployment
  [path]
  [deployment-resource])
