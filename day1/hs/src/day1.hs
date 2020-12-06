import Data.Maybe
import Data.List

main :: IO()
main =
  getInput "/tmp/aoc1" >>= putStrLn . show . product . fromJust . (find isSum2020) . tripleProduct

crossProduct left right = sequence [left, right]
tripleProduct xs = sequence [xs, xs, xs]

isSum2020 xs = sum xs == 2020

getInput :: FilePath -> IO [Int]
getInput f = readFile f >>= return . lines >>= return . (read <$>)
