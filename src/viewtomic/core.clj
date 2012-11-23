(ns viewtomic.core
  (:use [seesaw.core])
  (:require [viewtomic.db :as db])
  (:import org.pushingpixels.substance.api.SubstanceLookAndFeel)
  (:gen-class))

(native!)

(def f (frame 
  :title "Viewtomic"
  :on-close :exit))

(def data-grid
  (table
    :model [:columns [] :rows []]))

(defn display [content]
  (config! f :content content)
  content)

(defn volatile-field
  [field-text]
  (let [field (display (text field-text))]
    (listen field :focus-gained (fn [e]
      (if (= (text field) field-text)
        (text! field ""))))
    (listen field :focus-lost (fn [e]
      (if (= (text field) "")
        (text! field field-text))))
    field))

; Set the DB URL
(def db-url-field (volatile-field "Enter DB URL..."))
(text! db-url-field @db/url)
(def set-db-url-button (button :text "Set DB URL"))
(listen set-db-url-button :action (fn [e]
  (db/set-db-url (text db-url-field))))

(defn display-data
  [data]
  (config! data-grid 
      :model [:columns (vec (keys (first data))) 
      :rows (map #(into {} %) data)] )
  (-> f pack!))

; search by attribute name
(def attribute-seach-field (volatile-field "Enter attribute name to search for..."))
(def attribute-search-button (button :text "Search for field"))
(listen attribute-search-button :action 
  (fn [e] (display-data (db/find-with (text attribute-seach-field)))))

; search by ID
(def id-seach-field (volatile-field "Enter entity id to retrieve..."))
(def id-search-button (button 
  :text "Search for entity"))
(listen id-search-button :action 
  (fn [e] (display-data [(db/get-entity (read-string (text id-seach-field)))])))

(defn -main [& args]
  (display (vertical-panel :items [
    (horizontal-panel :items [
      db-url-field
      set-db-url-button])
    (horizontal-panel :items [
      attribute-seach-field
      attribute-search-button])
    (horizontal-panel :items [
      id-seach-field
      id-search-button])
    (scrollable data-grid)]))
  (-> f pack! show!))
