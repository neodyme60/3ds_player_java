package graphics.material.texture.factory;

import graphics.material.texture.io.ioBitmapFile;
import graphics.material.texture.io.ioFormatInterface;
import graphics.material.texture.textureBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class textureBitmapFile extends textureBase implements Runnable {
    private int[] dataBuffer;
    private String _uri = "";

    public void save(ioFormatInterface s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void copyDataFromArray(int[] source) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int[] getDataBuffer() {
        return dataBuffer;
    }

    public void textureBitmapFile() {
    }

    public void load(ioBitmapFile s) {
        _uri = s.getUri();
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        System.out.println("Download start for texture: " + getName());
        isLoaded = false;

        BufferedImage img = null;
        try {
            _uri = _uri.replace(".cel", ".jpg");
            _uri = _uri.replace(".gif", ".jpg");
            _uri = _uri.replace(".tga", ".jpg");
            img = ImageIO.read(new URL(_uri));

            System.out.println("Download end for texture: " + getName() + " " + img.getWidth(null) + " " + img.getHeight(null));
        } catch (Exception e) {
            img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < 256; x++)
                for (int y = 0; y < 256; y++) {
                    int c = ((x % 16) & (y % 16)) > 0 ? 0xffffff : 0x0;
                    img.setRGB(x, y, c);
                }

            System.out.println("Download error for texture: " + getName());
//            System.out.println(e.getStackTrace());
        }

        width = img.getWidth();
        height = img.getHeight(null);
        dataBuffer = new int[width * height];
        img.getRGB(0, 0, width, height, dataBuffer, 0, width);


        isLoaded = true;
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

}
