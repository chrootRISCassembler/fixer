package capslock.fixer.command;

import capslock.game_info.Game;
import capslock.game_info.JSONDBReader;
import capslock.game_info.JSONDBWriter;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Collect extends Command {
    private Path outputFilePath;
    private Path sourceGamesDir;

    public Collect(List<String> arg){
        super(arg);
        Logger.INST.debug("collect constructor called");
    }

    @Override
    public boolean run(){
        if(!parse())return false;
        if(sourceGamesDir == null)sourceGamesDir = Paths.get(consoleHandler.getCurrentDir() + "/Games/");
        if(outputFilePath == null)outputFilePath = Paths.get(consoleHandler.getCurrentDir() + "/GamesInfo.json");

        if(Files.notExists(sourceGamesDir)){
            outputConsole.out("Games/ が見つかりません. cdするか-sオプションで指定してください.");
            return false;
        }

        outputConsole.out("Seek \"" + sourceGamesDir + " \" ...");
        try(final Stream<Path> lsStream = Files.list(sourceGamesDir)) {
            final JSONDBWriter writer = new JSONDBWriter(outputFilePath);

            lsStream.parallel()
                    .map(gameRootDir -> Paths.get(gameRootDir + "/.signature.json"))
                    .peek(path -> System.err.println(path))
                    .map(path -> {
                        try {
                           return  new JSONDBReader(path);
                        }catch (IllegalArgumentException | IOException ex){
                            outputConsole.out("Failed to read " + path);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .peek(path -> outputConsole.out("Got signature from \"" + path + '\"'))
                    .map(reader -> reader.getDocumentList().get(0))
                    .forEach(writer::add);

            writer.flush();

        }catch (IOException ex){
            Logger.INST.logException(ex);
        }catch (SecurityException ex){
            Logger.INST.logException(ex);
        }

        outputConsole.out("Write info to \"" + outputFilePath + "\" done.");
        return true;
    }

    private boolean parse(){
        switch (arg.size()){
            case 1:
                return true;
            case 3:
            case 5:
                boolean hasOptionO = false;
                boolean hasOptionS = false;
                boolean ret;

                switch (arg.get(1)){
                    case "-o":
                        ret = optionO(arg.get(2));
                        hasOptionO = true;
                        break;
                    case "-s":
                        ret = optionS(arg.get(2));
                        hasOptionS = true;
                        break;
                    default:
                        outputConsole.out("引数が不正です.");
                        displayHelp();
                        return false;
                }

                if(arg.size() == 3)return ret;
                if(!ret)return false;

                switch (arg.get(3)){
                        case "-o":
                            if(hasOptionO){
                                outputConsole.out("同じオプションが指定されています.");
                                return false;
                            }
                            return optionO(arg.get(5));
                        case "-s":
                            if(hasOptionS){
                                outputConsole.out("同じオプションが指定されています.");
                                return false;
                            }
                            return optionS(arg.get(5));
                        default:
                            outputConsole.out("引数が不正です.");
                            displayHelp();
                            return false;

                }
        }

        outputConsole.out("引数の数が不正です.");
        displayHelp();
        return false;
    }

    private void displayHelp(){
        outputConsole.out("使い方 : collect [オプション]");
        outputConsole.out("\t-o FILE\t出力するファイル名をGamesInfo.jsonではなくFILEにする.");
        outputConsole.out("\t-s DIR\tGames/ディレクトリの代わりにDIRから各々のゲームの.signature.jsonを読み取る.");
    }

    private boolean optionO(String operand){
        try{
            outputFilePath = Paths.get(operand);
            return true;
        }catch (InvalidPathException ex){
            outputConsole.out("-o オプション : " + operand  + " は有効なパスではありません.");
            return false;
        }
    }

    private boolean optionS(String operand){
        try{
            sourceGamesDir = Paths.get(operand);
            return true;
        }catch (InvalidPathException ex){
            outputConsole.out("-o オプション : " + operand  + " は有効なパスではありません.");
            return false;
        }
    }
}
