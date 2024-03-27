package org.group.project.stackscenes.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.group.project.MainAppStack;
import org.group.project.stackscenes.presenter.FirstPresenter;

import java.io.IOException;

public class FirstView implements PresenterView {

    private Scene scene;

    private FirstPresenter presenter;

    @FXML
    private Label label;

    @FXML
    private Button nextButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label resultBox;

    public FirstView(FirstPresenter presenter) {
        this.presenter = presenter;

        try {
            FXMLLoader fxmlLoader =
                new FXMLLoader(MainAppStack.class.getResource("first.fxml"));
            fxmlLoader.setController(this);
            scene = new Scene(fxmlLoader.load(), 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        nextButton.setOnAction(e -> {
            presenter.goToSecondScene();
        });

        exitButton.setOnAction(e -> {
            presenter.exit();
        });

    }

    @Override
    public Scene getViewScene() {
        return scene;
    }
}
