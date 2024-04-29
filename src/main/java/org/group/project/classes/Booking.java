package org.group.project.classes;

import org.group.project.classes.auxiliary.DataManager;
import org.group.project.exceptions.TextFileNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles booking details made by customer.
 *
 * @author azmi_maz
 */
public class Booking implements NotifyAction {
    private final int bookingId;
    private final Customer customer;
    private final LocalDate bookingDate;
    private final LocalTime bookingTime;
    private final int numOfGuests;
    private final int bookingLengthInHour;
    private final List<Table> tablePreference = new ArrayList<Table>();
    private String bookingStatus;

    /**
     * Booking constructor where the customer did not specify booking length.
     * Default of booking length is one hour.
     *
     * @param bookingId    - the unique id.
     * @param customer     - customer who is making a table reservation.
     * @param bookingDate  - the date for the table reservation.
     * @param bookingTime  - the time for the table reservation.
     * @param numOfGuests  - number of guests for the table reservation.
     * @param tableRequest - list of one table or more for the guests.
     */
    public Booking(int bookingId, Customer customer, LocalDate bookingDate,
                   LocalTime bookingTime, int numOfGuests,
                   List<Table> tableRequest) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.numOfGuests = numOfGuests;
        this.tablePreference.addAll(tableRequest);
        this.bookingLengthInHour = 1;
    }

    /**
     * Booking constructor where the customer did specify the booking length.
     *
     * @param bookingId     - the unique id.
     * @param customer      - customer who is making a table reservation.
     * @param bookingDate   - the date for the table reservation.
     * @param bookingTime   - the time for the table reservation.
     * @param numOfGuests   - number of guests for the table reservation.
     * @param tableRequest  - list of one table or more for the guests.
     * @param bookingLength - the booking length requested by customer.
     */
    public Booking(int bookingId, Customer customer, LocalDate bookingDate,
                   LocalTime bookingTime, int numOfGuests,
                   List<Table> tableRequest, int bookingLength) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.numOfGuests = numOfGuests;
        this.tablePreference.addAll(tableRequest);
        this.bookingLengthInHour = bookingLength;
    }

    /**
     * Booking constructor where booking data is fully complete for viewing
     * purpose.
     *
     * @param bookingId     - the unique id.
     * @param customer      - customer who is making a table reservation.
     * @param bookingDate   - the date for the table reservation.
     * @param bookingTime   - the time for the table reservation.
     * @param numOfGuests   - number of guests for the table reservation.
     * @param tableRequest  - list of one table or more for the guests.
     * @param bookingLength - the booking length requested by customer.
     */
    public Booking(int bookingId, Customer customer, LocalDate bookingDate,
                   LocalTime bookingTime, int numOfGuests,
                   List<Table> tableRequest, int bookingLength, String status) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.numOfGuests = numOfGuests;
        this.tablePreference.addAll(tableRequest);
        this.bookingLengthInHour = bookingLength;
        this.bookingStatus = status;
    }

    // TODO
    public Booking(
            int newBookingId,
            int customerId,
            LocalDate bookingDate,
            LocalTime bookingTime,
            int numOfGuests,
            Table tablePreference,
            int bookingLength
    ) throws TextFileNotFoundException {

        try {

            bookingId = newBookingId;
            UserManagement userManagement = new UserManagement();
            customer = userManagement.getCustomerById(customerId);
            this.bookingDate = bookingDate;
            this.bookingTime = bookingTime;
            this.numOfGuests = numOfGuests;
            this.tablePreference.add(tablePreference);
            this.bookingLengthInHour = bookingLength;
            bookingStatus = "pending-approval";

        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

    }

    // TODO comment
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Getter method to get customer.
     *
     * @return Customer who made the booking.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Getter method to get the customer id.
     *
     * @return The customer id who made the booking.
     */
    public int getCustomerId() {
        return customer.getCustomerId();
    }

    /**
     * Getter method to get the booking date.
     *
     * @return The booking date.
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    // TODO comment
    public String getBookingDateInFormat() {
        return bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Getter method to get the booking time.
     *
     * @return The booking time.
     */
    public LocalTime getBookingTime() {
        return bookingTime;
    }

    // TODO comment
    public String getBookingTimeInFormat() {
        return bookingTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * Getter method to get the number of guests.
     *
     * @return The number of guests of the booking.
     */
    public int getNumOfGuests() {
        return numOfGuests;
    }

    /**
     * Getter method to get the booking length.
     *
     * @return The booking length in hour.
     */
    public int getBookingLengthInHour() {
        return bookingLengthInHour;
    }

    /**
     * Getter method to get the table preferences.
     *
     * @return The table preferences of the booking.
     */
    public List<Table> getTablePreference() {
        return tablePreference;
    }

    /**
     * This method prints out the table lists in TableView friendly-format.
     *
     * @return the list of table preferences.
     */
    public String getTableNames() {
        return getTablePreference().toString().replace("[", "").replace("]", "");
    }

    /**
     * Getter method to get the booking status.
     *
     * @return The current booking status.
     */
    public String getBookingStatus() {
        return bookingStatus;
    }

    /**
     * This method checks if the number of guests matches or less than the
     * tables requested.
     *
     * @param tableRequest - List of tables that are requested by the customer.
     * @return true if booking can be made, false if not.
     */
    public boolean checksTableAvailability(List<Table> tableRequest) {
        int seatsAvailable = 0;
        int seatsNeeded = getNumOfGuests();
        for (Table table : tableRequest) {
            seatsAvailable += table.getNumberOfSeats();
        }

        // If the no. of seats available is more than the no. of seats needed
        // then it's ok to approve the booking.
        if (seatsAvailable >= seatsNeeded) {
            return true;
        }
        return false;
    }

    /**
     * This method updates the booking status.
     *
     * @param newStatus The new status to update the booking status.
     */
    public void updateBookingStatus(String newStatus) {
        this.bookingStatus = newStatus;
    }

    /**
     * This method gets the time when a booking ends.
     *
     * @return the time a booking ends.
     */
    public String getEndBookingTime() {
        return bookingTime
                .plusHours(bookingLengthInHour)
                .format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * This method show the period of booking time from start to end.
     *
     * @return the time period of a booking.
     */
    public String getTimePeriodOfBooking() {
        return bookingTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                + " - "
                + getEndBookingTime();
    }

    // TODO comment
    public String successfulBookingMessage() {
        return String.format(
                "Good news! Your table reservation "
                        + "for %s at %s "
                        + "is now confirmed.",
                getBookingDateInFormat(),
                getBookingTimeInFormat()
        );
    }

    // TODO comment
    public String failedBookingMessage() {
        return String.format(
                "We're terribly sorry! Unfortunately; your table reservation "
                        + "for %s at %s is not successful.",
                getBookingDateInFormat(),
                getBookingTimeInFormat()
        );
    }

    // TODO comment
    public void approveBooking() throws TextFileNotFoundException {

        try {
            DataManager.editColumnDataByUniqueId("BOOKINGS",
                    bookingId, "bookingStatus",
                    "approved");
        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

    }

    // TODO comment
    public void cancelBooking() throws TextFileNotFoundException {
        try {
            DataManager.editColumnDataByUniqueId("BOOKINGS",
                    bookingId, "bookingStatus",
                    "failed");
        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteBooking() throws TextFileNotFoundException {
        try {
            DataManager.deleteUniqueIdFromFile("BOOKINGS",
                    bookingId);
        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void notifyCustomer(Customer customer,
                               boolean isSuccessfulRequest)
            throws TextFileNotFoundException {

        try {

            NotificationBoard notificationBoard = new NotificationBoard();
            String notificationType = "booking";
            Notification newNotification = notificationBoard
                    .createNewNotification(
                            customer.getCustomerId(),
                            notificationType
                    );

            if (isSuccessfulRequest) {
                newNotification.setMessageBody(
                        successfulBookingMessage()
                );
            } else {
                newNotification.setMessageBody(
                        failedBookingMessage()
                );
            }

            notificationBoard.addNotificationToDatabase(
                    newNotification
            );

        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
