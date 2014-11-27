package graphics.drawing;

import graphics.videoBuffer_c;

public class line {
    public final static int FRACBITS = 16;

    //
    // DDA Line Algorithm
    // fixed-point arithmetic to assure we get sub-pixel accuracy.
    //
    public static void draw(videoBuffer_c buf, double x1, double y1, double x2, double y2, int color) {
        int length;
        int x;
        int y;
        int xx;
        int yy;
        int index;
        int xincrement;
        int yincrement;
        double z;

        int xx1 = (int) (x1 * (1 << FRACBITS));
        int yy1 = (int) (y1 * (1 << FRACBITS));
        int xx2 = (int) (x2 * (1 << FRACBITS));
        int yy2 = (int) (y2 * (1 << FRACBITS));

        length = Math.abs(xx2 - xx1);

        if (Math.abs(yy2 - yy1) > length)
            length = Math.abs(yy2 - yy1);

        if (length == 0)
            return;

        length = length >> FRACBITS;

        xincrement = (xx2 - xx1) / length;
        yincrement = (yy2 - yy1) / length;

        x = xx1;
        y = yy1;
        for (int i = 1; i <= length; ++i) {
            xx = x >> FRACBITS;
            yy = y >> FRACBITS;
            if (xx < buf.xSize & 0 <= xx & yy < buf.ySize & 0 < yy) {
                index = (buf.xSize * yy + xx);
                buf.getPixelBufferCurrent()[index] = color;
            }
            x += xincrement;
            y += yincrement;
        }
    }

    //
    // DDA Line Algorithm with Zbuffer
    // fixed-point arithmetic to assure we get sub-pixel accuracy.
    //
    public static void drawZ(videoBuffer_c buf, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        int length;
        int x;
        int y;
        int xx;
        int yy;
        int index;
        int xincrement;
        int yincrement;
        double zincrement;
        double z;

        int xx1 = (int) (x1 * (1 << FRACBITS));
        int yy1 = (int) (y1 * (1 << FRACBITS));
        int xx2 = (int) (x2 * (1 << FRACBITS));
        int yy2 = (int) (y2 * (1 << FRACBITS));

        length = Math.abs(xx2 - xx1);

        if (Math.abs(yy2 - yy1) > length)
            length = Math.abs(yy2 - yy1);

        length = length >> FRACBITS;

        if (length == 0)
            return;

        xincrement = (xx2 - xx1) / length;
        yincrement = (yy2 - yy1) / length;
        zincrement = (z2 - z1) / length;

        x = xx1;
        y = yy1;
        z = z1;
        for (int i = 1; i <= length; ++i) {
            xx = x >> FRACBITS;
            yy = y >> FRACBITS;
            if (xx < buf.xSize & 0 <= xx & yy < buf.ySize & 0 < yy) {
                index = (buf.xSize * yy + xx);
                if (z < buf.zBuffer[index]) {
                    buf.zBuffer[index] = z;            //update zbufer
                    buf.getPixelBufferCurrent()[index] = color;
                }
            }
            x += xincrement;
            y += yincrement;
            z += zincrement;
        }
    }
}