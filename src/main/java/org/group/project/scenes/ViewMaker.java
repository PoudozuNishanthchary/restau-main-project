package org.group.project.scenes;

import javafx.scene.Scene;

import java.io.IOException;

/**
 * This class ensures that all views involved implement the given method
 * that provide their scenes.
 *
 * @author azmi_maz
 */
public interface ViewMaker {
    Scene getScene() throws IOException;
}
