package org.group.project.controllers.waiter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.group.project.classes.Booking;
import org.group.project.classes.Floor;
import org.group.project.classes.Kitchen;
import org.group.project.classes.Order;
import org.group.project.classes.auxiliary.AlertPopUpWindow;
import org.group.project.exceptions.TextFileNotFoundException;

import java.util.List;

public class WaiterMainNavbarCounter {

    @FXML
    private HBox counterBox;

    @FXML
    private Label mainCounter1;

    @FXML
    private Label mainCounter2;

    public void refreshMainCounter() {

        int newCounter = 0;

        try {

            Floor floor = new Floor();
            List<Booking> pendingBookingList = floor.getAllUniqueBookings();

            for (Booking booking : pendingBookingList) {

                // booking status
                String bookingStatus = booking.getBookingStatus();

                if (bookingStatus.equalsIgnoreCase("pending-approval")) {
                    newCounter++;
                }
            }

            Kitchen kitchen = new Kitchen();
            List<Order> pendingDeliveryList = kitchen.getAllOrderTickets();

            for (Order delivery : pendingDeliveryList) {

                if (kitchen.isDeliveryOrderClass(delivery)
                        && delivery.getOrderStatus()
                        .equalsIgnoreCase("pending-approval")) {
                    newCounter++;
                }
            }

        } catch (TextFileNotFoundException e) {
            AlertPopUpWindow.displayErrorWindow(
                    "Error",
                    e.getMessage()
            );
            e.printStackTrace();
        }

        if (newCounter == 0) {
            mainCounter1.setText("");
            mainCounter2.setText("");
            counterBox.getStyleClass().clear();
        } else if (newCounter > 0 && newCounter <= 9) {
            mainCounter1.setText(String.valueOf(newCounter));
            mainCounter2.setText("");
            counterBox.getStyleClass().clear();
            counterBox.getStyleClass().add("counterBox");
        } else if (newCounter > 9 && newCounter <= 99) {
            String count = String.valueOf(newCounter);
            mainCounter1.setText(String.valueOf(count.charAt(0)));
            mainCounter2.setText(String.valueOf(count.charAt(1)));
            counterBox.getStyleClass().clear();
            counterBox.getStyleClass().add("counterBox");
        }
    }
}
