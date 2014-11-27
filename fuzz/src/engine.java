import core.manager.materialsManager;
import core.manager.scenesManager;
import core.manager.texturesManager;
import core.primitive.Camera.factory.camera_c;
import core.scenes.scene;
import graphics.material.material.factory.materialMirror;
import graphics.material.material.materialBase;
import graphics.material.texture.factory.textureEmpty;
import graphics.rendering.renderList.renderListManager;
import graphics.rendering.renderer_c;
import graphics.videoBuffer_c;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class engine {
    private scenesManager sm = scenesManager.getInstance();
    private texturesManager tm = texturesManager.getInstance();
    private materialsManager mm = materialsManager.getInstance();

    private URL baseUrl;              //url du fichier de configuration
    private URL urlConfigBase;              //url du fichier de configuration

    //video configuration
    private boolean fog = false;
    private int fogR = 0;
    private int fogG = 0;
    private int fogB = 0;
    private int frameRate = 10;
    private double zNear = 0.1f;
    private double zFar = 3000f;
    private videoBuffer_c vidBuffer;

    private renderer_c renderEngin = new renderer_c();

    public videoBuffer_c getVideoBuffer() {
        return vidBuffer;
    }

    public scenesManager getSceneManager() {
        return sm;
    }

    public boolean isLoading() {
        return sm.isLoading() && mm.isLoading();
    }

    public void setRenderBuffer(int _xSize, int _ySize) {
        vidBuffer = new videoBuffer_c();
        //init video buffer
        vidBuffer.setSize(_xSize, _ySize);
        vidBuffer.init();
    }

    private boolean loadConfig(String _sceneName) {
        try {
            urlConfigBase = new URL(baseUrl.toString() + "scenes/" + _sceneName + "/config.ini");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //load config file
        try {
            String str;
            StreamTokenizer st = null;
            try {
                URLConnection uc = urlConfigBase.openConnection();
                uc.connect();
                InputStream in = uc.getInputStream();
                Reader r = new BufferedReader(new InputStreamReader(in));
                st = new StreamTokenizer(r);
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }

            //setup tokenizer
            st.parseNumbers();    //Specifies that numbers should be parsed by this tokenizer
            st.eolIsSignificant(false);        //eol is not a token
            st.wordChars('_', '_');
            // These calls caused comments to be discarded
            st.slashSlashComments(true);
            st.slashStarComments(true);
            st.commentChar('#');

            int token;
            boolean parse = true;

            while (parse) {
                //get token
                token = st.nextToken();

                //end of file
                if (StreamTokenizer.TT_EOF == token)
                    break;
                //					parse=false;

                if (StreamTokenizer.TT_WORD == token) {
                    //SIZE token
                    if (st.sval.equals("RENDERING")) {
                        //get token xsize
                        token = st.nextToken();
                        //setXSize((int) st.nval);
                        //get token ysize
                        token = st.nextToken();
                        //setYSize((int) st.nval);
//                        vb.setSize(xsize, ysize);
                        //get token swap effect
                        token = st.nextToken();
//                        vb.setSwapFx((int) st.nval);
                        //get token idbuffer
                        token = st.nextToken();
/*
                        if (st.sval.equals("true"))
                            vb.setIdBuffer(true);
                        else
                            vb.setIdBuffer(false);
*/
                        //get token swap effect
                        token = st.nextToken();
//                        vb.setPostFilter((int) st.nval);
                        //get token frame rate
                        token = st.nextToken();
                        frameRate = (int) st.nval;
                    } else if (st.sval.equals("FOG")) {
                        //get token fog
                        token = st.nextToken();
                        if (st.sval.equals("true"))
                            setFog(true);
                        else
                            setFog(false);

                        //get token fog colorR
                        token = st.nextToken();
                        fogR = (int) st.nval;
                        //get token fog colorG
                        token = st.nextToken();
                        fogG = (int) st.nval;
                        //get token fog colorB
                        token = st.nextToken();
                        fogB = (int) st.nval;
                    } else if (st.sval.equals("CAMERA")) {
                        //get token camera id
                        token = st.nextToken();
                        int camId = (int) st.nval;

                        //get znear
                        token = st.nextToken();
//                        scene.arrayCamera[camId].znear = (double) st.nval;
                        //get far
                        token = st.nextToken();
//                        scene.arrayCamera[camId].zfar = (double) st.nval;
                    }

                }
            }
            //			rd.close();
        } catch (IOException e) {
            System.err.println("unknown error");
        }

        //init video buffer
//        vb.init();

        //stay here while 3ds file is not loaded
        //while (Tload3ds.isAlive()) ;
        return true;
    }


    public void updateSceneAtTime(int scene_id, double frame, int cameraId) {
        scene s = sm.getSceneAt(scene_id);

        //check frame looping
        if (frame >= s.getKeyFramerEnd())
            frame = s.getKeyFramerStart();

        //compute camera matrix
        s.updateCamera(frame);
        //rebuild all hierarchy toCameraSpace matrix (local & world)
        s.updateHierarchy(frame);

        //rebuid mesh
        s.updateMesh(frame, s.getArrayCamera().get(cameraId));
    }

    public final void render(int scene_id, double frame, int cameraId) {
        int idRenderList = 1;

        //get scene
        scene s = sm.getSceneAt(scene_id);

        //get camera
        camera_c c = s.getArrayCamera().get(cameraId);

        //
        //pass:0
        //
        renderEngin.setup(s, vidBuffer);

        //reset the renderList
        renderListManager.getInstance().reset();

        //render meshes to renderList
        renderEngin.render(frame, c, 0); //pass=0

        //render the renderList
        renderListManager.getInstance().render(vidBuffer, 0, 0); //list=0 pass=0

        //
        //second pass for mirror
        //

        //if there is surface with mirror material
        ArrayList<materialBase> mml = renderListManager.getInstance().getMaterialList(1, 0);
        for (int j = 0; j < mml.size(); j++) {
            idRenderList = 1;

            //get the mirror material
            materialMirror mm = (materialMirror) mml.get(j);

            //get the texture
            textureEmpty te = (textureEmpty) mm.getDiffuseChannel().getTexture();

            //setup a new videobuffer of the size of the texture
            videoBuffer_c vidBuffer_tmp = new videoBuffer_c();
            vidBuffer_tmp.setSize(te.getWidth(), te.getHeight());
            vidBuffer_tmp.init();

            //copy the videobuffer in material texture
            renderEngin.setup(s, vidBuffer_tmp);

            //todo: setup camera
            camera_c mirror_camera = new camera_c();

            s.updateMesh(frame, mirror_camera);

            //render meshes mirror to renderlist 2
            renderEngin.render(frame, c, 1); //pass=1

            //render renderlist surface to videobuffer
            renderListManager.getInstance().render(vidBuffer_tmp, 0, 1); //list=0 pass=1

            //copy rendered videobuffer to texture
            te.copyDataFromArray(vidBuffer_tmp.getPixelBufferCurrent());

            //render mirror surface in original buffer
            //render renderlist surface to videobuffer
            renderListManager.getInstance().render(vidBuffer, 1, 0);//list=1 pass=0
        }
        //post prod effect
        //       vidBuffer.postProd();
    }

    public boolean getFog() {
        return fog;
    }

    public void setFog(boolean fog) {
        this.fog = fog;
    }

}
