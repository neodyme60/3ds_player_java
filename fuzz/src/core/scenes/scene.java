package core.scenes;

import core.manager.materialsManager;
import core.primitive.Camera.factory.cameraTarget;
import core.primitive.Camera.factory.camera_c;
import core.primitive.Light.lightBase;
import core.primitive.Mesh.meshBase;
import core.primitive.Mesh.mesh_c;
import core.primitive.objBase_c;
import graphics.Color.colorRGBA;
import graphics.material.material.factory.materialDefault;
import keyFramer.KeyFramer;
import math.linear.Matrix4;

import java.util.ArrayList;
import java.util.ListIterator;

public class scene {
    //engine verison string
    public static String version = "3D engine v1.0";
    //rendering frame rate
    public int frameRate;

    //id camera user to rendering
//    private camera_c cameraRendering=null;
    public int frame = 0;
    //3ds keyframer interface : range key of keyframer player
    public int keyFramerLenght;
    public int keyFramerStart;
    public int keyFramerEnd;
    public int sceneTotalPoly; //nb of poly in the scene
    public int sceneRotatedVertex;
    //scene name
    private String sceneName = "no name";
    //base url
    private String baseUrl;
    private int sceneTotalVertex;
    private colorRGBA ambiant = new colorRGBA();

    //object array
    private ArrayList<objBase_c> arrayObj = new ArrayList<objBase_c>();

    //kyframer array
    private ArrayList<KeyFramer> arrayKeyFramer = new ArrayList<KeyFramer>();

    //mesh array
    private ArrayList<meshBase> arrayMesh = new ArrayList<meshBase>();

    //camera array
    private ArrayList<camera_c> arrayCamera = new ArrayList<camera_c>();

    //camera target array
    private ArrayList<cameraTarget> arrayCameraTarget = new ArrayList<cameraTarget>();

    //light array
    private ArrayList<lightBase> arrayLight = new ArrayList<lightBase>();

    public scene(String codeBase, String _sceneName) {
        baseUrl = codeBase;
        sceneName = _sceneName;

        frameRate = 1;
        sceneTotalPoly = 0;
        setSceneTotalVertex(0);
        sceneRotatedVertex = 0;
    }

    /*
        public void setCameraRendering(camera_c c)
        {
            cameraRendering=c;
        }

        public camera_c getCameraRendering()
        {
            return cameraRendering;
        }
    */
    public ArrayList<objBase_c> getArrayObj() {
        return arrayObj;
    }

    public ArrayList<camera_c> getArrayCamera() {
        return arrayCamera;
    }

    public ArrayList<lightBase> getArrayLight() {
        return arrayLight;
    }

    public ArrayList<cameraTarget> getArrayCameraTarget() {
        return arrayCameraTarget;
    }

    public objBase_c getObjectByName(String name) {
        objBase_c o = null;

        o = findCameraByName(name);
        if (o == null)
            o = findCameraTargetByName(name);
        if (o == null)
            o = findMeshByName(name);
        if (o == null)
            o = findLightByName(name);

        return o;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public colorRGBA getAmbiant() {
        return ambiant;
    }

    public void setAmbiant(colorRGBA c) {
        ambiant = c;
    }

    public mesh_c findMeshByName(String name) {
        ListIterator iterateur = getArrayMesh().listIterator();
        while (iterateur.hasNext()) {
            mesh_c k = (mesh_c) iterateur.next();
            if (name.equals(k.getName()))
                return k;
        }
        return null;
    }

    public cameraTarget findCameraTargetByName(String name) {
        ListIterator iterateur = getArrayCameraTarget().listIterator();
        while (iterateur.hasNext()) {
            cameraTarget k = (cameraTarget) iterateur.next();
            if (name.equals(k.getName()))
                return k;
        }
        return null;
    }

    public camera_c findCameraByName(String name) {
        ListIterator iterateur = getArrayCamera().listIterator();
        while (iterateur.hasNext()) {
            camera_c k = (camera_c) iterateur.next();
            if (name.equals(k.getName()))
                return k;
        }
        return null;
    }

    public lightBase findLightByName(String name) {
        ListIterator iterateur = getArrayLight().listIterator();
        while (iterateur.hasNext()) {
            lightBase k = (lightBase) iterateur.next();
            if (name.equals(k.getName()))
                return k;
        }
        return null;
    }

    public void updateHierarchy(double frame) {
        Matrix4 mt = new Matrix4();

        ListIterator iterateur = null;

        //build all local matrix
        iterateur = getArrayKeyFramer().listIterator();
        while (iterateur.hasNext()) {
            KeyFramer o = (KeyFramer) iterateur.next();
            o.buildLocalMatrix(frame);
            o.hierarchyDone = false;//setup globalFinal matrix computed flag to false
        }

        //build hierarchy
        iterateur = getArrayKeyFramer().listIterator();
        while (iterateur.hasNext()) {
            KeyFramer kf = (KeyFramer) iterateur.next();

            kf.objWordlMatrix.setMat4(kf.objLocalMatrix);
            //set to identity the final global transforme matrix

//            KeyFramer father=kf.getKeyframerParent();

            if (kf.getKeyframerParent() != null) {
                KeyFramer kfDad = kf.getKeyframerParent();
                while (kfDad != null)// && father!=arrayKeyframer[father].hierarchyFatherId)
                {
                    //+pivot du parent
                    mt.setTranslate3fMat4(kfDad.pivot.x, kfDad.pivot.y, kfDad.pivot.z);
                    Matrix4.mulM4(mt, kf.objWordlMatrix, kf.objWordlMatrix);
    /*
                    if (kfDad.hierarchyDone)    //si la hierarchy du pere est deja calcule
                    {
                        Matrix4.mulM4(kfDad.objWordlMatrix, kf.objWordlMatrix, kf.objWordlMatrix);
                        break;    //on se casse, c fini pour ce keyframer
                    }
                    else
    */
                    Matrix4.mulM4(kfDad.objLocalMatrix, kf.objWordlMatrix, kf.objWordlMatrix);
                    //                    kf.hierarchyDone = true; //hierarchy is computed for this objet so set to true
                    //                    father = father.getKeyframerParent();
                    kfDad = kfDad.getKeyframerParent();
                    ;
//                    }
                }
            } else {
//                Matrix4.mulM4(kf.objLocalMatrix, kf.objWordlMatrix, kf.objWordlMatrix);
            }


        }
    }

    public lightBase addLight(String name) {
        lightBase l = new lightBase();
        l.setName(name);
        arrayObj.add(l);
        arrayLight.add(l);
        return l;
    }

    public camera_c addCamera(String name) {
        camera_c c = new camera_c();
        c.setName(name);
        arrayObj.add(c);
        arrayCamera.add(c);
        return c;
    }

    public cameraTarget addCameraTarget(String name) {
        cameraTarget c = new cameraTarget();
        c.setName(name);
        arrayObj.add(c);
        arrayCameraTarget.add(c);
        return c;
    }

    public KeyFramer addKeyframer(String name) {
        KeyFramer k = new KeyFramer();
        k.setName(name);
        arrayKeyFramer.add(k);
        return k;
    }

    public mesh_c addMesh(String name) {
        mesh_c m = new mesh_c();
        m.setName(name);
        arrayObj.add(m);
        arrayMesh.add(m);
        return m;
    }

    public materialDefault addMaterial(String name) {
        materialDefault m = new materialDefault();
        m.setName(name);
        materialsManager.getInstance().addMaterial(m);
        return m;
    }

    public final void updateCamera(double frame) {
        //
        //update  de la camera
        //
        ListIterator iterateur = arrayCamera.listIterator();
        while (iterateur.hasNext()) {
            camera_c cam = (camera_c) iterateur.next();

            //update camera projection matrix
            cam.updateProjectionMatrix(frame);

            //updateSceneAtTime eye camera
            cam.eye.setV3(cam.getKeyFramer().getTrackPos().eval(frame));

            //updateSceneAtTime at target
            cam.at.setV3(cam.getTartget().getKeyFramer().getTrackPos().eval(frame));

            //setup view matrix
            cam.setupViewMatrix();

            //rebuild the new frustum plane
            cam.setupCurrentWorldFrustumPlane();
        }
    }

    public final void updateMesh(double frame, camera_c camera) {
        //mise a jour des bounding box des meshs
        //toCameraSpace all vertex ( not in screen space yet ! only hierarchy in homogeneous world space )
        sceneRotatedVertex = 0;
        ListIterator iterateur = getArrayMesh().listIterator();
        while (iterateur.hasNext()) {
            mesh_c m = (mesh_c) iterateur.next();
            if (m.toCameraSpace(camera)) {
                sceneRotatedVertex += m.nbVertex;
            }
        }
    }

    public int getSceneTotalVertex() {
        return sceneTotalVertex;
    }

    public void setSceneTotalVertex(int sceneTotalVertex) {
        this.sceneTotalVertex = sceneTotalVertex;
    }

    public ArrayList<KeyFramer> getArrayKeyFramer() {
        return arrayKeyFramer;
    }

    public ArrayList<meshBase> getArrayMesh() {
        return arrayMesh;
    }

    public int getKeyFramerLenght() {
        return keyFramerLenght;
    }

    public void setKeyFramerLenght(int keyFramerLenght) {
        this.keyFramerLenght = keyFramerLenght;
    }

    public int getKeyFramerStart() {
        return keyFramerStart;
    }

    public void setKeyFramerStart(int keyFramerStart) {
        this.keyFramerStart = keyFramerStart;
    }

    public int getKeyFramerEnd() {
        return keyFramerEnd;
    }

    public void setKeyFramerEnd(int keyFramerEnd) {
        this.keyFramerEnd = keyFramerEnd;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}