package keyFramer.keys;

import math.linear.Vector3d;

public class KeyBase {
    private double frameTime;

    //spline
    private double continuity = 0.0;        /* données pour les splines */
    private double tension = 0.0;
    private double bias = 0.0;
    private double ease_to = 0.0;
    private double ease_from = 0.0;

    private Vector3d ds = new Vector3d();
    private Vector3d dd = new Vector3d();            //deriv

    public KeyBase() {
    }

    public double getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(double frameTime) {
        this.frameTime = frameTime;
    }

    public double getContinuity() {
        return continuity;
    }

    public void setContinuity(double continuity) {
        this.continuity = continuity;
    }

    public double getTension() {
        return tension;
    }

    public void setTension(double tension) {
        this.tension = tension;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getEase_to() {
        return ease_to;
    }

    public void setEase_to(double ease_to) {
        this.ease_to = ease_to;
    }

    public double getEase_from() {
        return ease_from;
    }

    public void setEase_from(double ease_from) {
        this.ease_from = ease_from;
    }

    public Vector3d getDs() {
        return ds;
    }

    public void setDs(Vector3d ds) {
        this.ds = ds;
    }

    public Vector3d getDd() {
        return dd;
    }

    public void setDd(Vector3d dd) {
        this.dd = dd;
    }
}
