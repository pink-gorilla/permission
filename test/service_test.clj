(ns service-test
  (:require
   [clojure.test :refer :all]
   [modular.permission.core :refer [start-permissions user-authorized?]]))

(def this1 (start-permissions {}))


(def this (start-permissions
           {:demo {:roles #{:logistic}
                   :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                   :email ["john@doe.com"]}
            :boss {:roles #{:logistic :supervisor :accounting}
                   :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                   :email ["boss@doe.com"]}
            :florian {:roles #{:logistic}
                      :password "a231498f6c1f441aa98482ea0b224ffa" ; 1234
                      :email ["hoertlehner@gmail.com"]}
            :john {:roles #{:logistic}
                   :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                   :email ["john@doe.com"]}}))


(deftest service
  (testing "service"
    (is (true? (user-authorized? this1 :service-admin :user1))) ; true because permissions are disabled
    (is (true? (user-authorized? this1 :time nil))) ; true because permissions are disabled
    (is (false? (user-authorized? this :service-management :user1))) ; service unknown
    (is (false? (user-authorized? this :service-management nil))) ; service unknown
    ))

