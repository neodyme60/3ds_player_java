package graphics.rendering.renderList;

import graphics.material.material.materialBase;
import math.geometry.Vertex;
import math.linear.Vector3d;

public class renderListItem {
    int shd = -1;
    private Vertex a = new Vertex();
    private Vertex b = new Vertex();
    private Vertex c = new Vertex();
    private Vector3d n = new Vector3d();
    private materialBase mat = null;

    public Vertex getA() {
        return a;
    }

    public void setA(Vertex a) {
        this.a.set(a);
    }

    public Vertex getB() {
        return b;
    }

    public void setB(Vertex b) {
        this.b.set(b);
    }

    public Vertex getC() {
        return c;
    }

    public void setC(Vertex c) {
        this.c.set(c);
    }

    public Vector3d getN() {
        return n;
    }

    public void setN(Vector3d n) {
        this.n.setV3(n);
    }

    public materialBase getMat() {
        return mat;
    }

    public void setMat(materialBase mat) {
        this.mat = mat;
    }

    public int getShd() {
        return shd;
    }

    public void setShd(int _shd) {
        shd = _shd;
    }
}
