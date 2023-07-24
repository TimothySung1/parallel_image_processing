import java.awt.Color;

@FunctionalInterface
public interface ColorFunction {
    public Color convertColor(Color color);
}
