package controllers;

import imageprocessing.ParallelIP;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.script.Bindings;
import java.awt.image.BufferedImage;
import java.util.List;

public class OptionsController {

    public static Controller controller;
    private boolean multithread = false;
    private List<BufferedImage> previous;

    @FXML
    private VBox box;

    @FXML
    private RadioButton rotateButton;

    @FXML
    private HBox rotateBox;

    @FXML
    private HBox contrastBox;

    @FXML
    private RadioButton contrastButton;

    @FXML
    private Slider contrastSlider;

    @FXML
    private Label contrastLabel;


    @FXML
    private void original(ActionEvent e) {
        controller.updateDestImage(controller.getSrcImage());
    }

    @FXML
    private void invert(ActionEvent e) {
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.invertImage(src, false, multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void grayscale(ActionEvent e) {
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.grayScaleImage(src, multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void toggleMultithread(ActionEvent e) {
        this.multithread = !multithread;
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

    @FXML
    private void applyContrast(ActionEvent e) {
        BufferedImage src = controller.getSrcImage();
        BufferedImage dest = ParallelIP.setContrast(src, (int) contrastSlider.getValue(), multithread);
        controller.updateDestImage(dest);
    }

    @FXML
    private void convertColor(ActionEvent e) {

    }

    @FXML
    private void openResize(ActionEvent e) {

    }

    public void initialize() {
        rotateButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    rotateBox.setDisable(false);
                } else {
                    rotateBox.setDisable(true);
                }
            }
        });

        contrastButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean prevSelected, Boolean nowSelected) {
                if (nowSelected) {
                    contrastBox.setDisable(false);
                } else {
                    contrastBox.setDisable(true);
                }
            }
        });

        contrastSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int contrast = (int) contrastSlider.getValue();
                contrastLabel.setText(Integer.toString(contrast));
            }
        });
    }
}
