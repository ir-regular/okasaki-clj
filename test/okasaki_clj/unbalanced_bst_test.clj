(ns okasaki-clj.unbalanced-bst-test
  (:refer-clojure :exclude [empty? contains?])
  (:require [clojure.test :refer :all]
            [okasaki-clj.unbalanced-bst :refer [->MapEntry]]))

; Test everything from unbalanced-bst that isn't being tested elsewhere
; (...the leftover being two ways to test MapEntry equality.)

(deftest equality-key-value-pairs
  (testing "Equality checks key and value"
    (is (= (->MapEntry :key :value) (->MapEntry :key :value)))
    (is (not= (->MapEntry :key :value) (->MapEntry :key :other-value)))))

(deftest comparison-key-value-pairs
  (testing "Comparison compares only keys"
    ; this is "like" equality, but not
    (is (zero? (compare (->MapEntry 1 :value) (->MapEntry 1 :other-value))))
    ; ...and comparison works as you would expect
    (is (neg? (compare (->MapEntry 1 :value) (->MapEntry 2 :value))))))