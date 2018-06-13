(ns okasaki-clj.set
  (:refer-clojure :exclude [empty? contains?]))

(defprotocol Set
  ; Note: shadowing builtins is bad practice.
  ; I'm attempting to stick to function names from the book here, although I Clojurify them somewhat.
  (empty? [set]
    "Returns true if `set` contains no elements, false otherwise.")
  (insert [set x]
    "Returns a new set with `x` added.")
  ; See note about empty?
  (contains? [set x]
    "Returns true if `x` is stored in `set`, false otherwise."))
