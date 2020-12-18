(ns day18
  (:require [clojure.string :as s]
            [clojure.walk :as walk]))

(defn parse-input [input]
  (->> input
       (s/split-lines)
       (map (fn [line]
              (str "(" line ")")))
       (map read-string)))

(defn flip [x]
  (list (nth x 1)
        (nth x 0)
        (nth x 2)))

(defn flippity [not-sexp]
  (loop [xs not-sexp]
    (let [[e1 op e2 & rst] xs]
      (if (or (= 3 (count xs)))
        (flip xs)
        (recur (concat (list (list op e1 e2))
                       rst))))))

(defn ->sexp [line]
  (walk/postwalk
    (fn [x]
      (if (seq? x)
        (flippity x)
        x))
    line))

(defn prioritise-+ [expr]
  (loop [xs expr result []]
    (let [[e1 op e2 & rst] xs]
      (cond
        (and (= 3 (count xs)) (empty? result))
        xs

        (and (= 3 (count xs)) (= '* op))
        (seq (conj result e1 op e2))

        (= 3 (count xs))
        (seq (conj result (list e1 op e2)))

        (= '* op)
        (recur (cons e2 rst)
               (conj result e1 op))
        :else
        (recur (cons (list e1 op e2) rst)
               result)))))

(defn order-precedence [line]
  (walk/postwalk
    (fn [x]
      (cond
        (and (seq? x) (= 3 (count x)))
        x

        (seq? x)
        (prioritise-+ x)

        :else
        x))
    line))

(defn get-input []
  (slurp "/tmp/aoc18"))

(defn part1 []
  (->> (get-input)
       parse-input
       (map ->sexp)
       (map eval)
       (reduce +)))

(defn part2 []
  (->> (get-input)
       parse-input
       (map order-precedence)
       (map ->sexp)
       (map eval)
       (reduce +)))
