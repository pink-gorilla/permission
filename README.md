# permission [![GitHub Actions status |pink-gorilla/permission](https://github.com/pink-gorilla/permission/workflows/CI/badge.svg)](https://github.com/pink-gorilla/permission/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/permission.svg)](https://clojars.org/org.pinkgorilla/permission)

**End Users** this project is not for you.

## Permission Management

It is used to check if a user is permissioned for a service.
A service can be a http route or a websocket message type.

It checks if user has a role that is required for a service.

nil means unknown user, or no permission needed.

User data is provided via a `Users` protocol implementation (`modular.permission.user.atom` or `modular.permission.user.datahike`).

```
(def users
  [{:user/name "florian"
    :user/password "xxxxxxxx"
    :user/email ["andreas.wolfgang.bauer@gmail.com"]
    :user/roles #{:logistic}}
   {:user/name "david"
    :user/password "xxxxxx"
    :user/email []
    :user/roles #{}}])

(def permissions
  (modular.permission.core/start-permissions
    (modular.permission.user.atom/create-user-manager users)))
```

Pass `nil` to `start-permissions` to disable permission checks.

```
(modular.permission.service/add-permissioned-services
 permissions
 {:time nil
  :get-orders #{}
  :transfer-money #{:management}})

(modular.permission.service/service-authorized?
 permissions
 :time
 nil)
-> yes, because time does not need user to be logged in

(modular.permission.service/service-authorized?
 permissions
 :get-orders
 nil)
-> no, because :get-orders need user to be logged in

(modular.permission.service/service-authorized?
 permissions
 :get-orders
 "florian")
-> yes, because :get-orders need user to be logged in, and florian is logged in

(modular.permission.service/service-authorized?
 permissions
 :transfer-money
 "florian")
-> no, because :transfer-money needs :management role
```
