package graphics.material.texture;

import core.primitive.Camera.cameraInterface;
import core.scenes.scene;
import graphics.Color.colorRGBA;
import graphics.material.texture.io.ioFormatInterface;

public interface textureInterface {
    public void load(ioFormatInterface s);

    public void save(ioFormatInterface s);

    public void update(double time, cameraInterface c, scene s);

    public colorRGBA getData(double x, double y, double z);

    public int getDatai(double x, double y, double z);

    public int getRedi(double x, double y, double z);

    public int getGreeni(double x, double y, double z);

    public int getBLuei(double x, double y, double z);

    public double getRedf(double x, double y, double z);

    public double getGreenf(double x, double y, double z);

    public double getBLuef(double x, double y, double z);

    public colorRGBA getData(int x, int y, int z);

    public int getDatai(int x, int y, int z);

    public int getRedi(int x, int y, int z);

    public int getGreeni(int x, int y, int z);

    public int getBLuei(int x, int y, int z);

    public double getRedf(int x, int y, int z);

    public double getGreenf(int x, int y, int z);

    public double getBLuef(int x, int y, int z);

    public boolean isLoaded();

    public int getWidth();

    public int getHeight();

    public int[] getDataBuffer();
}
