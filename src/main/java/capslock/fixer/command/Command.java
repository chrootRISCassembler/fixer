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

import capslock.game_info.GameDocument;

import java.nio.file.Path;
import java.util.List;

/**
 * コマンドの共通基底クラス.
 */
public abstract class Command {
    /**
     * 現在のゲーム情報
     */
    List<GameDocument> gameList;
    /**
     * アタッチしているJSONファイル
     */
    Path jsonDBFile;
    /**
     * アタッチしているGames/ディレクトリ
     */
    Path gmaesDir;

    /**
     * コマンドの実体
     * @param line 一行分の入力
     * @return 成功時 {@code true}, 失敗時 {@code false}
     */
    public abstract boolean run(String line);
}
