(ns modular.permission.user.atom
  (:require
   [modular.permission.user :as user]))

(defn- normalize-user [{:user/keys [roles email] :as u}]
  (assert (set? roles) (str "roles must be a set for user " (:user/name u)))
  (assoc u :user/email (vec (or email [])) :user/roles roles))

(defn- has-email? [email]
  (fn [u]
    (some #(= email %) (:user/email u))))

(defrecord AtomUserManager [index]
  user/Users
  (get-user [_ user-name]
    (get index user-name))

  (get-user-roles [_ user-name]
    (if-let [u (get index user-name)]
      (:user/roles u)
      nil))

  (find-user-id-via-email [_ email]
    (->> index
         vals
         (filter (has-email? email))
         first
         :user/name)))

(defn create-user-manager [users]
  (->AtomUserManager
   (into {} (map (juxt :user/name normalize-user) users))))
