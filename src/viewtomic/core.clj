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

; search by attribute name
(def attribute-seach-field (volatile-field "Enter attribute name to search for..."))
(def attribute-search-button (button 
  :text "Search for field"))
(listen attribute-search-button :action (fn [e]
  (let [data (db/find-with (text attribute-seach-field))]
    (config! data-grid 
      :model [:columns (vec (keys (first data))) 
      :rows (map #(into {} %) data)] ))
    (-> f pack!)))

; search by ID
(def id-seach-field (volatile-field "Enter entity id to retrieve..."))
(def id-search-button (button 
  :text "Search for entity"))
(listen id-search-button :action (fn [e] 
  (let [data [(db/get-entity (read-string (text id-seach-field)))]]
    (config! data-grid 
      :model [:columns (vec (keys (first data))) 
      :rows (map #(into {} %) data)] ))
    (-> f pack!)))

(defn -main [& args]
  (display (vertical-panel :items [
    (horizontal-panel :items [
      attribute-seach-field
      attribute-search-button])
    (horizontal-panel :items [
      id-seach-field
      id-search-button])
    (scrollable data-grid)]))
  (-> f pack! show!))
