(ns session-test
  (:require
   [clojure.test :refer :all]
   [modular.permission.core :refer [start-permissions]]
   [modular.permission.service :as service]
   [modular.permission.session :as session]))

(def this (start-permissions
           {:demo {:roles #{:admin :logistic}
                   :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                   :email ["john@doe.com"]}
            :awb99 {:roles #{:management :admin}
                    :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                    :email ["awb99@gmail.com"]}}))

(service/add-permissioned-services this {:time nil ; no permission needed for this.
                                         :service-management #{:management} ; only logged in users can access this service
                                         :service-admin #{:admin :supervisor}})

(def session-id "asdflk9dfasdf")

(session/set-user! this session-id :demo)

(deftest service
  (testing "session"
    (is (= :demo (session/get-user this session-id)))
    (is (true? (session/service-authorized? this :service-admin session-id))) ; demo has admin role 
    (is (true? (session/service-authorized? this :time session-id))) ; :time service does not require anythinig
    (is (false? (session/service-authorized? this :service-management session-id))))) ; demo does not have management role

