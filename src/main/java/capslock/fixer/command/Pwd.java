package capslock.fixer.command;

import capslock.fixer.main.Console;

public class Pwd extends Command {

    @Override
    public boolean run(String line){
        Console.INST.out(console.getCurrentDir().toString());
        return true;
    }
}
