(ns modular.permission.core
  (:require
   [modular.permission.session :as session]
   [modular.permission.service :as service]))

(defn permission-active? [{:keys [user-manager] :as _this}]
  (some? user-manager))

(defn start-permissions
  "Starts a permission manager with the specified user-manager.
   Services need to be added separately.
   Pass nil to disable permission checks."
  ([]
   (start-permissions nil))
  ([user-manager]
   {:user-manager user-manager
    :services (atom {})
    :sessions (atom {})}))

(defn user-authorized? [this service-kw-or-symbol user-id]
  (if (permission-active? this)
    (service/service-authorized? this service-kw-or-symbol user-id)
    true))

(defn session-authorized? [this service-kw-or-symbol session-id]
  (if (permission-active? this)
    (session/service-authorized? this service-kw-or-symbol session-id)
    true))
