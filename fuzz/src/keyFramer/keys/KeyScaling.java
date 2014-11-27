package keyFramer.keys;

import math.linear.Vector3d;

public class KeyScaling extends KeyBase {
    private Vector3d frameScall = new Vector3d(1.0f, 1.0f, 1.0f);

    public KeyScaling() {
        frameScall = new Vector3d();
    }

    public Vector3d getFrameScall() {
        return frameScall;
    }

    public void setFrameScall(Vector3d _v) {
        frameScall.setV3(_v);
    }

    public void setFrameScall(double _x, double _y, double _z) {
        frameScall.set(_x, _x, _y);
    }
}