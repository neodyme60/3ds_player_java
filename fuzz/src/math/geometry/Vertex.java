package math.geometry;

import math.linear.Vector3d;
import math.linear.Vector4d;

public class Vertex {
    private Vector3d vPos0 = new Vector3d();
    private Vector3d vPos1 = new Vector3d();   //objet de base
    private Vector4d vPos2 = new Vector4d();  //transforme local + graphics.clipping in homogeneous space
    private Vector3d vPos3 = new Vector3d();  //to screen ( /W )

    private Vector3d vN = new Vector3d();   //normal
    private Vector3d vN2 = new Vector3d();   //normal toCameraSpace�
    private Vector3d vM = new Vector3d();   //mapping coordinate [0..1] not scalled
    private Vector3d vM2 = new Vector3d();   //mapping coordinate scalled with texture size [0..texture width/height]

    public Vertex() {
    }

    public Vertex set(Vertex v) {
        vPos0.setV3(v.getvPos0());
        vPos1.setV3(v.getvPos1());
        vPos2.setV4(v.getvPos2());
        vPos3.setV3(v.getvPos3());
        vM.setV3(v.getvM());
        vN.setV3(v.getvN());
        vN2.setV3(v.getvN2());
        return this;
    }

    public Vertex lerp(Vertex a, Vertex b, double f) {
        vPos0.lerp(a.getvPos0(), b.getvPos0(), f);
        vPos1.lerp(a.getvPos1(), b.getvPos1(), f);
        vPos2.setV4(a.getvPos2(), b.getvPos2(), f);
        vPos3.lerp(a.getvPos3(), b.getvPos3(), f);
        vM.lerp(a.getvM(), b.getvM(), f);
        vN.lerp(a.getvN(), b.getvN(), f);
        vN2.lerp(a.getvN2(), b.getvN2(), f);
        return this;
    }

    public Vector3d getvPos0() {
        return vPos0;
    }

    public void setvPos0(Vector3d _vPos0) {
        vPos0.setV3(_vPos0);
    }

    public Vector3d getvPos1() {
        return vPos1;
    }

    public void setvPos1(Vector3d _vPos1) {
        vPos1.setV3(_vPos1);
    }

    public Vector4d getvPos2() {
        return vPos2;
    }

    public void setvPos2(Vector4d _vPos2) {
        vPos2.setV3(_vPos2);
    }

    public Vector3d getvPos3() {
        return vPos3;
    }

    public void setvPos3(Vector3d _vPos3) {
        vPos3.setV3(_vPos3);
    }

    public Vector3d getvN() {
        return vN;
    }

    public void setvN(Vector3d _vN) {
        vN.setV3(_vN);
    }

    public Vector3d getvN2() {
        return vN2;
    }

    public void setvN2(Vector3d _vN2) {
        vN2.setV3(_vN2);
    }

    public Vector3d getvM() {
        return vM;
    }

    public void setvM(Vector3d _vM) {
        vM.setV3(_vM);
    }

    public Vector3d getvM2() {
        return vM2;
    }

    public void setvM2(Vector3d _vM2) {
        vM2.setV3(_vM2);
    }

}