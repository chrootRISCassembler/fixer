package capslock.fixer.command;

import capslock.game_info.GameDocument;

import java.util.List;
import java.util.stream.Collectors;

public class Unregister extends Command {
    @Override
    public boolean run(String line) {
        if(gameList == null) {
            System.err.println("ゲーム情報が読み込まれていません.先にattachしてください");
            return false;
        }

        if(gameList.isEmpty()){
            System.err.println("読み込まれているゲームのドキュメントが一つもありません.");
            return false;
        }

        final String[] wordArray = line.split(" ");

        if(wordArray.length < 2){
            System.err.println("オペランドがありません.");
            System.out.println("使い方 : Unregister UUID");
            return false;
        }

        final List<GameDocument> hitGames = gameList.parallelStream()
                .filter(doc -> doc.getUUID().toString().startsWith(wordArray[1]))
                .collect(Collectors.toList());

        if(hitGames.size() != 1){
            System.err.println("ゲームを一意に特定のできませんでした. UUIDを短縮せずに指定してみて下さい.");
            hitGames.forEach(game -> System.out.println("UUID : " + game.getUUID()));
            return false;
        }


        System.out.println("UUID : " + hitGames.get(0).getUUID() + "　の登録情報を削除しました.");
        gameList.remove(hitGames.get(0));

        return true;
    }
}
