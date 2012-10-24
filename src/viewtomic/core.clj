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
  (let [db (db (conn))]
    (map #(d/entity db (first %)) query-function)))
(defn get-entities
  ([field]
  (entities (q '[:find ?n :in $ ?f :where [?n ?f]] (dbconn) field))))
(defn find-with
  [field]
  (get-entities field))

(native!)

(def f (frame 
  :title "Get to know Seesaw"
  :on-close :exit))

(def itemlist (listbox
  :model (find-with :creative-size/dimensions)))

(defn display [content]
  (config! f :content content)
  content)

(def field (display (text "Enter field to search for...")))

(def b (button 
  :text "Search"))

(listen b :action (fn [e] 
  (config! itemlist :model (find-with (text field)))))

(defn -main [& args]
  (display (vertical-panel :items [
    b
    field
    (scrollable itemlist)]))
  (-> f pack! show!))
