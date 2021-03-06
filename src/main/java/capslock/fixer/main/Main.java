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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main{

    /**
     * エントリポイント
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        System.out.println("fixer - GameInfo edit tool");

        try (final var stream = new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream("BuildInfo.txt")))){
            stream.lines().forEach(System.out::println);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        //System.console()はIDEからつかえないことが多い nullを返される

        final var scanner = new Scanner(System.in);
        Command.setScanner(scanner);
        while (true) {
            System.out.print('>');
            System.out.flush();
            final String line = scanner.nextLine();
            if ("quit".equals(line)) {
                System.out.println("Bye.");
                break;
            }

            final String trimmed = line.trim();

            if(trimmed.isEmpty())continue;

            final int firstSpace = trimmed.indexOf(' ');
            final String firstWord = firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace);

            final var classNameBuilder = new StringBuilder("capslock.fixer.command.");
            classNameBuilder.append(Character.toUpperCase(firstWord.charAt(0)));
            classNameBuilder.append(firstWord.substring(1));

            final Command commandInstance;

            try {
                Class<? extends Command> commandClass = Class.forName(classNameBuilder.toString()).asSubclass(Command.class);
                commandInstance = commandClass.getDeclaredConstructor().newInstance();
            }catch (ClassNotFoundException ex){
                System.err.println(firstWord + " command isn't found.");
                continue;
            }catch (NoSuchMethodException ex){
                System.err.println("CRITICAL : Constructor isn't found.");
                continue;
            }catch (InstantiationException | IllegalAccessException | InvocationTargetException ex){
                System.err.println("CRITICAL : new failed.");
                continue;
            }

            commandInstance.run(trimmed);
        }
        scanner.close();
    }
}
