package capslock.fixer.command;

import capslock.game_info.GameDocument;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

public final class Insert extends Command {

    @Override
    public boolean run(String line){
        if(gameList == null) {
            System.err.println("ゲーム情報が読み込まれていません.先にattachしてください");
            return false;
        }

        final var document = new GameDocument();

        while (true){
            System.out.println("UUID (空Enterで自動指定)? >");

            final String trimmed = scanner.nextLine().trim();
            if(trimmed.isEmpty()){
                break;
            }else {
                try {
                    document.setUUID(UUID.fromString(line));
                    break;
                }catch (IllegalArgumentException ex){
                    System.err.println("UUIDの指定が不正です.");
                }
            }
        }

        System.out.println("UUID : " + document.getUUID());

        while (true){
            System.out.println("exe relative path ? >");
            final String trimmed = scanner.nextLine().trim();
                if(trimmed.isEmpty()){
                    System.err.println("exe フィールドは空にできません.");
                    continue;
                }

                try {
                    document.setExe(Paths.get(trimmed));
                    break;
                }catch (InvalidPathException ex){
                    System.err.println(trimmed + " は有効なパスではありません.");
                }
        }

        System.out.println("exe : " + document.getExe());

        {
            System.out.println("name ? >");
            final String trimmed = scanner.nextLine().trim();
            if(!trimmed.isEmpty())document.setName(trimmed);
        }

        System.out.println("name : " + document.getName());

        {
            System.out.println("description ? >");
            final String trimmed = scanner.nextLine().trim();
            if(!trimmed.isEmpty())document.setDesc(trimmed);
        }

        System.out.println("desc : " + document.getDesc());

        while (true){
            System.out.println("panel relative path ? >");
            final String trimmed = scanner.nextLine().trim();
            if (trimmed.isEmpty()){
                break;
            }else {
                try {
                    document.setPanel(Paths.get(trimmed));
                    break;
                }catch (InvalidPathException ex){
                    System.err.println(trimmed + " は有効なパスではありません.");
                }
            }
        }

        System.out.println("panel : " + document.getPanel());

        movies: while (true){
            System.out.println("movie relative paths ? >");
            final String trimmed = scanner.nextLine().trim();
            if (trimmed.isEmpty()) {
                break;
            } else {
                final List<Path> movieList = new ArrayList<>();
                for (final String operand : trimmed.split(" ")) {
                    try {
                        movieList.add(Paths.get(operand));
                    } catch (InvalidPathException ex) {
                        System.err.println(operand + " は有効なパスではありません.");
                        continue movies;
                    }
                }
                document.setMovieList(movieList);
            }
        }

        System.out.println("movies : " + document.getMovieList());

        images: while (true){
            System.out.println("image relative paths ? >");
            final String trimmed = scanner.nextLine().trim();
            if(trimmed.isEmpty()){
                break;
            }else {
                final List<Path> imageList = new ArrayList<>();

                for (final String operand : trimmed.split(" ")) {
                    try {
                        imageList.add(Paths.get(operand));
                    } catch (InvalidPathException ex) {
                        System.err.println(operand + " は有効なパスではありません.");
                        continue images;
                    }
                }
                document.setMovieList(imageList);
            }
        }

        System.out.println("images : " + document.getImageList());

        while (true){
            System.out.println("gameID ? >");
            final String trimmed = scanner.nextLine().trim();
            if (trimmed.isEmpty()){
                break;
            } else {
                try {
                    document.setGameID(Integer.valueOf(trimmed));
                    break;
                } catch (NumberFormatException ex) {
                    System.err.println(trimmed + "　は整数に変換できません.");
                }
            }
        }

        System.out.println("gameID : " + document.getGameID());

        while (true){
            System.out.println("lastMod (空Enterで現在時刻が自動挿入) ? >");
            final String trimmed = scanner.nextLine().trim();
            if(trimmed.isEmpty()){
                document.setLastMod(Instant.now());
                break;
            }else {
                try {
                    document.setLastMod(Instant.parse(trimmed));
                    break;
                }catch (DateTimeParseException ex) {
                    System.out.println("時刻の指定が不正です.");
                }
            }
        }

        System.out.println("lastMod : " + document.getLastMod());
        gameList.add(document);
        System.out.println("登録に成功しました.");
        return true;
    }
}
