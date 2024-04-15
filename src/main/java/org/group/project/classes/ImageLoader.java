package org.group.project.classes;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.group.project.Main;

/**
 * This class set up graphic Javafx elements with png file.
 *
 * @author azmi_maz
 */
public class ImageLoader {

    public static void setUpGraphicButton(Button button,
                                          double width,
                                          double height, String fileName) {
        Image img = new Image(Main.class.getResourceAsStream(
                "images/icons/"+fileName+".png"));
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
        button.setGraphic(imgView);
//        URL imageUrl = Main.class.getResource("images/icons/resized.png");
//        button.setStyle("-fx-graphic: url('" + imageUrl + "');");
    }
}
