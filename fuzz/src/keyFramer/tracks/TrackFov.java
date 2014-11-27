package keyFramer.tracks;

import keyFramer.keys.KeyFov;


public class TrackFov extends TrackBase {
    private double defaultFov = 60.0f;
//    public double currentFov;

    public TrackFov() {
//        currentFov=defaultFov;
    }

    public final Double eval(double frame) {
        double fov = defaultFov;
        KeyFov keyA;
        KeyFov keyB;

        //0 key
        if (getKeyCount() == 0)
            return fov;

        //1 key
        if (getKeyCount() == 1) {
            fov = ((KeyFov) getKeyFrameAt(0)).getFrameFOV();
        } else {
            //at least 2 keys
            int keyFrameIndex = findKeyIndexAtFrame(frame);
            if (frame >= getKeyFrameAt(getKeyCount() - 1).getFrameTime())
                fov = ((KeyFov) getKeyFrameAt(getKeyCount() - 1)).getFrameFOV();
            else {
                keyA = (KeyFov) getKeyFrameAt(keyFrameIndex);
                keyB = (KeyFov) getKeyFrameAt(keyFrameIndex + 1);

                double t = (frame - keyA.getFrameTime()) / (keyB.getFrameTime() - keyA.getFrameTime());
                fov = keyA.getFrameFOV() + (keyB.getFrameFOV() - keyA.getFrameFOV()) * t;
            }
        }
        return fov;
    }

}
