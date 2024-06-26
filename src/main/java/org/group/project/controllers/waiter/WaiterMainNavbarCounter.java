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

/**
 * This class loads up the counter of pending table reservations and
 * pending delivery orders for waiters approval.
 *
 * @author azmi_maz
 */
public class WaiterMainNavbarCounter {
    private static final String PENDING = "pending-approval";
    private static final String COUNTERBOX_STYLE = "counterBox";
    private static final int LESS_THAN_TEN = 9;
    private static final int LESS_THAN_HUNDRED = 99;
    @FXML
    private HBox counterBox;

    @FXML
    private Label mainCounter1;

    @FXML
    private Label mainCounter2;

    /**
     * This method refreshes the counter of the combined pending items.
     */
    public void refreshMainCounter() {

        int newCounter = 0;

        try {

            Floor floor = new Floor();
            List<Booking> pendingBookingList = floor.getAllUniqueBookings();

            for (Booking booking : pendingBookingList) {

                // booking status
                String bookingStatus = booking.getBookingStatus();

                if (bookingStatus.equalsIgnoreCase(
                        PENDING)) {
                    newCounter++;
                }
            }

            Kitchen kitchen = new Kitchen();
            List<Order> pendingDeliveryList = kitchen.getAllOrderTickets();

            for (Order delivery : pendingDeliveryList) {

                if (kitchen.isDeliveryOrderClass(delivery)
                        && delivery.getOrderStatus()
                        .equalsIgnoreCase(PENDING)) {
                    newCounter++;
                }
            }

        } catch (TextFileNotFoundException e) {
            AlertPopUpWindow.displayErrorWindow(
                    e.getMessage()
            );
            e.printStackTrace();
        }

        if (newCounter == 0) {
            mainCounter1.setText("");
            mainCounter2.setText("");
            counterBox.getStyleClass().clear();
        } else if (newCounter > 0 && newCounter <= LESS_THAN_TEN) {
            mainCounter1.setText(String.valueOf(newCounter));
            mainCounter2.setText("");
            counterBox.getStyleClass().clear();
            counterBox.getStyleClass().add(COUNTERBOX_STYLE);
        } else if (newCounter > LESS_THAN_TEN
                && newCounter <= LESS_THAN_HUNDRED) {
            String count = String.valueOf(newCounter);
            mainCounter1.setText(String.valueOf(count.charAt(0)));
            mainCounter2.setText(String.valueOf(count.charAt(1)));
            counterBox.getStyleClass().clear();
            counterBox.getStyleClass().add(COUNTERBOX_STYLE);
        }
    }
}
