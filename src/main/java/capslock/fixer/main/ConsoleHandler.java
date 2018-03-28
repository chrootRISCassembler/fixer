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
import methg.commonlib.trivial_logger.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum ConsoleHandler {
    INST;

    private ConsoleController controller;
    Path connectedJSON = null;

    final void commandRequest(String rawInput){
        final List<String> wordList = Arrays.stream(rawInput.trim().split(" "))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());

        if(wordList.isEmpty())return;

        try {
            Class<? extends Command> commandClass = Class.forName("capslock.fixer.Command." + wordList.get(0)).asSubclass(Command.class);
            Constructor<? extends Command> commandConstructor = commandClass.getDeclaredConstructor(List.class);
            Command commandObject = commandConstructor.newInstance(wordList);
        }catch (ClassNotFoundException ex){
            Logger.INST.debug(wordList.get(0) + " class is not found");

        }catch (NoSuchMethodException ex){
            ex.printStackTrace();
            System.err.println("constructor err");
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
            System.err.println("new fail");
        }
    }
    void setController(ConsoleController controller){
        this.controller = controller;
    }
}
