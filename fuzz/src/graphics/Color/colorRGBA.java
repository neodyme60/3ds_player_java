package graphics.Color;

public class colorRGBA {
    private static int MASK7Bit = 0xFEFEFF;
    private static int pixel;
    private static int overflow;

    private double rf, gf, bf, af;
    private int ri, gi, bi, ai;

    public colorRGBA() {
        setBlack();
    }

    public colorRGBA(double r, double g, double b, double a) {
        rf = r;
        gf = g;
        bf = b;
        ri = (int) (rf * 255.0f);
        gi = (int) (r * 255.0f);
        bi = (int) (r * 255.0f);
    }

    public colorRGBA(int r, int g, int b, int a) {
        ri = r;
        gi = g;
        bi = b;
        rf = ((double) r) / 255.0f;
        gf = ((double) g) / 255.0f;
        bf = ((double) b) / 255.0f;
    }

    //******************************************************
    public static int add(int c1, int c2) {
        colorRGBA.pixel = (c1 & colorRGBA.MASK7Bit) + (c2 & colorRGBA.MASK7Bit);
        colorRGBA.overflow = colorRGBA.pixel & 0x1010100;
        colorRGBA.overflow = colorRGBA.overflow - (colorRGBA.overflow >> 8);
        return colorRGBA.overflow | colorRGBA.pixel;
    }

    public static int transparency(int bkgrd, int color, int alpha)
    // alpha=0 : opaque , alpha=255: full transparent
    {
        if (0 == alpha) return color;
        if (255 == alpha) return bkgrd;
        if (127 == alpha) return mix(bkgrd, color);

        int r = (alpha * (((bkgrd >> 16) & 255) - ((color >> 16) & 255)) >> 8) + ((color >> 16) & 255);
        int g = (alpha * (((bkgrd >> 8) & 255) - ((color >> 8) & 255)) >> 8) + ((color >> 8) & 255);
        int b = (alpha * ((bkgrd & 255) - (color & 255)) >> 8) + (color & 255);

        return (r << 16) | (g << 8) | b;
    }

    public static final int blend(int c1, int c2, int value256) {
        // how much should we take from each color?
        int v1 = value256 & 0xFF;
        int v2 = 255 - v1;
        // get components
        int a1 = (c1 >> 24) & 0xFF;
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = (c1 >> 0) & 0xFF;
        int a2 = (c2 >> 24) & 0xFF;
        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = (c2 >> 0) & 0xFF;
        // mix them given the requested amount
        int a = (a1 * v1 + a2 * v2) >> 8;
        int r = (r1 * v1 + r2 * v2) >> 8;
        int g = (g1 * v1 + g2 * v2) >> 8;
        int b = (b1 * v1 + b2 * v2) >> 8;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static final int mulBlend(int c1, int c2) {
        // how much should we take from each color?
        // get components
        int a1 = (c1 >> 24) & 0xFF;
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = (c1 >> 0) & 0xFF;
        int a2 = (c2 >> 24) & 0xFF;
        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = (c2 >> 0) & 0xFF;
        // mix them given the requested amount
        int a = (a1 * a2) >> 8;
        int r = (r1 * r2) >> 8;
        int g = (g1 * g2) >> 8;
        int b = (b1 * b2) >> 8;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    //
    // Returns the averidge colorRGBA from 2 colors
    //
    public static int mix(int c1, int c2) {
        //		return ALPHA|(((color1&MASK7Bit)>>1)+((color2&MASK7Bit)>>1));
//        return (((color1 & colorRGBA.MASK7Bit) >> 1) + ((color2 & colorRGBA.MASK7Bit) >> 1));

        // This corresponds to "blend(c1, c2, 0x7f)"
        int c_RB = (((c1 & 0x00FF00FF) + (c2 & 0x00FF00FF)) >> 1) & 0x00FF00FF;
        int c_AG = (((c1 & 0xFF00FF00) >>> 1) + ((c2 & 0xFF00FF00) >>> 1)) & 0xFF00FF00;
        return c_RB | c_AG;
    }

    public static int mix(int c1, int c2, int c3, int c4) {
        int c_RB = (
                ((c1 & 0x00FF00FF) + (c2 & 0x00FF00FF) + (c3 & 0x00FF00FF)
                        + (c4 & 0x00FF00FF)) >> 2) & 0x00FF00FF;

        int c_AG = (
                ((c1 & 0xFF00FF00) >>> 2) + ((c2 & 0xFF00FF00) >>> 2) +
                        ((c3 & 0xFF00FF00) >>> 2) + ((c4 & 0xFF00FF00) >>> 2)
        ) & 0xFF00FF00;
        return c_RB | c_AG;
    }

    public int geti() {
        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }

    public double getRf() {
        return rf;
    }

    public void setRf(double r) {
        rf = r;
        ri = (int) (r * 255.0f);
    }

    public double getGf() {
        return gf;
    }

    public void setGf(double g) {
        gf = g;
        gi = (int) (g * 255.0f);
    }

    public double getBf() {
        return bf;
    }

    public void setBf(double b) {
        bf = b;
        bi = (int) (b * 255.0f);
    }

    public int getRi() {
        return ri;
    }

    public void setRi(int r) {
        ri = r;
        rf = ((double) r) / 255.0f;
    }

    public int getGi() {
        return gi;
    }

    public void setGi(int g) {
        gi = g;
        gf = ((double) g) / 255.0f;
    }

    public int getBi() {
        return bi;
    }

    public void setBi(int b) {
        bi = b;
        bf = ((double) b) / 255.0f;
    }

    public colorRGBA setWhite() {
        ri = gi = bi = 0xff;
        rf = gf = bf = 1.0f;
        return this;
    }

    public colorRGBA setBlack() {
        ri = gi = bi = 0x00;
        rf = gf = bf = 0.0f;
        return this;
    }
    /*
    public double colorRf;
    public double colorGf;
    public double colorBf;
    public int colorRi;
    public int colorGi;
    public int colorBi = 0;
    public int colorRGBA = 0;

    public static int setColor3i(int r, int g, int b)
    {
        colorRGBA = b | (g << 8) | (r << 16);
        colorRi = r;
        colorGi = g;
        colorBi = b;

        colorRf = ((double) r) / 255.0f;
        colorGf = ((double) g) / 255.0f;
        colorBf = ((double) b) / 255.0f;
    }

    public void setColor3f(double r, double g, double b)
    {
        colorRf = r;
        colorGf = g;
        colorBf = b;

        colorRi = (int) (r * 255.0f);
        colorGi = (int) (g * 255.0f);
        colorBi = (int) (b * 255.0f);

        colorRGBA = colorRi | (colorGi << 8) | (colorRi << 16);
    }
*/
}