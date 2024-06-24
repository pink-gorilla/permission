(ns role-test
  (:require
   [clojure.test :refer :all]
   [modular.permission.role :as role]))

(deftest roles
  (testing "user-has-role"
    (is (true? (role/user-has-role #{:logistic :admin} :admin))
        (is (false? (role/user-has-role #{:logistic :admin} :management)))))
  (testing "authorized-roles"
    (is (true? (role/authorized-roles? nil #{}))) ; true = route does not need roles
    (is (true? (role/authorized-roles? nil #{:admin}))) ; true = route does not need roles
    (is (true? (role/authorized-roles? nil nil))) ; true = route does not need roles.
    (is (true? (role/authorized-roles? #{} #{}))) ; route needs login, and user is logged in
    (is (false? (role/authorized-roles? #{} nil)))) ; route needs login, but user not logged in
  (testing "one-role"
    (is (true? (role/user-has-one-role-that-service-requires #{:accounting :management} #{:accounting :x :y})))
    (is (true? (role/user-has-one-role-that-service-requires #{:accounting :management} #{:management :x :y})))
    (is (false? (role/user-has-one-role-that-service-requires #{:accounting :management} #{:z :x :y})))))









