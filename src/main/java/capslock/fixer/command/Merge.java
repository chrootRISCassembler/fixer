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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class Merge extends Command {
    @Override
    public boolean run(String line) {
        final String[] args = line.split(" ");
        if(args.length == 2){

            final Path gameRootDir;
            try {
                gameRootDir = Paths.get(args[1]);
            }catch (InvalidPathException ex){
                System.err.println(args[1] + "は有効なパスではありません.");
                displayHelp();
                return false;
            }


            final JSONDBReader reader;
            final Path jsonFile;

            try {
                jsonFile = Paths.get(gameRootDir + "/.signature.json");
                reader = new JSONDBReader(jsonFile);
            }catch (InvalidPathException ex){
                System.err.println("パスの構築中に例外が発生しました.");
                ex.printStackTrace();
                return false;
            }catch (IOException ex){
                System.err.println(" * Failed to read " + jsonFile + "\t[NG]");
                return false;
            }


            System.out.println("* Got the signature " + doc.getName() + "\t[OK]");

        }else if (args.length == 3){
            if(!args[1].equals("-r")){
                System.err.println("不正なオプションです.");
                displayHelp();
                return false;
            }

            final Path gameRootDir;
            try {
                gameRootDir = Paths.get(args[1]);
            }catch (InvalidPathException ex){
                System.err.println(args[1] + "は有効なパスではありません.");
                displayHelp();
                return false;
            }

        }else {
            System.err.println("引数が不正です.");
            displayHelp();
            return false;
        }
        return false;
    }

    private void displayHelp() {
        System.out.println("merge [-r] TARGET\n" +
                "他のゲームディレクトリを指定して現在のゲーム情報とGames/にマージする.\n" +
                "-r\n+" +
                "\tTARGETディレクトリの中に複数のゲームが格納されているものとしてマージする");
    }

}
