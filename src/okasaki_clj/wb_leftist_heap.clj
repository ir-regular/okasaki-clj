(ns okasaki-clj.wb-leftist-heap
  (:refer-clojure :exclude [empty? merge])
  (:require [okasaki-clj.heap :refer :all]))

; internal-use constructor
(declare wb-leftist-heap-node)
; internal-use getters
(declare get-weight)
(declare get-element)

; weight-biased leftist heap: weight of any left child is at least as large as that of its right sibling
; node weight: how many nodes a subtree rooted in this node contains (including the node itself)
(deftype WBLeftistHeap [weight element left right]
  Object
  (equals [this that]
    (and (instance? WBLeftistHeap that)
         (= (.-left this) (.-left that))
         (= (.-right this) (.-right that))))
  Heap
  (empty? [heap] (zero? weight))
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
    (let [[element x] (sort [element x]) ; element < x or element is nil
          x-heap (wb-leftist-heap-node x nil nil)
          left-element (get-element left)]
      (cond
        (empty? heap) x-heap

        (nil? left-element)
        (wb-leftist-heap-node element x-heap nil)

        (pos? (compare x left-element))
        (wb-leftist-heap-node element left (if (empty? right) x-heap (insert right x)))

        :else
        (wb-leftist-heap-node element (insert left x) right))))

  (merge [this that]
    (cond
      (empty? this) that

      (empty? that) this

      (pos? (compare element (get-element that)))
      (wb-leftist-heap-node (get-element that) (.-left that) (merge this (.-right that)))

      ; top of "this" is less or equal than top of "that", so it stays as top
      :else
      (wb-leftist-heap-node element left (merge that right)))))

; internal-use constructor

(defn- wb-leftist-heap-node [element & heaps]
  ; nils always come first (so it will be either every? nil?, or nil? left)
  (let [[left right] (sort-by get-weight heaps)
        weight (+ 1 (get-weight left) (get-weight right))]
    (cond
      (empty? right) (->WBLeftistHeap weight element nil nil)
      (nil? left) (->WBLeftistHeap weight element right nil)
      :else (->WBLeftistHeap weight element left right))))

(defn wb-leftist-heap
  "Constructs a leftist binary min heap from a list of elements"
  ([] (->WBLeftistHeap 0 nil nil nil))
  ([xs]
   (->> xs
        (map #(->WBLeftistHeap 1 % nil nil))
        (iterate (fn [xs] (map #(merge (first %) (second %)) (partition-all 2 xs))))
        (drop-while #(some? (second %)))
        (first)
        (first))))

; internal use getters

(defn- get-weight [heap]
  (if-not (empty? heap)
    (when (instance? WBLeftistHeap heap) (.-weight heap))
    0))

(defn- get-element [heap]
  (when-not (empty? heap)
    (when (instance? WBLeftistHeap heap)
      (.-element heap))))

; debugging/pretty-printing

(defn- to-vector [heap]
  (if (empty? heap)
    []
    (let [e (get-element heap) l (.-left heap) r (.-right heap)]
      [e (to-vector l) (to-vector r)])))

(defmethod print-method WBLeftistHeap [o ^java.io.Writer w]
  (.write w (str (class o) ":\n"))
  (.write w (str (to-vector o) "\n")))


; (fn[& x] (map x) (fn[& a] #(apply %1 a)x))

; (->> #(vec %&) (map #(apply %1 %2)))
