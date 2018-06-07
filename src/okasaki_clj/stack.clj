(ns okasaki-clj.stack
  ; avoid conflicts with existing core Clojure functions
  (:refer-clojure :exclude [empty? cons concat])
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

(deftype Cons [head tail ^boolean empty]
  Stack
  (empty? [stack] (.empty stack))
  (cons [stack x] (Cons. x stack false))
  (head [stack] head)
  (tail [stack] tail)
  Object
  (equals [this that]
    (and (instance? Cons that)
         (= (.head this) (.head that))
         (= (.tail this) (.tail that))))
  Comparable
  (compareTo [this that]
    {:pre [(instance? Cons that)]}
    (if (not= (.head this) (.head that))
      (compare (.head this) (.head that))
      (compare (.tail this) (.tail that)))))

(defn cons-stack
  ; sentinel - empty list
  ([] (->Cons nil nil true))
  ; normal usecase
  ([& xs] (reduce cons (cons-stack) xs)))

(defn concat [xs ys]
  (if (empty? xs) ys (cons (concat (tail xs) ys) (head xs))))

(defn suffixes [xs]
  (if (empty? xs) [xs] (cons (suffixes (tail xs)) xs)))
