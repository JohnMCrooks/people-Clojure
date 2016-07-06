(defproject people-closure "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.4.0"]   
                 [hiccup "1.0.5"]  ;clojure equivalent of mustache, but better.
                 [compojure "1.5.0"]] ; allows you to write routes
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [people-closure.core]
  :main people-closure.core)
