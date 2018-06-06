(ns okasaki-clj.stack
  (:import (clojure.lang IPersistentVector)))

(defprotocol Stack
  (empty? [stack])
  (cons [stack x])
  (head [stack])
  (tail [stack]))

(extend-protocol Stack
  IPersistentVector
  (empty? [stack] (= stack []))
  (cons [stack x] (conj stack x))
  (head [stack] (peek stack))
  (tail [stack] (pop stack)))

(deftype Cons [head tail]
  Stack
  (empty? [stack] (nil? head))
  (cons [stack x] {:pre (some? x)} (Cons. x stack))
  (head [stack] head)
  (tail [stack] tail)
  Object
  (equals [this that]
    (and (= (type this) (type that))
         (= (.head this) (.head that))
         (= (.tail this) (.tail that))))
  Comparable
  (compareTo [this that]
    {:pre (= (type this) (type that))}
    (if (not= (.head this) (.head that))
      (compare (.head this) (.head that))
      (compare (.tail this) (.tail that)))))

(defn cons-stack
  ; sentinel - empty list
  ([] (->Cons nil nil))
  ; normal usecase
  ([& xs] (reduce cons (cons-stack) xs)))
