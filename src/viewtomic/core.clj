(ns viewtomic.core
  (:use [seesaw.core]
        [datomic.api :only [q db] :as d])
  (:import org.pushingpixels.substance.api.SubstanceLookAndFeel)
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

(native!)

(def f (frame 
  :title "Get to know Seesaw"
  :on-close :exit))

(def itemlist (listbox
  :model (find-with :creative-size/dimensions)))

(defn display [content]
  (config! f :content content)
  content)

; search by attribute name
(def field (display (text "Enter attribute name to search for...")))
(def b (button 
  :text "Search for field"))
(listen b :action (fn [e] 
  (config! itemlist :model (find-with (text field)))))

; search by ID
(def id-input (display (text "Enter entity id to retrieve...")))
(def b2 (button 
  :text "Search for entity"))
(listen b2 :action (fn [e] 
  (config! itemlist :model (get-entity (read-string (text id-input))))))

(defn -main [& args]
  (display (vertical-panel :items [
    (horizontal-panel :items [
      field
      b])
    (horizontal-panel :items [
      id-input
      b2])
    (scrollable itemlist)]))
  (-> f pack! show!))
