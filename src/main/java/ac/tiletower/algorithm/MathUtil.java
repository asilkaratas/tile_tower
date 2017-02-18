package ac.tiletower.algorithm;

/**
 * Created by asilkaratas on 12/3/16.
 */
public class MathUtil {


    /**
     *
     * @param x 0 to 12 or +inf
     * @return 1 to 0
     */
    public static double sigmoid(double x, double a) {
        return 2-2/(1+Math.exp(-x/a));
    }
}
