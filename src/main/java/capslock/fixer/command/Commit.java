package capslock.fixer.command;

import capslock.game_info.JSONDBWriter;

import java.io.IOException;

public class Commit extends Command {
    @Override
    public boolean run(String line) {
        try {
            final var writer = new JSONDBWriter(jsonDBFile);
            gameList.forEach(writer::add);
            writer.flush();
            return true;
        }catch (IOException ex){
            System.err.println("ファイル書き出しに失敗しました.");
            ex.printStackTrace();
            return false;
        }
    }
}
