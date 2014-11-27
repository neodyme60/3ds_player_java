package core.primitive.BoundingVolume;

/*
axis aligned bounding box
*/


import math.geometry.Face;
import math.geometry.Plan;
import math.geometry.Vertex;
import math.linear.Matrix4;
import math.linear.Vector3d;
import math.linear.Vector4d;

public class bbox_c {
    //temp variable
    static int i;
    //the min and max of the AABOX in objet space
    public Vector3d pMax;
    public Vector3d pMin;
    public Face[] arrayFace;
    public Vertex[] arrayVertex;


    //	math.linear.Vector3d	center;
    //math.linear.Vector3d	halfSize;
    public Plan[] planW;    //plan equation in world space

    //**************************************************
    public bbox_c() {
        pMax = new Vector3d();
        pMin = new Vector3d();

        arrayVertex = new Vertex[8];
        for (bbox_c.i = 0; 8 > i; arrayVertex[bbox_c.i++] = new Vertex()) ;

        arrayFace = new Face[12];
        for (bbox_c.i = 0; 12 > i; arrayFace[bbox_c.i++] = new Face()) ;

        planW = new Plan[6];
        for (bbox_c.i = 0; 6 > i; planW[bbox_c.i++] = new Plan()) ;


        //prebuild face
        arrayFace[0].add(0);
        arrayFace[0].add(1);
        arrayFace[0].add(4);
        arrayFace[1].add(4);
        arrayFace[1].add(1);
        arrayFace[1].add(5);
        arrayFace[2].add(2);
        arrayFace[2].add(3);
        arrayFace[2].add(0);
        arrayFace[3].add(0);
        arrayFace[3].add(3);
        arrayFace[3].add(1);
        arrayFace[4].add(7);
        arrayFace[4].add(6);
        arrayFace[4].add(2);
        arrayFace[5].add(7);
        arrayFace[5].add(2);
        arrayFace[5].add(3);
        arrayFace[6].add(4);
        arrayFace[6].add(5);
        arrayFace[6].add(7);
        arrayFace[7].add(4);
        arrayFace[7].add(7);
        arrayFace[7].add(6);
        arrayFace[8].add(1);
        arrayFace[8].add(3);
        arrayFace[8].add(7);
        arrayFace[9].add(7);
        arrayFace[9].add(5);
        arrayFace[9].add(1);
        arrayFace[10].add(2);
        arrayFace[10].add(0);
        arrayFace[10].add(4);
        arrayFace[11].add(4);
        arrayFace[11].add(6);
        arrayFace[11].add(2);
    }
    //**************************************************

    //
    //build the 8 point in world space of the aabox from it's local pMin & pMax
    //The matrix must be hierarchy*Tocamera
    //

    public final void transform(Matrix4 m) {
        double tempf;
        Vector4d v;
        Vector3d vv;

        //rot face normal
        for (bbox_c.i = 0; 12 > i; bbox_c.i++)
            m.mulV3f3Mat4(arrayFace[bbox_c.i].getN(), 1.0f, arrayFace[bbox_c.i].getN2());

        //rot vertex
        for (bbox_c.i = 0; 8 > i; bbox_c.i++) {
            v = arrayVertex[bbox_c.i].getvPos2();
            m.mulV3f4Mat4(arrayVertex[bbox_c.i].getvPos0(), 1.0f, v);

            v.y *= 160.0f / 100.0f;
        }

        //build plane in world space
        planW[0].setP3V(arrayVertex[0].getvPos2(), arrayVertex[1].getvPos2(), arrayVertex[4].getvPos2());
        planW[1].setP3V(arrayVertex[2].getvPos2(), arrayVertex[3].getvPos2(), arrayVertex[0].getvPos2());
        planW[2].setP3V(arrayVertex[2].getvPos2(), arrayVertex[6].getvPos2(), arrayVertex[7].getvPos2());
        planW[3].setP3V(arrayVertex[4].getvPos2(), arrayVertex[5].getvPos2(), arrayVertex[7].getvPos2());
        planW[4].setP3V(arrayVertex[1].getvPos2(), arrayVertex[3].getvPos2(), arrayVertex[7].getvPos2());
        planW[5].setP3V(arrayVertex[2].getvPos2(), arrayVertex[0].getvPos2(), arrayVertex[4].getvPos2());
    }

    //**************************************************
    public final void build(Vertex[] arrayV, int nbV) {
        //compute center of mesh
        Vector3d v;
        pMin.set(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        pMax.set(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        for (int i = 0; i < nbV; i++) {
            v = arrayV[i].getvPos0();

            if (v.x < pMin.x)
                pMin.x = v.x;
            if (v.y < pMin.y)
                pMin.y = v.y;
            if (v.z < pMin.z)
                pMin.z = v.z;

            if (v.x > pMax.x)
                pMax.x = v.x;
            if (v.y > pMax.y)
                pMax.y = v.y;
            if (v.z > pMax.z)
                pMax.z = v.z;
        }

        //
        //build 8 point BBOX
        //taken from quake2 src
        for (bbox_c.i = 0; 8 > i; bbox_c.i++) {
            arrayVertex[bbox_c.i].getvPos0().x = (0 < (i & 1)) ? pMin.x : pMax.x;
            arrayVertex[bbox_c.i].getvPos0().y = (0 < (i & 2)) ? pMin.y : pMax.y;
            arrayVertex[bbox_c.i].getvPos0().z = (0 < (i & 4)) ? pMin.z : pMax.z;
        }

    }
    //**************************************************
}
