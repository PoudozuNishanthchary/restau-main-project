package org.group.project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.group.project.classes.User;
import org.group.project.classes.auxiliary.AlertPopUpWindow;
import org.group.project.classes.auxiliary.DataManager;
import org.group.project.exceptions.ClearFileFailedException;
import org.group.project.scenes.MainScenes;
import org.group.project.scenes.chef.ChefScenesMap;
import org.group.project.scenes.customer.CustomerScenesMap;
import org.group.project.scenes.driver.DriverScenesMap;
import org.group.project.scenes.main.LoginView;
import org.group.project.scenes.manager.ManagerScenesMap;
import org.group.project.scenes.waiter.WaiterScenesMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is main class that starts the application.
 *
 * @author azmi_maz
 */
public class Main extends Application {

    private static final String restaurantName =
            "Cafe94 Restaurant";
    private static final String OK = "OK_DONE";
    private static final String ACTIVE_USER_FILE = "ACTIVE_USER";
    private static final String EXIT = "Exit";
    private static final String CONFIRM_EXIT = "Do you want to exit " +
            "the program?";
    private static Map<MainScenes, Scene> scenes = new HashMap<>();

    private static Stage stage;

    private static User currentUser;

    /**
     * This method starts the application.
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     */
    @Override
    public void start(Stage stage) {

        Main.stage = stage;

        // Holds the various user scenes to switch between.
        scenes.put(MainScenes.LOGIN,
                new LoginView(stage).getScene());
        scenes.put(MainScenes.CUSTOMER,
                new CustomerScenesMap(stage).getScene());
        scenes.put(MainScenes.MANAGER,
                new ManagerScenesMap(stage).getScene());
        scenes.put(MainScenes.WAITER,
                new WaiterScenesMap(stage).getScene());
        scenes.put(MainScenes.CHEF,
                new ChefScenesMap(stage).getScene());
        scenes.put(MainScenes.DRIVER,
                new DriverScenesMap(stage).getScene());


        // Starts with user log in.
        stage.setScene(scenes.get(MainScenes.LOGIN));
        stage.setTitle(restaurantName);
        stage.show();

        // When user wants to close the application.
        stage.setOnCloseRequest(e -> {

            Optional<ButtonType> userChoice = promptForUserAcknowledgement();

            if (userChoice.get()
                    .getButtonData().toString()
                    .equalsIgnoreCase(OK)) {

                try {
                    DataManager.clearFileData(ACTIVE_USER_FILE);
                } catch (ClearFileFailedException ex) {
                    AlertPopUpWindow.displayErrorWindow(
                            ex.getMessage()
                    );
                    ex.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }
            // This cancels the exit request.
            e.consume();

        });

    }

    /**
     * This getter method gets the Map of main scenes.
     *
     * @return Returns a Map of the scenes.
     */
    public static Map<MainScenes, Scene> getScenes() {
        return scenes;
    }

    /**
     * This method gets the stage used by Main.
     *
     * @return the active stage.
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * This method gets the current user currently logged in.
     *
     * @return - the current user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * This method sets the user that successfully logged in as the active
     * user.
     *
     * @param user - the logged-in user;
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    private Optional<ButtonType> promptForUserAcknowledgement() {
        return AlertPopUpWindow.displayConfirmationWindow(
                EXIT,
                CONFIRM_EXIT
        );
    }

    /**
     * This the main method used to launch the application.
     *
     * @param args - empty/ not used for this application.
     */
    public static void main(String[] args) {
        launch();
    }


}