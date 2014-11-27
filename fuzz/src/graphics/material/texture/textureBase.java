package graphics.material.texture;

import core.primitive.Camera.cameraInterface;
import core.scenes.scene;
import graphics.Color.colorRGBA;
import graphics.material.texture.io.ioFormatInterface;

public abstract class textureBase implements textureInterface {
    protected String name = "no name";
    protected boolean isAnimated = false;
    protected boolean isLoaded = false;
    protected int width = 0;
    protected int height = 0;

    public void update(double time, cameraInterface c, scene s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    abstract public void copyDataFromArray(int[] source);


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public void load(ioFormatInterface s) {

    }

    public int[] getDataBuffer() {
        return null;
    }

    public colorRGBA getData(double x, double y, double z) {
        return new colorRGBA().setBlack();
    }

    public int getDatai(double x, double y, double z) {
        return 0;
    }

    public int getRedi(double x, double y, double z) {
        return 0;
    }

    public int getGreeni(double x, double y, double z) {
        return 0;
    }

    public int getBLuei(double x, double y, double z) {
        return 0;
    }

    public double getRedf(double x, double y, double z) {
        return 0;
    }

    public double getGreenf(double x, double y, double z) {
        return 0.0f;
    }

    public double getBLuef(double x, double y, double z) {
        return 0.0f;
    }

    public colorRGBA getData(int x, int y, int z) {
        return new colorRGBA().setBlack();
    }

    public int getDatai(int x, int y, int z) {
        return 0;
    }

    public int getRedi(int x, int y, int z) {
        return 0;
    }

    public int getGreeni(int x, int y, int z) {
        return 0;
    }

    public int getBLuei(int x, int y, int z) {
        return 0;
    }

    public double getRedf(int x, int y, int z) {
        return 0.0f;
    }

    public double getGreenf(int x, int y, int z) {
        return 0.0f;
    }

    public double getBLuef(int x, int y, int z) {
        return 0.0f;
    }
}
