package imageprocessing;

import java.awt.image.BufferedImage;

public class RotateIPThread implements Runnable {

    private BufferedImage newImage;
    private BufferedImage image;
    private int startRow;
    private int numRows;
    private PixelTransformer transformer;

    public RotateIPThread(PixelTransformer transformer, BufferedImage newImage, BufferedImage image, int startRow, int numRows) {
        this.newImage = newImage;
        this.image = image;
        this.startRow = startRow;
        this.numRows = numRows;
        this.transformer = transformer;

    }

    @Override
    public void run() {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = startRow; y < startRow + numRows; y++) {
            for (int x = 0; x < width; x++) {
                int[] coordinates = transformer.transform(x, y, width, height);
                newImage.setRGB(coordinates[0], coordinates[1], image.getRGB(x, y));
            }
        }
    }
}
