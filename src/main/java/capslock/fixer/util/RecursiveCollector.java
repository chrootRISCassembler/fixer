package capslock.fixer.util;

import capslock.game_info.GameDocument;
import capslock.game_info.JSONDBReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * ディレクトリ内のゲーム情報をかき集めるクラス
 * <p>
 *     例えばGames/の中にA_game/, B_game/, C_game/というゲームがあり,それぞれのゲームディレクトリの中に.signature.json
 *     が存在するとき,ゲーム情報をkey,ゲームのルートディレクトリをvalueとするMapを作る.
 * </p>
 */
public class RecursiveCollector {
    Map<GameDocument, Path> collectedGame = new HashMap<>();

    static private final class Tuple<X, Y>{
        private final X x;
        private final Y y;
        Tuple(X x, Y y){
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 唯一のコンストラクタ.
     * @param gamesDir Gamesディレクトリか,それに相当するディレクトリ.
     */
    public RecursiveCollector(Path gamesDir){
        try(final Stream<Path> lsStream = Files.list(gamesDir)) {
            lsStream.parallel()
                    .map(gameRootDir -> {
                        try {
                            return new Tuple<>(gameRootDir, new JSONDBReader(Paths.get(gameRootDir + "/.signature.json")));
                        }catch (IllegalArgumentException | IOException ex){
                            System.err.println(" * Failed to read " + gameRootDir + "\t[NG]");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(tuple -> new Tuple<>(tuple.x, tuple.y.getDocumentList().get(0)))
                    .peek(tuple -> System.out.println("* Got the signature " + tuple.x + "\t[OK]"))
                    .forEach(tuple -> collectedGame.put(tuple.y, tuple.x));
        }catch (IOException | SecurityException ex){
            System.err.println("ファイルの収集中に例外が発生しました.");
            ex.printStackTrace();
            return false;
        }
    }

}
