package keyFramer.tracks;

import keyFramer.keys.KeyBase;

import java.util.ArrayList;


public abstract class TrackBase {
    private ArrayList<KeyBase> keyArray = new ArrayList<KeyBase>();

    TrackBase() {
    }

    abstract Object eval(double frame);

    final int findKeyIndexAtFrame(double frame) {
        if (getKeyCount() == 0)
            return -1;

        if (getKeyCount() == 1)
            return 0;

        int i = 0;
        for (; ; ) {
            if (frame >= getKeyFrameAt(i).getFrameTime() && frame < getKeyFrameAt(i + 1).getFrameTime())
                break;
            else {
                if ((i + 1) == (getKeyCount() - 1))
                    break;
            }
            i++;
        }
        return i;
    }

    final double Ease(double t, double a, double b) {
        double k;
        double s = a + b;
        if (0.0f == s)
            return t;
        if (1.0f < s) {
            a = a / s;
            b = b / s;
        }
        k = 1.0f / (2.0f - a - b);
        if (t < a)
            return ((k / a) * t * t);
        else {
            if (t < 1.0f - b)
                return (k * (2.0f * t - a));
            else {
                t = 1.0f - t;
                return (1.0f - (k / b) * t * t);
            }
        }
    }

    public int getKeyCount() {
        return keyArray.size();
    }

    public KeyBase getKeyFrameAt(int i) {
        return keyArray.get(i);
    }

    public void setKeyFrameAt(KeyBase k, int i) {
        keyArray.set(i, k);
    }

    public void addKeyFrame(KeyBase k) {
        keyArray.add(k);
    }

    public void insertKeyFrameAt(KeyBase k, int i) {
        keyArray.add(i, k);
    }
}
