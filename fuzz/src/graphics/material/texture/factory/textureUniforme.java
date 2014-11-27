package graphics.material.texture.factory;

import graphics.Color.colorRGBA;
import graphics.material.texture.io.ioFormatInterface;
import graphics.material.texture.textureBase;

public class textureUniforme extends textureBase {
    private colorRGBA c = new colorRGBA();

    public void setColor(colorRGBA _c) {
        c = _c;
    }

    public colorRGBA getData(int x, int y) {
        return c;
    }

    public colorRGBA getData(double x, double y) {
        return c;
    }

    public void save(ioFormatInterface s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void copyDataFromArray(int[] source) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDatai(int x, int y, int z) {
        return c.geti();
    }

}
