package keyFramer.keys;


public class KeyFov extends KeyBase {
    private double frameFOV = 80;

    public KeyFov() {
    }

    public KeyFov(double _frameTime, double _frameFOV) {
        setFrameTime(_frameTime);
        frameFOV = _frameFOV;
    }

    public double getFrameFOV() {
        return frameFOV;
    }

    public void setFrameFOV(double _frameFOV) {
        frameFOV = _frameFOV;
    }
}
