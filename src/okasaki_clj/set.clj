(ns okasaki-clj.set
  (:refer-clojure :exclude [empty? contains?]))

(defprotocol Set
  (empty? [set])
  (insert [set x])
  (contains? [set x]))

(deftype UnbalancedSet [value left right]
  Set
  ; we do not support storing nil in the tree as anything else than value of sentinel/empty tree
  (empty? [set] (nil? value))
  (contains? [set x]
    (if (empty? set)
      false
      (loop [set set maybeX nil]
        (cond
          (nil? set) (= x maybeX)
          (< x (.value set)) (recur (.left set) maybeX)
          :else (recur (.right set) (.value set))))))
  (insert [set x]
    {:pre [(instance? Comparable x)]}
    (if (empty? set)
      (UnbalancedSet. x nil nil)
      (try
        ((fn insert' [set x]
           (if (nil? set)
             (UnbalancedSet. x nil nil)
             (let [value (.value set) left (.left set) right (.right set)]
               (cond
                 (< x value) (UnbalancedSet. value (insert' left x) right)
                 (> x value) (UnbalancedSet. value left (insert' right x))
                 ; guard against cloning a path of the tree leading to the existing element
                 :else (throw (UnsupportedOperationException. "Inserting a duplicate element"))))))
          set x)
        (catch UnsupportedOperationException e
          ; in case of a duplicate element insert attempt, return the existing collection instead
          set))))
  Object
  (equals [this that]
    (and (= (instance? UnbalancedSet that))
         (= (.value this) (.value that))
         (= (.left this) (.left that))
         (= (.right this) (.right that)))))

(defn unbalanced-set
  ; sentinel-ish - empty set
  ([] (->UnbalancedSet nil nil nil))
  ([xs] (reduce insert (unbalanced-set) xs)))