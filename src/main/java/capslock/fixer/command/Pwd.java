package capslock.fixer.command;

import capslock.fixer.main.Console;
import methg.commonlib.trivial_logger.Logger;

import java.util.List;

public class Pwd extends Command {
    public Pwd(List<String> arg){
        super(arg);
        Logger.INST.debug("Detach constructor");
    }

    @Override
    public boolean run(){
        Console.INST.out(console.getCurrentDir().toString());
        return true;
    }
}
