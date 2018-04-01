package capslock.fixer.command;

import capslock.game_info.GameDocument;

import java.util.List;
import java.util.stream.Collectors;

public class Unregister extends Command {
    @Override
    public boolean run(String line) {
        if(console.getDocumentList() == null){
            console.out("ゲーム情報がロードされていません. attachコマンドでロードしてください.");
            return false;
        }

        final String[] wordArray = line.trim().split(" ");

        if(wordArray.length < 2){
            console.out("オペラントがありません.");
            console.out("使い方 : Unregister UUID ...");
            return false;
        }

        final List<GameDocument> hitGames = console.getDocumentList().parallelStream()
                .filter(doc -> doc.getUUID().toString().startsWith(wordArray[1]))
                .collect(Collectors.toList());

        if(hitGames.size() != 1){
            console.out("ゲームを一意に特定のできませんでした. UUIDを短縮せずに指定してみて下さい.");
            hitGames.forEach(game -> console.out("UUID : " + game.getUUID()));
            return false;
        }


        console.out("UUID : " + hitGames.get(0).getUUID() + "　の登録情報を削除しました.");
        console.getDocumentList().remove(hitGames.get(0));

        return true;
    }
}
