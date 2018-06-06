(ns okasaki-clj.set-test
  (:refer-clojure :exclude [empty? contains?])
  (:require [clojure.test :refer :all]
            [okasaki-clj.set :refer :all]))

(deftest unbalanced-set-test
  (testing "UnbalancedSet implements protocol Set using a binary search tree structure"
    ; checks emptiness
    (is (empty? (unbalanced-set)))
    (is (not (empty? (unbalanced-set [1]))))
    ; checks member existence
    (is (contains? (unbalanced-set [1 2 3]) 1))
    (is (not (contains? (unbalanced-set [1 2 3]) 4)))
    ; inserts members
    (is (contains? (insert (unbalanced-set) 1) 1))
    (is (contains? (insert (unbalanced-set [1 2]) 3) 3))
    ; and by the way, make sure equality checks work
    (is (= (unbalanced-set) (unbalanced-set)))
    (is (= (unbalanced-set [1 2 3]) (unbalanced-set [1 2 3])))
    (is (not= (unbalanced-set [1 2]) (unbalanced-set [2 3])))))

(deftest unbalanced-set-unique-test
  (testing "UnbalancedSet insertion only adds unique elements"
    (is (let [t (insert (unbalanced-set [1]) 1)]
          (and (contains? t 1)
            (every? nil? [(.left t) (.right t)]))))))
