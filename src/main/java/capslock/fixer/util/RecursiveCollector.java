package capslock.fixer.util;

import capslock.game_info.GameDocument;
import capslock.game_info.JSONDBReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * ディレクトリ内のゲーム情報をかき集めるクラス
 * <p>
 *     例えばGames/の中にA_game/, B_game/, C_game/というゲームがあり,それぞれのゲームディレクトリの中に.signature.json
 *     が存在するとき,ゲーム情報をkey,ゲームのルートディレクトリをvalueとするMapを作る.
 * </p>
 */
public class RecursiveCollector {
    private final Map<GameDocument, Path> collectedGame;

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
    public RecursiveCollector(Path gamesDir) throws IOException, SecurityException{
        final Map<GameDocument, Path> modifiableMap = new HashMap<>();

        try(final Stream<Path> lsStream = Files.list(gamesDir)) {
            lsStream.parallel()
                    .map(gameRootDir -> {
                        try {
                            return new Tuple<>(new JSONDBReader(Paths.get(gameRootDir + "/.signature.json")), gameRootDir);
                        }catch (IllegalArgumentException | IOException ex){
                            System.err.println(" * Failed to read " + gameRootDir + "\t[NG]");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(tuple -> new Tuple<>(tuple.x.getDocumentList().get(0), tuple.y))
                    .peek(tuple -> System.out.println("* Got the signature " + tuple.y + "\t[OK]"))
                    .forEach(tuple -> modifiableMap.put(tuple.x, tuple.y));
        }catch (IOException | SecurityException ex){
            System.err.println("ファイルの収集中に例外が発生しました.");
            ex.printStackTrace();
            throw ex;
        }

        collectedGame = Collections.unmodifiableMap(modifiableMap);
    }

    /**
     * 読み取ったゲームに対してforEachする.
     * @param action 各エントリに対して実行するアクション.
     */
    public final void forEach(BiConsumer<GameDocument, Path> action){
        collectedGame.forEach(action);
    }
}
