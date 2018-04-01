/*
    Copyright (C) 2018 RISCassembler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package capslock.fixer.main;

import capslock.fixer.command.Command;
import capslock.game_info.GameDocument;
import methg.commonlib.trivial_logger.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * コンソールの実体
 * 様々な環境変数を保持する.
 */
public enum Console {
    INST;

    private ConsoleController controller;
    private Path CurrentDir = Paths.get("").toAbsolutePath();

    private List<GameDocument> documentList;
    private boolean isInteractiveMode = false;
    private Command commandInstance;

    final void commandRequest(String rawInput){
        if(isInteractiveMode){
            commandInstance.readLine(rawInput);
            return;
        }

        final int firstSpace = rawInput.indexOf(' ');
        final String command = firstSpace == -1 ? rawInput : rawInput.substring(0, firstSpace);

//        final List<String> wordList = Arrays.stream(rawInput.trim().split(" "))
//                .filter(str -> !str.isEmpty())
//                .collect(Collectors.toList());

        //if(wordList.isEmpty())return;

        final StringBuilder classNameBuilder = new StringBuilder("capslock.fixer.command.");
        classNameBuilder.append(Character.toUpperCase(command.charAt(0)));
        classNameBuilder.append(command.substring(1));

        try {
            Class<? extends Command> commandClass = Class.forName(classNameBuilder.toString()).asSubclass(Command.class);
            commandInstance = commandClass.newInstance();
        }catch (ClassNotFoundException ex) {
            Logger.INST.debug(command + " class is not found");
            controller.printNewLine(command + "というコマンドは見つかりません.");
            return;
        } catch (InstantiationException | IllegalAccessException ex){
            ex.printStackTrace();
            System.err.println("new fail");
            return;
        }

        commandInstance.run(rawInput);

        if(!isInteractiveMode){
            //GCに回収されるように参照を外す.
            commandInstance = null;
        }
    }

    /**
     * コンソールに出力する.
     */
    public final void out(String str){
        controller.printNewLine(str);
    }

    void setController(ConsoleController controller){
        this.controller = controller;
    }

    public final void setCurrentDir(Path dir){
        CurrentDir = dir;
    }

    public final  Path getCurrentDir(){
        return CurrentDir;
    }

    public final void setDocumentList(List<GameDocument> list){
        documentList = list;
    }

    public final List<GameDocument> getDocumentList(){
        return documentList;
    }

    public final void enableInteractive(){
        isInteractiveMode = true;
    }
    public final void disableInteractive(){
        isInteractiveMode = false;
    }
}
