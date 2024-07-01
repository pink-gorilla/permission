(ns modular.permission.core
  (:require
   [modular.permission.user :as user]
   [modular.permission.session :as session]
   [modular.permission.service :as service]))

(defn permission-active? [{:keys [users] :as _this}]
  (and (map? @users) (seq @users)))

(defn start-permissions
  "starts a permission manager with the specified users.
   services need to be added separately.
   An empty map means permission checks are disabled."
  [users]
  (assert (map? users)  "arg users needs to be a map")
  (let [this {:users (atom {})
              :services (atom {})
              :sessions (atom {})}]
    (user/set-users! this users)
    ;(println "state : " this)
    this))

(defn user-authorized? [this service-kw-or-symbol user-id]
  (if (permission-active? this)
    (service/service-authorized? this service-kw-or-symbol user-id)
    true))

(defn session-authorized? [this service-kw-or-symbol session-id]
  (if (permission-active? this)
    (session/service-authorized? this service-kw-or-symbol session-id)
    true))

