package math.geometry;

import graphics.material.material.materialBase;
import math.linear.Vector3d;

import java.util.ArrayList;

public class Face {
    private ArrayList<Integer> vertexIndexList = new ArrayList<Integer>();
    //    private int zMoyen=0;
    private Vector3d n = new Vector3d(0.0f, 1.0f, 0.0f);
    private Vector3d n2 = new Vector3d(0.0f, 1.0f, 0.0f);

    private materialBase m = null;

    public Face() {
    }

    /*
        public void set(int i, int j, int k)
        {
            setV1(i);
            setV2(j);
            lerp(k);
        }
    */
    public boolean containIndex(int v) {
        for (int i = 0; i < vertexIndexList.size(); i++)
            if (vertexIndexList.get(i) == v)
                return true;
        return false;
    }

    public int getSize() {
        return vertexIndexList.size();
    }

    public void add(int i) {
        vertexIndexList.add(i);
    }

    public int getAt(int i) {
        return vertexIndexList.get(i);
    }

    /*
        public int getzMoyen()
        {
            return zMoyen;
        }

        public void setzMoyen(int zMoyen)
        {
            this.zMoyen = zMoyen;
        }
    */
    public Vector3d getN() {
        return n;
    }

    public void setN(Vector3d _n) {
        n.setV3(_n);
    }

    public Vector3d getN2() {
        return n2;
    }

    public void setN2(Vector3d _n2) {
        n2.setV3(_n2);
    }

    public materialBase getMaterial() {
        return m;
    }

    public void setMaterial(materialBase _m) {
        m = _m;
    }
}

