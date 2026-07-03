(ns modular.permission.service
  (:require
   [modular.permission.user :as user]
   [modular.permission.role :as role]))

(defn add-permissioned-service [{:keys [services] :as _this} service-kw-or-symbol required-permission]
  (swap! services assoc service-kw-or-symbol required-permission))

(defn add-permissioned-services [{:keys [services] :as _this} permissioned-services]
  (assert (map? permissioned-services))
  (swap! services merge permissioned-services))

(defn has-permission-for-service [{:keys [services] :as _this} service-kw-or-symbol]
  (contains? @services service-kw-or-symbol))

(defn required-permission-for-service [{:keys [services] :as _this} service-kw-or-symbol]
  (get @services service-kw-or-symbol))

(defn service-authorized? [this service-kw-or-symbol user-id]
  (let [um (:user-manager this)
        user (when um (user/get-user um user-id))
        has-permission? (has-permission-for-service this service-kw-or-symbol)
        required-roles (required-permission-for-service this service-kw-or-symbol)
        user-roles (when um (user/get-user-roles um user-id))
        a? (if (and user has-permission?)
             (role/authorized-roles? required-roles user-roles)
             false)]
    (cond
      (not has-permission?)
      false

      (nil? required-roles)
      true

      (not user)
      false

      a?
      true

      (not a?)
      false

      :else
      false)))
