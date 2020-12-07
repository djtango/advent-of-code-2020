(ns day6
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


(s/def ::bag-stuff (s/alt :bags (s/+ ::amount-of-bags) :end #{"no other bags."}))

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

(s/def ::rule (s/cat :c ::colour :- ::contain :rule ::bag-stuff))
(comment (clojure.pprint/pprint (s/exercise ::rule)))

(defn -main []
  (println "foo"))
