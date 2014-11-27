package keyFramer.tracks;

import keyFramer.keys.KeyScaling;
import math.linear.Matrix4;

public class TrackScaling extends TrackBase {
//    public Matrix4 currentScall=new Matrix4();

    public TrackScaling() {
//        currentScall.setIdentityMat4();
    }

    public final Matrix4 eval(double frame) {
        Matrix4 currentScall = new Matrix4().setIdentityMat4();
        KeyScaling keyA;
        KeyScaling keyB;

        if (getKeyCount() == 0)
            return currentScall;    //set to default scall

        if (getKeyCount() == 1) {
            currentScall.setScallV3Mat4(((KeyScaling) getKeyFrameAt(0)).getFrameScall());
        } else {
            int keyFrameIndex = findKeyIndexAtFrame(frame);
            if (frame >= getKeyFrameAt(getKeyCount() - 1).getFrameTime())
                currentScall.setScallV3Mat4(((KeyScaling) getKeyFrameAt(getKeyCount() - 1)).getFrameScall());
            else {
                keyA = (KeyScaling) getKeyFrameAt(keyFrameIndex);
                keyB = (KeyScaling) getKeyFrameAt(keyFrameIndex + 1);
                double t = Ease((frame - keyA.getFrameTime()) / (keyB.getFrameTime() - keyA.getFrameTime()), keyA.getEase_from(), keyA.getEase_to());

                double t2 = t * t;
                double t3 = t2 * t;
                double h0 = 2 * t3 - 3 * t2 + 1;
                double h1 = -2 * t3 + 3 * t2;
                double h2 = t3 - 2 * t2 + t;
                double h3 = t3 - t2;

                currentScall.setScale3fMat4(
                        keyA.getFrameScall().x * h0 + keyB.getFrameScall().x * h1 + keyA.getDd().x * h2 + keyB.getDs().x * h3,
                        keyA.getFrameScall().y * h0 + keyB.getFrameScall().y * h1 + keyA.getDd().y * h2 + keyB.getDs().y * h3,
                        keyA.getFrameScall().z * h0 + keyB.getFrameScall().z * h1 + keyA.getDd().z * h2 + keyB.getDs().z * h3);
            }
        }
        return currentScall;
    }
}
