import core.scenes.importer.format.format_3ds.format3ds;
import core.scenes.scene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

public class fuzzPlayer extends JFrame
{
    float fps = 60;
    float frame = 500;
    float fframe = 0;
    int scene_id = 0;
    int x = 150, y = 100, xdir = 1, ydir = 2;
    Image img;
    int _width = 600;
    int _height = 500;
    Panel panel;

    Image offScreenImage;
    MemoryImageSource mmm;
    ColorModel TargetCM;
    Thread playerThread = null;
    engine myEngine = new engine();
    Graphics DoubleBufferGraphics;
    MemoryImageSource TargetMIS;
    Image TargetImage;
    Object isRendering = new Object();
    long time = System.currentTimeMillis();
    boolean doitonce = false;

    fuzzPlayer() {
        boolean initial = true;
        setVisible(true);
        setSize(_width, _height);
        setResizable(false);

        myEngine.setRenderBuffer(_width, _height);

        TargetCM = new DirectColorModel(32, 0x00FF0000, 0x000FF00, 0x000000FF, 0);
        offScreenImage = createImage(_width, _height);
        TargetMIS = new MemoryImageSource(_width, _height, TargetCM, myEngine.getVideoBuffer().getPixelBufferPrevious(), 0, _width);

        scene s = new scene("File:/g:/dev/java/trunk/3dMotorNew/trunk/applet", "room");
        format3ds loader = new format3ds(s);
        myEngine.getSceneManager().addScene(s, loader);

//        myEngine.getSceneManager().addSceneFromFile("File:/D:/dev/java/trunk/3dMotor","chevy", fileFormat.fileFormat_e.format_3ds);

        playerThread = new Thread() {
            public void run() {
                while (true) {

                    if (myEngine.isLoading()) {
                    } else {
/*
                        myEngine.getVideoBuffer().swap();
                        myEngine.getVideoBuffer().clear(0);

                        myEngine.updateSceneAtTime(scene_id,frame, 0);
                        myEngine.render(scene_id, frame,0);

                        myEngine.getVideoBuffer().swap();
*/
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }

                }
            }
        };
        playerThread.start();
    }

    public static void main(String args[]) {
        fuzzPlayer SG = new fuzzPlayer();

        while (true) {
            SG.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public synchronized void update(Graphics g) {
        paint(g);
    }

    @Override
    public synchronized void paint(Graphics g) {

        if (myEngine.getSceneManager().isLoading()) {
            Dimension appletSize = this.getSize();
            g.fillRect(0, 0, appletSize.width, appletSize.height);
            g.setColor(Color.white);
            g.drawString("Please wait loading...", (appletSize.width / 2) - 30, (appletSize.height / 2));
        } else {
            if (doitonce == false) {
                doitonce = true;

                //patch scene
    /*
                materialsManager mm=materialsManager.getInstance();
                materialMirror mr=new materialMirror(256,256,"mirror");
                materialBase m=mm.getMaterialAt(2);
                scene s= myEngine.getSceneManager().getSceneAt(0);
                for(int i=0;i<s.getArrayMesh().size();i++)
                {
                    mesh_c me=(mesh_c)s.getArrayMesh().get(i);
                    if (me.getMaterial()==m)
                        me.setMaterial(mr);
                    for(int j=0;j<me.arrayFace.length;j++)
                    {
                        if (me.arrayFace[j].getMaterial()==m)
                        {
                            me.arrayFace[j].setMaterial(mr);
                        }

                    }
                }
 */
            }
            long time2 = System.currentTimeMillis();
            frame += fps * (time2 - time) / 1000;
            time = time2;

            //check frame looping
            if (frame >= myEngine.getSceneManager().getSceneAt(0).getKeyFramerEnd())
                frame = myEngine.getSceneManager().getSceneAt(0).getKeyFramerStart();
//frame=528;
            myEngine.getVideoBuffer().swap(scene_id);

            myEngine.updateSceneAtTime(scene_id, frame, 0);
            myEngine.render(scene_id, frame, 0);


            TargetMIS.newPixels(0, 0, _width, _height, false);
            TargetImage = createImage(TargetMIS);
            g.drawImage(TargetImage, 0, 0, this);

            g.setColor(Color.white);
            g.drawString("frame:" + java.lang.String.valueOf(frame), 5, 90);
//            g.drawString("FPS : " + fps + " fframe=" + fframe + " framespd=" + framespd, 5, 150);

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
//        TargetMIS.setAnimated(true);
//        TargetMIS.setFullBufferUpdates(true);

    }

}
