/*
    Copyright (C) 2018 RISCassembler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package capslock.fixer.command;

import capslock.fixer.main.Console;
import capslock.game_info.GameDocument;
import capslock.game_info.JSONDBReader;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Attach extends Command {
    public Attach(List<String> arg){
        super(arg);
        Logger.INST.debug("Attach constructor");
    }

    @Override
    public boolean run() {
        Path json;
        try {
            json = Paths.get(arg.get(1));
        }catch (IndexOutOfBoundsException ex){
            json = Paths.get(console.getCurrentDir() + "/GamesInfo.json");
        }

        final JSONDBReader reader;
        try {
            reader = new JSONDBReader(json);
        }catch (IOException ex){
            Logger.INST.warn("Failed to read the JSON file.").logException(ex);
            Console.INST.out("JSONファイルの読み込みに失敗しました.");
            return false;
        }

        final List<GameDocument> gameList = reader.getDocumentList();
        Console.INST.out(gameList.size() + "件のゲーム情報を読み込みました.");
        console.setDocumentList(gameList);
        return true;
    }
}
