package graphics.material.texture.factory;

import graphics.material.texture.io.ioFormatInterface;
import graphics.material.texture.textureBase;

public class textureEmpty extends textureBase {
    private int[] dataBuffer;

    public textureEmpty(int _width, int _height) {
        width = _width;
        height = _height;
        dataBuffer = new int[width * height];
    }

    @Override
    public int getDatai(int x, int y, int z) {
        int i = 0;
        try {
            int tx = (((x >> 16) % getWidth()) + getWidth()) % getWidth();
            int ty = (((y >> 16) % getHeight()) + getHeight()) % getHeight();
            i = dataBuffer[tx + (ty * getWidth())];
        } catch (Exception e) {
            System.out.print("eee");
        }

        return i;
    }


    @Override
    public void copyDataFromArray(int[] source) {
        System.arraycopy(source, 0, dataBuffer, 0, width * height);
    }

    @Override
    public void save(ioFormatInterface s) {
    }
}
