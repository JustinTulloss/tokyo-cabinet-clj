; Justin Tulloss
; 
; A wrapper to access Tokyo Cabinet from clojure

(ns tokyo-cabinet
    ; exclude symbols we'll use in our API
    (:refer-clojure :exclude [use get])
    (:import (tokyocabinet HDB FDB BDB)))


(declare *db*)

(def *db-types* {
    :bdb BDB,
    :fdb FDB,
    :hdb HDB
})

(defmacro getStatic [c f] 
    `(.get (.getField ~c (name '~f)) ~c))

(defn get [key] (.get *db* key))

(defn put [key value] (.put *db* key value))

(defmacro use [filename type & body]
    (let [klass (type *db-types*)]
        (import '(tokyocabinet HDB))
        (println (.newInstance klass))
        `(with-open [db# (.newInstance ~klass)]
            (.open db# ~filename (bit-or 
                (getStatic ~klass OWRITER)
                (getStatic ~klass OCREAT)))
            (binding [*db* db#]
                (do ~@body)))))
