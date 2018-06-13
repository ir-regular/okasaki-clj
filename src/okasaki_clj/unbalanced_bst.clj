(ns okasaki-clj.unbalanced-bst
  (:refer-clojure :exclude [empty? contains? set map])
  (:require [okasaki-clj.set :as set]
            [okasaki-clj.map :as map]))

; Somewhat "clever" implementation of a key-value pair.
; Provides a way to test both key and value equality (through equals),
; and a second one checking only key (through compareTo).

(deftype MapEntry [key value]
  Object
  (equals [this that]
    (and (instance? MapEntry that)
         (= (.-key this) (.-key ^MapEntry that))
         (= (.-value this) (.-value ^MapEntry that))))
  Comparable
  (compareTo [this that]
    (compare (.-key this) (.-key ^MapEntry that))))

; ...and an implementation of both Set and Map interfaces using a BST and MapEntry.
; This is an equivalent of UnbalancedSet from the book.

(deftype UnbalancedBST [value left right]
  Object
  (equals [this that]
    (and (= (instance? UnbalancedBST that))
         (= value (.-value ^UnbalancedBST that))
         (= left (.-left ^UnbalancedBST that))
         (= right (.-right ^UnbalancedBST that)))))

(extend-type UnbalancedBST
  map/FiniteMap
  (empty? [map] (nil? (.-value map)))
  (lookup [map k]
    (when (map/empty? map)
      (throw (IndexOutOfBoundsException. "Key not found")))
    (when (not (instance? Comparable k))
      (throw (IllegalArgumentException. "UnbalancedBST does not support non-comparable keys")))
    (let [x (MapEntry. k nil)]
      (loop [map map maybeX nil]
        (cond
          (nil? map)
          (if (zero? (compare x maybeX))
            (.-value maybeX)
            (throw (IndexOutOfBoundsException. "Key not found")))

          (neg? (compare x (.-value map)))
          (recur (.-left map) maybeX)

          :else
          (recur (.-right map) (.-value map))))))
  (bind [map k v]
    (when (not (instance? Comparable k))
      (throw (IllegalArgumentException. "UnbalancedBST does not support non-comparable keys")))
    (when (nil? v)
      (throw (IllegalArgumentException. "UnbalancedBST does not support storing nil")))
    (let [x (MapEntry. k v)]
      (try
        (loop [map (if (map/empty? map) nil map)
               maybeX nil
               fs '()]
          (cond
            (nil? map)
            (if (= x maybeX)
              ; guard against cloning a path of the tree leading to the existing element
              (throw (UnsupportedOperationException. "Inserting a duplicate element"))
              (reduce #(%2 %1) (UnbalancedBST. x nil nil) fs))

            (neg? (compare x (.-value map)))
            (recur (.-left map) maybeX (conj fs #(UnbalancedBST. (.-value map) % (.-right map))))

            :else
            (recur (.-right map) (.-value map) (conj fs #(UnbalancedBST. (.-value map) (.-left map) %)))))
        (catch UnsupportedOperationException e
          ; in case of a duplicate element insertion attempt, return the existing collection instead
          map))))
  set/Set
  ; A set is simply a map of (key, _) pairs, where value of the pair can be ignored.
  ; However, storing true as a value happens to provide a convenient return value for contains? :)
  (empty? [set]
    (map/empty? set))
  (contains? [set x]
    (try (map/lookup set x) (catch IndexOutOfBoundsException e false)))
  (insert [set x]
    (map/bind set x true)))

(defn unbalanced-set
  "Generates an UnbalancedBST-based Set from a collection of keys"
  ; sentinel-ish - empty set
  ([] (->UnbalancedBST nil nil nil))
  ([xs] (reduce set/insert (unbalanced-set) xs)))

(defn unbalanced-map
  "Generates an UnbalancedBST-based Map from key-value pairs"
  ; sentinel-ish, empty map
  ([] (->UnbalancedBST nil nil nil))
  ; expects a vector of [key, value] vectors
  ([kvs] (reduce (partial apply map/bind) (unbalanced-map) kvs)))
