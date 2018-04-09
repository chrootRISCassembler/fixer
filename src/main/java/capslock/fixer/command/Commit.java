package capslock.fixer.command;

import capslock.game_info.JSONDBWriter;

import java.io.IOException;

public class Commit extends Command {
    @Override
    public boolean run(String line) {
        try {
            final var writer = new JSONDBWriter(jsonDBFile);
            System.out.println("ファイルへの書き込みを開始します...");
            gameList.forEach(writer::add);
            writer.flush();
            System.out.println(gameList.size() + "件のゲーム情報を書き込みました.");
            return true;
        }catch (IOException ex){
            System.err.println("ファイル書き出しに失敗しました.");
            ex.printStackTrace();
            return false;
        }
    }
}
