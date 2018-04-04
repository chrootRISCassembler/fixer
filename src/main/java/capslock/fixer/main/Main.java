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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    }
}
