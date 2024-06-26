package org.group.project.controllers.waiter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.group.project.classes.FoodDrink;
import org.group.project.classes.Menu;
import org.group.project.classes.auxiliary.AlertPopUpWindow;
import org.group.project.exceptions.TextFileNotFoundException;

import java.util.List;

/**
 * This class enables the waiter to add a dine-in order.
 *
 * @author azmi_maz
 */
public class WaiterDineinAddOrderController {
    private static final String INVALID_QUANTITY = "Quantity cannot " +
            "be less than 0";
    private static final String DEFAULT_QUANTITY = "1";
    private static final String CHOOSE_ITEM = "Choose Item";
    private static final String INVALID_INPUTS = "Please enter valid inputs.";
    @FXML
    private VBox vbox;

    @FXML
    private ComboBox<String> comboItemName;

    @FXML
    private TextField quantityTextField;

    @FXML
    private Button addItemButton;

    @FXML
    private Button cancelButton;

    private List<FoodDrink> orderList;

    /**
     * This initializes the controller for the fxml.
     */
    public void initialize() {

        quantityTextField.setOnAction(e -> {
            int quantityValue = Integer.parseInt(quantityTextField.getText());
            if (quantityValue < 0) {
                AlertPopUpWindow.displayErrorWindow(
                        INVALID_QUANTITY
                );
                quantityTextField.setText(DEFAULT_QUANTITY);
            }
        });

        comboItemName.setValue(CHOOSE_ITEM);

        try {

            Menu menu = new Menu();
            menu.updateDineinMenuChoiceBox(
                    comboItemName
            );

        } catch (TextFileNotFoundException e) {
            AlertPopUpWindow.displayErrorWindow(
                    e.getMessage()
            );
            e.printStackTrace();
        }

        addItemButton.setOnAction(e -> {

            if (
                    !comboItemName.getValue().equals(CHOOSE_ITEM)
                            && Integer.parseInt(
                            quantityTextField.getText()) > 0
            ) {

                String selectedItemName = comboItemName.getValue().toString();

                String selectedItemType = "";
                try {

                    Menu menu = new Menu();
                    selectedItemType = menu
                            .findTypeByItemName(selectedItemName);

                } catch (TextFileNotFoundException ex) {
                    AlertPopUpWindow.displayErrorWindow(
                            ex.getMessage()
                    );
                    ex.printStackTrace();
                }

                int enteredQuantity = Integer.parseInt(
                        quantityTextField.getText());

                orderList.add(new FoodDrink(
                        selectedItemName,
                        selectedItemType,
                        enteredQuantity
                ));

                closeWindow();

            } else {
                AlertPopUpWindow.displayErrorWindow(
                        INVALID_INPUTS
                );
                quantityTextField.setText(DEFAULT_QUANTITY);
            }

        });

        cancelButton.setOnAction(e -> {
            closeWindow();
        });

    }

    /**
     * This gets the current order list made by the waiter.
     *
     * @param orderList - the current order list.
     */
    public void getCurrentOrderList(
            List<FoodDrink> orderList
    ) {
        this.orderList = orderList;

    }

    private void closeWindow() {
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.close();
    }

}
