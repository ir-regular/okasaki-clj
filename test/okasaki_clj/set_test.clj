(ns okasaki-clj.set-test
  (:refer-clojure :exclude [empty? contains?])
  (:require [clojure.test :refer :all]
            [okasaki-clj.set :refer :all]
            [okasaki-clj.unbalanced-bst :refer :all])
  (:import (okasaki_clj.unbalanced_bst UnbalancedBST)))

; test UnbalancedBST implementation of Set protocol

(deftest unbalanced-set-empty
  (testing "UnbalancedBST recognises emptiness"
    (is (empty? (unbalanced-set)))
    (is (not (empty? (unbalanced-set [1]))))))

(deftest unbalanced-set-contains
  (testing "UnbalancedBST checks membership"
    (is (contains? (unbalanced-set [1 2 3]) 2))
    (is (contains? (unbalanced-set [1 2 3]) 1))
    (is (not (contains? (unbalanced-set [1 2 3]) 4)))
    ; empty set does not contain elements
    (is (not (contains? (unbalanced-set) 1)))
    ; and also, specifically, does not contain nil
    (is (not (contains? (unbalanced-set) nil)))))

(deftest unbalanced-set-insert
  (testing "UnbalancedBST inserts new members"
    (is (contains? (insert (unbalanced-set) 1) 1))
    ; cheating: unbalanced-set reduces arg using insert
    (is (contains? (insert (unbalanced-set [1 2]) 3) 3))))

(deftest unbalanced-set-equals
  (testing "Equality checks between instances of UnbalancedBST used as a Set"
    (is (= (unbalanced-set) (unbalanced-set)))
    (is (= (unbalanced-set [1 2 3]) (unbalanced-set [1 2 3])))
    (is (not= (unbalanced-set [1 2]) (unbalanced-set [2 3])))))

(deftest unbalanced-set-insert-unique
  (testing "UnbalancedBST insertion only adds unique elements"
    (is (let [t (insert (unbalanced-set [1]) 1)]
          (and (contains? t 1)
               (every? nil? [(.-left ^UnbalancedBST t) (.-right ^UnbalancedBST t)]))))))

(deftest unbalanced-set-nil
  (testing "UnbalancedBST does not allow to insert nil"
    (is (thrown? IllegalArgumentException (insert (unbalanced-set) nil)))))