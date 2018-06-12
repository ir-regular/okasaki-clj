(ns okasaki-clj.tree)

(defrecord Tree [value left right])

(defn complete [x d]
  (when (not (and (integer? d) (pos? d)))
    (throw (IllegalArgumentException. "Tree depth should be a positive integer")))
  (first (take 1 (drop (dec d)
                       (iterate #(Tree. x % %) (->Tree x nil nil))))))