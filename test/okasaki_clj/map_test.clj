(ns okasaki-clj.map-test
  (:refer-clojure :exclude [empty? contains?])
  (:require [clojure.test :refer :all]
            [okasaki-clj.map :refer :all]
            [okasaki-clj.unbalanced-bst :refer [unbalanced-map]]))

; test UnbalancedBST implementation of FiniteMap protocol

(deftest unbalanced-map-empty
  (testing "UnbalancedBST recognises emptiness"
    (is (empty? (unbalanced-map)))
    (is (not (empty? (unbalanced-map [[:key :value]]))))))

(deftest unbalanced-map-lookup
  (testing "UnbalancedBST looks up values by key"
    (is (= :bar (lookup (unbalanced-map [[1 :foo] [2 :bar] [3 :baz]]) 2)))
    (is (= :foo (lookup (unbalanced-map [[1 :foo] [2 :bar] [3 :baz]]) 1)))
    (is (thrown? IndexOutOfBoundsException (lookup (unbalanced-map [[1 :foo] [2 :bar] [3 :baz]]) 4)))
    ; empty set does not contain elements
    (is (thrown? IndexOutOfBoundsException (lookup (unbalanced-map) 1)))
    ; and also, specifically, does not contain nil
    (is (thrown? IndexOutOfBoundsException (lookup (unbalanced-map) nil)))))

(deftest unbalanced-set-bind
  (testing "UnbalancedBST inserts new members"
    (is (= :foo (lookup (bind (unbalanced-map) 1 :foo) 1)))
    ; cheating: unbalanced-map reduces arg using bind
    (is (= :bar (lookup (bind (unbalanced-map [[1 :foo]]) 2 :bar) 2)))))

(deftest unbalanced-set-equals
  (testing "Equality checks between instances of UnbalancedBST used as a Map"
    (is (= (unbalanced-map) (unbalanced-map)))
    (is (= (unbalanced-map [[1 :foo] [2 :bar]]) (unbalanced-map [[1 :foo] [2 :bar]])))
    (is (not= (unbalanced-map [[1 :foo] [2 :bar]]) (unbalanced-map [[1 :foo] [3 :baz]])))))
