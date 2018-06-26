(ns okasaki-clj.leftist-heap
  (:refer-clojure :exclude [empty? merge])
  (:require [okasaki-clj.heap :refer :all]))

(declare leftist-heap-node)

; leftist heap: rank of any left child is at least as large as that of its right sibling
; node rank: length of its right spine
; right spine: rightmost path from the node to an empty node
(deftype LeftistHeap [rank element left right]
  Comparable
  (compareTo [this that]
    (and (instance? LeftistHeap that))
    (if (not= (.-rank this) (.-rank ^LeftistHeap that))
      (compare (.-rank this) (.-rank ^LeftistHeap that))
      (compare (.-element this) (.-element ^LeftistHeap that))))
  Object
  (equals [this that]
    (and (instance? LeftistHeap that)
         (zero? (compare this that))))
  Heap
  (empty? [heap] (zero? rank))
  (find-min [heap]
    (when (empty? heap)
      (throw (IndexOutOfBoundsException. "Cannot retrieve min element of empty heap")))
    element)
  (delete-min [heap]
    (when (empty? heap)
      (throw (IndexOutOfBoundsException. "Cannot retrieve min element of empty heap")))
    (cond
      (every? nil? [left right]) (LeftistHeap. 0 nil nil nil)
      (nil? left) right
      (nil? right) left
      :else (merge left right)))
  (insert [heap x]
    (when (not (instance? Comparable x))
      (throw (IllegalArgumentException. "Cannot build heap from non-Comparable objects")))
    (merge (LeftistHeap. 1 x nil nil) heap))
  (merge [this that]
    (cond
      (empty? this) that
      (or (nil? that) (empty? that)) this
      ; note that arg order of merge is important, type of first arg determines which merge impl gets called
      ; `this` and `that` is guaranteed to be a non-empty heap; not so their left and right elements
      (neg? (compare element (.-element that))) (leftist-heap-node element left (merge that right))
      :else (leftist-heap-node (.-element that) (.-left that) (merge this (.-right that))))))

(defn- leftist-heap-node [element & heaps]
  ; nils always come first (so it will be either every? nil?, or nil? left)
  (let [[left right] (sort heaps)]
    (cond
      (every? nil? heaps) (LeftistHeap. 0 nil nil nil)
      (nil? left) (LeftistHeap. (inc (.-rank right)) element right nil)
      :else (LeftistHeap. (inc (max (.-rank left) (.-rank right))) element left right))))

(defn leftist-heap
  "Constructs a leftist binary min heap from a list of elements"
  ([] (->LeftistHeap 0 nil nil nil))
  ; to be optimised:
  ([xs] (reduce insert (leftist-heap) xs)))

; debugging/pretty-printing

(defn- to-vector [heap]
  (if (or (nil? heap) (empty? heap))
    []
    (let [e (.-element heap) l (.-left heap) r (.-right heap)]
      [e (to-vector l) (to-vector r)])))

(defmethod print-method LeftistHeap [o ^java.io.Writer w]
  (.write w (str (class o) ":\n"))
  (.write w (str (to-vector o) "\n")))
