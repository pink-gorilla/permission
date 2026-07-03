(ns modular.permission.session
  (:require
   [modular.permission.service :as service]))

(defn set-user! [{:keys [sessions] :as _this} session-id user-name]
  (swap! sessions assoc session-id user-name))

(defn get-user [{:keys [sessions] :as _this} session-id]
  (get @sessions session-id))

(defn service-authorized? [this service-kw session-id]
  (if-let [user-id (get-user this session-id)]
    (service/service-authorized? this service-kw user-id)
    false))
