package math.geometry;

import math.linear.Vector3d;
import math.linear.Vector4d;

public class Plan {
    //normal & distance( origine du repere projete perpendiculairement sur le plan)
    private Vector4d p;

    //**************************************************
    public Plan() {
        setP(new Vector4d());
    }

    public Vector3d mirroringVector(Vector3d _p) {
        //http://www.gamedev.net/community/forums/topic.asp?topic_id=411626
        //http://knol.google.com/k/mirroring-a-point-on-a-3d-plane

        Vector3d n = _p.normalizeV3();
        double d = (n.dotProductV3(_p));
        return new Vector3d(_p.x - 2.0f * n.x * d, _p.y - 2.0f * n.y * d, _p.z - 2.0f * n.z * d);
    }

    //**************************************************
    public void swapNormal() {
        getP().x = -getP().x;
        getP().y = -getP().y;
        getP().z = -getP().z;
    }

    //**************************************************
    public Vector3d getNormal() {
        return new Vector3d(getP());
    }

    //**************************************************
    public void setNormal(Vector3d _v) {
        getP().x = _v.x;
        getP().y = _v.y;
        getP().z = _v.z;
    }

    //**************************************************
    public double getDistPtr() {
        return getP().w;
    }

    //**************************************************
    public void setDist(double d) {
        getP().w = d;
    }

    //**************************************************
    public Plan setP(Plan _p) {
        getP().setV4(_p.getP());
        return this;
    }

    //**************************************************
    public Plan setP3V(Vector3d _v1, Vector3d _v2, Vector3d _v3) {
        getP().setV3((_v1.subV3(_v2)).scalairProductV3(_v3.subV3(_v2)));
        getP().normalizeV3();
        getP().w = -(getP().dotProductV3(_v1));
        return this;
    }

    //**************************************************
    public Plan setP4f(double _a, double _b, double _c, double _d) {
        getP().setV4(_a, _b, _c, _d);
        return this;
    }

    //**************************************************
    public Plan setP1V1f(Vector3d v, double _d) {
        getP().setV3(v);
        getP().w = _d;
        return this;
    }

    //**************************************************
    public double dist(Vector3d v) {
        return getP().dotProductV3(v) + getP().w;
    }
    //**************************************************

    public Vector4d getP() {
        return p;
    }

    public void setP(Vector4d p) {
        this.p = p;
    }
}