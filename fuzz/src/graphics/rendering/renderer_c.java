package graphics.rendering;

import core.primitive.Camera.factory.camera_c;
import core.primitive.Mesh.meshBase;
import core.primitive.Mesh.mesh_c;
import core.scenes.scene;
import graphics.Shader.factory.shaderDefault;
import graphics.Shader.factory.shaderFlat;
import graphics.Shader.factory.shaderWire;
import graphics.clipping;
import graphics.drawing.line;
import graphics.material.material.factory.materialMirror;
import graphics.material.material.materialBase;
import graphics.rendering.renderList.renderListManager;
import graphics.shading.ShadingTypeEnum;
import graphics.videoBuffer_c;
import math.geometry.Face;
import math.geometry.Vertex;
import math.linear.Vector3d;
import math.linear.Vector4d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class renderer_c {
    //    private int renderType;
    private scene myScene;
    private videoBuffer_c videoBuffer;

    //for graphics.clipping
    private ArrayList<Vertex> clipArrayIn = new ArrayList<Vertex>();
    //    private ArrayList<Vertex> clipArrayOut = new ArrayList<Vertex>();
    private Vector3d dir;
    private double tempf;
    private int cri;
    private int cgi;
    private int cbi;
    private int c;
    private Vector3d v1;
    private Vector3d v2;
    private Vector3d v3;

    //frame info
    private int renderedMesh;    //nb obj rendu in frame
    private int rejectedMesh;    //nb obj rendu in frame
    private int renderedCameraId;
    private int renderedPoly; //

    private boolean renderMeshWire = false;
    private boolean renderBBOXWire = false;
    private boolean renderBBOX = false;

    private shaderFlat sf = new shaderFlat();
    private shaderWire sw = new shaderWire();
    private shaderDefault sa = new shaderDefault();


    public renderer_c() {
        clipping.init();
    }

    public final void setup(scene _scene, videoBuffer_c _videoBuffer) {
        myScene = _scene;
        videoBuffer = _videoBuffer;
    }

    public void setRenderMeshWire(boolean v) {
        renderMeshWire = v;
    }

    public void setRenderBBOX(boolean v) {
        renderBBOX = v;
    }

    public void setRenderBBOXWire(boolean v) {
        renderBBOXWire = v;
    }

    public final void render(double frame, camera_c c, int renderPass) {
        //mesh=> renderlist
        renderMesh(myScene, frame, c, renderPass);

        //if (renderBBOX)
//            renderBBOX();

        renderMeshWire = false;

/*
        if (renderInfo)
            renderInfo(b);

        if (renderFog)
            renderFog(b);
*/
    }

    //*****************************

    private final void renderBBOX() {
        int i;
        int j;
        int k;
        int g;
        Face[] bboxArrayFace;
        Vertex[] bboxArrayVertex;
        double dot;
        double cr;
        double cg;
        double cb;
        Vector3d v3Temp1;
        Vector4d v3Temp2;

        //render
        Enumeration en = Collections.enumeration(myScene.getArrayMesh());
        while (en.hasMoreElements()) {
            meshBase mn = (meshBase) en.nextElement();
            mesh_c mesh = (mesh_c) mn;

            bboxArrayFace = mesh.boundingVolume.arrayFace;
            bboxArrayVertex = mesh.boundingVolume.arrayVertex;

            //if bounding box intersect frustum
            //for each mesh face
            for (j = 0; 12 > j; j++) {
                //if face if visible ( front facing ), use plane equation for testing
                if (-0.1 < mesh.boundingVolume.planW[j / 2].getP().z) {
                    //build input clip array
                    clipArrayIn.clear();

                    clipArrayIn.add(bboxArrayVertex[bboxArrayFace[j].getAt(0)]);
                    clipArrayIn.add(bboxArrayVertex[bboxArrayFace[j].getAt(2)]);
                    clipArrayIn.add(bboxArrayVertex[bboxArrayFace[j].getAt(3)]);

                    //clip array
                    int c = clipping.clip(clipArrayIn);

                    //4D space to screen space
                    for (k = 0; k < c; k++) {
                        v3Temp1 = clipping.getClipOut().get(k).getvPos3();
                        v3Temp2 = clipping.getClipOut().get(k).getvPos2();

                        double tempf = 1.0 / v3Temp2.w;

                        v3Temp1.x = -v3Temp2.x * tempf;
                        v3Temp1.y = -v3Temp2.y * tempf;
                        v3Temp1.z = v3Temp2.z * tempf;

                        //rescall zbuffer [-1...1] - > [0..1]
                        v3Temp1.z = (v3Temp1.z + 1.0) * 0.5;

                        //2d scall viewport
                        v3Temp1.x *= (videoBuffer.xSize / 2.0);
                        v3Temp1.y *= (videoBuffer.ySize / 2.0);

                        //2d center viewport
                        v3Temp1.x += (videoBuffer.xSize / 2.0);
                        v3Temp1.y += (videoBuffer.ySize / 2.0);

                        v3Temp1.x = (int) (v3Temp1.x) << 16;
                        v3Temp1.y = (int) (v3Temp1.y) << 16;
                    }


                    dot = Math.abs(mesh.boundingVolume.planW[j / 2].getP().dotProductV3(dir) / 1.0);
                    cr = cg = cb = 0.2f;

                    if (bboxArrayFace[j].getMaterial() != null) {
                        materialBase m = bboxArrayFace[j].getMaterial();
                        cr = m.getDiffuseChannel().getRedf(0, 0, 0);
                        cg = m.getDiffuseChannel().getRedf(0, 0, 0);
                        cb = m.getDiffuseChannel().getRedf(0, 0, 0);
                    }

                    cr += (0.8 * dot);
                    cg += (0.8 * dot);
                    cb += (0.8 * dot);
                    int cri = (int) (cr * 255.0);
                    if (0xff < cri) cri = 0xff;
                    int cgi = (int) (cg * 255.0);
                    if (0xff < cgi) cgi = 0xff;
                    int cbi = (int) (cb * 255.0);
                    if (0xff < cbi) cbi = 0xff;
                    int co = cbi | (cgi << 8) | (cri << 16) | (0xff000000);
                    rasterizer_c.setAmbiantColor(c);
                    //
                    //render the face
                    //
                    for (g = 0; g < (c - 2); g++) {
                        if (!renderBBOXWire) {
//                            rasterizer_c.shade(videoBuffer, clipArrayOut[0], clipArrayOut[g + 1], clipArrayOut[g + 2]);
                        } else {
                            Vector3d v1 = clipping.getClipOut().get(0).getvPos3();
                            Vector3d v2 = clipping.getClipOut().get(g + 1).getvPos3();
                            Vector3d v3 = clipping.getClipOut().get(g + 2).getvPos3();

                            double ff = 1.0 / 65536.0;
                            line.drawZ(videoBuffer, v1.x * ff, v1.y * ff, v1.z, v2.x * ff, v2.y * ff, v2.z, co);
                            line.drawZ(videoBuffer, v2.x * ff, v2.y * ff, v2.z, v3.x * ff, v3.y * ff, v3.z, co);
                            line.drawZ(videoBuffer, v3.x * ff, v3.y * ff, v3.z, v1.x * ff, v1.y * ff, v1.z, co);
                        }
                    }
                }
            }
        }
    }

    //*****************************
    private final void renderInfo(videoBuffer_c b) {

    }

    //*****************************
 /*
    private final void renderFog(videoBuffer_c b) {
        int index;

        int colorFinalR;
        int colorFinalG;
        int colorFinalB;
        int colorSceneRGB;
        double colorSceneR;
        double colorSceneG;
        double colorSceneB;

        double pixelZ;

        double pixelOneMinusZ;

        index = (b.xSize * b.ySize) - 1;
        while (0 <= index) {
            colorSceneRGB = b.buffer[index];
            pixelZ = b.zBuffer[index];

            colorSceneB = (double) (colorSceneRGB & 0xff);
            colorSceneG = (double) ((colorSceneRGB >> 8) & 0xff);
            colorSceneR = (double) ((colorSceneRGB >> 16) & 0xff);

            pixelOneMinusZ = 1.0f - pixelZ;

            colorFinalR = (int) ((fogColorR * pixelZ) + (pixelOneMinusZ * colorSceneR));
            colorFinalG = (int) ((fogColorG * pixelZ) + (pixelOneMinusZ * colorSceneG));
            colorFinalB = (int) ((fogColorB * pixelZ) + (pixelOneMinusZ * colorSceneB));

            b.buffer[index--] = colorFinalB | (colorFinalG << 8) | (colorFinalR << 16);
        }
    }
*/
    //*****************************
    private final void renderMesh(scene s, double frame, camera_c c, int renderPass) {
        int j;
        int k;
        int g;

        Vertex[] meshArrayVertex;
        Vector3d v3Temp1;
        Vector4d v3Temp2;

        //for statistics
        renderedPoly = 0;
        renderedMesh = 0;
        rejectedMesh = 0;

        renderListManager rlm = renderListManager.getInstance();

        //render
        Enumeration en = Collections.enumeration(myScene.getArrayMesh());
        while (en.hasMoreElements()) {
            meshBase mn = (meshBase) en.nextElement();
            mesh_c mesh = (mesh_c) mn;

            meshArrayVertex = mesh.arrayVertex;

            //if bounding box intersect frustum
            if (mesh.isVisible) {
                renderedMesh++;
                //for each mesh face
                for (j = 0; j < mesh.nbFace; j++) {
                    Face face = mesh.arrayFace[j];

                    //build face normal
                    Vector3d v3 = meshArrayVertex[face.getAt(2)].getvPos2();
                    Vector3d v1 = meshArrayVertex[face.getAt(0)].getvPos2();
                    Vector3d v2 = meshArrayVertex[face.getAt(1)].getvPos2();
                    Vector3d n = ((v1.subV3(v2)).scalairProductV3(v3.subV3(v2))).normalizeV3();
                    face.setN(n);

                    //cull face
                    Vector3d eyeToViewPoint = new Vector3d(v1);
                    eyeToViewPoint.subV3((c.getKeyFramer()).getTrackPos().eval(frame));
                    if (n.dotProductV3(eyeToViewPoint) < 0.0)
                        continue;

                    //
                    //graphics.clipping section
                    //

                    //build input clip array

                    clipArrayIn.clear();
                    for (int m = 0; m < face.getSize(); m++)
                        clipArrayIn.add(meshArrayVertex[face.getAt(m)]);

                    //clip array
                    int clippedSize = clipping.clip(clipArrayIn);

                    ArrayList<Vertex> vla = clipping.getClipOut();
                    //4D space to screen space
                    for (k = 0; k < clippedSize; k++) {
                        v3Temp1 = vla.get(k).getvPos3();
                        v3Temp2 = vla.get(k).getvPos2();

                        tempf = 1.0 / v3Temp2.w;

                        v3Temp1.x = -v3Temp2.x * tempf;
                        v3Temp1.y = -v3Temp2.y * tempf;
                        v3Temp1.z = v3Temp2.z * tempf;

                        //rescall zbuffer [-1...1] - > [0..1]
                        v3Temp1.z = (v3Temp1.z + 1.0) * 0.5;

                        //scall viewport
                        v3Temp1.x *= videoBuffer.xAlfeSizef;
                        v3Temp1.y *= videoBuffer.yAlfeSizef;

                        //center viewport
                        v3Temp1.x = v3Temp1.x + videoBuffer.xAlfeSizef;
                        v3Temp1.y = v3Temp1.y + videoBuffer.yAlfeSizef;

                        v3Temp1.x = (int) (v3Temp1.x * 65536.0);
                        v3Temp1.y = (int) (v3Temp1.y * 65536.0);
                    }

                    //
                    //rendering section
                    //

                    //get face material
                    materialBase matFace = face.getMaterial();
                    if (matFace == null)
                        matFace = mesh.getMaterial();
                    if (matFace == null)
                        continue;


                    //renderlist=0  solid
                    //renderlist=1  mirror
                    //renderlist=2  transparent
                    int idRenderList = 0;
                    if (matFace.getClass().equals(materialMirror.class))
                        idRenderList = 1;
                    if (matFace.hasOpacityChannel())
                        idRenderList = 2;

                    for (g = 0; g < (clippedSize - 2); g++) {
                        rlm.add(
                                vla.get(0),
                                vla.get(g + 1),
                                vla.get(g + 2),
                                face.getN(),
                                matFace,
                                ShadingTypeEnum.autodetect,
                                idRenderList,
                                renderPass
                        );
                    }
                }
            } else
                rejectedMesh++;
        }
    }
}
