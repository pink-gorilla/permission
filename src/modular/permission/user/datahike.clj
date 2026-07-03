(ns modular.permission.user.datahike
  (:require
   [datahike.api :as d]
   [modular.permission.user :as user]))

(def schema
  [{:db/ident :user/name
    :db/unique :db.unique/identity
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many}
   {:db/ident :user/roles
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/many}])

(defn- normalize-user [u]
  (when u
    (-> u
        (update :user/email #(vec (or % [])))
        (update :user/roles #(into #{} (or % []))))))

(defn- pull-user [db user-name]
  (-> '[:find [(pull ?id [:user/name
                          :user/password
                          [:user/email :limit nil]
                          [:user/roles :limit nil]]) ...]
        :in $ ?name
        :where [?id :user/name ?name]]
      (d/q db user-name)
      first
      normalize-user))

(defrecord DatahikeUserManager [conn]
  user/Users
  (get-user [_ user-name]
    (pull-user @conn user-name))

  (get-user-roles [this user-name]
    (if-let [u (user/get-user this user-name)]
      (:user/roles u)
      nil))

  (find-user-id-via-email [_ email]
    (d/q '[:find ?name .
           :in $ ?email
           :where
           [?id :user/name ?name]
           [?id :user/email ?email]]
         @conn email)))

(defn create-user-manager [conn]
  (->DatahikeUserManager conn))

(defn seed-users [users]
  (fn [conn]
    (doseq [user users]
      (d/transact conn [user]))))
