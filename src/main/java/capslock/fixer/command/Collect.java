package capslock.fixer.command;

import capslock.fixer.main.Console;
import capslock.game_info.JSONDBReader;
import capslock.game_info.JSONDBWriter;
import methg.commonlib.tiny_parser.BasicParser;
import methg.commonlib.tiny_parser.Parser;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Collect extends Command {
    private Path outputFilePath;
    private Path sourceGamesDir;
    private boolean forceWriteOver = false;

    public Collect(){
        if(sourceGamesDir == null)sourceGamesDir = Paths.get(console.getCurrentDir() + "/Games/");
        if(outputFilePath == null)outputFilePath = Paths.get(console.getCurrentDir() + "/GamesInfo.json");
    }

    @Override
    public boolean run(String line){
        final Parser parser = new BasicParser(Arrays.stream(line.trim().split(" "))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList()));

        if(parser.hasOption('s')){
            sourceGamesDir = Paths.get(parser.getOperand('s'));
        }

        if(parser.hasOption('o')){
            outputFilePath = Paths.get(parser.getOperand('o'));
        }

        if(Files.notExists(sourceGamesDir)){
            Console.INST.out(sourceGamesDir + "が見つかりません.");
            return false;
        }

        if(!parser.hasOption('f') && Files.exists(outputFilePath)){
            Console.INST.out("既に" + outputFilePath + "が存在するため中止します.　" +
                    "上書きを許可する場合は-fオプションを指定してください.");
            return false;
        }

        Console.INST.out("Seek \"" + sourceGamesDir + " \" ...");
        try(final Stream<Path> lsStream = Files.list(sourceGamesDir)) {
            final JSONDBWriter writer = new JSONDBWriter(outputFilePath);

            lsStream.parallel()
                    .map(gameRootDir -> Paths.get(gameRootDir + "/.signature.json"))
                    .peek(path -> System.err.println(path))
                    .map(path -> {
                        try {
                            return new JSONDBReader(path);
                        }catch (IllegalArgumentException | IOException ex){
                            Console.INST.out(" * Failed to read " + path + "\t[NG]");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(reader -> reader.getDocumentList().get(0))
                    .peek(doc -> Console.INST.out("* Got the signature " + doc.getName() + "\t[OK]"))
                    .forEach(writer::add);

            writer.flush();

        }catch (IOException ex){
            Logger.INST.logException(ex);
        }catch (SecurityException ex){
            Logger.INST.logException(ex);
        }

        Console.INST.out("Write info to \"" + outputFilePath + "\" done.");
        return true;
    }

    private void displayHelp(){
        Console.INST.out("使い方 : collect [オプション]");
        Console.INST.out("\t-o FILE\t出力するファイル名をGamesInfo.jsonではなくFILEにする.");
        Console.INST.out("\t-s DIR\tGames/ディレクトリの代わりにDIRから各々のゲームの.signature.jsonを読み取る.");
        Console.INST.out("\t-f GamesInfo.jsonが存在する場合上書きする.");
    }
}
