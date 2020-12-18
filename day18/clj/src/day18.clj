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

(defn ->sexp [not-sexp]
  (loop [xs not-sexp]
    (let [[e1 op e2 & rst] xs]
      (if (= 3 (count xs))
        (flip xs)
        (recur (concat (list (list op e1 e2))
                       rst))))))
;; (->sexp '(8 * 3 + 9 + 3 * 4 * 3))
;; => '(* (* (+ (+ (* 8 3) 9) 3) 4) 3)

(defn rearrange [line]
  (walk/postwalk
    (fn [x]
      (if (seq? x)
        (->sexp x)
        x))
    line))

;; (rearrange '(8 * 3 + 9 + 3 * 4 * 3))
;; (rearrange '(5 + (8 * 3 + 9 + 3 * 4 * 3)))
;; (rearrange '(((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2))

(defn get-input []
  (slurp "/tmp/aoc18"))


(defn part1 []
  (->> (get-input)
       parse-input
       (map rearrange)
       (map eval)
       (reduce +)))
