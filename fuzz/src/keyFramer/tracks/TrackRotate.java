package keyFramer.tracks;

import keyFramer.keys.KeyRotate;
import math.linear.Matrix4;
import math.linear.Quaternion;

public class TrackRotate extends TrackBase {
    public TrackRotate() {
    }

    public final Matrix4 eval(double frame) {
        Matrix4 currentRot = new Matrix4().setIdentityMat4();
        KeyRotate keyA;
        KeyRotate keyB;

        if (getKeyCount() == 0)
            return currentRot;

        if (getKeyCount() == 1)
            currentRot.setMat4FromQuat(((KeyRotate) getKeyFrameAt(0)).getqA());
        else {
            int keyFrameIndex = findKeyIndexAtFrame(frame);
            if (frame >= getKeyFrameAt(getKeyCount() - 1).getFrameTime()) {
                currentRot.setIdentityMat4();
                currentRot.setMat3FromQuat(((KeyRotate) getKeyFrameAt(getKeyCount() - 1)).getqA());
            } else {
                keyA = (KeyRotate) getKeyFrameAt(keyFrameIndex);
                keyB = (KeyRotate) getKeyFrameAt(keyFrameIndex + 1);

                double t = Ease((frame - keyA.getFrameTime()) / (keyB.getFrameTime() - keyA.getFrameTime()), keyA.getEase_from(), keyB.getEase_to());

                double a = keyB.getqA().w - keyA.getqA().w;
                double spin;
                if (0.0 < a)
                    spin = Math.floor(a / (2.0 * Math.PI));
                else
                    spin = Math.ceil(a / (2.0 * Math.PI));

                currentRot.setMat3FromQuat(Quaternion.quatSlerp(t, keyA.getqA(), keyB.getqA(), spin));
            }
        }
        return currentRot;
    }
}
