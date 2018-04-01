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

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

import java.util.stream.Stream;


public class ConsoleController{
    static private final int BUFFER_MAX = 50;

    @FXML private TextField textField;
    @FXML private TextArea textArea;

    void onCreate(WindowEvent event) {

    }

    @FXML
    private void onEnter(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER)return;

        final String rawInput = textField.getText();
        textField.setText(null);

        Console.INST.commandRequest(rawInput);
    }

    final void printNewLine(String message) {
        final StringBuilder stringBuilder = new StringBuilder(message + '\n');

        Stream.of(textArea.getText().split("\n"))
                .limit(BUFFER_MAX)
                .forEach(str -> {
                    stringBuilder.append(str);
                    stringBuilder.append('\n');
                });

        textArea.setText(stringBuilder.toString());
    }
}
