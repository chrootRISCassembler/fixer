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

import capslock.fixer.util.FileUtil;
import capslock.fixer.util.RecursiveCollector;
import capslock.game_info.GameDocument;
import capslock.game_info.JSONDBReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class Merge extends Command {
    @Override
    public boolean run(String line) {
        final String[] args = line.split(" ");
        if(args.length == 2){
            return mergeSingleGame(args[1]);

        }else if (args.length == 3){
            if(!args[1].equals("-r")){
                System.err.println("不正なオプションです.");
                displayHelp();
                return false;
            }

           return mergeOtherDir(args[2]);
        }else {
            System.err.println("引数が不正です.");
            displayHelp();
            return false;
        }
    }

    private void displayHelp() {
        System.out.println("merge [-r] TARGET\n" +
                "他のゲームディレクトリを指定して現在のゲーム情報とGames/にマージする.\n" +
                "-r\n+" +
                "\tTARGETディレクトリの中に複数のゲームが格納されているものとしてマージする");
    }

    private boolean mergeSingleGame(String targetPath){
        final Path jsonFile;
        try {
            jsonFile = Paths.get(targetPath + "/.signature.json");
        }catch (InvalidPathException ex){
            System.err.println(targetPath + "は有効なパスではありません.");
            displayHelp();
            return false;
        }

        final JSONDBReader reader;

        try {
            reader = new JSONDBReader(jsonFile);
        }catch (IOException ex){
            System.err.println(" * Failed to read " + jsonFile + "\t[NG]");
            return false;
        }

        final GameDocument doc = reader.getDocumentList().get(0);
        System.out.println("* Got the signature from" + jsonFile + "\t[OK]");

        final Optional<GameDocument> duplicated = gameList.parallelStream()
                .filter(game -> game.getUUID().equals(doc.getUUID()))
                .findAny();

        if(duplicated.isPresent()){
            final Instant currentVer = duplicated.get().getLastMod();
            if(currentVer == null){
                System.err.println(duplicated.get().getUUID() + " isn\'t set lastMod");
                return false;
            }

            final Instant mergeVer = doc.getLastMod();
            if(mergeVer == null){
                System.err.println(doc.getUUID() + " isn\'t set lastMod");
                return false;
            }

            final int compare = mergeVer.compareTo(currentVer);
            if(compare > 0){
                System.out.println("Target is newer.");

                System.out.println("Replace old game directory with new one?");
                if(!Command.demandYN()){
                    System.out.println("merge quit.");
                    return true;
                }

                if(!RemoveOldGameRootDir(duplicated.get()))return false;

                gameList.remove(duplicated.get());

                if(!MoveGameRootDir(Paths.get(targetPath)))return false;

                gameList.add(doc);
                System.out.println("merge finished.");
                return true;
            }else{
                System.err.println("Target is older or same.");
                return false;
            }

        } else {
            System.out.println("This is a new game.");
            System.out.println("Merge with whole directory?");
            if(!Command.demandYN()){
                System.out.println("merge quit.");
                return true;
            }

            if(!MoveGameRootDir(Paths.get(targetPath)))return false;

            gameList.add(doc);
            System.out.println("ゲームのマージに成功しました.");
        }
        return true;
    }

    private boolean mergeOtherDir(String targetPath){
        final RecursiveCollector collector;

        try {
            collector = new RecursiveCollector(Paths.get(targetPath));
        }catch (InvalidPathException ex){
            System.err.println(targetPath + "は有効なパスではありません.");
            displayHelp();
            return false;
        }catch (IOException ex){
            System.err.println("ファイル収集中に例外が発生しました.");
            return false;
        }

        final List<GameDocument> removeList = new ArrayList<>();
        final Map<GameDocument, Path> NonCollisionMap = new HashMap<>();
        final Map<GameDocument, Path> CollisionMap = new HashMap<>();

        //計算量がO(n^2)だがnは十分に少ないため愚直なやり方を採用している
        collector.forEach((key, value) -> {
            final Optional<GameDocument> duplicated = gameList.parallelStream()
                    .filter(game -> game.getUUID().equals(key.getUUID()))
                    .findAny();

            if(duplicated.isPresent()){
                final Instant currentVer = duplicated.get().getLastMod();
                if(currentVer == null){
                    System.err.println(duplicated.get().getUUID() + " isn\'t set lastMod. passed");
                    return;
                }

                final Instant mergeVer = key.getLastMod();
                if(mergeVer == null){
                    System.err.println(key.getUUID() + " isn\'t set lastMod. passed");
                    return;
                }

                if(mergeVer.compareTo(currentVer) <= 0){
                    System.err.println(key.getUUID() + " target is older or same. passed");
                    return;
                }

                removeList.add(duplicated.get());
                CollisionMap.put(key, value);
            }else {
                NonCollisionMap.put(key, value);
            }
        });

        System.out.println(CollisionMap.size() + "件のゲームが更新され, "
                + NonCollisionMap.size() + "件のゲームが新規に追加されます");

        if(!demandYN()){
            System.out.println("merge quit.");
            return true;
        }

        removeList.forEach(this::RemoveOldGameRootDir);
        CollisionMap.forEach((doc, dir) -> {
            MoveGameRootDir(dir);
            Command.gameList.add(doc);
        });
        NonCollisionMap.forEach((doc, dir) -> {
            MoveGameRootDir(dir);
            Command.gameList.add(doc);
        });

        System.out.println("merge finished.");
        return true;
    }

    private boolean RemoveOldGameRootDir(GameDocument oldGameDocument){
        System.out.println("古いゲームディレクトリを削除します... これには時間がかかります.");
        try {
            FileUtil.rmRecursive(FileUtil.getGameRootDir(gamesDir, oldGameDocument.getExe()));
        }catch (IOException ex){
            System.err.println("古いゲームディレクトリの削除に失敗しました.");
            return false;
        }
        System.out.println("古いゲームディレクトリの削除が終了しました.");
        return true;
    }

    private boolean MoveGameRootDir(Path gameRootDir){
        System.out.println("ディレクトリの移動を開始します... これには時間がかかります.");

        try {
            Files.move(gameRootDir, Command.gamesDir);
        }catch (IOException ex){
            System.err.println("ディレクトリの移動に失敗しました.");
            ex.printStackTrace();
            return false;
        }
        System.out.println("ディレクトリの移動が終了しました.");
        return true;
    }
}
