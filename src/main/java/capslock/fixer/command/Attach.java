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

import methg.commonlib.trivial_logger.Logger;

import java.util.List;

public class attach extends command {
    public attach(List<String> arg){
        super(arg);
        Logger.INST.debug("attach constructor");
    }

    @Override
    public boolean run() {
        Logger.INST.debug("attach run");
        return true;
    }
}
