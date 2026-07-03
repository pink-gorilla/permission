(ns user-test
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [modular.permission.core :refer [start-permissions]]
   [modular.permission.user :as user]
   [modular.permission.user.atom :as user-atom]))

(def users
  (-> "users.edn" io/resource slurp edn/read-string))

(def this
  (start-permissions (user-atom/create-user-manager users)))

(deftest user
  (let [um (:user-manager this)]
    (testing "get-user"
      (is (= (first users) (user/get-user um "demo"))))
    (testing "get-user-roles"
      (is (= #{:management :admin} (user/get-user-roles um "awb99"))))
    (testing "find-user by email"
      (is (= "awb99" (user/find-user-id-via-email um "awb99@gmail.com"))))))
