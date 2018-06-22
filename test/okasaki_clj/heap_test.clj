(ns okasaki-clj.heap-test
  (:refer-clojure :exclude [empty? merge])
  (:require [clojure.test :refer :all]
            [okasaki-clj.heap :refer :all]))

(deftest nil-heap
  (testing "Behaviour of nil as heap"
    (is (empty? nil))
    (is (nil? (merge nil nil)))))
