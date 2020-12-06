import Data.Maybe

main :: IO()
main =
  getInput "/tmp/aoc1" >>= (\ xs ->
    putStrLn $ extractValue $ filter isJust $ find2020Product <$> tripleProduct xs xs xs)


input = [1721,979,366,299,675,1456]

crossProduct left right = sequence [left, right]
tripleProduct left middle right = sequence [left, middle, right]

find2020Product xs = if (sum xs) == 2020 then Just $ product xs else Nothing
extractValue = show . fromJust . head

getInput :: FilePath -> IO [Int]
getInput f = readFile f >>= return . lines >>= return . (read <$>)
