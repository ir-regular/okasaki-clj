(ns okasaki-clj.heap
  (:refer-clojure :exclude [empty? merge]))

(defprotocol Heap
  "Min-heap"
  (empty? [heap]
    "Returns true if heap contains no elements, false otherwise.")
  (insert [heap x]
    "Returns a new heap that contains `x`")
  (merge [this that]
    "Returns a new heap that contains elements of both `this` and `that`")
  (find-min [heap]
    "Returns the smallest element of the heap. Throws if heap is empty.")
  (delete-min [heap]
    "Returns a new heap with the smallest element removed. Throws if heap is empty."))

(extend-protocol Heap
  nil
  (empty? [heap] true)
  (merge [this that] that))
