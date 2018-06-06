(ns okasaki-clj.stack-test
  (:require [clojure.test :refer :all]
            [okasaki-clj.stack :refer :all]))

(deftest vector-stack-test
  (testing "Vector based Stack implementation"
    (are [res test] (= res test)
                    true (empty? [])
                    false (empty? [1])
                    1 (head [1])
                    2 (head [1 2])
                    [] (tail [1])
                    [1] (tail [1 2])
                    [1 2] (cons [1] 2))))

(deftest cons-stack-test
  (testing "Custom Cons type based Stack implementation"
    (are [res test] (= res test)
                    ; Stack protocol implemented
                    true (empty? (cons-stack))
                    false (empty? (cons-stack 1))
                    1 (head (cons-stack 1))
                    2 (head (cons-stack 1 2))
                    ; ...also able to check Cons for equality
                    (cons-stack) (tail (cons-stack 1))
                    (cons-stack 1) (tail (cons-stack 1 2))
                    (cons-stack 1 2) (cons (cons-stack 1) 2))))

(deftest cons-stack-nils-test
  (testing "Can store nils on Cons-based Stacks"
    (is (not (empty? (cons-stack nil))))
    (is (= (cons-stack 1 nil) (cons (cons-stack 1) nil)))
    (is (not= (cons-stack nil) (cons-stack)))))

(deftest cons-stack-comparison-test
  (testing "Comparison of Cons-based Stacks"
    (are [res test] (= res test)
                    0 (compare (cons-stack 1) (cons-stack 1))
                    1 (compare (cons-stack 1 2) (cons-stack 1))
                    -1 (compare (cons-stack 1) (cons-stack 1 2))
                    1 (compare (cons-stack 1 2) (cons-stack 1 1))
                    -1 (compare (cons-stack 1 2) (cons-stack 1 3)))))

(deftest concat-vectors-test
  (testing "Concat of vector-based Stacks"
    (are [res test] (= res test)
                    [3 4 1 2] (concat [1 2] [3 4])
                    [1 2] (concat [1 2] [])
                    [1 2] (concat [] [1 2]))))

(deftest concat-conses-test
  (testing "Concat of Cons-based Stacks"
    (are [res test] (= res test)
                    (cons-stack 3 4 1 2) (concat (cons-stack 1 2) (cons-stack 3 4))
                    (cons-stack 1 2) (concat (cons-stack 1 2) (cons-stack))
                    (cons-stack 1 2) (concat (cons-stack) (cons-stack 1 2)))))
