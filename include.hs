#!/usr/bin/env runhaskell
-- includes.hs
import Text.Pandoc.JSON
import Text.Pandoc
import Text.Pandoc.Error
import qualified Data.Text.IO as TIO
import qualified Data.Text as T
import Control.Monad

stripPandoc :: Either PandocError Pandoc -> [Block]
stripPandoc p =
  case p of
    Left _ -> []
    Right (Pandoc _ blocks) -> blocks

doInclude :: Block -> IO [Block]
doInclude cb@(CodeBlock (id, classes, namevals) contents) =
  case lookup (T.pack "include") namevals of
       Just f     -> do
          c <- TIO.readFile (T.unpack f)
          p <- runIO $ readMarkdown def c
          return $! stripPandoc p
       Nothing    -> return [cb]
doInclude x = return [x]

main :: IO ()
main = toJSONFilter doInclude