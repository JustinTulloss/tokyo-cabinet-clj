; Copyright (c) 2009 Justin Tulloss
;
; Permission is hereby granted, free of charge, to any person obtaining a copy
; of this software and associated documentation files (the "Software"), to deal
; in the Software without restriction, including without limitation the rights
; to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
; copies of the Software, and to permit persons to whom the Software is
; furnished to do so, subject to the following conditions:
; 
; The above copyright notice and this permission notice shall be included in
; all copies or substantial portions of the Software.
; 
; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
; FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
; AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
; LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
; THE SOFTWARE.

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
