import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/*
 * Build a javafx program that can take in image files and can convert their filetype, set individual colors to other colors,
 * save and download the output images, grayscale, crop, basically a mini photoshop
 */

public class ParallelIP {

    private static int numThreads = 1;

    // public static BufferedImage invertImage(BufferedImage image, boolean invertAlpha, boolean multithread) {
    //     int height = image.getHeight();
    //     int width = image.getWidth();
        
    //     BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //     // if numThreads > height, separate by width
    //     // in each thread, loop through designated rows until finished

    //     if (!multithread) {
    //         for (int i = 0; i < height; i++) {
    //             for (int j = 0; j < width; j++) {
    //                 // get rgb value of current pixel
    //                 int rgb = image.getRGB(j, i);
    //                 int[] colors = getColors(rgb);
    //                 // invert the pixel values
    //                 if (invertAlpha) {
    //                     colors[0] = 255 - colors[0];
    //                 }
    //                 for (int k = 1; k < 4; k++) {
    //                     colors[k] = 255 - colors[k];
    //                 }
    //                 // set inverted pixel to new image
    //                 int inverted = getRGB(colors);
    //                 newImage.setRGB(j, i, inverted);
    //             }
    //         }
    //     } else {
    //         int numRows = height / numThreads;
    //         int extra = height % numThreads;
    //         int i = 0;
    //         while (i < numThreads) {
    //             new Thread(() -> {
    //                 for (int row = i * numRows; row < height; row++) {

    //                 }
    //             });
    //             i++;
    //         }
    //     }
        
    //     return newImage;
    // }

    public static BufferedImage grayScaleImage(BufferedImage image) {
        return null;
    }

    public static BufferedImage convertColor(BufferedImage image, Color replaceWith) {
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int[] colors = getColors(rgb);
                if (colors[0] == 0) {
                    newImage.setRGB(x, y, rgb);
                } else {
                    // keep original opacity
                    int a = colors[0];
                    newImage.setRGB(x, y, replaceWith.getRGB() | (a << 24));
                }
            }
        }

        return newImage;
    }

    public static BufferedImage convertColor(BufferedImage image, Color toReplace, Color replaceWith) {
        return null;
    }

    private static int[] getColors(int argb) {
        int alpha = (argb >> 24) & 0xff;
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = argb & 0xff;
        
        return new int[] {alpha, r, g, b};
    }

    private static int getRGB(int alpha, int red, int green, int blue) {
        return 0;
    }

    private static int getRGB(int[] colors) {
        return colors[0] << 24 | colors[1] << 16 | colors[2] << 8 | colors[3];
    }

    private static BufferedImage copyImage(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        newImage.getGraphics().drawImage(image, 0, 0, null);
        return newImage;
    }

    public static int getNumThreads() {
        return numThreads;
    }

    public static void setNumThreads(int numThreads) {
        ParallelIP.numThreads = numThreads;
    }

    public static void main(String[] args) throws IOException {
        /*
        // width and height of image
        int width;
        int height;
        String path = "C:/Users/Desktop/Projects/Parallel Image Processing/images/red-among-us-png";

        BufferedImage image = null;
        BufferedImage newImage = null;

        // read image
        
        File input_image = new File("images/red-among-us-png.png");
        image = ImageIO.read(input_image);
        width = image.getWidth();
        height = image.getHeight();

        newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        newImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

        
        

        // write image (transparent bg becomes white)

        ImageIO.write(newImage, "png", new File("test2.png"));
        // ImageIO.write(image, "png", new File("images/test.png"));
        */
    }
}