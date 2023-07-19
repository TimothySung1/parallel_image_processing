import java.io.IOException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;

public class ParallelIPTester {
    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                b.setRGB(x, y, source.getRGB(x, y));
            }
        }
        return b;
    }
    public static void main(String[] args) throws IOException {
        // BufferedImage image = ImageIO.read(new File("images/red-among-us-png.png"));

        // ImageIO.write(ParallelIP.invertImage(image, false), "png", new File("images/copy.png"));
        
        //BufferedImage invertedImage = ParallelIP.invertImage(image, false);
        //ImageIO.write(invertedImage, "png", new File("images/inverted_among_us.png"));

        BufferedImage image1 = ImageIO.read(new File("images/contacticon.png"));
        ImageIO.write(ParallelIP.convertColor(image1, Color.WHITE), "png", new File("images/newcontacticon.png"));
    }
}
