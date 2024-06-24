(ns modular.permission.service
  (:require
   [modular.permission.user :refer [get-user get-user-roles]]
   [modular.permission.role :as role]))

(defn add-permissioned-services [{:keys [services] :as _this} permissioned-services]
  (assert (map? permissioned-services))
  ; {:time/now-date #{} 
  ;  :time/local nil}
  (swap! services merge permissioned-services))

(defn has-permission-for-service [{:keys [services] :as _this} service-kw-or-symbol]
  (contains? @services service-kw-or-symbol))

(defn required-permission-for-service [{:keys [services] :as _this} service-kw-or-symbol]
  (get @services service-kw-or-symbol))

(defn service-authorized? [this service-kw-or-symbol user-id]
  (let [user (get-user this user-id)
        has-permission? (has-permission-for-service this service-kw-or-symbol)
        required-roles (required-permission-for-service this service-kw-or-symbol)
        user-roles (get-user-roles this user-id)
        a? (if (and user has-permission?)
             (role/authorized-roles? required-roles user-roles)
             false)]
    (cond
      (not has-permission?)
      false ; service has no permission entry.

      (nil? required-roles)
      true  ; if the service does not require anything, then authorized.

      (not user)
      false

      a?
      true

      (not a?)
      false ; user lacks permission for service

      :else ; should not happen, just to be safe.
      false)))


