package capslock.fixer.command;

import capslock.game_info.GameDocument;
import methg.commonlib.trivial_logger.Logger;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

public final class Insert extends Command {
    private State state = State.UUID;
    private GameDocument document = new GameDocument();

    private enum State{
        UUID,
        EXE,
        NAME,
        DESC,
        PANEL,
        MOVIES,
        IMAGES,
        GAMEID,
        LAST_MOD;
    }

    @Override
    public boolean run(String line){
        if(console.getDocumentList() == null){
            console.out("ゲーム情報がまだロードされていません. attachコマンドでロードしてください.");
            return false;
        }

        console.enableInteractive();
        console.out("UUID (空Enterで自動指定)?");
        return true;
    }

    @Override
    public void readLine(String line) {
        switch (state) {
            case UUID:
                if(line.trim().isEmpty()){
                    console.out("UUID : " + document.getUUID());
                }else {
                    final UUID uuid;
                    try {
                        uuid = UUID.fromString(line);
                    }catch (IllegalArgumentException ex){
                        console.out("UUIDの指定が不正です.");
                        return;
                    }
                    console.out("UUID : " + uuid);
                    document.setUUID(uuid);
                }

                console.out("exe relative path ?");
                state = State.EXE;
                return;


            case EXE:
                if(line.trim().isEmpty()){
                    console.out("exe フィールドは空にできません.");
                    return;
                }

                final Path exe;
                try {
                    exe = Paths.get(line.trim());
                }catch (InvalidPathException ex){
                    console.out(line + " は有効なパスではありません.");
                    Logger.INST.logException(ex);
                    return;
                }

                console.out("exe : " + exe);
                document.setExe(exe);
                console.out("name ?");
                state = State.NAME;
                return;


            case NAME:
                if(line.trim().isEmpty()){
                    console.out("name : null");
                }else {
                    console.out("name : " + line);
                    document.setName(line);
                }
                console.out("desc ?");
                state = State.DESC;
                return;


            case DESC:
                if(line.trim().isEmpty()){
                    console.out("desc : null");
                }else {
                    console.out("desc : " + line);
                    document.setDesc(line);
                }
                console.out("panel relative path ?");
                state = State.PANEL;
                return;


            case PANEL:
                if(line.trim().isEmpty()){
                    console.out("panel : null");
                }else {
                    final Path panel;
                    try {
                        panel = Paths.get(line);
                    }catch (InvalidPathException ex){
                        console.out(line + " は有効なパスではありません.");
                        Logger.INST.logException(ex);
                        return;
                    }
                }
                console.out("movie relative paths ?");
                state = State.MOVIES;
                return;


            case MOVIES: {
                final String trimmedStr = line.trim();

                if (trimmedStr.isEmpty()) {
                    console.out("movie : null");
                } else {
                    final List<Path> movieList = new ArrayList<>();

                    for (final String operand : trimmedStr.split(" ")) {
                        try {
                            movieList.add(Paths.get(operand));
                        } catch (InvalidPathException ex) {
                            console.out(operand + " は有効なパスではありません.");
                            Logger.INST.logException(ex);
                            return;
                        }
                    }

                    movieList.forEach(movie -> console.out("movie : " + movie));
                    document.setMovieList(movieList);
                }
                console.out("image relative paths ?");
                state = State.IMAGES;
            }
                return;


            case IMAGES: {
                final String trimmedStr = line.trim();

                if (trimmedStr.isEmpty()) {
                    console.out("image : null");
                } else {
                    final List<Path> imageList = new ArrayList<>();

                    for (final String operand : trimmedStr.split(" ")) {
                        try {
                            imageList.add(Paths.get(operand));
                        } catch (InvalidPathException ex) {
                            console.out(operand + " は有効なパスではありません.");
                            Logger.INST.logException(ex);
                            return;
                        }
                    }

                    imageList.forEach(movie -> console.out("image : " + movie));
                    document.setMovieList(imageList);
                }
                console.out("gameID ?");
                state = State.GAMEID;
            }
                return;


            case GAMEID:
                if (line.trim().isEmpty()){
                    console.out("gameID : null");
                }else {
                    try {
                        final int id = Integer.valueOf(line.trim());
                        document.setGameID(id);
                        console.out("gameID : " + id);
                    }catch (NumberFormatException ex){
                        console.out("整数に変換できません.");
                        Logger.INST.logException(ex);
                        return;
                    }
                }
                console.out("lastMod (空Enterで現在時刻が自動挿入) : ?");
                state = State.LAST_MOD;
                return;


            case LAST_MOD:
                if(line.trim().isEmpty()){
                    final Instant now = Instant.now();
                    console.out("lastMod :" + now);
                    document.setLastMod(now);
                }else {
                    try {
                       final Instant time = Instant.parse(line.trim());
                       console.out("lastMod :" + time);
                       document.setLastMod(time);
                    }catch (DateTimeParseException ex){
                        Logger.INST.logException(ex);
                        console.out("時刻の指定が不正です.");
                        return;
                    }
                }

                console.getDocumentList().add(document);
                console.out("登録に成功しました.");
                console.disableInteractive();
        }
    }
}
