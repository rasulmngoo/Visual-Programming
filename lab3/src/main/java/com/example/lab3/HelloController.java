package com.example.lab3;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class HelloController {

    @FXML
    private TextField txtNumber1;

    @FXML
    private TextField txtNumber2;

    @FXML
    private Label lblResult;

    @FXML
    private Label lblOperation;

    private int operationCount = 0;

    @FXML
    private void onOperation(ActionEvent event) {
        if (txtNumber1.getText().isBlank() || txtNumber2.getText().isBlank()) {
            showError("Введите оба числа.");
            return;
        }

        try {
            double number1 = Double.parseDouble(txtNumber1.getText());
            double number2 = Double.parseDouble(txtNumber2.getText());

            Button button = (Button) event.getSource();
            String operation = button.getText();

            double result = 0;

            switch (operation) {
                case "+":
                    result = number1 + number2;
                    break;
                case "-":
                    result = number1 - number2;
                    break;
                case "x":
                case "*":
                    result = number1 * number2;
                    break;
                case "÷":
                case "/":
                    if (number2 == 0) {
                        showError("Деление на ноль невозможно.");
                        return;
                    }
                    result = number1 / number2;
                    break;
                case "Среднее":
                    result = (number1 + number2) / 2.0;
                    break;
                default:
                    return;
            }

            operationCount++;

            lblResult.setText(String.format("Результат: %.2f", result));
            lblOperation.setText("Операция: " + operation + " | Успешно: " + operationCount);

        } catch (NumberFormatException e) {
            showError("Введите корректные числа.");
        }
    }

    @FXML
    private void onClearClick() {
        txtNumber1.clear();
        txtNumber2.clear();
        lblResult.setText("Результат: 0");
        lblOperation.setText("Операция: -");
        txtNumber1.requestFocus();
    }

    @FXML
    private void onExitClick() {
        Platform.exit();
    }

    @FXML
    private void onMouseEntered(MouseEvent event) {
        lblOperation.setText("Выберите операцию");
    }

    @FXML
    private void onMouseExited(MouseEvent event) {
        if (lblResult.getText().equals("Результат: 0")) {
            lblOperation.setText("Операция: -");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
