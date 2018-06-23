(ns okasaki-clj.leftist-heap
  (:refer-clojure :exclude [empty? merge])
  (:require [okasaki-clj.heap :refer :all]))

; internal-use constructor
(declare leftist-heap-node)
; internal-use getters
(declare get-rank)
(declare get-element)

; leftist heap: rank of any left child is at least as large as that of its right sibling
; node rank: length of its right spine
; right spine: rightmost path from the node to an empty node
(deftype LeftistHeap [rank element left right]
  Object
  (equals [this that]
    (and (instance? LeftistHeap that)
         (= (.-left this) (.-left that))
         (= (.-right this) (.-right that))))
  Heap
  (empty? [heap] (zero? rank))
  (find-min [heap]
    (when (empty? heap)
      (throw (IndexOutOfBoundsException. "Cannot retrieve min element of empty heap")))
    element)
  (delete-min [heap]
    (when (empty? heap)
      (throw (IndexOutOfBoundsException. "Cannot retrieve min element of empty heap")))
    (merge left right))
  (insert [heap x]
    (when (not (instance? Comparable x))
      (throw (IllegalArgumentException. "Cannot build heap from non-Comparable objects")))
    (merge (leftist-heap-node x nil nil) heap))
  (merge [this that]
    (cond
      (empty? this) that
      (empty? that) this

      (pos? (compare element (get-element that)))
      (leftist-heap-node (get-element that) (.-left that) (merge this (.-right that)))

      :else ; top of "this" is less or equal than top of "that", so it stays as top
      (leftist-heap-node element left (merge that right)))))

; internal-use constructor

(defn- leftist-heap-node [element & heaps]
  ; nils always come first (so it will be either every? nil?, or nil? left)
  (let [[left right] (sort-by get-rank heaps)
        rank (inc (get-rank right))]
    (cond
      (empty? right) (LeftistHeap. rank element nil nil)
      (nil? left) (LeftistHeap. rank element right nil)
      :else (LeftistHeap. rank element left right))))

(defn leftist-heap
  "Constructs a leftist binary min heap from a list of elements"
  ([] (->LeftistHeap 0 nil nil nil))
  ; to be optimised:
  ([xs] (reduce insert (leftist-heap) xs)))

; internal use getters

(defn- get-rank [heap]
  (if-not (empty? heap)
    (when (instance? LeftistHeap heap) (.-rank heap))
    0))

(defn- get-element [heap]
  (when-not (empty? heap)
    (when (instance? LeftistHeap heap)
      (.-element heap))))

; debugging/pretty-printing

(defn- to-vector [heap]
  (if (empty? heap)
    []
    (let [e (get-element heap) l (.-left heap) r (.-right heap)]
      [e (to-vector l) (to-vector r)])))

(defmethod print-method LeftistHeap [o ^java.io.Writer w]
  (.write w (str (class o) ":\n"))
  (.write w (str (to-vector o) "\n")))
