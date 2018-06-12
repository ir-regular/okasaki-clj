(ns okasaki-clj.tree)

(defrecord Tree [value left right])

(defn complete
  "Generate a complete binary tree of depth d, every node containing value x"
  [x d]
  (when (not (and (integer? d) (pos? d)))
    (throw (IllegalArgumentException. "Tree depth should be a positive integer")))
  (first (take 1 (drop (dec d)
                       (iterate #(Tree. x % %) (->Tree x nil nil))))))

; for an n-element tree, given m=((n-1) div 2)
;   - if n is odd, tree has two subtrees, both with m elements:
;     (x, balanced(x, m), balanced(x, m))
;   - if n is even, tree has two subtrees, one with m elements, one with m+1 elements:
;     (x, balanced(x, m), (x, balanced(x, m) nil))
; therefore to build the tree bottom-up, you need to generate a sequence of:
;   n, ((n-1) div 2), ... 0

(defn balanced
  "Generate a balanced binary tree of n nodes, all containing value x"
  [x n]
  (when (not (pos? n))
    (throw (IllegalArgumentException. "Tree size should be a positive integer")))
  (loop [[m & ms] (reverse (take-while pos? (iterate #(quot (dec %) 2) n)))
         base nil]
    ; tests (predicate? m)
    (condp #(%1 %2) m
      nil? base
      odd? (recur ms (->Tree x base base))
      ; :else
      (recur ms (->Tree x (->Tree x base nil) base)))))
