An experiment in implementing examples and exercises
from Chris Okasaki's Purely Functional Data Structures
in pure Clojure.

Although I mostly stick to function names selected in the book,
sometimes I modify them to follow Clojure naming conventions
(for example using `empty?` over `isEmpty`.)

## Chapter 2

### Data structures

- [Stack definition](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/stack.clj#L6-L10)
    - [built-in (vector) implementation](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/stack.clj#L12-L17)
    - [Cons implementation](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/stack.clj#L19-L35)
- [FiniteMap definition](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/map.clj)
    - [UnbalancedBST implementation](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/unbalanced_bst.clj#L10-L76)
- [Set definition](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/set.clj)
    - [UnbalancedBST implementation](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/unbalanced_bst.clj#L77-L85)

### Exercises

- [Exercise 2.1](https://github.com/ir-regular/okasaki-clj/commit/2c011990a30ad12fd1e637657caa20ffa9a4ba3b)
- [Exercise 2.2](https://github.com/ir-regular/okasaki-clj/commit/e1102c71c92976cf5bb90f352c1fc6fde11f3e3f)
- [Exercise 2.3](https://github.com/ir-regular/okasaki-clj/commit/cbc53f4359e2f1c0b729a595b78c887a15d2caa4)
- [Exercise 2.4](https://github.com/ir-regular/okasaki-clj/commit/42ba295ee09905e2b091dfa78e883e020c1e7f09)
- [Exercise 2.5.a](https://github.com/ir-regular/okasaki-clj/commit/925bb01ee155da4f4c86232b51f9ed067672069a)
- [Exercise 2.5.b](https://github.com/ir-regular/okasaki-clj/commit/f4853668b48a16f4d06da19437c5390bc42fbe0a)
- [Exercise 2.6](https://github.com/ir-regular/okasaki-clj/commit/238b9ba4deeff3d2dbf97bdd79ff9474bd6ead82)

## Chapter 3

### Data structures

- [Heap definition](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/heap.clj#L4-L15)
    - [LeftistHeap implementation](https://github.com/ir-regular/okasaki-clj/blob/master/src/okasaki_clj/leftist_heap.clj#L14-L81)

### Exercises

- [Exercise 3.2](https://github.com/ir-regular/okasaki-clj/commit/f7214cdfd9d3f32e8ba84dfc5d8acdeb84ce7074)
- [Exercise 3.3](https://github.com/ir-regular/okasaki-clj/commit/96f4727577a2af577a365324c04b1849226eb671)
