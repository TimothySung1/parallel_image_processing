package controllers;

import imageprocessing.ParallelIP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.image.BufferedImage;
import java.util.List;

public class OptionsController {

    public static Controller controller;
    private boolean multithread = false;
    private List<BufferedImage> previous;

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
        System.out.println(multithread);
    }
}
