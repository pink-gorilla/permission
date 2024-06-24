(ns user-test
  (:require
   [clojure.test :refer :all]
   [modular.permission.core :refer [start-permissions]]
   [modular.permission.user :as user]
   [modular.permission.role :as role]))

(def this (start-permissions
           {:demo {:roles #{:admin :logistic}
                   :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                   :email ["john@doe.com"]}
            :awb99 {:roles #{:management :admin}
                    :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
                    :email ["awb99@gmail.com"]}}))

(deftest user
  (testing "get-user"
    (is (= {:roles #{:admin :logistic}
            :id :demo
            :password "a231498f6c1f441aa98482ea0b224ffa" ; "1234"
            :email ["john@doe.com"]}
           (user/get-user this :demo))))
  #_(testing "get-user-roles"
      (is (= (user/get-user-roles this :awb99)
             #{:management :admin})))
  #_(testing "find-user by email"
      (is (= (:id (user/find-user-id-via-email this "awb99@gmail.com"))
             :awb99))))

#_(deftest user-roles
    (testing "user-roles"
      (is (true? (role/authorized-roles? #{:admin} :demo))) ; demo has admin role => true
      (is (false? (role/authorized-roles?  #{:accounting} :demo))) ; demo does not have accouting role => false
      (is (false? (role/authorized-roles?  #{:accounting :management} :demo))) ; demo does not have accouting role => false
      (is (true? (role/authorized-roles?  #{:accounting :logistic} :demo))) ; demo does not logistic role => true
      (is (false? (role/authorized-roles?  #{:admin} :hacker)))  ; hacker has no roles
      (is (false? (role/authorized-roles?  #{:admin} nil)))))  ; not logged in user has no roles









