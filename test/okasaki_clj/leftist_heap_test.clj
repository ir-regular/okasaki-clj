(ns okasaki-clj.leftist-heap-test
  (:refer-clojure :exclude [empty? merge])
  (:require [clojure.test :refer :all]
            [okasaki-clj.heap :refer :all]
            [okasaki-clj.leftist-heap :refer :all]))

(deftest empty-heap
  (testing "Behaviour of empty heap"
    (is (empty? (leftist-heap)))
    (is (not (empty? (leftist-heap [1]))))
    (is (thrown? IndexOutOfBoundsException (find-min (leftist-heap))))
    (is (thrown? IndexOutOfBoundsException (delete-min (leftist-heap))))
    (is (= (leftist-heap) (merge (leftist-heap) (leftist-heap))))))

(deftest building-heap
  (testing "Building heap through merge and insert"
    (is (= (leftist-heap [1 2]) (insert (insert (leftist-heap) 2) 1)))
    (is (= (leftist-heap [1 2 3 4]) (merge (leftist-heap [1 2]) (leftist-heap [3 4]))))
    (is (= (leftist-heap [1]) (merge (leftist-heap [1]) nil)))
    (is (= (leftist-heap [1]) (merge (leftist-heap [1]) (leftist-heap))))))

(deftest min-heap-check
  (testing "Finding and removing minimum of a heap"
    (is (= 1 (find-min (leftist-heap [1]))))
    (is (= 0 (find-min (leftist-heap (range 20)))))
    (is (empty? (delete-min (leftist-heap [1]))))
    (is (= 1 (find-min (delete-min (leftist-heap (range 20))))))))