(ns session-test
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [modular.permission.core :refer [start-permissions]]
   [modular.permission.service :as service]
   [modular.permission.session :as session]
   [modular.permission.user.atom :as user-atom]))

(def users
  (-> "users.edn" io/resource slurp edn/read-string))

(def this (start-permissions (user-atom/create-user-manager users)))

(service/add-permissioned-services this {:time nil
                                         :service-management #{:management}
                                         :service-admin #{:admin :supervisor}})

(def session-id "asdflk9dfasdf")

(session/set-user! this session-id "demo")

(deftest session
  (testing "session"
    (is (= "demo" (session/get-user this session-id)))
    (is (true? (session/service-authorized? this :service-admin session-id)))
    (is (true? (session/service-authorized? this :time session-id)))
    (is (false? (session/service-authorized? this :service-management session-id)))))
