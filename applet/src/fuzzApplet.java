import core.scenes.importer.format.format_3ds.format3ds;
import core.scenes.scene;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;

public class fuzzApplet extends Applet {

        int width = 600;
        int height = 600;
        int scene_id = 0;

        Cursor emptyCursor, defaultCursor;
        boolean hadFocus = false;
        boolean running;
        Image offScreenImage;
        MemoryImageSource mmm;
        ColorModel TargetCM;
        Thread playerThread = null;
        engine myEngine = new engine();
        Graphics DoubleBufferGraphics;
        MemoryImageSource TargetMIS;
        Image TargetImage;
        private BufferedImage img;
        private int[] pixels;

        int nbframes;
        int fpstimer;
        float fframe;
        double fps = 60;
        float framespd;
        float frame = 0.0f;

        long time = System.currentTimeMillis();

    public fuzzApplet() {
        myEngine.setRenderBuffer(width, height);

//        TargetCM = new DirectColorModel(32, 0x00FF0000, 0x000FF00, 0x000000FF, 0);
//        offScreenImage = createImage(width, height);
//        TargetMIS = new MemoryImageSource(width, height, TargetCM,myEngine.getVideoBuffer().getPixelBufferPrevious(), 0, width);

        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty");
        defaultCursor = getCursor();


//        DoubleBufferGraphics = offScreenImage.getGraphics();

//            URL u=new URL("file:core.scenes/head/scene.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/birdwalk/scene.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/chevy/scene.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/room/scene.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/head/scene.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/girl/scene.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/cityanim/scene.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/cruiser/scene.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/roby/scene.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/gato/scene.3ds"); //ok
//            URL u=new URL(getCodeBase()+"core.scenes/flyg/scene.3ds"); //error dummy
//            URL u=new URL(getCodeBase()+"core.scenes/dude/scene.3ds"); //error dummy
//            URL u=new URL(getCodeBase()+"core.scenes/50PMAN/scene.3ds"); //pb map
//            URL u=new URL(getCodeBase()+"core.scenes/poser/scene.3ds"); //ok
//            URL u=new URL(getCodeBase()+"core.scenes/piston/scene.3ds"); //pb ?
//            URL u=new URL(getCodeBase()+"core.scenes/step/scene.3ds"); //error dummy
//            URL u=new URL(getCodeBase()+"core.scenes/ik_dino/scene.3ds"); //error dy ummy
//            URL u=new URL(getCodeBase()+"core.scenes/achooo/scene.3ds"); //error
//            URL u=new URL(getCodeBase()+"core.scenes/enginani/scene.3ds"); //error  chunk !!
//            URL u=new URL(getCodeBase()+"core.scenes/hh/scene.3ds"); //ok
//            URL u=new URL(getCodeBase()+"core.scenes/flagmorph.3ds"); //error  chunk !!
//            URL u=new URL(getCodeBase()+"core.scenes/MARBVASE.3ds"); //ok
//            URL u=new URL(getCodeBase()+"core.scenes/robot.3ds");//pb pose keyframer
//            URL u=new URL(getCodeBase()+"core.scenes/dude1.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/pool.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/gold.3ds"); //rien a l'ecran
//            URL u=new URL(getCodeBase()+"core.scenes/BIRDSHOW.3ds"); //rien a l'ecran
//            URL u=new URL(getCodeBase()+"core.scenes/CA-CHOPP.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/EYEBALL.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/KSCR5.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/chopper.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/chopper2.3ds");//error
//            URL u=new URL(getCodeBase()+"core.scenes/chopper3.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/EYEBALL.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/hand.3ds");  //cyclyque keyframer reference
//            URL u=new URL(getCodeBase()+"core.scenes/COLUMN.3ds"); //error
//            URL u=new URL(getCodeBase()+"core.scenes/mouth.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/JELYFISH.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/ROBOTARM.3ds");
//            URL u=new URL(getCodeBase()+"core.scenes/MASK.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/stones.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/TUBER2.3ds");//error keyframer before
//            URL u=new URL(getCodeBase()+"core.scenes/wire.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/climb.3ds");//error
//            URL u=new URL(getCodeBase()+"core.scenes/stairs.3ds");//error
//            URL u=new URL(getCodeBase()+"core.scenes/QBICDEMO.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/LAMP3.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/KEROLAMP.3ds");//ok
//            URL u=new URL(getCodeBase()+"core.scenes/thead2.3ds");// super !!
//            URL u=new URL(getCodeBase()+"core.scenes/DS13DS02.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/DS13DS04.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/droid.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/church2.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/church.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/head_femal.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/obama.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/GAZEBO.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/Freebie.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/Mosque_Masjed_Mohammad_Ali.3ds");//
//            URL u=new URL(getCodeBase()+"core.scenes/Ruined_Building.3ds");//


        scene s = new scene(fuzzApplet.class.getResource(".").toString(), "house3");
        format3ds loader = new format3ds(s);
        myEngine.getSceneManager().addScene(s, loader);
    }

    private void tick() {
        if (hasFocus()) {
            //game.tick(inputHandler.keys);
        }
    }

    public void run() {
        int frames = 0;

        double unprocessedSeconds = 0;
        long lastTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;

        requestFocus();

        while (running) {
            long now = System.nanoTime();
            long passedTime = now - lastTime;
            lastTime = now;
            if (passedTime < 0) passedTime = 0;
            if (passedTime > 100000000) passedTime = 100000000;

            unprocessedSeconds += passedTime / 1000000000.0;

            boolean ticked = false;
            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;

                tickCount++;
                if (tickCount % 60 == 0) {
                    System.out.println(frames + " fps");
                    lastTime += 1000;
                    frames = 0;
                }
            }

            if (ticked) {
                render();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void render() {
        if (hadFocus != hasFocus()) {
            hadFocus = !hadFocus;
            setCursor(hadFocus ? emptyCursor : defaultCursor);
        }
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        try {
            if (myEngine.isLoading()) {
                Dimension appletSize = this.getSize();
                g.fillRect(0, 0, appletSize.width, appletSize.height);
                g.setColor(Color.white);
                g.drawString("Please wait loading...", (appletSize.width / 2) - 30, (appletSize.height / 2));
            } else {
                myEngine.getVideoBuffer().swap(scene_id);

                long time2 = System.currentTimeMillis();
                frame += fps * (time2 - time) / 1000;
                time = time2;


                //check frame looping
                if (frame >= myEngine.getSceneManager().getSceneAt(0).getKeyFramerEnd())
                    frame = myEngine.getSceneManager().getSceneAt(0).getKeyFramerStart();

                myEngine.updateSceneAtTime(scene_id, frame, 0);


                myEngine.render(scene_id, frame, 0);

                TargetMIS.newPixels(0, 0, width, height, false);
                TargetImage = createImage(TargetMIS);
                g.drawImage(TargetImage, 0, 0, this);

                //update rendering buffer
    /*
                g.drawImage(TargetImage, 0, 0, this);
                g.setColor(Color.white);
                g.drawString("frame:" + java.lang.String.valueOf(frame), 5, 90);
                g.drawString("FPS : " + fps + " fframe=" + fframe + " framespd=" + framespd, 5, 150);
    */
            }
            repaint();
        }
        catch (Exception ex)
        {

        }

    }

    //implement the "start" methode of  "applet" class
    //start is called after init();
    public void start()
    {
        if (running) return;
        running = true;
        playerThread = new Thread(this);
        playerThread.start();
    }

    //implement the "stop"  methode of  "applet" class
    public void stop()
    {
        if (!running) return;
        running = false;
        try
        {
            playerThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}