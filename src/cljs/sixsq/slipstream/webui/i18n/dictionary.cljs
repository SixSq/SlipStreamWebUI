(ns sixsq.slipstream.webui.i18n.dictionary
  ;; the moment.js locale must be included for any supported language
  (:require
    [cljsjs.moment.locale.fr]))


(def dictionary
  {:en {:lang                "english"

        :active?             "active only?"
        :add                 "add"
        :aggregation         "aggregation"
        :application         "application"
        :cancel              "cancel"
        :cimi                "cimi"
        :close               "close"
        :cloud               "cloud"
        :columns             "columns"
        :create              "create"
        :dashboard           "dashboard"
        :data                "data"
        :delete              "delete"
        :delete-resource     "delete resource"
        :delete-resource-msg "delete resource %1?"
        :delta-min           "delta [min]"
        :deployment          "deployment"
        :describe            "describe"
        :documentation       "documentation"
        :drop-file           "drop file"
        :edit                "edit"
        :editing             "editing"
        :error               "error"
        :event               "event"
        :events              "events"
        :execute-action      "execute action"
        :execute-action-msg  "execute action %1?"
        :fields              "fields"
        :filter              "filter"
        :first               "first"
        :from                "from"
        :id                  "id"
        :knowledge-base      "knowledge base"
        :last                "last"
        :last-30-days        "last 30 days"
        :last-7-days         "last 7 days"
        :legal               "legal information"
        :less                "less"
        :limit               "limit"
        :loading             "loading"
        :login               "login"
        :login-failed        "login failed"
        :logout              "logout"
        :messages            "messages"
        :metrics             "metrics"
        :module              "module"
        :modules             "modules"
        :more                "more"
        :no-messages         "no messages"
        :offset              "offset"
        :order               "order"
        :parameters          "parameters"
        :profile             "profile"
        :refresh             "refresh"
        :reports             "reports"
        :resource-type       "resource type"
        :results             "results"
        :search              "search"
        :select              "select"
        :select-file         "select file"
        :session             "current session"
        :settings            "settings"
        :signup              "sign up"
        :signup-failed       "sign up failed"
        :start               "start"
        :state               "state"
        :statistics          "statistics"
        :status              "status"
        :summary             "summary"
        :support             "support"
        :tags                "tags"
        :terminate           "terminate"
        :timestamp           "timestamp"
        :to                  "to"
        :today               "today"
        :type                "type"
        :unauthorized        "unauthorized"
        :update              "update"
        :usage               "usage"
        :url                 "URL"
        :username            "username"
        :vms                 "VMs"
        :welcome             "welcome"
        :yesterday           "yesterday"}

   :fr {:lang                "français"

        :add                 "ajouter"
        :active?             "uniquement active ?"
        :aggregation         "aggréger"
        :application         "application"
        :cancel              "annuler"
        :close               "fermer"
        :cloud               "nuage"
        :cimi                "cimi"
        :columns             "colonnes"
        :create              "créer"
        :dashboard           "tableau de bord"
        :data                "données"
        :delete              "supprimer"
        :delete-resource     "supprimer ressource"
        :delete-resource-msg "supprimer ressource %1?"
        :delta-min           "delta [min]"
        :deployment          "déploiement"
        :describe            "décrire"
        :documentation       "documentation"
        :drop-file           "déposer un fichier"
        :edit                "modifier"
        :editing             "modification en cours"
        :error               "erreur"
        :event               "événement"
        :events              "événements"
        :execute-action      "exécuter le tâche"
        :execute-action-msg  "exécuter le tâche %1?"
        :fields              "champs"
        :filter              "filtre"
        :first               "début"
        :from                "de"
        :id                  "id"
        :knowledge-base      "knowledge base"
        :last                "fin"
        :last-30-days        "derniers 30 jours"
        :last-7-days         "derniers 7 jours"
        :legal               "mentions légales"
        :less                "moins"
        :limit               "limite"
        :loading             "chargement en cours"
        :login               "se connecter"
        :login-failed        "la connexion a échoué"
        :logout              "déconnexion"
        :messages            "messages"
        :metrics             "métriques"
        :module              "module"
        :modules             "modules"
        :more                "plus"
        :no-messages         "aucune message"
        :offset              "décalage"
        :order               "ordonner"
        :parameters          "paramètres"
        :profile             "profile d'utilisateur"
        :refresh             "actualiser"
        :reports             "rapports"
        :resource-type       "type de ressource"
        :results             "résultats"
        :search              "chercher"
        :select              "selection"
        :select-file         "choisir un fichier"
        :session             "session actuelle"
        :settings            "paramètres"
        :start               "début"
        :summary             "résumé"
        :support             "support"
        :signup              "s'inscrire"
        :signup-failed       "l'inscription a échoué"
        :state               "état"
        :statistics          "statistiques"
        :status              "statut"
        :tags                "mots clés"
        :terminate           "terminer"
        :timestamp           "horodatage"
        :to                  "à"
        :today               "aujourd'hui"
        :type                "type"
        :unauthorized        "non autorisé"
        :update              "mettre à jour"
        :url                 "URL"
        :usage               "usage"
        :username            "nom d'utilisateur"
        :vms                 "VMs"
        :welcome             "bienvenue"
        :yesterday           "hier"}})
