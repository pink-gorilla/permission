(ns service-test
  (:require
   [clojure.test :refer :all]
   [modular.permission.core :refer [start-permissions user-authorized?]]
   [modular.permission.user.atom :as user-atom]))

(def users
  [{:user/name "demo"
    :user/password "a231498f6c1f441aa98482ea0b224ffa"
    :user/email ["john@doe.com"]
    :user/roles #{:logistic}}
   {:user/name "boss"
    :user/password "a231498f6c1f441aa98482ea0b224ffa"
    :user/email ["boss@doe.com"]
    :user/roles #{:logistic :supervisor :accounting}}
   {:user/name "florian"
    :user/password "a231498f6c1f441aa98482ea0b224ffa"
    :user/email ["hoertlehner@gmail.com"]
    :user/roles #{:logistic}}
   {:user/name "john"
    :user/password "a231498f6c1f441aa98482ea0b224ffa"
    :user/email ["john@doe.com"]
    :user/roles #{:logistic}}])

(def this1 (start-permissions nil))

(def this (start-permissions (user-atom/create-user-manager users)))

(deftest service
  (testing "service"
    (is (true? (user-authorized? this1 :service-admin "user1")))
    (is (true? (user-authorized? this1 :time nil)))
    (is (false? (user-authorized? this :service-management "user1")))
    (is (false? (user-authorized? this :service-management nil)))))
