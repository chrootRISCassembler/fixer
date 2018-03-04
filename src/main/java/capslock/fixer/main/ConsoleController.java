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
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleController implements Initializable {

    @FXML private TextField textField;

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }

    void onCreate(WindowEvent event) {

    }

    @FXML
    private void onEnter(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER)return;

        final String rawInput = textField.getText();
        textField.setText(null);

        ConsoleHandler.INST.commandRequest(rawInput);
    }
}
