package keyFramer.tracks;

import keyFramer.keys.KeyRoll;
import math.linear.Matrix4;
import math.linear.Quaternion;

public class TrackRoll extends TrackBase {
//    public Matrix4 currentRoll=new Matrix4();

    public TrackRoll() {
//        currentRoll.setIdentityMat4();
    }

    public final Object eval(double frame) {
        Matrix4 currentRoll = new Matrix4().setIdentityMat4();
        KeyRoll keyA;
        KeyRoll keyB;

        if (getKeyCount() == 0)
            return currentRoll;

        if (getKeyCount() == 1) {
            currentRoll.setMat3FromQuat(((KeyRoll) getKeyFrameAt(0)).getFrameRoll());
        } else {
            int keyFrameIndex = findKeyIndexAtFrame(frame);
            if (frame >= getKeyFrameAt(getKeyCount() - 1).getFrameTime())
                currentRoll.setMat3FromQuat(((KeyRoll) getKeyFrameAt(getKeyCount() - 1)).getFrameRoll());
            else {
                keyA = (KeyRoll) getKeyFrameAt(keyFrameIndex);
                keyB = (KeyRoll) getKeyFrameAt(keyFrameIndex + 1);

                double t = (frame - keyA.getFrameTime()) / (keyB.getFrameTime() - keyA.getFrameTime());

                //				currentRollQuat.setQ(math.linear.Quaternion.qSlerp(t,keyA.frameRoll,keyB.frameRoll,0));

                currentRoll.setMat3FromQuat(Quaternion.quatSlerp(t, keyA.getFrameRoll(), keyB.getFrameRoll(), 0));
            }
        }
        return currentRoll;
    }
}
