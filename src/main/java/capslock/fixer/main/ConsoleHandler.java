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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ConsoleHandler {
    INST;

    private ConsoleController controller;
    private Path CurrentDir = Paths.get("").toAbsolutePath();

    private List<GameDocument> documentList;

    final void commandRequest(String rawInput){
        final List<String> wordList = Arrays.stream(rawInput.trim().split(" "))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());

        if(wordList.isEmpty())return;

        final StringBuilder classNameBuilder = new StringBuilder("capslock.fixer.command.");
        classNameBuilder.append(Character.toUpperCase(wordList.get(0).charAt(0)));
        classNameBuilder.append(wordList.get(0).substring(1));

        final Command commandObject;

        try {
            Class<? extends Command> commandClass = Class.forName(classNameBuilder.toString()).asSubclass(Command.class);
            Constructor<? extends Command> commandConstructor = commandClass.getDeclaredConstructor(List.class);
            commandObject = commandConstructor.newInstance(wordList);
        }catch (ClassNotFoundException ex){
            Logger.INST.debug(wordList.get(0) + " class is not found");
            controller.out(wordList.get(0) + "というコマンドは見つかりません.");
            return;

        }catch (NoSuchMethodException ex){
            ex.printStackTrace();
            System.err.println("constructor err");
            return;
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
            System.err.println("new fail");
            return;
        }

        commandObject.run();
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
}
