package capslock.fixer.command;

import capslock.game_info.JSONDBReader;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.stream.Stream;

public class Collect extends Command {

    @Override
    public boolean run(String line) {
        if(gamesDir == null){
            System.err.println("Games/ ディレクトリがアタッチされていません.\nattacheコマンドを先に実行してください.");
            return false;
        }

        if(!gameList.isEmpty()){
            System.err.println("ゲーム情報のドキュメントが既に存在するため中止します.");
            return false;
        }

        try(final Stream<Path> lsStream = Files.list(gamesDir)) {
            lsStream.parallel()
                    .map(gameRootDir -> Paths.get(gameRootDir + "/.signature.json"))
                    .peek(System.out::println)
                    .map(path -> {
                        try {
                            return new JSONDBReader(path);
                        }catch (IllegalArgumentException | IOException ex){
                            System.err.println(" * Failed to read " + path + "\t[NG]");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(reader -> reader.getDocumentList().get(0))
                    .peek(doc -> System.out.println("* Got the signature " + doc.getName() + "\t[OK]"))
                    .forEach(gameList::add);
        }catch (IOException | SecurityException ex){
            System.err.println("ファイルの収集中に例外が発生しました.");
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
