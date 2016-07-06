(ns people-closure.core
  (:require [clojure.string :as str] ;require is the equivalent of import in Java ;the :as is so you can shorthand the call instead of writing it out every time
            [compojure.core :as c]
            [ring.adapter.jetty :as j]; jetty is a commonly used java webserver 
            [hiccup.core :as h])  ; almost the equivalent of mustache
  (:gen-class))

(defn read-people []
  (let [people (slurp "people.csv")     ;let makes the variables localized, slurp is scanning everything into memory from the file
        people (str/split-lines people) ; redefining people as a vector of strings split by line
        people (map (fn [line] (str/split line #","))  ; creating a map to split the individual lines by comma, a regular expression is a string starting with a #
                 people)
        header (first people) ;this will return the very first line in the people vector which has the Column info
        people (rest people)   ; This will return everything after the first row, allowing for ease of creating hashmaps in the coming steps
        people (map (fn [line] (zipmap header line))  ;Loops through each line and applies the keypair method "zipmap" to each
                 people)]
    people))  ; this last 'people' is the object being returned

(defn people-html [people]
  [:html
   [:body
    [:ol            ;creating an organized list
     (map (fn [person]        
            [:li (str (get person "first_name") " " (get person "last_name"))])  ;grabbing and combining just the first and last names from the person hashmap and combining them with 'str'
       people)]]])

(defn filter-by-country [people country]
  (filter (fn [person]
            (= country (get person "country")))
    people))

(c/defroutes app  ; defining routes using compojure
  (c/GET "/:country{.*}" [country]   ; if it takes paramters they would go in the vector '[]', were using country in this case. 
    (let [people (read-people)
          people (if (= 0 (count country))   ; if country is a blank string return all people
                   people
                   (filter-by-country people country))]  ;else filter by country
      (h/html (people-html people)))))  ;html structure is made with hiccup by nesting all the same attributes as you would in a static html page

(defonce server (atom nil));atoms are containers that can hold any value (mutable global variable) can be reset with (reset! name new-value)
                           ;defonce prevents the value from being overwritten when the information is reloaded

(defn -main []
  (if @server
    (.stop @server))  ;This line will stop the server if the server is alreadry running. Call a java method in clojure using a period before the method. 
  (reset! server  ;sets ther server atom to the value the server returns
    (j/run-jetty app {:port 3000 :join? false})))  ;first arg 'app' defines the routes to be run , second arg is configuration  hash. 'join? false' allows the server to be non-blocking and allows us to use REPL while
  
  