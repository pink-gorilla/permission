(ns user-manager-test
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [datahike.api :as d]
   [modular.permission.core :refer [start-permissions]]
   [modular.permission.user :as user]
   [modular.permission.user.atom :as user-atom]
   [modular.permission.user.datahike :as user-datahike]))

(def users
  (-> "users.edn" io/resource slurp edn/read-string))

(defn assert-user-ops [um seed-users]
  (doseq [{:user/keys [name email roles] :as u} seed-users]
    (is (= u (user/get-user um name)))
    (is (= roles (user/get-user-roles um name)))
    (doseq [e email]
      (is (= name (user/find-user-id-via-email um e))))))

(deftest atom-user-manager
  (assert-user-ops (user-atom/create-user-manager users) users))

(deftest datahike-user-manager
  (let [cfg {:store {:backend :memory :id (random-uuid)}
             :initial-tx user-datahike/schema}
        _ (d/create-database cfg)
        conn (d/connect cfg)
        um (do
             ((user-datahike/seed-users users) conn)
             (user-datahike/create-user-manager conn))]
    (assert-user-ops um users)))

(deftest start-permissions-with-user-manager
  (let [this (start-permissions (user-atom/create-user-manager users))]
    (is (= "demo" (user/find-user-id-via-email (:user-manager this) "john@doe.com")))))
