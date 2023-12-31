package imageprocessing;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/*
 * Build a javafx program that can take in image files and can convert their filetype, set individual colors to other colors,
 * save and download the output images, grayscale, crop, basically a mini photoshop
 */

 // TODO: make private methods that invert, grayscale, etc the int rgb and pass those directly into the threads
 // TODO: image enhancing, compression, restoration ?
 // TODO: for other image processing, use a framework ?

public class ParallelIP {

    private static int numThreads = 6;

    public static BufferedImage copyImage(BufferedImage image, boolean multithread) {
        int height = image.getHeight();
        int width = image.getWidth();

        return null;
    }

    public static BufferedImage invertImage(BufferedImage image, boolean invertAlpha, boolean multithread) {
        int height = image.getHeight();
        int width = image.getWidth();
        
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        if (!multithread) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    // get rgb value of current pixel
                    int rgb = image.getRGB(j, i);
                    int[] colors = getColors(rgb);
                    // invert the pixel values
                    if (invertAlpha) {
                        colors[0] = 255 - colors[0];
                    }
                    for (int k = 1; k < 4; k++) {
                        colors[k] = 255 - colors[k];
                    }
                    // set inverted pixel to new image
                    int inverted = getRGB(colors);
                    newImage.setRGB(j, i, inverted);
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;
            // create an rgb inverter to be used in the threads
            RGBConverter rgbInverter = (int rgb) -> {
                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                if (invertAlpha) a = 255 - a;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                return getRGB(a, r, g, b);
            };
            Thread[] threads = new Thread[numThreads];
            // create threads, inverting in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new IPThread(rgbInverter, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new IPThread(rgbInverter, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return newImage;
    }

    public static BufferedImage grayScaleImage(BufferedImage image, boolean multithread) {
        // weighted/luminosity: grayscale = .299r + .587g + .114b
        
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int[] colors = getColors(rgb);
                    int grayscale = (int) (.299 * colors[1] + .587 * colors[2] + .114 * colors[3]);
                    for (int i = 1; i < 4; i++) {
                        colors[i] = grayscale;
                    }
                    newImage.setRGB(x, y, getRGB(colors));
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            RGBConverter grayscaler = (int rgb) -> {
                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                int grayscale = (int) (.299 * r + .587 * g + .114 * b);
                return getRGB(a, grayscale, grayscale, grayscale);
            };

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new IPThread(grayscaler, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new IPThread(grayscaler, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return newImage;
    }

    // Converts the entire image into the rgb value replaceWith
    // Assume replaceWith has no alpha value
    public static BufferedImage convertColor(BufferedImage image, int replaceWith, boolean multithread) {
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int[] colors = getColors(rgb);
                    if (colors[0] == 0) {
                        newImage.setRGB(x, y, rgb);
                    } else {
                        // keep original opacity
                        int a = colors[0];
                        newImage.setRGB(x, y, replaceWith | (a << 24));
                    }
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            RGBConverter converter = (int rgb) -> {
                return (replaceWith & 0x00ffffff) | (rgb & 0xff000000);
            };

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new IPThread(converter, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new IPThread(converter, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImage;
    }

    public static BufferedImage convertColor(BufferedImage image, int toReplace, int replaceWith, boolean multithread) {
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    // if color matches toReplace, replace it
                    // ignore alpha
                    if ((rgb & 0x00ffffff) == (toReplace & 0x00ffffff)) {
                        // keep original alpha, use replaceWith RGB
                        newImage.setRGB(x, y, (rgb & 0xff000000) | (replaceWith & 0x00ffffff));
                    } else {
                        newImage.setRGB(x, y, rgb);
                    }
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            RGBConverter converter = (int rgb) -> {
                if ((rgb & 0x00ffffff) == (toReplace & 0x00ffffff)) {
                    return (rgb & 0xff000000) | (replaceWith & 0x00ffffff);
                }
                return rgb;
            };

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new IPThread(converter, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new IPThread(converter, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImage;
    }

    public static BufferedImage rotate(BufferedImage image, int degree, boolean multithread) {
        if (degree == 90) {
            return rotate90(image, multithread);
        } else if (degree == 180) {
            return rotate180(image, multithread);
        } else if (degree == 270) {
            return rotate270(image, multithread);
        }
        // BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return null;
    }

    public static BufferedImage rotate90(BufferedImage image, boolean multithread) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage newImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    newImage.setRGB(y, width - x - 1, rgb);
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            PixelTransformer transformer = (x, y, w, h) -> {
                int[] coords = new int[2];
                coords[0] = y;
                coords[1] = w - x - 1;
                return coords;
            };

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new RotateIPThread(transformer, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new RotateIPThread(transformer, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImage;
    }

    public static BufferedImage rotate180(BufferedImage image, boolean multithread) {
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    newImage.setRGB(width - x - 1, height - y - 1, rgb);
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            PixelTransformer transformer = (x, y, w, h) -> {
                int[] coords = new int[2];
                coords[0] = w - x - 1;
                coords[1] = h - y - 1;
                return coords;
            };

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new RotateIPThread(transformer, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new RotateIPThread(transformer, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImage;
    }

    public static BufferedImage rotate270(BufferedImage image, boolean multithread) {
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    newImage.setRGB(height - y - 1, x, rgb);
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            PixelTransformer transformer = (x, y, w, h) -> {
                int[] coords = new int[2];
                coords[0] = h - y - 1;
                coords[1] = x;
                return coords;
            };

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new RotateIPThread(transformer, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new RotateIPThread(transformer, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImage;
    }

    // contrast between -255 and 255
    public static BufferedImage setContrast(BufferedImage image, int contrast, boolean multithread) {
        // determine contrast
        double contrastCorrectionFactor = 259 * (contrast + 255.0) / (255 * (259 - contrast));
        RGBConverter converter = (rgb) -> {
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;

            // calculate altered rgb value
            int newR = (int) (contrastCorrectionFactor * (r - 128) + 128);
            int newG = (int) (contrastCorrectionFactor * (g - 128) + 128);
            int newB = (int) (contrastCorrectionFactor * (b - 128) + 128);

            // truncate rgb values to the range 0-255 inclusive
            if (newR > 255) newR = 255;
            if (newG > 255) newG = 255;
            if (newB > 255) newB = 255;

            if (newR < 0) newR = 0;
            if (newG < 0) newG = 0;
            if (newB < 0) newB = 0;

            return (a << 24) | (newR << 16) | (newG << 8) | (newB);
        };

        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        if (!multithread) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    newImage.setRGB(x, y, converter.convert(rgb));
                }
            }
        } else {
            int numRows = height / numThreads;
            int extra = height % numThreads;

            Thread[] threads = new Thread[numThreads];
            // create threads, grayscaling in their respective rows (divide process)
            for (int i = 0; i < numThreads - 1; i++) {
                threads[i] = new Thread(new IPThread(converter, newImage, image, i * numRows, numRows));
            }
            threads[numThreads - 1] = new Thread(new IPThread(converter, newImage, image, numRows * (numThreads - 1), numRows + extra));
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImage;
    }

    private static void processConversion(int height, RGBConverter converter) {

    }

    private static void processTransformation(int height, PixelTransformer transformer) {
        
    }

    private static int[] getColors(int argb) {
        int alpha = (argb >> 24) & 0xff;
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = argb & 0xff;
        
        return new int[] {alpha, r, g, b};
    }

    private static int getRGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
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
}