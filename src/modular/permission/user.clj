(ns modular.permission.user)

(defn- add-user [[user-id {:keys [roles email] :as user}]]
  (assert (keyword user-id))
  (assert (set? roles))
  [user-id (assoc user
                  :id user-id
                  :email (or email [])
                  :roles roles)])

(defn- add-user-ids [users]
  (->> users
       (map add-user) ; seq of [id user]
       (into {})))

(defn set-users! [{:keys [users] :as _this} new-users]
  (reset! users (add-user-ids new-users)))

(defn get-user [{:keys [users] :as _this} user-id]
  ;(println "get-user: " user-id "users: " @users)
  (get @users user-id))

(defn get-user-roles [this user-id]
  (if-let [user (get-user this user-id)]
    (if-let [roles (:roles user)]
      roles
      #{})
    #{}))

; find user by email

(defn- has-email? [email]
  (fn [user]
    (let [emails (or (:email user) [])]
      (some #(= email %) emails))))

(defn find-user-id-via-email [{:keys [users] :as _this} email]
  (->> @users
       (filter (has-email? email))
       first
       :id))

