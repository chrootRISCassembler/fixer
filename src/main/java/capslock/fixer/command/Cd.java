package capslock.fixer.command;

import capslock.fixer.main.Console;
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
            Console.INST.out("引数が不正です.");
            Console.INST.out("使い方 : cd ディレクトリ");
            return false;
        }

        final Path newDir;

        try {
            newDir = Paths.get(arg.get(1));
        }catch (InvalidPathException ex){
            Console.INST.out(arg.get(1) + " はパスではありません.");
            return false;
        }

        if(Files.notExists(newDir)){
            Console.INST.out(arg.get(1) + " のようなパスは存在しません.");
            return false;
        }

        if(!Files.isDirectory(newDir)){
            Console.INST.out(arg.get(1) + " はディレクトリではありません.");
            return false;
        }

        try {
            console.setCurrentDir(newDir.normalize().toRealPath());
        }catch (IOException | SecurityException ex){
            Console.INST.out("パス解決に失敗しました.");
            return false;
        }

        Console.INST.out("cd to " + newDir);
        return true;
    }
}
