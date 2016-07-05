(ns people-clojure.core
  (:require [clojure.string :as str]) ;require is the equivalent of import in Java ;the :as is so you can shorthand the call instead of writing it out every time
  (:gen-class))

(defn -main []
  (let [people (slurp "people.csv")     ;let makes the variables localized, slurp is scanning everything into memory from the file
        people (str/split-lines people) ; redefining people as a vector of strings split by line
        people (map (fn [line] (str/split line #","))  ; creating a map to split the individual lines by comma, a regular expression is a string starting with a #
                 people)
        header (first people) ;this will return the very first line in the people vector which has the Column info
        people (rest people)   ; This will return everything after the first row, allowing for ease of creating hashmaps in the coming steps
        people (map (fn [line] (zipmap header line))  ;Loops through each line and applies the keypair method "zipmap" to each
                 people)
        people (filter (fn [line] (= "Brazil" (get line "country")))   ;filters by country
                 people)] 
    people))  ; this last people is the object being returned