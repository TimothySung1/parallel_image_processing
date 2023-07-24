import java.util.function.Consumer;
import java.awt.image.BufferedImage;

public class IPThread implements Runnable {
    private RGBConverter converter;
    private BufferedImage image;
    private BufferedImage newImage;
    private int startRow;
    private int numRows;
    public IPThread(RGBConverter converter, BufferedImage newImage, BufferedImage image, int startRow, int numRows) {
        this.converter = converter;
        this.image = image;
        this.startRow = startRow;
        this.numRows = numRows;
        this.newImage = newImage;
    }

    @Override
    public void run() {
        int width = image.getWidth();
        for (int y = startRow; y < startRow + numRows; y++) {
            for (int x = 0; x < width; x++) {
                // System.out.printf("x: %d, y: %d\n", x, y);
                newImage.setRGB(x, y, converter.convert(image.getRGB(x, y)));
            }
        }
    }
}
