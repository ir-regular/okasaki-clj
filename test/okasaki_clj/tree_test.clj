(ns okasaki-clj.tree-test
  (:require [clojure.test :refer :all]
            [okasaki-clj.tree :refer :all]))

(deftest build-complete-tree
  (testing "Building complete tree"
    ; can't provide a non-positive int depth
    (is (thrown? IllegalArgumentException (complete :x 0)))
    ; builds a complete tree as expected
    (is (= (->Tree :x nil nil) (complete :x 1)))
    (is (= (->Tree :x (->Tree :x nil nil) (->Tree :x nil nil)) (complete :x 2)))
    ; left and right branches of a node contain the same object
    (is (let [t (complete :x 2)] (identical? (:left t) (:right t))))))