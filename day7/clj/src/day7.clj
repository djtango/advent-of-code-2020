(ns day7
  (:require [clojure.alpha.spec :as s]
            [clojure.string :as str]))

(defn- ?str->int [s]
  (try (Integer/parseInt s)
       (catch NumberFormatException _ nil)))

(defn- with-separator [words]
  (->> (for [w words s ["" "," "."]]
         (str w s))
       (into #{})))

(def -bags (with-separator #{"bag" "bags"}))
(s/def ::bags -bags)
(s/def ::word (s/and string? #(not (str/blank? %)) #(not (str/includes? % " ")) #(not (contains? -bags %))))
(s/def ::quantity (s/and string? ?str->int))
(s/def ::colour (s/+ ::word))
(s/def ::amount-of-bags (s/cat :n ::quantity :c ::colour :bags ::bags))

(s/def ::end (s/cat :no #{"no"} :other #{"other"} :bags #{"bags."}))
(s/def ::bag-stuff (s/alt :bags (s/+ ::amount-of-bags) :end ::end))

(s/def ::contain #{"contain"})

(s/def ::rule (s/cat :c ::colour :bags ::bags :- ::contain :rule ::bag-stuff))

(defn parse [rules]
  (let [get-rule (comp :rule)
        ->rule (juxt :c get-rule)
        normalise (fn [rule]
                    (-> rule
                        (update 1 #(if (= :bags (first %))
                                     (second %)
                                     []))))
        clean-colours (fn [[k v]]
                        [(str/join " " k)
                         (map (fn [c]
                                (update c :c #(str/join " " %))) v)])]
    (->> rules
         (map #(clojure.string/split % #" "))
         (map #(s/conform ::rule %) )
         (map ->rule)
         (map normalise)
         (map clean-colours)
         (into {}))))

(defn colour-links [rules]
  (->> rules
       (map (fn [[k v]] [k (into #{} (map :c v))]))))

(defn traverse [rules]
  (letfn [(trace [c']
            (if (empty? (get rules (:c c')))
              [c']
              (into [c'] (mapv trace (get rules (:c c'))))))]
    (let [trace* (memoize trace)]
      (reduce
       (fn [nodes colour]
         (assoc nodes
                colour
                (trace* {:c colour})))
       {}
       (keys rules)))))

(defn mapm [kf vf m]
  (->> m
       (map (juxt kf vf))
       (into {})))

(defn part1 []
  (->> (slurp "/tmp/aoc7")
       str/split-lines
       parse
       traverse
       (mapm key (comp flatten val))
       (mapm key (comp (partial map :c) val))
       (filter (comp #(contains? (set %) "shiny gold") val))
       count
       dec))

(defn walk-tree [t]
  (mapv (fn [x]
          (if (map? x)
            (?str->int (:n x))
            (walk-tree x))) t))

(defn sum-tree [t]
  (* (first t)
     (reduce (fn [acc x]
               (+ acc (sum-tree x)))
             1
             (rest t))))

(defn part2 []
  (->> (slurp "/tmp/aoc7")
       str/split-lines
       parse
       traverse
       ((fn [x]
          (get x "shiny gold")))
       walk-tree
       ((fn [x] (assoc x 0 1)) )
       sum-tree
       dec)) ;; TODO I don't know why there is an off by one lol


(defn -main []
  (println "foo"))
