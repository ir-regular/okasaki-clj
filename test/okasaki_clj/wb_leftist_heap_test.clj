(ns okasaki-clj.wb-leftist-heap-test
  (:refer-clojure :exclude [empty? merge])
  (:require [clojure.test :refer :all]
            [okasaki-clj.heap :refer :all]
            [okasaki-clj.wb-leftist-heap :refer :all]))

(deftest empty-heap
  (testing "Behaviour of empty heap"
    (is (empty? (wb-leftist-heap)))
    (is (not (empty? (wb-leftist-heap [1]))))
    (is (thrown? IndexOutOfBoundsException (find-min (wb-leftist-heap))))
    (is (thrown? IndexOutOfBoundsException (delete-min (wb-leftist-heap))))
    (is (empty? (merge (wb-leftist-heap) (wb-leftist-heap))))))

(deftest building-heap
  (testing "Building heap through merge and insert"
    (are
      ; to-vector is a ^:private helper, but it's useful in tests as well
      [v h] (= v (#'okasaki-clj.wb-leftist-heap/to-vector h))
            [1 [2 [] []] [3 [] []]] (wb-leftist-heap [1 2 3])
            [1 [2 [3 [] []] []] []] (wb-leftist-heap [3 2 1])
            [1 [2 [] []] []] (insert (insert (wb-leftist-heap) 2) 1)
            [1 [2 [] []] [3 [4 [] []] []]] (insert (wb-leftist-heap [1 2 3]) 4)
            [1 [2 [] []] [3 [4 [] []] []]] (merge (wb-leftist-heap [1 2]) (wb-leftist-heap [3 4]))
            [1 [] []] (merge (wb-leftist-heap [1]) (wb-leftist-heap))
            [1 [1 [] []] []] (insert (insert (wb-leftist-heap) 1) 1)
            [1 [1 [2 [] []] [3 [] []]] []] (merge (wb-leftist-heap [1]) (wb-leftist-heap [1 2 3])))))

(deftest min-heap-check
  (testing "Finding and removing minimum of a heap"
    (is (= 1 (find-min (wb-leftist-heap [1]))))
    (is (= 0 (find-min (wb-leftist-heap (range 20)))))
    (is (empty? (delete-min (wb-leftist-heap [1]))))
    (is (= 1 (find-min (delete-min (wb-leftist-heap (range 20))))))
    (is (= 1 (find-min (delete-min (wb-leftist-heap [1 2 1])))))))

(deftest nil-heap
  (testing "Interaction of nil heap and LeftistHeap"
    (let [h (wb-leftist-heap [1 2])]
      (is (= h (merge nil h)))
      (is (= h (merge h nil))))))

; TODO: so... what's a good example of a weight-biased leftist heap that doesn't work
; as a leftist heap? or vice-versa?
