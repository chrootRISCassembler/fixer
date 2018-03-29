package capslock.fixer.command;

import methg.commonlib.trivial_logger.Logger;

import java.util.List;

public class Pwd extends Command {
    public Pwd(List<String> arg){
        super(arg);
        Logger.INST.debug("Detach constructor");
    }

    @Override
    public boolean run(){
        outputConsole.out(consoleHandler.getCurrentDir().toString());
        return true;
    }
}
