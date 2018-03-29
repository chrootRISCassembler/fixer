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

package capslock.fixer.command;

import capslock.fixer.main.ConsoleController;

import java.util.List;

public abstract class Command {
    static ConsoleController outputConsole;

    private List<String> arg;
    Command(List<String> arg){

    }

    abstract public boolean run();

    static public void setConsole(ConsoleController console){
        outputConsole = console;
    }
}
