package math;

public class fast {


    /**
     * Fast approximation of 1.0 / sqrt(x).
     * See <a href="http://www.beyond3d.com/content/articles/8/">http://www.beyond3d.com/content/articles/8/</a>
     *
     * @param x Positive value to estimate inverse of square root of
     * @return Approximately 1.0 / sqrt(x)
     */
    public static double invSqrt(double x) {
        double xhalf = 0.5 * x;
        long i = Double.doubleToRawLongBits(x);
        i = 0x5FE6EB50C7B537AAL - (i >> 1);
        x = Double.longBitsToDouble(i);
        x = x * (1.5 - xhalf * x * x);
        return x;
    }

    public static double exp(double val) {
        final long tmp = (long) (1512775 * val + (1072693248 - 60801));
        return Double.longBitsToDouble(tmp << 32);
    }

    //fast square root for intel cpu
    //http://martin.ankerl.com/2009/01/05/approximation-of-sqrtx-in-java/
    //
    public static double sqrt(final double a) {
        final long x = Double.doubleToLongBits(a) >> 32;
        double y = Double.longBitsToDouble((x + 1072632448) << 31);

        // repeat the following line for more precision
        //y = (y + a / y) * 0.5;
        return y;
    }

    //http://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
    public static double pow(final double a, final double b) {
        final int x = (int) (Double.doubleToLongBits(a) >> 32);
        final int y = (int) (b * (x - 1072632447) + 1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }

    public double ln(double val) {
        final double x = (Double.doubleToLongBits(val) >> 32);
        return (x - 1072632447) / 1512775;
    }
}
