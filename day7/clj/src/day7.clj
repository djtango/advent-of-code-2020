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
                                     []))))]
    (->> rules
       (map #(clojure.string/split % #" "))
       (map #(s/conform ::rule %) )
       (map ->rule)
       (map normalise)
       (into {}))))

(comment
  (parse rules)
  )

(comment (clojure.pprint/pprint (s/exercise ::rule)))

(defn -main []
  (println "foo"))
