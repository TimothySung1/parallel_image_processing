package controllers;

import imageprocessing.ParallelIP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.List;

public class OptionsController {

    public static Controller controller;
    private boolean multithread = false;
    private boolean hidden = true;
    private List<BufferedImage> previous;

    @FXML
    private VBox box;

    private HBox rotateBox;


    @FXML
    private void original(ActionEvent e) {
        controller.updateDestImage(controller.getSrcImage());
        hideRotate(e);
    }

    @FXML
    private void hideRotate(ActionEvent e) {
        if (hidden) return;
        box.getChildren().remove(5);
        hidden = true;
    }

    @FXML
    private void invert(ActionEvent e) {
        hideRotate(e);
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.invertImage(src, false, multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void grayscale(ActionEvent e) {
        hideRotate(e);
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.grayScaleImage(src, multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void toggleMultithread(ActionEvent e) {
        this.multithread = !multithread;
        System.out.println(multithread);
    }


    // add event handler to hbox to only show if rotate is toggled
    @FXML
    private void toggleRotate(ActionEvent e) {
        // create rotate buttons
        if (rotateBox == null) {

            rotateBox = new HBox();

            ToggleGroup rotateGroup = new ToggleGroup();
            RadioButton rotate90 = new RadioButton("Rotate 90");
            RadioButton rotate180 = new RadioButton("Rotate 180");
            RadioButton rotate270 = new RadioButton("Rotate 270");
            rotate90.setToggleGroup(rotateGroup);
            rotate180.setToggleGroup(rotateGroup);
            rotate270.setToggleGroup(rotateGroup);

            rotate90.setOnAction((event) -> {
                rotate90(event);
            });

            rotate180.setOnAction((event) -> {
                rotate180(event);
            });

            rotate270.setOnAction((event) -> {
                rotate270(event);
            });

            rotateBox.getChildren().add(rotate90);
            rotateBox.getChildren().add(rotate180);
            rotateBox.getChildren().add(rotate270);
        }
        for (Node node : rotateBox.getChildren()) {
            RadioButton button = (RadioButton) node;
            button.setSelected(false);
        }
        box.getChildren().add(5, rotateBox);
        hidden = false;
    }

    @FXML
    private void rotate90(ActionEvent e) {
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.rotate90(src, multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void rotate180(ActionEvent e) {
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.rotate180(src, multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void rotate270(ActionEvent e) {
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.rotate270(src, multithread);
        controller.updateDestImage(dest);
    }
}
