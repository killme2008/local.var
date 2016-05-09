(ns local.var-test
  (:require [clojure.test :refer :all]
            [local.var :refer :all]))

(deftest test-transient-var
  (testing "create/get/reset! local-transient var."
    (let [x (transient-var! 1)]
      (is (= 1 @x))
      (is (= 1 (deref x)))
      (is (= 2 (ltv-swap! x inc)))
      (is (= 2 @x))
      (is (= 3 (ltv-swap! x inc)))
      (is (= 3 @x))
      (is (= 13 (ltv-swap! x + 10)))
      (is (= 13 @x))
      (is (= 34 (ltv-swap! x + 10 11)))
      (is (= 34 @x))
      (is (= 80 (ltv-swap! x + 10 11 12 13)))
      (is (= 80 @x))
      (ltv-reset! x 100)
      (is (= 100 @x))
      (ltv-reset! x 200)
      (is (= 200 @x))
      (is (transient-var? x))
      (is (instance? IllegalAccessError
                     (deref
                      (future (try (ltv-swap! x inc)
                                   (catch Throwable t
                                     t))))))
      (is (instance? IllegalAccessError
                     (deref
                      (future (try (ltv-reset! x 300)
                                   (catch Throwable t
                                     t))))))
      (is (not (transient-var? 1)))
      (is (not (transient-var? "hello world"))))))
