package imageprocessing;

@FunctionalInterface
public interface PixelTransformer {
    int[] transform(int x, int y, int width, int height);
}
