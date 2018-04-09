package capslock.fixer.util;

import capslock.game_info.GameDocument;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * ディレクトリ内のゲーム情報をかき集めるクラス
 */
public class RecursiveCollector {
    Map<GameDocument, Path> collectedGame = new HashMap<>();

}
