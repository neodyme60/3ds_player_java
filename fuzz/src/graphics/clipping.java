package graphics;

import math.geometry.Vertex;
import math.linear.Vector4d;

import java.util.ArrayList;

public class clipping {
    private static ArrayList<Vertex> vOutClipLeft = new ArrayList<Vertex>();
    private static ArrayList<Vertex> vOutClipRight = new ArrayList<Vertex>();
    private static ArrayList<Vertex> vOutClipTop = new ArrayList<Vertex>();
    private static ArrayList<Vertex> vOutClipBottom = new ArrayList<Vertex>();
    private static ArrayList<Vertex> vOutClipZNear = new ArrayList<Vertex>();
    private static ArrayList<Vertex> vOutClipZfar = new ArrayList<Vertex>();

    public static ArrayList<Vertex> getClipOut() {
        return vOutClipZfar;
    }

    public static void init() {
        for (int i = 0; i < 12; i++) {
            vOutClipLeft.add(new Vertex());
            vOutClipRight.add(new Vertex());
            vOutClipTop.add(new Vertex());
            vOutClipBottom.add(new Vertex());
            vOutClipZNear.add(new Vertex());
            vOutClipZfar.add(new Vertex());
        }
    }

    //
    //clip triangle (v1 v2 v3) by the camera frustum in homogenous camera space (-w<..<w).
    //return number of vertex of the new poly created by the triangle/frustum graphics.clipping.
    //return 0 if triange if full outside the frustum.
    //

    public static int clip(ArrayList<Vertex> vIn) {
        int nbOut = 0;
        nbOut = clippAxis(vIn, vOutClipLeft, vIn.size(), 0);
        nbOut = clippAxis(vOutClipLeft, vOutClipRight, nbOut, 1);
        nbOut = clippAxis(vOutClipRight, vOutClipBottom, nbOut, 2);
        nbOut = clippAxis(vOutClipBottom, vOutClipTop, nbOut, 3);
        nbOut = clippAxis(vOutClipTop, vOutClipZNear, nbOut, 4);
        nbOut = clippAxis(vOutClipZNear, vOutClipZfar, nbOut, 5);
        return nbOut;
    }

    //
    //
    //clip inV ( vertex array) with axis in homogeneous space
    //result in outV
    //return nb of vertex in clipped array
    private static int clippAxis(ArrayList<Vertex> vIn, ArrayList<Vertex> vOut, int size, int axis) {
        int nbOut = 0;        //nb of vertex in clipped array
        double f = 0;
        boolean outA = false;
        boolean outB = false;
        Vector4d vA = null;
        Vector4d vB = null;

        for (int i = 0; i < size; i++) {
            vA = vIn.get(i).getvPos2();        //get last point
            vB = vIn.get((i + 1) % size).getvPos2();

            switch (axis) {
                case 0:
                    outA = vA.x < -vA.w;
                    outB = vB.x < -vB.w;
                    break;    //LEFT
                case 1:
                    outA = vA.x > vA.w;
                    outB = vB.x > vB.w;
                    break;        //RIGHT
                case 2:
                    outA = vA.y < -vA.w;
                    outB = vB.y < -vB.w;
                    break;    //BOTTOM
                case 3:
                    outA = vA.y > vA.w;
                    outB = vB.y > vB.w;
                    break;        //TOP
                case 4:
                    outA = vA.z < -vA.w;
                    outB = vB.z < -vB.w;
                    break;    //NEAR
                case 5:
                    outA = vA.z > vA.w;
                    outB = vB.z > vB.w;
                    break;        //FAR
            }

            if (outA != outB)        //need to clip
            {
                switch (axis) {
                    case 0:
                        f = (-vA.w - vA.x) / (vB.x - vA.x + vB.w - vA.w);
                        break;    //LEFT
                    case 1:
                        f = (vA.w - vA.x) / (vB.x - vA.x - vB.w + vA.w);
                        break;    //RIGHT
                    case 2:
                        f = (-vA.w - vA.y) / (vB.y - vA.y + vB.w - vA.w);
                        break;    //BOTTOM
                    case 3:
                        f = (vA.w - vA.y) / (vB.y - vA.y - vB.w + vA.w);
                        break;    //TOP
                    case 4:
                        f = (-vA.w - vA.z) / (vB.z - vA.z + vB.w - vA.w);
                        break;    //NEAR
                    case 5:
                        f = (vA.w - vA.z) / (vB.z - vA.z - vB.w + vA.w);
                        break;    //FAR
                }

                //LINEAR interpol               
                vOut.get(nbOut).lerp(vIn.get(i), vIn.get((i + 1) % size), f);//build a new vertex by interpolating 2 other
                nbOut++;
            }

            if ((outA && !outB) || (!outA && !outB)) {
                vOut.get(nbOut).set(vIn.get((i + 1) % size));
                nbOut++;
            }
        }
        return nbOut;
    }
}