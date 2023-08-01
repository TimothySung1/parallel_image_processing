package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

// TODO: add settings class that contains information (multithread, live display, etc)
// have method to transfer info to options controller

public class Controller {

    public static Stage primaryStage;
    public static Scene scene;
    public static OptionsController optionsController;
    private File srcPath;
    private File destPath;
    private BufferedImage srcImage;
    private BufferedImage destImage;

    private ImgType type;
    private FileChooser chooser;
    private FileChooser saver;
    @FXML
    private StackPane imgPane;
    private Image img;
    private ImageView imgView;

    public void selectFile(ActionEvent e) {
        // initialize chooser if not already
        if (chooser == null) {
            chooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image files", "*.jpeg", "*.png", "*.bmp");
            chooser.getExtensionFilters().add(filter);
            chooser.setTitle("Select an Image");
        }
        // set src and dst path the same
        srcPath = chooser.showOpenDialog(primaryStage);
        destPath = srcPath;

        // get file extension
        String ext = srcPath.getName().substring(srcPath.getName().length() - 5, srcPath.getName().length());
        int imgType = BufferedImage.TYPE_INT_RGB;;
        if (ext.equals(".jpeg")) {
            type = ImgType.jpeg;
        } else if (ext.substring(1, 5).equals(".png")) {
            type = ImgType.png;
            imgType = BufferedImage.TYPE_INT_ARGB;
        } else if (ext.substring(1, 5).equals(".bmp")) {
            type = ImgType.bmp;
        } else {
            System.out.println("Invalid extension");
        }

        // create src and destination images
        try {
            srcImage = ImageIO.read(srcPath);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        destImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), imgType);

        // add image to the image pane
        img = SwingFXUtils.toFXImage(srcImage, null);
        if (imgView == null) {
            imgView = new ImageView();
            imgView.setPreserveRatio(true);
            imgView.fitWidthProperty().bind(imgPane.widthProperty());
            imgView.fitHeightProperty().bind(imgPane.heightProperty());
            imgPane.getChildren().clear();
            imgPane.getChildren().add(imgView);
        }

        // toggle all checkboxes (except multithread)

        imgView.setImage(img);
        // System.out.println(type);
    }

    public void updateDestImage(BufferedImage image) {
        destImage = image;
        img = SwingFXUtils.toFXImage(image, null);
        imgView.setImage(img);
    }

    public void openSettings(ActionEvent e) {

    }

    public void save(ActionEvent e) throws IOException {
        // if destination path is equal to srcPath, call saveAs
        if (destPath == srcPath) {
            saveAs(e);
        } else {

            ImageIO.write(destImage, type.toString(), destPath);
        }
    }

    public void saveAs(ActionEvent e) throws IOException {
        System.out.println(type);
        if (saver == null) {
            saver = new FileChooser();
            saver.setTitle("Save an Image");
            saver.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.jpeg", "*.png", "*.bmp"));
        }
        destPath = saver.showSaveDialog(primaryStage);
        ImageIO.write(destImage, type.toString(), destPath);
    }

    enum ImgType {
        // gif can be used but needs plugin
        jpeg, png, bmp
    }

    public BufferedImage getDestImage() {
        return this.destImage;
    }
    public BufferedImage getSrcImage() {
        return this.srcImage;
    }
}
