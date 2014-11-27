package graphics.material.channel;

import graphics.Color.colorRGBA;
import graphics.material.texture.textureInterface;

public class channelBase implements channelInterface {
    private textureInterface ti = null;

    public textureInterface getTexture() {
        return ti;
    }

    public void setTexture(textureInterface _ti) {
        ti = _ti;
    }

    public colorRGBA getData(double x, double y, double z) {
        if (ti == null) return new colorRGBA().setBlack();
        return ti.getData(x, y, z);
    }

    public int getDatai(double x, double y, double z) {
        if (ti == null) return 0;
        return ti.getDatai(x, y, z);
    }

    public int getRedi(double x, double y, double z) {
        if (ti == null) return 0;
        return ti.getRedi(x, y, z);
    }

    public int getGreeni(double x, double y, double z) {
        if (ti == null) return 0;
        return ti.getGreeni(x, y, z);
    }

    public int getBLuei(double x, double y, double z) {
        if (ti == null) return 0;
        return ti.getBLuei(x, y, z);
    }

    public double getRedf(double x, double y, double z) {
        if (ti == null) return 0.0f;
        return ti.getRedf(x, y, z);
    }

    public double getGreenf(double x, double y, double z) {
        if (ti == null) return 0.0f;
        return ti.getGreenf(x, y, z);
    }

    public double getBLuef(double x, double y, double z) {
        if (ti == null) return 0.0f;
        return ti.getBLuef(x, y, z);
    }

    public colorRGBA getData(int x, int y, int z) {
        if (ti == null) return new colorRGBA().setBlack();
        return ti.getData(x, y, z);
    }

    public int getDatai(int x, int y, int z) {
        if (ti == null) return 0;
        return ti.getDatai(x, y, z);
    }

    public int getRedi(int x, int y, int z) {
        if (ti == null) return 0;
        return ti.getRedi(x, y, z);
    }

    public int getGreeni(int x, int y, int z) {
        if (ti == null) return 0;
        return ti.getGreeni(x, y, z);
    }

    public int getBLuei(int x, int y, int z) {
        if (ti == null) return 0;
        return ti.getBLuei(x, y, z);
    }

    public double getRedf(int x, int y, int z) {
        if (ti == null) return 0.0f;
        return ti.getRedf(x, y, z);
    }

    public double getGreenf(int x, int y, int z) {
        if (ti == null) return 0.0f;
        return ti.getGreenf(x, y, z);
    }

    public double getBLuef(int x, int y, int z) {
        if (ti == null) return 0.0f;
        return ti.getBLuef(x, y, z);
    }

}
