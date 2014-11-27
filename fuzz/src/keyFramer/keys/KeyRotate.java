package keyFramer.keys;

import math.linear.Quaternion;
import math.linear.Vector3d;

public class KeyRotate extends KeyBase {
    private double frameRotX = 0.0;
    private double frameRotY = 0.0;
    private double frameRotZ = 0.0;
    private double angle = 0.0;

    private Vector3d rot = new Vector3d();
    private Quaternion qA = new Quaternion();
    private Quaternion qR = new Quaternion();

    public KeyRotate() {
    }

    public KeyRotate(double _frameTime, double framePos) {
        setFrameTime(_frameTime);
        framePos = framePos;
    }


    public double getFrameRotX() {
        return frameRotX;
    }

    public void setFrameRotX(double frameRotX) {
        this.frameRotX = frameRotX;
    }

    public double getFrameRotY() {
        return frameRotY;
    }

    public void setFrameRotY(double frameRotY) {
        this.frameRotY = frameRotY;
    }

    public double getFrameRotZ() {
        return frameRotZ;
    }

    public void setFrameRotZ(double frameRotZ) {
        this.frameRotZ = frameRotZ;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Vector3d getRot() {
        return rot;
    }

    public void setRot(Vector3d rot) {
        this.rot = rot;
    }

    public Quaternion getqA() {
        return qA;
    }

    public void setqA(Quaternion qA) {
        this.qA = qA;
    }

    public Quaternion getqR() {
        return qR;
    }

    public void setqR(Quaternion qR) {
        this.qR = qR;
    }
}
