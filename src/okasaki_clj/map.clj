(ns okasaki-clj.map
  (:refer-clojure :exclude [empty?]))

(defprotocol FiniteMap
  ; Note: shadowing builtins is bad practice.
  ; I'm attempting to stick to function names from the book here, although I Clojurify them somewhat.
  (empty? [map]
    "Returns true if `map` contains no elements, false otherwise.")
  (bind [map k v]
    "Returns a new map with value `v` stored under lookup key `k`.")
  (lookup [map k]
    "Returns value stored under key `k`. Throws if `k` not in `map`."))
