(ns okasaki-clj.set
  (:refer-clojure :exclude [empty? contains?]))

(defprotocol Set
  (empty? [set])
  (insert [set x])
  (contains? [set x]))

(deftype UnbalancedSet [value left right]
  Set
  (empty? [set] (nil? value))
  (contains? [set x]
    (if (empty? set)
      false
      (loop [set set maybeX nil]
        (cond
          (nil? set) (= x maybeX)
          (< x (.-value set)) (recur (.-left set) maybeX)
          :else (recur (.-right set) (.-value set))))))
  (insert [set x]
    (when (or (nil? x) (not (instance? Comparable x)))
      (throw (IllegalArgumentException. "UnbalancedSet does not support storing nil")))
    (try
      (loop [set (if (empty? set) nil set)
             maybeX nil
             fs '()]
        (cond
          (nil? set)
          (if (= x maybeX)
            ; guard against cloning a path of the tree leading to the existing element
            (throw (UnsupportedOperationException. "Inserting a duplicate element"))
            (reduce #(%2 %1) (UnbalancedSet. x nil nil) fs))

          (< x (.-value set))
          (recur (.-left set) maybeX (conj fs #(UnbalancedSet. (.-value set) % (.-right set))))

          :else
          (recur (.-right set) (.-value set) (conj fs #(UnbalancedSet. (.-value set) (.-left set) %)))))
      (catch UnsupportedOperationException e
        ; in case of a duplicate element insert attempt, return the existing collection instead
        set)))
  Object
  (equals [this that]
    (and (= (instance? UnbalancedSet that))
         (= (.-value this) (.-value ^UnbalancedSet that))
         (= (.-left this) (.-left ^UnbalancedSet that))
         (= (.-right this) (.-right ^UnbalancedSet that)))))

(defn unbalanced-set
  ; sentinel-ish - empty set
  ([] (->UnbalancedSet nil nil nil))
  ([xs] (reduce insert (unbalanced-set) xs)))