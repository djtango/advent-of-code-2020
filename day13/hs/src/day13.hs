main :: IO()
main =
  let input = getInput "/tmp/aoc13"
      firstLine :: IO(Int)
      firstLine = input >>= return . read . head
      secondLine :: IO([Int])
      secondLine = input >>= return . (read <$>) . filter (/= "x") . splitCommas . head . tail
  in
    do startTime <- firstLine
       busIds <- secondLine
       putStrLn $ show $ answer startTime $ earliestBus $ zip busIds $ (earliestDeparture startTime) <$> busDepartures <$> busIds

answer startTime (id, busTime) = (busTime - startTime) * id
earliestBus departures =
  let minBus nxt curr = if snd curr > snd nxt then nxt
                        else curr
  in foldr minBus (head departures) (tail departures)

earliestDeparture startTime = head . (dropWhile (startTime >))
busDepartures id = filter ((0 ==) . (`mod` id)) integers
integers = [0..]

getInput :: FilePath -> IO [String]
getInput f = readFile f >>= return . lines


splitCommas :: String -> [String]
splitCommas s = case dropWhile (== ',') s of
                  "" -> []
                  s' -> w : splitCommas s''
                        where (w, s'') = break (==',') s'
