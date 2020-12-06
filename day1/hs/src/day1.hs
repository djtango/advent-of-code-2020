import Data.Maybe
import Data.List
import Control.Category hiding ((.))

main :: IO()
main =
  getInput "/tmp/aoc1" >>= (tripleProduct >>> (find isSum2020) >>> fromJust >>> product >>> show >>> putStrLn)

crossProduct left right = sequence [left, right]
tripleProduct xs = sequence [xs, xs, xs]

isSum2020 xs = sum xs == 2020

getInput :: FilePath -> IO [Int]
getInput f = readFile f >>= return . lines >>= return . (read <$>)
