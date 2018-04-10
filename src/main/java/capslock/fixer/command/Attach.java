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

import capslock.game_info.JSONDBReader;
import methg.commonlib.tiny_parser.BasicParser;
import methg.commonlib.tiny_parser.Parser;
import methg.commonlib.tiny_parser.Parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * GamesInfo.jsonをJSONDB,Games/をゲームディレクトリとして接続(アタッチ)する.
 * <p>
 *     オプションを何も指定しないと,　それぞれカレントディレクトリのGamesInfo.jsonとGames/にアタッチしようとする
 * </p>
 */
public final class Attach extends Command {
    private static final String DEFAULT_JSONDB_FILE_NAME = "./GamesInfo.json";
    private static final String DEFAULT_GAMES_DIR_NAME = "./Games/";

    private Path json;
    private Path gamesDir;

    @Override
    public boolean run(String line) {
        if(!parse(line))return false;

        if(Files.notExists(json)){
            System.err.println(json + "が見つからないためゲーム情報のロードをパスします.");
            gameList = new ArrayList<>();
        }else {
            final JSONDBReader reader;
            try {
                reader = new JSONDBReader(json);
            }catch (IOException ex){
                System.err.println(json + "の読み込み中に例外が発生しました.");
                ex.printStackTrace();
                return false;
            }
            gameList = reader.getDocumentList();
            System.out.println(gameList.size() + "game document detected.");
        }

        jsonDBFile = json;
        Command.gamesDir = this.gamesDir;
        return true;
    }

    private boolean parse(String line){
        final Parser parser;
        if(line.indexOf(' ') == -1){
            parser = Parsers.emptyParser();
        }else {
            parser = new BasicParser(Arrays.asList(line.split(" ")));
        }

        if(parser.hasOption('j')){
            final String jsonPathString = parser.getOperand('j');
            if(jsonPathString == null){
                System.err.println("-j オプションのオペランドがありません.");
                return false;
            }

            try {
                json = Paths.get(jsonPathString);
            }catch (InvalidPathException ex){
                System.err.println(jsonPathString + " は有効なパスではありません.");
                ex.printStackTrace();
                return false;
            }
        }else {
            json = Paths.get(System.getProperty("user.dir") + DEFAULT_JSONDB_FILE_NAME);
        }

        if(parser.hasOption('d')){
            final String gamesDirString = parser.getOperand('d');
            if(gamesDirString == null) {
                System.err.println("-d オプションのオペランドがありません.");
                return false;
            }

            try {
                gamesDir = Paths.get(gamesDirString);
            }catch (InvalidPathException ex){
                System.err.println(gamesDirString + " は有効なパスではありません.");
                ex.printStackTrace();
                return false;
            }
        }else{
            gamesDir = Paths.get(System.getProperty("user.dir") + '/' + DEFAULT_GAMES_DIR_NAME);
        }

        return true;
    }

    private void displayHelp(){
        System.out.println("attach - GamesInfo.jsonとGamesディレクトリに接続する." +
                "attach [-j JSON_FILE] [-d GAMES_DIR]" +
                "-j JSON_FILE" +
                "\tJSONファイルを指定して接続する." +
                "-d GAMES_DIR" +
                "\tGames/を指定して接続する");
    }
}
