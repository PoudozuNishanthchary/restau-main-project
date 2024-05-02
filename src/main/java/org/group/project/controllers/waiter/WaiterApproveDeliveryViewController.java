package org.group.project.controllers.waiter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.group.project.Main;
import org.group.project.classes.DeliveryOrder;
import org.group.project.classes.Kitchen;
import org.group.project.classes.Waiter;
import org.group.project.classes.auxiliary.AlertPopUpWindow;
import org.group.project.classes.auxiliary.ImageLoader;
import org.group.project.exceptions.TextFileNotFoundException;
import org.group.project.scenes.WindowSize;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * This class enables the waiter to view pending delivery orders for
 * approval and driver assignment.
 *
 * @author azmi_maz
 */
public class WaiterApproveDeliveryViewController {

    @FXML
    private TableColumn<DeliveryOrder, String> orderNoColumn;

    @FXML
    private TableColumn<DeliveryOrder, String> customerColumn;

    @FXML
    private TableColumn<DeliveryOrder, String> orderDateColumn;

    @FXML
    private TableColumn<DeliveryOrder, String> orderTimeColumn;

    @FXML
    private TableColumn<DeliveryOrder, String> orderListColumn;

    @FXML
    private TableColumn<DeliveryOrder, String> orderStatusColumn;

    @FXML
    private TableColumn<DeliveryOrder, Button> actionButtonColumn;

    @FXML
    private TableColumn<DeliveryOrder, Button> actionButtonColumn1;

    @FXML
    private TableColumn<DeliveryOrder, Button> actionButtonColumn2;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TableView<DeliveryOrder> pendingDeliveryTable = new TableView<>();
    private ObservableList<DeliveryOrder> data =
            FXCollections.observableArrayList();

    /**
     * This initializes the controller for the fxml.
     */
    public void initialize() {

        Image bgImage = null;
        try {
            bgImage = new Image(Main.class.getResource("images" +
                    "/background/waiter-main" +
                    ".jpg").toURI().toString());
        } catch (URISyntaxException e) {
            AlertPopUpWindow.displayErrorWindow(
                    e.getMessage()
            );
            e.printStackTrace();
        }

        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO, false,
                false, true, true);

        borderPane.setBackground(new Background(new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));

        refreshPendingDeliveryList();

        orderNoColumn.setText("Order No.");
        orderNoColumn.setMinWidth(65);
        orderNoColumn.setStyle("-fx-alignment: CENTER;");
        orderNoColumn.setCellValueFactory(
                new PropertyValueFactory<>("orderId"));

        customerColumn.setText("Customer");
        customerColumn.setMinWidth(150);
        customerColumn.setStyle("-fx-alignment: CENTER;");
        customerColumn.setCellValueFactory(
                new PropertyValueFactory<>("customer"));

        orderDateColumn.setText("Date");
        orderDateColumn.setMinWidth(150);
        orderDateColumn.setStyle("-fx-alignment: CENTER;");
        orderDateColumn.setCellValueFactory(cellData -> {
            String formattedDate =
                    cellData.getValue().getOrderDateInFormat();
            return new SimpleObjectProperty<>(formattedDate);
        });

        orderTimeColumn.setText("Time");
        orderTimeColumn.setMinWidth(150);
        orderTimeColumn.setStyle("-fx-alignment: CENTER;");
        orderTimeColumn.setCellValueFactory(cellData -> {
            String formattedTime = cellData.getValue().getOrderTimeInFormat();
            return new SimpleObjectProperty<>(formattedTime);
        });

        orderListColumn.setText("Item Ordered");
        orderListColumn.setMinWidth(200);
        orderListColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        orderListColumn.setCellValueFactory(cellData -> {
            String displayList = cellData.getValue().getListOfItemsForDisplay();
            return new SimpleObjectProperty<>(displayList);
        });

        orderStatusColumn.setText("Status");
        orderStatusColumn.setMinWidth(150);
        orderStatusColumn.setStyle("-fx-alignment: CENTER;");
        orderStatusColumn.setCellValueFactory(
                new PropertyValueFactory<>("orderStatus"));

        actionButtonColumn.setText("Action");

        actionButtonColumn1.setMinWidth(35);
        actionButtonColumn1.setStyle("-fx-alignment: CENTER;");
        actionButtonColumn1.setCellValueFactory(cellData -> {
            Button viewButton = new Button();
            viewButton.setTooltip(new Tooltip("View details"));
            ImageLoader.setUpGraphicButton(viewButton,
                    15, 15, "view-details");
            DeliveryOrder selectedOrder = cellData.getValue();

            viewButton.setOnAction(e -> {

                try {
                    FXMLLoader fxmlLoader =
                            new FXMLLoader(Main.class.getResource(
                                    "smallwindows/" +
                                            "waiter-editdeliveryorder" +
                                            ".fxml"));

                    VBox vbox = fxmlLoader.load();

                    WaiterEditDeliveryOrderController controller =
                            fxmlLoader.getController();

                    controller.populateOrderDetails(
                            selectedOrder
                    );
                    Scene editScene = new Scene(vbox,
                            WindowSize.MEDIUM.WIDTH,
                            WindowSize.MEDIUM.HEIGHT);

                    Stage editStage = new Stage();
                    editStage.setScene(editScene);

                    editStage.setTitle("Edit Delivery Order");

                    editStage.initModality(Modality.APPLICATION_MODAL);

                    editStage.showAndWait();

                    refreshPendingDeliveryList();

                } catch (IOException ex) {
                    AlertPopUpWindow.displayErrorWindow(
                            ex.getMessage()
                    );
                    ex.printStackTrace();
                }

            });

            return new SimpleObjectProperty<>(viewButton);
        });

        actionButtonColumn2.setMinWidth(35);
        actionButtonColumn2.setStyle("-fx-alignment: CENTER;");
        actionButtonColumn2.setCellValueFactory(cellData -> {
            Button cancelButton = new Button();
            // TODO use tool tips for other buttons, where necessary
            cancelButton.setTooltip(new Tooltip("Cancel"));
            ImageLoader.setUpGraphicButton(cancelButton,
                    15, 15, "cancel");
            DeliveryOrder selectedOrder = cellData.getValue();

            cancelButton.setOnAction(e -> {
                Waiter waiter = (Waiter) Main.getCurrentUser();

                Optional<ButtonType> userChoice = promptForUserAcknowledgement(
                        "Delivery Order Cancellation",
                        "Do you want to cancel this delivery order?"
                );

                if (userChoice.get()
                        .getButtonData().toString()
                        .equalsIgnoreCase("OK_DONE")) {

                    try {
                        boolean isSuccessful = waiter.cancelDeliveryOrder(
                                selectedOrder
                        );
                        if (isSuccessful) {
                            AlertPopUpWindow.displayInformationWindow(
                                    "Delivery Order Update",
                                    String.format(
                                            "Delivery order no.%d was " +
                                                    "cancelled successfully.",
                                            selectedOrder.getOrderId()
                                    ),
                                    "Ok"
                            );
                        }

                        refreshPendingDeliveryList();

                    } catch (TextFileNotFoundException ex) {
                        AlertPopUpWindow.displayErrorWindow(
                                ex.getMessage()
                        );
                        ex.printStackTrace();
                    }
                }
            });

            return new SimpleObjectProperty<>(cancelButton);
        });

        pendingDeliveryTable.setItems(data);

    }

    /**
     * This method refreshes the table of pending delivery orders.
     */
    public void refreshPendingDeliveryList() {

        pendingDeliveryTable.getItems().clear();
        data.clear();

        try {

            Kitchen kitchen = new Kitchen();

            kitchen.getPendingApprovalOrderData(data);

        } catch (TextFileNotFoundException e) {
            AlertPopUpWindow.displayErrorWindow(
                    e.getMessage()
            );
            e.printStackTrace();
        }

    }

    private Optional<ButtonType> promptForUserAcknowledgement(
            String header,
            String message
    ) {
        return AlertPopUpWindow.displayConfirmationWindow(
                header,
                message
        );
    }
}
