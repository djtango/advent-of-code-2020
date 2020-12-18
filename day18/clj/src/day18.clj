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
  (loop [xs not-sexp cnt 0]
    (let [[e1 op e2 & rst] xs]
      (if (or (= 3 (count xs)) (> cnt 100))
        (flip xs)
        (recur (concat (list (list op e1 e2))
                       rst)
               (inc cnt))))))
;; (->sexp '(8 * 3 + 9 + 3 * 4 * 3))
;; => '(* (* (+ (+ (* 8 3) 9) 3) 4) 3)

(defn rearrange [line]
  (walk/postwalk
    (fn [x]
      (if (seq? x)
        (->sexp x)
        x))
    line))

(defn prioritise-+ [expr]
  (loop [xs expr result []]
    (let [[e1 op e2 & rst] xs]
      (cond
        (= 3 (count xs))
        (seq
          (cond
            (empty? result)
            xs

            (= '* op)
            (conj result e1 op e2)
            :else (conj result (list e1 op e2))))

        (= '* op)
        (recur (cons e2 rst)
               (conj result e1 op))
        :else
        (recur (cons (list e1 op e2) rst)
               result)))))

;; (prioritise-+ '(8 * 3 + 9 + 3 * 4 * 3))
;; => '(* (* (* 8 (+ (+ 3 9) 3)) 4) 3)
;; => '(8 * ((3 + 9) + 3) * 4 * 3)
;; (prioritise-+ '(1 + (2 * 3) + (4 * (5 + 6))))
;; '((1 + (2 * 3)) + (4 * (5 + 6)))
;; (prioritise-+ '(2 * 3 + (4 * 5)))



(defn order-precedence [line]
  (walk/postwalk
    (fn [x]
      (println x)
      (cond
        (and (seq? x) (= 3 (count x)))
        x

        (seq? x)
        (prioritise-+ x)

        :else
        x))
    line))

(comment
  (do
    (assert
      (= (order-precedence '(1 + (2 * 3) + (4 * (5 + 6))))
         '((1 + (2 * 3)) + (4 * (5 + 6)))))

    (assert
      (= (order-precedence '(2 * 3 + (4 * 5)))
         '(2 * (3 + (4 * 5)))))))

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

(defn part2 [i]
  (->> (get-input)
       parse-input
       (map order-precedence)
       (map rearrange)
       (map eval)
       (reduce +)))

;; (prioritise-+ '((1 + (2 * 3)) + (4 * (5 + 6))))
;; => (eval (rearrange (order-precedence '(((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2))))
;;=> (rearrange '((((((2 + 4) * 9) * (((6 + 9) * (8 + 6)) + 6)) + 2) + 4) * 2))


(comment
  (part2 ['(1 + (2 * 3) + (4 * (5 + 6)))
          '(2 * 3 + (4 * 5))
          '(5 + (8 * 3 + 9 + 3 * 4 * 3))
          '(5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)))
          '(((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2)]))
