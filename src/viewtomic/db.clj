(ns viewtomic.db
  (:use [datomic.api :only [q db] :as d])
  (:gen-class))

(def uri "datomic:free://localhost:4334//media-plans")

(defn conn [] (d/connect uri))
(defn dbconn [] (db (conn)))
(defn- entities
  [query-function]
  (map #(d/entity (dbconn) (first %)) query-function))
(defn get-entities
  ([field]
  (entities (q '[:find ?n :in $ ?f :where [?n ?f]] (dbconn) field))))
(defn find-with
  [field]
  (get-entities field))

(defn get-entity
  [id]
  (d/entity (dbconn) id))