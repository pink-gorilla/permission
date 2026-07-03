(ns modular.permission.user)

(defprotocol Users
  (get-user [this user-name]
    "Returns user map or nil. user-name is a string.")
  (get-user-roles [this user-name]
    "Returns role set or nil if user not found.")
  (find-user-id-via-email [this email]
    "Returns user-name string or nil."))
