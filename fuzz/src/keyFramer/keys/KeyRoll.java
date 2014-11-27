package keyFramer.keys;

import math.linear.Quaternion;

public class KeyRoll extends KeyBase {
    private Quaternion frameRoll = new Quaternion();

    KeyRoll() {
    }

    public KeyRoll(double _frameTime, Quaternion _frameRoll) {
        setFrameTime(_frameTime);
        frameRoll = _frameRoll;
    }

    public Quaternion getFrameRoll() {
        return frameRoll;
    }
}