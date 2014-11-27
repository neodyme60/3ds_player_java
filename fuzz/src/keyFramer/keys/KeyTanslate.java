package keyFramer.keys;

import math.linear.Vector3d;

public class KeyTanslate extends KeyBase {
    private Vector3d framePos = new Vector3d(0.0f, 0.0f, 0.0f);

    public KeyTanslate() {
    }

    public KeyTanslate(double _frameTime, Vector3d _framePos) {
        setFrameTime(_frameTime);
        framePos.setV3(_framePos);
    }

    public Vector3d getFramePos() {
        return framePos;
    }

    public void setPos(Vector3d _v) {
        framePos.setV3(_v);
    }
}