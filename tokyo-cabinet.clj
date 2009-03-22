; Justin Tulloss
; 
; A wrapper to access Tokyo Cabinet from clojure

(ns tokyo-cabinet
    ; exclude symbols we'll use in our API
    (:refer-clojure :exclude [use get]))


(declare *db*)

(defn get [key] (.get *db* key))

(defn put [key value] (.put *db* key value))

(defmacro use [filename & body]
    (import '(tokyocabinet HDB))
    `(with-open [hdb# (HDB.)]
        (.open hdb# ~filename (bit-or HDB/OWRITER HDB/OCREAT))
        (binding [*db* hdb#]
            (do ~@body))))
