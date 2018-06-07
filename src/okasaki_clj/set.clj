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
      (loop [set set]
        (cond (nil? set) false
              (< x (.value set)) (recur (.left set))
              (> x (.value set)) (recur (.right set))
              :else true))))
  (insert [set x]
    {:pre [(instance? Comparable x)]}
    (cond
      (empty? set) (UnbalancedSet. x nil nil)
      (< x value) (UnbalancedSet. value (if left (insert left x) (UnbalancedSet. x nil nil)) right)
      (> x value) (UnbalancedSet. value left (if right (insert right x) (UnbalancedSet. x nil nil)))
      :else set))
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