(ns okasaki-clj.set-test
  (:refer-clojure :exclude [empty? contains?])
  (:require [clojure.test :refer :all]
            [okasaki-clj.set :refer :all]))

(deftest unbalanced-set-empty
  (testing "UnbalancedSet recognises emptiness"
    (is (empty? (unbalanced-set)))
    (is (not (empty? (unbalanced-set [1]))))))

(deftest unbalanced-set-contains
  (testing "UnbalancedSet checks membership"
    ; checks member existence
    (is (contains? (unbalanced-set [1 2 3]) 2))
    (is (contains? (unbalanced-set [1 2 3]) 1))
    (is (not (contains? (unbalanced-set [1 2 3]) 4)))))

(deftest unbalanced-set-insert
  (testing "UnbalancedSet inserts new members"
    (is (contains? (insert (unbalanced-set) 1) 1))
    (is (contains? (insert (unbalanced-set [1 2]) 3) 3))))

(deftest unbalanced-set-equals
  (testing "Equality checks between instances of UnbalancedSet"
    (is (= (unbalanced-set) (unbalanced-set)))
    (is (= (unbalanced-set [1 2 3]) (unbalanced-set [1 2 3])))
    (is (not= (unbalanced-set [1 2]) (unbalanced-set [2 3])))))

(deftest unbalanced-set-insert-unique
  (testing "UnbalancedSet insertion only adds unique elements"
    (is (let [t (insert (unbalanced-set [1]) 1)]
          (and (contains? t 1)
            (every? nil? [(.left t) (.right t)]))))))
