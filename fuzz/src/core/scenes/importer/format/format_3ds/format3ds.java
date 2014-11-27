package core.scenes.importer.format.format_3ds;

import core.manager.materialsManager;
import core.manager.texturesManager;
import core.primitive.Camera.factory.cameraTarget;
import core.primitive.Camera.factory.camera_c;
import core.primitive.Light.lightBase;
import core.primitive.Light.lightInterface;
import core.primitive.Mesh.meshBase;
import core.primitive.Mesh.mesh_c;
import core.primitive.objBase_c;
import core.scenes.importer.importerBaseClass;
import core.scenes.scene;
import graphics.Color.colorRGBA;
import graphics.material.channel.*;
import graphics.material.material.factory.materialDefault;
import graphics.material.texture.factory.textureBitmapFile;
import graphics.material.texture.io.ioBitmapFile;
import graphics.material.texture.texture_c;
import keyFramer.KeyFramer;
import keyFramer.keys.*;
import math.linear.Quaternion;
import math.linear.Vector3d;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ListIterator;
import java.util.zip.ZipFile;

public class format3ds extends importerBaseClass {
    private static int currentChunkId;
    private static int currentChunkLenght;

    private static String currentObjectName = null;
    private static mesh_c currentMesh = null;
    private static KeyFramer currentKeyFramer = null;
    private static camera_c currentCamera = null;
    private static objBase_c currentObjBase = null;
    private static materialDefault currentMaterial = null;
    private static lightInterface currentLight = null;
    private static boolean endOfStream = false;
    //    private static Vector3d vector3dTemp = new Vector3d();
    private static short hierarchyId;
    private static InputStream fileStream;

    private static int KeyFrameUseTensionMask = 0x01;
    private static int KeyFrameUseContinuityMask = 0x02;
    private static int KeyFrameUseBiasMask = 0x04;
    private static int KeyFrameUseEaseToMask = 0x08;
    private static int KeyFrameUseEaseFromMask = 0x10;

    private static int formatRevision;        //version of file format 1,2 or 3
    protected URL file3DS;
    private int mapType = -1;
    private boolean isLoadingFlag;

    public format3ds(scene _targetscene) {
        super(_targetscene);

        try {
            String uu = _targetscene.getBaseUrl();
/*
            if (!uu.isEmpty())
                if (uu.charAt(_targetscene.getBaseUrl().length()-1)!='/')
                uu+='/';
*/
            file3DS = new URL(uu + "/scenes/" + _targetscene.getSceneName() + "/scene.3ds");
        } catch (Exception e) {
        }

    }

    @Override
    public void run() {
        isLoadingFlag = true;

        //check if we have a zip or gz file
        String temp = file3DS.toString();

        if (temp.endsWith(".zip") | temp.endsWith(".gz")) {
            ZipFile zf = null;
            //import from a zip file ( get the first file in zip archive )
            try {
                zf = new ZipFile(file3DS.getFile());
            } catch (Exception e) {
                System.err.println("error load3ds_c: zipfile " + zf);
            }


            try {
                fileStream = zf.getInputStream(zf.getEntry(zf.entries().nextElement().toString()));
            } catch (Exception e) {
                System.err.println("error load3ds_c: getInputStream" + fileStream);
            }


            try {
                importFromStream(fileStream, targetscene);
                zf.close();
            } catch (Exception e) {
                System.err.println("error load3ds_c: importFromStreamm");
            }

/*
            try
            {
                zf=new ZipFile(url2.getFile());
                fileStream=zf.getInputStream(zf.getEntry(zf.entries().nextElement().toString()));
                importFromStream(fileStream,targetscene);
                zf.close();
            }catch (Exception e)
            {System.err.println("error load3ds_c: "+zf+"  "+fileStream);
            }
*/

            System.err.println("valid zip file");

        } else {
            try {
                fileStream = file3DS.openStream();
                importFromStream(fileStream, targetscene);
            } catch (Exception e) {
                System.err.println("error aaa");
            }
        }

        Enumeration en = Collections.enumeration(scene.getArrayMesh());
        while (en.hasMoreElements()) {
            meshBase m = (meshBase) en.nextElement();

            mesh_c mm = (mesh_c) m;

            //vertex from world to objet space coord
            //todo 
            mm.toLocal();

            //build mesh normals in objet space coor
            mm.buildFaceNormal();

            //build vertex normals in objet space coor
            mm.buildVertexNormal();

            //build AABOX
            mm.boundingVolume.build(mm.arrayVertex, mm.nbVertex);
        }

        ListIterator iterateur = scene.getArrayKeyFramer().listIterator();
        while (iterateur.hasNext()) {
            KeyFramer k = (KeyFramer) iterateur.next();
            if (null != k.getTrackPos()) k.DerivSplinePos();
            if (null != k.getTrackScall()) k.DerivSplineScall();
            if (null != k.getTrackRot()) k.DerivSplineRot();
        }
        isLoadingFlag = false;
    }

    //***********************************************************
    public void importFromStream(InputStream inStream, scene targetscene) {
        System.out.println(">> Importing scene from 3ds stream ...");
        scene = targetscene;
        BufferedInputStream in = new BufferedInputStream(inStream);
        try {
            load3DS(in);
        } catch (Throwable ignored) {
            System.err.println("Can't open 3ds file");
        }
    }

    private String readString(InputStream in) throws IOException {
        String result = new String();
        byte inByte;
        while (0 != (inByte = (byte) in.read())) result += (char) inByte;
        return result;
    }

    private int readInt(InputStream in) throws IOException {
        try {
            return in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
        } catch (Exception e) {
            System.err.println("error readInt");
            return -1;
        }
    }

    //***********************************************************
/*
    private short readShort(InputStream in) throws IOException
    {
        short i=-1;
        try
        {
            i=  (short) (in.read() | (in.read() << 8));
        }
        catch (Exception e)
        {
            System.err.println("error readShort");
        }
        return i;
    }
*/
    //***********************************************************
    private int readShort(InputStream in) throws IOException {
        int i = -1;
        try {
            i = (in.read() | (in.read() << 8));
        } catch (Exception e) {
            System.err.println("error readShort");
        }
        return i;
    }

    //***********************************************************
    private Vector3d readVector3d(InputStream in) throws IOException {
        Vector3d v = new Vector3d();
        try {
            v.x = Float.intBitsToFloat(readInt(in));
            v.y = Float.intBitsToFloat(readInt(in));
            v.z = Float.intBitsToFloat(readInt(in));
        } catch (Exception e) {
            System.err.println("error readFoat");
        }
        return v;
    }

    //***********************************************************
    private double readdouble(InputStream in) throws IOException {
        try {
            return Float.intBitsToFloat(readInt(in));
        } catch (Exception e) {
            System.err.println("error readFoat");
            return -1.0f;
        }
    }

    //***********************************************************
    private int readByte(InputStream in) throws IOException {
        try {
            return in.read();
        } catch (Exception e) {
            System.err.println("error readByte");
            return -1;
        }
    }

    //***********************************************************
    private Vector3d readVector(InputStream in) throws IOException {
        Vector3d v = new Vector3d();

        v.x = readdouble(in);
        v.z = readdouble(in);
        v.y = readdouble(in);
        return v;
    }

    //***********************************************************
    private void readChunkHeader(InputStream in) throws IOException {
        currentChunkId = readShort(in);
        currentChunkLenght = readInt(in);
        endOfStream = 0 > currentChunkId;

        System.err.println("CHUNK :" + Integer.toHexString(currentChunkId) + " lenght :" + currentChunkLenght);
    }

    //***********************************************************
    private void load3DS(InputStream in) throws IOException {
        //read first chunk
        readChunkHeader(in);

        int chunk_lenght_temp = 0;
        int chunk_lenght = currentChunkLenght;

        //check first chunk
        if (currentChunkId != format_3ds_chunk_c.CHUNK_PRIMARY) {
            //not a 3ds file
            throw new IOException();
        }

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case format_3ds_chunk_c.CHUNK_EDIT3DS:
                    load3DS_CHUNK_EDIT3DS(in, currentChunkLenght);
                    break;

                case format_3ds_chunk_c.CHUNK_KEYF3DS:
                    load3DS_CHUNK_KEYF3DS(in, currentChunkLenght);
                    break;
                case format_3ds_chunk_c.CHUNK_VERSION:
                case format_3ds_chunk_c.CHUNK_MESHVERSION:
                default:
                    skipChunk(in);
            }

        }


    }

    private void load3DS_CHUNK_EDIT3DS(InputStream in, int chunk_lenght) throws IOException {
        int chunk_lenght_temp = 0;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case format_3ds_chunk_c.CHUNK_EDIT_MATERIAL:
                    load3DS_CHUNK_EDIT_MATERIAL(in, currentChunkLenght);
                    break;

                case format_3ds_chunk_c.CHUNK_EDIT_OBJECT:
                    load3DS_CHUNK_EDIT_OBJECT(in, currentChunkLenght);
                    break;

                case 0x2100://  Ambient Color Block
                    targetscene.setAmbiant(load3DS_CHUNK_COLOR(in, currentChunkLenght));
                    break;

                case 0x0100:
                case 0x1100:
                case 0x1200://  Background Color
                case 0x1201: //  unknown
                case 0x1300://  unknown
                case 0x1400://  unknown
                case 0x1420://  unknown
                case 0x1450://  unknown
                case 0x1500://  unknown

                case 0x2200://  fog ?
                case 0x2201://  fog ?
                case 0x2210://  fog ?
                case 0x2300://  unknown
                case 0x3000://  unknown
                case 0x3D3E://  Editor configuration main block
                default:
                    skipChunk(in);
            }
        }
    }

    private void load3DS_CHUNK_KEYF_OBJDES(InputStream in, int chunk_lenght) throws IOException {
        int chunk_lenght_temp = 0;
        int _nbKey = 0;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case 0xB030: // Keyframer NODE_ID Hierarchy position
                    readShort(in);    //hFather
                    break;

                case 0xB013: // Object pivot point
//                    currentKeyFramer.pivot = readVector3d(in);
                    currentKeyFramer.pivot.setV3(readVector(in));
                    break;

                //                case 0xB014: // BOUNDBOX
                //                currentKeyFramer.localAABOX.pMin.set(readdouble(in),readdouble(in),readdouble(in));
                //                currentKeyFramer.localAABOX.pMax.set(readdouble(in),readdouble(in),readdouble(in));
                //                break;

                case 0xB010:  //keyframer node hdr,Object name, parameters and hierarchy father
                    String nameOfObject = readString(in);
                    System.err.println(" keyframer of object : " + nameOfObject);

                    currentKeyFramer = scene.addKeyframer(nameOfObject);

                    objBase_c o = null;
                    if (!nameOfObject.equals("$$$DUMMY")) //if dummy not need to find attached object. dummy is just a keyframer 
                    {
                        //search in mesh
                        meshBase mb = scene.findMeshByName(nameOfObject);
                        if (mb != null)
                            o = (objBase_c) mb;

                        //search in light
                        if (o == null) {
                            lightBase lb = scene.findLightByName(nameOfObject);
                            if (lb != null)
                                o = (objBase_c) lb;
                        }

                        if (o == null) {
                            camera_c cb = scene.findCameraByName(nameOfObject);
                            if (cb != null) {
                                if (cb.getKeyFramer() == null)
                                    o = (objBase_c) cb;
                                else
                                    o = (objBase_c) cb.getTartget();
                            }
                        }

                        if (o != null)
                            o.setKeyframer(currentKeyFramer);
                        else {
                            //get the lastest camera target and set it the keyframer.....strange it works
                            ArrayList<cameraTarget> act = scene.getArrayCameraTarget();
                            cameraTarget ct = act.get(act.size() - 1);
                            ct.setKeyframer(currentKeyFramer);
                            o = (objBase_c) ct;
                        }
                    }
                    readShort(in);
                    readShort(in);

                    //set hierarchy father
                    short father_id = (short) readShort(in);
                    if (father_id != -1) {
                        try {
                            ArrayList<KeyFramer> kl = scene.getArrayKeyFramer();
                            KeyFramer k = kl.get(father_id);
                            if (k != null)
                                currentKeyFramer.setKeyframerParent(k);
                        } catch (IndexOutOfBoundsException ex) {

                        }
                    }
                    break;

                case 0xB011: //KEYF_OBJDUMMYNAME
                    String nameDummyObject = readString(in);
                    System.err.println(" keyframer dummy name : " + nameDummyObject);
                    break;

                //scall track
                case 0xB022:
                    //readtrack head
                    readShort(in);
                    readInt(in);
                    readInt(in);

                    _nbKey = readInt(in);

                    while (_nbKey-- > 0) {
                        KeyScaling k = new KeyScaling();

                        k.setFrameTime(readInt(in));
                        readKeyParam(in, k, readShort(in));

                        Vector3d vv = readVector(in);
                        k.setFrameScall(vv);

                        currentKeyFramer.getTrackScall().addKeyFrame(k);
                    }
                    break;

                //fov track
                case 0xB023:
                    //readtrack head
                    readShort(in);
                    readInt(in);
                    readInt(in);
                    _nbKey = readInt(in);

                    while (_nbKey-- > 0) {
                        KeyFov k = new KeyFov();

                        k.setFrameTime(readInt(in));
                        readKeyParam(in, k, readShort(in));
                        k.setFrameFOV(readdouble(in));
                        currentKeyFramer.getTrackFOV().addKeyFrame(k);
                    }
                    break;

                //pos track
                case 0xB020:
                    //readtrack head
                    readShort(in);
                    readInt(in);
                    readInt(in);
                    _nbKey = readInt(in);

                    while (_nbKey-- > 0) {
                        KeyTanslate k = new KeyTanslate();

                        k.setFrameTime(readInt(in));
                        readKeyParam(in, k, readShort(in));
                        Vector3d vv = readVector(in);

                        vv.x -= currentKeyFramer.pivot.x;
                        vv.y -= currentKeyFramer.pivot.y;
                        vv.z -= currentKeyFramer.pivot.z;

                        k.setPos(vv);
                        currentKeyFramer.getTrackPos().addKeyFrame(k);
                    }
                    break;

                //rot track
                case 0xB021:
                    //readtrack head
                    readShort(in);
                    readInt(in);
                    readInt(in);
                    _nbKey = readInt(in);

                    for (int i = 0; i < _nbKey; i++) {
                        KeyRotate k = new KeyRotate();

                        k.setFrameTime(readInt(in));
                        readKeyParam(in, k, readShort(in));

                        k.setAngle(readdouble(in));

                        Vector3d vv = readVector(in);

                        k.setFrameRotX(vv.x);
                        k.setFrameRotY(vv.y);
                        k.setFrameRotZ(vv.z);

                        currentKeyFramer.getTrackRot().addKeyFrame(k);
                    }

                    //convert rotation to equivalent quaternion
                    Quaternion qa;
                    Quaternion qb = new Quaternion();
                    qb.setIdentity();
                    Vector3d v = new Vector3d();
                    double angle = 0.0;

                    for (int j = 0; j < _nbKey; j++) {
                        //get the keys
                        KeyRotate k = (KeyRotate) (currentKeyFramer.getTrackRot().getKeyFrameAt(j));

                        //build a quaternion with keys value
                        v.set(k.getFrameRotX(), k.getFrameRotY(), k.getFrameRotZ());
                        qa = new Quaternion(k.getAngle(), v);

                        //relatif quaternion
                        if (0 == j)
                            angle = k.getAngle();
                        else
                            angle += k.getAngle();

                        k.setqR(new Quaternion(angle, v));


                        //absolut quaternion
                        Quaternion.mulQ(qa, qb, qb);
                        k.getqA().setQ(qb);
                    }

                    /*
                    for(int j=0;j<nbKey;j++)
                    {
                    keyFramer.keys.KeyRotate		k=(keyFramer.keys.KeyRotate)(currentKeyFramer.trackRot.keyFrameArray[j]);
                    v.set(k.frameRotX,k.frameRotZ,k.frameRotY);
                    qa=new math.linear.Quaternion(k.angle,v);
                    math.linear.Quaternion.mulQ(qb,qa,qb);

                    k.qA.setQ(qb);
                    }
                    */
                    break;
                case 0xB029: //Hide track
                case 0xB028: //Falloff track
                case 0xB027: //Hotspot track
                case 0xB026: //Morph track
                case 0xB025: //Color track
                case 0xB024: //Roll track
                case 0xB015: //Object morph angle
                default:
                    skipChunk(in);
            }
        }
    }


    private void load3DS_CHUNK_KEYF3DS(InputStream in, int chunk_lenght) throws IOException {
        int chunk_lenght_temp = 0;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            //
            //keyframer
            //
            switch (currentChunkId) {
                case 0xB00A:
                    readShort(in);
                    String s = readString(in);

                    scene.setKeyFramerLenght(readInt(in));
                    break;

                case 0xB008://Frames (Start and End)
                    scene.setKeyFramerStart(readInt(in));
                    scene.setKeyFramerEnd(readInt(in));
                    break;

                case 0xB002: //Mesh information block
                    load3DS_CHUNK_KEYF_OBJDES(in, currentChunkLenght);
                    break;

                case 0xB003: //Camera information block
                    load3DS_CHUNK_KEYF_OBJDES(in, currentChunkLenght);
                    break;
                case 0xB004: //Camera target information block
                    load3DS_CHUNK_KEYF_OBJDES(in, currentChunkLenght);
                    break;

                case 0xB009:
                case 0xB001: //Ambient light information block
                case 0xB005: //Omni light information block
                case 0xB006: //Spot light target information block
                case 0xB007: //Spot light information block
                default:
                    skipChunk(in);
            }
        }
    }

    private void load3DS_CHUNK_TRI_FACEL1(InputStream in, int chunk_lenght) throws IOException {
        int chunk_lenght_temp = readPointList(in);

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case 0x4130:  // MSH_MAT_GROUP
                    //find material
                    String mn = readString(in);
                    materialDefault m = materialsManager.getInstance().getMaterialByName(mn);

                    //set material to mesh
                    currentMesh.setMaterial(m);

                    //set material to each face
                    int dd = readShort(in);
                    for (int i = 0; i < dd; i++) {
                        int index = readShort(in);
                        currentMesh.arrayFace[index].setMaterial(m);
                    }
                    break;

                case 0x4150:
                    for (int i = 0; i < currentMesh.arrayFace.length; i++) {
                        int a = readInt(in);
                    }
                    break;
                default:
                    skipChunk(in);
            }
        }
    }

    private void load3DS_CHUNK_OBJ_TRIMESH(InputStream in, int chunk_lenght) throws IOException {
        currentMesh = scene.addMesh(currentObjectName);

        currentMaterial = null;

        int chunk_lenght_temp = currentObjectName.length() + 1;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case 0x4110: // Vertex list
                    readVertexList(in);
                    break;

                case 0x4120: // Point list
                    load3DS_CHUNK_TRI_FACEL1(in, currentChunkLenght);
                    break;


                case 0x4140: // Mapping coordinates
                    readMappingCoordinates(in);
                    break;
                case 0x4165:  // MESH_COLOR in user interace, so not needed
                    readByte(in);
                    break;


                case 0x4160:
                    currentMesh.localToGlobalMat4.m11 = readdouble(in);
                    currentMesh.localToGlobalMat4.m21 = readdouble(in);
                    currentMesh.localToGlobalMat4.m31 = readdouble(in);

                    currentMesh.localToGlobalMat4.m12 = readdouble(in);
                    currentMesh.localToGlobalMat4.m22 = readdouble(in);
                    currentMesh.localToGlobalMat4.m32 = readdouble(in);

                    currentMesh.localToGlobalMat4.m13 = readdouble(in);
                    currentMesh.localToGlobalMat4.m23 = readdouble(in);
                    currentMesh.localToGlobalMat4.m33 = readdouble(in);

                    currentMesh.localToGlobalMat4.m41 = 0;
                    currentMesh.localToGlobalMat4.m42 = 0;
                    currentMesh.localToGlobalMat4.m43 = 0;

                    currentMesh.localToGlobalMat4.m14 = readdouble(in);
                    currentMesh.localToGlobalMat4.m24 = readdouble(in);
                    currentMesh.localToGlobalMat4.m34 = readdouble(in);
                    currentMesh.localToGlobalMat4.m44 = 1.0f;

                    currentMesh.localToGlobalMat4.inverse(currentMesh.globalToLocalMat4);
                    break;

                case 0x4111:
                case 0x4170:
                default:
                    skipChunk(in);
            }
        }
    }


    private void load3DS_CHUNK_OBJ_CAMERA(InputStream in, int chunk_lenght) throws IOException {
        //create camera
        currentCamera = scene.addCamera(currentObjectName);

        //create camera target
        currentCamera.setTarget(scene.addCameraTarget(currentObjectName));

        currentCamera.eye.setV3(readVector(in));
        currentCamera.at.setV3(readVector(in));

        currentCamera.up.set(0.0f, 1.0f, 0.0f);

        // Camera focal length in millimeters
        currentCamera.roll = readdouble(in);

        //Camera bank angle in degrees
        readdouble(in);
//        currentCamera.updateProjectionMatrix(0);//);

        int chunk_lenght_temp = 32;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunkd
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case 0x4720:  //
                    readdouble(in);
                    readdouble(in);
                    //currentCamera.znear=readdouble(in);
                    //currentCamera.zfar=readdouble(in);
                    break;
                case 0x4710:  //
                default:
                    skipChunk(in);
            }
        }
    }

    private void load3DS_CHUNK_EDIT_OBJECT(InputStream in, int chunk_lenght) throws IOException {

        currentObjectName = readString(in);

        System.err.println(" new object  : " + currentObjectName);
        int chunk_lenght_temp = currentObjectName.length() + 1;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case format_3ds_chunk_c.CHUNK_OBJ_TRIMESH:  // Triangular polygon object
                    load3DS_CHUNK_OBJ_TRIMESH(in, currentChunkLenght);
                    break;

                case format_3ds_chunk_c.CHUNK_OBJ_CAMERA: // N_CAMERA
                    load3DS_CHUNK_OBJ_CAMERA(in, currentChunkLenght);
                    break;

                case format_3ds_chunk_c.CHUNK_OBJ_LIGHT: // OBJ_LIGHT
//                    load3DS_CHUNK_OBJ_LIGHT(in,currentChunkLenght);
                case 0x4710: // OBJ_UNKNWN01
                case 0x4720: // OBJ_UNKNWN02
                default:
                    skipChunk(in);
            }

        }
    }

    private void load3DS_CHUNK_OBJ_LIGHT(InputStream in, int chunk_lenght) throws IOException {
//        currentLight = scene.addLight(currentObjectName);

        int chunk_lenght_temp = 0;

        boolean isSpotlight = false;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            readdouble(in); //x
            readdouble(in); //y
            readdouble(in); //z

            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;
            switch (currentChunkId) {
                case MaxChunkIDs.LIGHT_SPOTLIGHT:
                    isSpotlight = true;

                    readdouble(in); //x
                    readdouble(in); //y
                    readdouble(in); //z

                    readdouble(in); // hotspot_ang
                    readdouble(in); //falloff_ang
                    break;
                default:
                    skipChunk(in);
            }
/*
            if (isSpotlight)
                currentLight=new spotlight();
            else
                currentLight=new omni();
*/

        }
    }

    private void load3DS_CHUNK_MAP(InputStream in, int chunk_lenght) throws IOException {
        int chunk_lenght_temp = 0;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case 0xA300: //map filename
                    texture_c texture;

                    if (-1 == mapType)    //unknown map type so get off here
                        return;

                    String name = targetscene.getBaseUrl() + "/scenes/" + targetscene.getSceneName() + "/images/" + readString(in).toLowerCase();
                    System.err.println(" load texture: " + name);

//nico
                    //create texture
                    textureBitmapFile tbf = new textureBitmapFile();
                    tbf.setName(name);
                    //add it to core.manager
                    int id = texturesManager.getInstance().add(tbf);
                    //
                    ioBitmapFile iob = new ioBitmapFile(name);
                    //load
                    tbf.load(iob);

                    switch (mapType) {
                        case texture_c.MAT_TEXMAP:
                            channelDiffuse cd = new channelDiffuse();
                            cd.setTexture(tbf);
                            currentMaterial.setDiffuseChannel(cd);
                            break;

                        case texture_c.MAT_BUMPMAP:
                            channelBump cb = new channelBump();
                            cb.setTexture(tbf);
                            currentMaterial.setBumpChannel(cb);
                            break;

                        case texture_c.MAT_OPACMAP:
                            channelOpacity co = new channelOpacity();
                            co.setTexture(tbf);
                            currentMaterial.setOpacityChannel(co);
                            break;

                        case texture_c.MAT_REFLMAP:
                            channelReflection cr = new channelReflection();
                            cr.setTexture(tbf);
                            currentMaterial.setEnvironmentChannel(cr);
                            break;

                        case texture_c.MAT_SPECULAR:
                            channelSpecular cs = new channelSpecular();
                            cs.setTexture(tbf);
                            currentMaterial.setSpecularChannel(cs);
                            break;

                    }

                    mapType = -1; //reset maptype to no type
                    break;

                case 0xA351: //Mapping parameters
                case 0xA353: //Blur percent
                case 0xA354: //V scale
                case 0xA356: //U scale
                case 0xA358: //U offset
                case 0xA35A: //V offset
                case 0xA35C: //Rotation angle
                case 0xA360: //RGB Luma/Alpha tint 1
                case 0xA362: //RGB Luma/Alpha tint 2
                case 0xA364: //RGB tint R
                case 0xA366: //RGB tint G
                case 0xA368: //RGB tint B
                default:
                    skipChunk(in);
            }
        }
    }

    private double percent_CHUNK(InputStream in, int chunk_lenght) throws IOException {
        int chunk_lenght_temp = 0;
        double percent = 0;

        //read chunk
        readChunkHeader(in);
        chunk_lenght_temp += currentChunkLenght;

        switch (currentChunkId) {
            case MaxChunkIDs.PRCT_INT_FRMT:
                percent = readShort(in) / 100f;
                break;
            case MaxChunkIDs.PRCT_FLT_FRMT:
                percent = readdouble(in);
                break;
        }
        return percent;
    }

    private colorRGBA load3DS_CHUNK_COLOR(InputStream in, int chunk_lenght) throws IOException {
        colorRGBA gamaColor = null;
        colorRGBA regColor = null;
        int chunk_lenght_temp = 0;

        //read chunk
        readChunkHeader(in);
        chunk_lenght_temp += currentChunkLenght;

        switch (currentChunkId) {
            case MaxChunkIDs.COLOR_BYTE:
                regColor = new colorRGBA(readByte(in), readByte(in), readByte(in), 255);
                break;
            case MaxChunkIDs.CLR_BYTE_GAMA:
                gamaColor = new colorRGBA(readByte(in), readByte(in), readByte(in), 255);
                break;
            case MaxChunkIDs.COLOR_double:
                regColor = new colorRGBA(readdouble(in), readdouble(in), readdouble(in), 1.0f);
                break;
            case MaxChunkIDs.CLR_double_GAMA:
                gamaColor = new colorRGBA(readdouble(in), readdouble(in), readdouble(in), 1.0f);
                break;
            default:
                break;
        }
        if (regColor != null) return regColor;
        return gamaColor;
    }

    private void load3DS_CHUNK_EDIT_MATERIAL(InputStream in, int chunk_lenght) throws IOException {
        currentMaterial = scene.addMaterial("no name");

        int chunk_lenght_temp = 0;

        while (chunk_lenght_temp < (chunk_lenght - 6)) {
            //read chunk
            readChunkHeader(in);
            chunk_lenght_temp += currentChunkLenght;

            switch (currentChunkId) {
                case MaxChunkIDs.MAT_NAME: //MAT_NAME
                    currentMaterial.setName(readString(in));
                    break;

                case MaxChunkIDs.MAT_AMB_COLOR: //MAT_AMBIENT
                    currentMaterial.setAmbiant(load3DS_CHUNK_COLOR(in, currentChunkLenght));
                    break;

                case MaxChunkIDs.MAT_DIF_COLOR:
                    currentMaterial.setDiffuse(load3DS_CHUNK_COLOR(in, currentChunkLenght));
                    break;

                case MaxChunkIDs.MAT_SPEC_CLR:
                    currentMaterial.setSpecularColor(load3DS_CHUNK_COLOR(in, currentChunkLenght));
                    break;

                case MaxChunkIDs.MAT_SHADING:
                    readShort(in); // ignored
                    break;

                case MaxChunkIDs.MAT_TEXMAP:
                    mapType = texture_c.MAT_TEXMAP;
                    load3DS_CHUNK_MAP(in, currentChunkLenght);
                    break;

                case MaxChunkIDs.MAT_TEX_BUMPMAP: //MAT_BUMPMAP
                    mapType = texture_c.MAT_BUMPMAP;
                    load3DS_CHUNK_MAP(in, currentChunkLenght);
                    break;

                case MaxChunkIDs.MAT_OPACMAP: //MAT_OPACMAP
                    mapType = texture_c.MAT_OPACMAP;
                    load3DS_CHUNK_MAP(in, currentChunkLenght);
                    break;

                case MaxChunkIDs.MAT_REFLECT_MAP: //MAT_REFLMAP
                    mapType = texture_c.MAT_REFLMAP;
                    load3DS_CHUNK_MAP(in, currentChunkLenght);
                    break;

                case MaxChunkIDs.MAT_SHINE:
                    mapType = texture_c.MAT_SPECULAR;
                    load3DS_CHUNK_MAP(in, currentChunkLenght);
                    break;
                case MaxChunkIDs.MAT_SHINE_STR:
                case MaxChunkIDs.MAT_ALPHA:
                case MaxChunkIDs.MAT_ALPHA_FAL:
                case MaxChunkIDs.MAT_REF_BLUR:
                case MaxChunkIDs.MAT_SELF_ILUM:
                case MaxChunkIDs.MAT_WIRE_SIZE:
                case MaxChunkIDs.IN_TRANC_FLAG:
                case MaxChunkIDs.MAT_SOFTEN:
                case 0xA081: //2 sided
                case 0xA083: //Add trans
                case 0xA085: //Wire frame on
                case 0xA088: //Face map
                case 0xA08E: //Wire in units
                case 0xA240: //Transparency falloff percent present
                case 0xA250: //Reflection blur percent present
                case 0xA252: //Bump map present (true percent)

                case 0xA33A: //texture map 2
                case 0xA33C: //Shininess map
                case 0xA204: //Specular map
                case 0xA33D: //Self illum. map
                case 0xA33E: //Mask for texture map 1
                case 0xA340: //Mask for texture map 2
                case 0xA342: //Mask for opacity map
                case 0xA344: //Mask for bump map
                case 0xA346: //Mask for shininess map
                case 0xA348: //Mask for specular map
                case 0xA34A: //Mask for self illum. map
                case 0xA34C: //Mask for reflection map

                default:
                    skipChunk(in);
            }

        }
    }

    //***********************************************************
    private void skipChunk(InputStream in) throws IOException, OutOfMemoryError {
        System.err.println(" skype chunk : " + Integer.toHexString(currentChunkId));
        for (int i = 0; (i < currentChunkLenght - 6) && (!endOfStream); i++)
            endOfStream = 0 > in.read();
    }

    //***********************************************************
    private void readVertexList(InputStream in) throws IOException {
        int vertices = readShort(in);
        currentMesh.setVertexArray(vertices);

        targetscene.setSceneTotalVertex(targetscene.getSceneTotalVertex() + vertices);

        for (int i = 0; i < vertices; i++) {
            currentMesh.arrayVertex[i].setvPos0(readVector3d(in));
        }
    }

    //***********************************************************
    private int readPointList(InputStream in) throws IOException {
        int triangles = readShort(in);
        currentMesh.setFaceArray(triangles);

        targetscene.sceneTotalPoly += triangles;

        for (int i = 0; i < triangles; i++) {
            currentMesh.arrayFace[i].add(readShort(in));
            currentMesh.arrayFace[i].add(readShort(in));
            currentMesh.arrayFace[i].add(readShort(in));
            readShort(in);
        }

        return 2 + ((4 * 2) * triangles);
    }

    //***********************************************************
    private void readMappingCoordinates(InputStream in) throws IOException {
        int vertices = readShort(in);
        for (int i = 0; i < vertices; i++) {
            double tx = (readdouble(in));
            double ty = (readdouble(in));
            currentMesh.arrayVertex[i].getvM().x = tx;
            currentMesh.arrayVertex[i].getvM().y = (1.0 - ty);
        }
    }

    //***********************************************************
    private int readKeyParam(InputStream in, KeyBase key, int flag) throws IOException {
        int byteRead = 0;

        if ((flag & KeyFrameUseTensionMask) == KeyFrameUseTensionMask) {
            key.setTension(readdouble(in));
            byteRead += 4;
        }

        if ((flag & KeyFrameUseContinuityMask) == KeyFrameUseContinuityMask) {
            key.setContinuity(readdouble(in));
            byteRead += 4;
        }

        if ((flag & KeyFrameUseBiasMask) == KeyFrameUseBiasMask) {
            key.setBias(readdouble(in));
            byteRead += 4;
        }

        if ((flag & KeyFrameUseEaseToMask) == KeyFrameUseEaseToMask) {
            key.setEase_to(readdouble(in));
            byteRead += 4;
        }

        if ((flag & KeyFrameUseEaseFromMask) == KeyFrameUseEaseFromMask) {
            key.setEase_from(readdouble(in));
            byteRead += 4;
        }

        return byteRead;
    }

    public synchronized boolean isLoading() {
        return isLoadingFlag;
    }

    public void load(String fileName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int progressLoaded() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
