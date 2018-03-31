package capslock.fixer.command;

import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Cd extends Command {
    public Cd(List<String> arg){
        super(arg);
        Logger.INST.debug("cd constructor called");
    }

    @Override
    public boolean run(){
        if(arg.size() != 2){
            outputConsole.out("引数が不正です.");
            outputConsole.out("使い方 : cd ディレクトリ");
            return false;
        }

        final Path newDir;

        try {
            newDir = Paths.get(arg.get(1));
        }catch (InvalidPathException ex){
            outputConsole.out(arg.get(1) + " はパスではありません.");
            return false;
        }

        if(Files.notExists(newDir)){
            outputConsole.out(arg.get(1) + " のようなパスは存在しません.");
            return false;
        }

        if(!Files.isDirectory(newDir)){
            outputConsole.out(arg.get(1) + " はディレクトリではありません.");
            return false;
        }

        try {
            console.setCurrentDir(newDir.normalize().toRealPath());
        }catch (IOException | SecurityException ex){
            outputConsole.out("パス解決に失敗しました.");
            return false;
        }

        outputConsole.out("cd to " + newDir);
        return true;
    }
}
