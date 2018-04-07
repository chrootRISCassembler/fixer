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

public class Find extends Command {
    @Override
    public boolean run(String line) {
        if(line.equals("find")){
            for (final var game : gameList){
                final var str = new StringBuilder("{")
                        .append("UUID : ")
                        .append(game.getUUID())
                        .append(", exe : ")
                        .append(game.getExe())
                        .append(", name : ")
                        .append(game.getName())
                        .append(", desc : ")
                        .append(game.getDesc())
                        .append(", panel : ")
                        .append(game.getPanel())
                        .append(", movieList : ")
                        .append(game.getMovieList())
                        .append(", imageList : ")
                        .append(game.getImageList())
                        .append(", gameID : ")
                        .append(game.getGameID())
                        .append(", lastMod : ")
                        .append(game.getLastMod())
                        .append('}')
                        .toString();
                System.out.println(str);
            }
        }
        return true;
    }
}
