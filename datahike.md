modular.permission.user manages users via the Users protocol.

change how users datastructure works:
- see user-test/this for the old structure of the users map.
- currently/old structure :id which is a keyword
- new structure is :user/name which is a string and needs to be unique.
- prior simple keywords for :email and :roles. now they are 
  called :user/email and :user/roles
- the change to namespaced attributes makes it easier to use a datahike
  schema.
- previously the users seed data was a map, now it is a vector,
  contains users and each user contains :user/name :user/email and :user/roles
  and :user/password  

create the protocol "Users" that contains all functions that 
need to query users. 
  - get-user
  - get-user-roles
  - find-user-id-via-email

modular.permission.user namespace
- this will contain the protocol definition only (no facade fns)


modular.permission.user.atom namespace
  - defrecord AtomUserManager implements Users protocol
  - a function (defn create-user-manager [users]) will instanciate it and 
    will return instance that implements the protocol.

modular.permission.user.datahike namespace
  - defrecord DatahikeUserManager implements Users protocol
  - a function (defn create-user-manager [conn]) will instanciate it and 
    will return instance that implements the protocol. 
  - schema (def schema []) that will define the datahike schema.  
    :user/name needs to be unique.
    :user/roles is a set that contains keywords
    :user/email is many cardinality strings (singular attribute name)
  - seed-users (defn seed-users [users]
                  (fn [db-conn]))
      db-conn is a datahike db connection, 
      users is the same format as used to seed modular.permission.user.atom
      seed-users will transact each user (all new users, no :db/id lookup refs).
      returns a function that is called with a datahike db connection.

modular.permission.core
  - start-permissions used to get a map as parameter, now it will get 
    an object that implements the Users protocol (or nil).

modular.permission.service
  - calls protocol on (:user-manager this): (user/get-user um user-name)

modular.permission.core
  - :user-manager is either nil (permissions disabled) or a Users record
  - permission-active? will return true when :user-manager is not nil.


you need to create a unit test
that will 
- create a in-memory datahike database (use :memory backend).
- create um via modular.permission.user.datahike/create-user-manager passing in the conn.
- create modular.permission.user.datahike/seed-users passing in data from a users.edn
  file (or a def users [] structure), then calling the received fn with the db conn
- start modular.permission.core/start-permissions passing in um.
- run get-user (input: user name)
- run get-user-roles (input: user name)
- find-user-id-via-email (input : email)
= the data returned from the 3 functions should match the data that was passed in from
  the edn file or the def users [].

 another unit test 
 - create modular.permission.user.atom/create-user-manager passing in data from a users.edn
 - the rest of the unit test will be identical to the other test.

 you will need to add datahike as a dependency. 

you can also google and look for either datahike docs,and you can also look for datomic docs   
  (datomic follows almost the same standard), and you can look for datalog schema and queries
  (datalog is almost the same thing also)


