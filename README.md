# Weblypermission [![GitHub Actions status |pink-gorilla/permission](https://github.com/pink-gorilla/permission/workflows/CI/badge.svg)](https://github.com/pink-gorilla/permission/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/permission.svg)](https://clojars.org/org.pinkgorilla/permission)

**End Users** this project is not for you.

Permission Management

It is used to check if a user is permissioned for a service.
A service can be a http route or a websocket message type.

It checks if user has a role that is required for a service.

nil means unknown user, or no permission needed.


You need to set this in your config
```
{:users {:florian {:roles #{:logistic}
                   :password "xxxxxxxx" 
                   :email ["andreas.wolfgang.bauer@gmail.com"]}
         :david {:roles #{}
		         :password "xxxxxx"
				 :email []}}}
```

```
(modular.permission.service/add-permissioned-services 
   {:time nil
    :get-orders #{} 
    :transfer-money #{:management})
	
(modular.permission.service/service-authorized?	
   :time nil)
-> yes, because time does not need user to be logged in   

(modular.permission.service/service-authorized?	
   :get-orders nil)
-> no, because :get-orders need user to be logged in 

(modular.permission.service/service-authorized?	
   :get-orders :florian)
-> yes, because :get-orders need user to be logged in, and :florian is logged in

(modular.permission.service/service-authorized?	
   :transfer-money :florian)
-> no, because :transfer-mone needs :management role


```
