package keyFramer.tracks;

import keyFramer.keys.KeyTanslate;
import math.linear.Vector3d;

public class TrackTranslate extends TrackBase {
    public TrackTranslate() {
    }

    public final Vector3d eval(double frame) {
        Vector3d currentPosTrack = new Vector3d(0.0f, 0.0f, 0.0f);
        KeyTanslate keyA;
        KeyTanslate keyB;

        if (getKeyCount() == 0)
            return currentPosTrack; //set to default pos

        if (getKeyCount() == 1)
//            currentPosTrack.setTranslateV3Mat4(((KeyTanslate) getKeyFrameAt(0)).getFramePos());
            currentPosTrack.setV3(((KeyTanslate) getKeyFrameAt(0)).getFramePos());
        else {
            int keyFrameIndex = findKeyIndexAtFrame(frame);
            if (frame >= getKeyFrameAt(getKeyCount() - 1).getFrameTime())
//                currentPosTrack.setTranslateV3Mat4(((KeyTanslate) getKeyFrameAt(getKeyCount() - 1)).getFramePos());
                currentPosTrack.setV3(((KeyTanslate) getKeyFrameAt(getKeyCount() - 1)).getFramePos());
            else {
                keyA = (KeyTanslate) getKeyFrameAt(keyFrameIndex);
                keyB = (KeyTanslate) getKeyFrameAt(keyFrameIndex + 1);

                double t = Ease((frame - keyA.getFrameTime()) / (keyB.getFrameTime() - keyA.getFrameTime()), keyA.getEase_from(), keyA.getEase_to());
                double t2 = t * t;
                double t3 = t2 * t;
                double h0 = 2 * t3 - 3 * t2 + 1;
                double h1 = -2 * t3 + 3 * t2;
                double h2 = t3 - 2 * t2 + t;
                double h3 = t3 - t2;
/*
                currentPosTrack.setTranslate3fMat4(
                        keyA.getFramePos().x * h0 + keyB.getFramePos().x * h1 + keyA.getDd().x * h2 + keyB.getDs().x * h3,
                        keyA.getFramePos().y * h0 + keyB.getFramePos().y * h1 + keyA.getDd().y * h2 + keyB.getDs().y * h3,
                        keyA.getFramePos().z * h0 + keyB.getFramePos().z * h1 + keyA.getDd().z * h2 + keyB.getDs().z * h3
                );
*/
                currentPosTrack.set(
                        keyA.getFramePos().x * h0 + keyB.getFramePos().x * h1 + keyA.getDd().x * h2 + keyB.getDs().x * h3,
                        keyA.getFramePos().y * h0 + keyB.getFramePos().y * h1 + keyA.getDd().y * h2 + keyB.getDs().y * h3,
                        keyA.getFramePos().z * h0 + keyB.getFramePos().z * h1 + keyA.getDd().z * h2 + keyB.getDs().z * h3
                );
            }
        }
        return currentPosTrack;
    }
}
