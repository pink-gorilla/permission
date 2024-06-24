(ns modular.permission.role)

(defn user-has-role [user-roles role]
  ;(println "user roles: " user-roles " role: " role)
  (contains? user-roles role))

(defn user-has-one-role-that-service-requires [required-roles user-roles]
  (let [r (some (partial user-has-role user-roles) required-roles)]
    (if r true false)))

(defn authorized-roles? [required-roles user-roles]
  ;(println "authorized-roles: required: " required-roles " user: " user-roles)
  (cond
    (nil? required-roles)
    true

    (empty? required-roles)
    (if user-roles true false)

    :else
    (user-has-one-role-that-service-requires required-roles user-roles)))
