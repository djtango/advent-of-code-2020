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

(comment (s/exercise ::word))

(s/def ::quantity (s/and string? ?str->int))
(comment (s/exercise ::quantity))

(s/def ::colour (s/+ ::word))
(comment
  (s/exercise ::colour))
(s/def ::amount-of-bags (s/cat :n ::quantity :c ::colour :bags ::bags))
(comment (try (s/exercise ::colour)
              (catch Throwable t (.printStackTrace t))))


(s/def ::end (s/cat :no #{"no"} :other #{"other"} :bags #{"bags."}))
(s/def ::bag-stuff (s/alt :bags (s/+ ::amount-of-bags) :end ::end))

(comment (s/exercise ::bag-stuff))
(s/def ::contain #{"contain"})

(comment
  ;; makes exercising awkward
  (defn cat-of
   "Make or spec where tags match names"
   [& spec-names]
   (let [tag-names (->> spec-names (map name) (map keyword))]
     (s/resolve-spec (cons `s/cat (interleave tag-names spec-names)))))
  (def -rule (cat-of ::colour ::contain ::bag-stuff)))

(s/def ::rule (s/cat :c ::colour :bags ::bags :- ::contain :rule ::bag-stuff))
(def rules
  ["light red bags contain 1 bright white bag, 2 muted yellow bags."
   "dark orange bags contain 3 bright white bags, 4 muted yellow bags."
   "bright white bags contain 1 shiny gold bag."
   "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags."
   "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
   "dark olive bags contain 3 faded blue bags, 4 dotted black bags."
   "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags."
   "faded blue bags contain no other bags."
   "dotted black bags contain no other bags."])

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

(defn c [x]
  (println (count x))
  x)

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
       ))

(comment (clojure.pprint/pprint (count (keys (part1)))))

(comment
  (time (traverse (parse rules))))
  (traverse
   {:c1 #{:c2 :c3}
    :c2 #{:c4}
    :c3 #{}
    :c4 #{}}
   )
  {:c1 [:c1 [:c2 [:c4]] [:c3]]
   :c2 [:c2 [:c4]]
   :c3 [:c3]
   :c4 [:c4]}
  )

(comment (clojure.pprint/pprint (s/exercise ::rule)))

(defn -main []
  (println "foo"))
[#{nil ["plaid" "brown"] ["shiny" "gold"] ["drab" "beige"]
   ["dull" "lime"]}
 #{nil ["muted" "gold"] ["shiny" "gold"] ["bright" "red"]}
 #{nil ["muted" "tomato"] ["shiny" "gold"] ["striped" "chartreuse"]}
 #{nil ["striped" "crimson"] ["shiny" "gold"] ["striped" "red"]}
 #{nil ["light" "silver"] ["shiny" "gold"] ["dark" "red"]
   ["faded" "yellow"]}
 #{nil ["mirrored" "olive"] ["shiny" "gold"]}
 #{nil ["mirrored" "lavender"] ["vibrant" "indigo"] ["shiny" "gold"]}
 #{nil ["clear" "cyan"] ["vibrant" "yellow"] ["shiny" "gold"]
   ["posh" "gray"]}
 #{nil ["dark" "tomato"] ["shiny" "gold"] ["dim" "orange"]
   ["plaid" "chartreuse"]}]
