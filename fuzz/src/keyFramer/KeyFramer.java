package keyFramer;


import core.primitive.BoundingVolume.bbox_c;
import keyFramer.keys.*;
import keyFramer.tracks.*;
import math.linear.Matrix4;
import math.linear.Quaternion;
import math.linear.Vector3d;
import math.linear.Vector4d;

public class KeyFramer {
    public static String version = "keyFramer engine v1.0";
    //************************************************************************************************
    static Quaternion qprev = new Quaternion();
    static Quaternion qnext = new Quaternion();
    static Quaternion q = new Quaternion();
    static Quaternion qp = new Quaternion();
    static Quaternion qm = new Quaternion();
    static Quaternion qa = new Quaternion();
    static Quaternion qb = new Quaternion();
    static Quaternion qae = new Quaternion();
    static Quaternion qbe = new Quaternion();
    // keys quaternions
    static Quaternion QA = new Quaternion();
    static Quaternion QB = new Quaternion();
    static Quaternion QC = new Quaternion();
    // keys angle/axis representation
    static Vector4d QAA = new Vector4d();
    static Vector4d QAB = new Vector4d();
    static Vector4d QAC = new Vector4d();
    static double tm;
    static double cm;
    static double cp;
    static double bm;
    static double bp;
    static double tmcm;
    static double tmcp;
    static double ksm;
    static double ksp;
    static double kdm;
    static double kdp;
    static double dt;
    static double fp;
    static double fn;
    static double c;
    //bounding box
    public bbox_c localAABOX = new bbox_c();
    public bbox_c hierarchyAABOX = new bbox_c();
    //objet pivot
    public Vector3d pivot = new Vector3d(0.0, 0.0, 0.0);
    public Matrix4 objLocalMatrix = new Matrix4().setIdentityMat4();
    public Matrix4 objWordlMatrix = new Matrix4().setIdentityMat4();
    //flag for speeding up hierarchy computation
    public boolean hierarchyDone = false;
    private String name = "no name";
    private TrackTranslate trackTranslate = new TrackTranslate();
    private TrackRotate trackRotate = new TrackRotate();
    private TrackRoll trackRoll = new TrackRoll();
    private TrackScaling trackScaling = new TrackScaling();
    private TrackFov trackFOV = new TrackFov();
    private KeyFramer father = null;
    public KeyFramer() {
    }

    public KeyFramer getKeyframerParent() {
        return father;
    }

    public void setKeyframerParent(KeyFramer _KeyFramer) {
        father = _KeyFramer;
    }

    public KeyTanslate getKeyFramePosAt(int i) {
        return (KeyTanslate) trackTranslate.getKeyFrameAt(i);
    }

    public KeyRotate getKeyFrameRotAt(int i) {
        return (KeyRotate) trackRotate.getKeyFrameAt(i);
    }

    public KeyRoll getKeyFrameRollAt(int i) {
        return (KeyRoll) trackRoll.getKeyFrameAt(i);
    }

    public KeyScaling getKeyFrameScallAt(int i) {
        return (KeyScaling) trackScaling.getKeyFrameAt(i);
    }

    public KeyFov getKeyFrameFovAt(int i) {
        return (KeyFov) trackFOV.getKeyFrameAt(i);
    }

    public void buildLocalMatrix(double frame) {
        Matrix4 mt = new Matrix4();

        //set to identity the final local transforme matrix
        objLocalMatrix.setIdentityMat4();

        //center to pivot
        mt.setTranslate3fMat4(-pivot.x, -pivot.y, -pivot.z);
        Matrix4.mulM4(mt, objLocalMatrix, objLocalMatrix);

        //do local scall
        Matrix4.mulM4(trackScaling.eval(frame), objLocalMatrix, objLocalMatrix);

        //do local rot
        Matrix4.mulM4(trackRotate.eval(frame), objLocalMatrix, objLocalMatrix);

        //do local translation
        mt.setTranslateV3Mat4(trackTranslate.eval(frame));
        Matrix4.mulM4(mt, objLocalMatrix, objLocalMatrix);

        //re-translation to pivot
        mt.setTranslate3fMat4(pivot.x, pivot.y, pivot.z);
        Matrix4.mulM4(mt, objLocalMatrix, objLocalMatrix);
    }

    public void CompDeriv(KeyBase prevkey,
                          KeyBase key,
                          KeyBase nextkey,
                          Vector3d prevKeyPos,
                          Vector3d keyPos,
                          Vector3d nextKeyPos) {
        double dsA;
        double dsB;
        double ddA;
        double ddB;
        double dsAdjust;
        double ddAdjust;
        double v1;
        double v2;
        double pf;
        double f;
        double nf;

        pf = prevkey.getFrameTime();
        f = key.getFrameTime();
        nf = nextkey.getFrameTime();
        /*
        keyFramer.keys.KeyBase LastFrame;
        LastFrame=Return_Last_Frame((FRAME *)keys);
        if (pf > f)
        {			// if track have LOOP flag.
        f += LastFrame->keys;
        nf += LastFrame->keys;
        }
        else
        if (f > nf)
        {
        nf += LastFrame->keys;
        }
        */

        dsA = (1.0 - key.getTension()) * (1.0 - key.getContinuity()) * (1.0 + key.getBias());
        dsB = (1.0 - key.getTension()) * (1.0 + key.getContinuity()) * (1.0 - key.getBias());
        dsAdjust = (f - pf) / (nf - pf);

        ddA = (1.0 - key.getTension()) * (1.0 + key.getContinuity()) * (1.0 + key.getBias());
        ddB = (1.0 - key.getTension()) * (1.0 - key.getContinuity()) * (1.0 - key.getBias());
        ddAdjust = (nf - f) / (nf - pf);

        // X derivative.
        v1 = keyPos.x - prevKeyPos.x;
        v2 = nextKeyPos.x - keyPos.x;
        key.getDs().x = dsAdjust * (dsA * v1 + dsB * v2);
        key.getDd().x = ddAdjust * (ddA * v1 + ddB * v2);

        // Y derivative.
        v1 = keyPos.y - prevKeyPos.y;
        v2 = nextKeyPos.y - keyPos.y;
        key.getDs().y = dsAdjust * (dsA * v1 + dsB * v2);
        key.getDd().y = ddAdjust * (ddA * v1 + ddB * v2);

        // Z derivative.
        v1 = keyPos.z - prevKeyPos.z;
        v2 = nextKeyPos.z - keyPos.z;
        key.getDs().z = dsAdjust * (dsA * v1 + dsB * v2);
        key.getDd().z = ddAdjust * (ddA * v1 + ddB * v2);
    }

    public void CompDerivFirst(KeyBase key, KeyBase keyn, KeyBase keynn, Vector3d keyPos,
                               Vector3d keynPos, Vector3d keynnPos) {
        double f20;
        double f10;
        double v20;
        double v10;
        f20 = keynn.getFrameTime() - key.getFrameTime();
        f10 = keyn.getFrameTime() - key.getFrameTime();

        v20 = keynnPos.x - keyPos.x;
        v10 = keynPos.x - keyPos.x;
        key.getDd().x = (1.0 - key.getTension()) * (v20 * (0.25 - f10 / (2.0 * f20)) + (v10 - v20 / 2.0) * 3 / 2 + v20 / 2.0);

        v20 = keynnPos.y - keyPos.y;
        v10 = keynPos.y - keyPos.y;
        key.getDd().y = (1 - key.getTension()) * (v20 * (0.25 - f10 / (2.0 * f20)) + (v10 - v20 / 2.0) * 3 / 2 + v20 / 2.0);

        v20 = keynnPos.z - keyPos.z;
        v10 = keynPos.z - keyPos.z;
        key.getDd().z = (1 - key.getTension()) * (v20 * (0.25 - f10 / (2.0 * f20)) + (v10 - v20 / 2.0) * 3 / 2 + v20 / 2.0);
    }

    public void CompDerivLast(KeyBase keypp, KeyBase keyp, KeyBase key, Vector3d keyppPos, Vector3d keypPos, Vector3d keyPos) {
        double f20;
        double f10;
        double v20;
        double v10;
        f20 = key.getFrameTime() - keypp.getFrameTime();
        f10 = key.getFrameTime() - keyp.getFrameTime();

        v20 = keyPos.x - keyppPos.x;
        v10 = keyPos.x - keypPos.x;
        key.getDs().x = (1.0 - key.getTension()) * (v20 * (0.25 - f10 / (2.0 * f20)) + (v10 - v20 / 2.0) * 3 / 2 + v20 / 2.0);

        v20 = keyPos.y - keyppPos.y;
        v10 = keyPos.y - keypPos.y;
        key.getDs().y = (1.0 - key.getTension()) * (v20 * (0.25 - f10 / (2.0 * f20)) + (v10 - v20 / 2.0) * 3 / 2 + v20 / 2.0);

        v20 = keyPos.z - keyppPos.z;
        v10 = keyPos.z - keypPos.z;
        key.getDs().z = (1.0 - key.getTension()) * (v20 * (0.25 - f10 / (2.0 * f20)) + (v10 - v20 / 2.0) * 3 / 2 + v20 / 2.0);
    }

    public void CompDerivFirst2(KeyBase key, Vector3d posKey, Vector3d posKeyn) {
        double v;
        v = posKeyn.x - posKey.x;
        key.getDd().x = v * (1.0 - key.getTension());

        v = posKeyn.y - posKey.y;
        key.getDd().y = v * (1.0 - key.getTension());

        v = posKeyn.z - posKey.z;
        key.getDd().z = v * (1.0 - key.getTension());
    }

    public void CompDerivLast2(KeyBase key, Vector3d posKeyp, Vector3d posKey) {
        double v;
        v = posKey.x - posKeyp.x;
        key.getDs().x = v * (1.0 - key.getTension());

        v = posKey.y - posKeyp.y;
        key.getDs().y = v * (1.0 - key.getTension());

        v = posKey.z - posKeyp.z;
        key.getDs().z = v * (1.0 - key.getTension());
    }

    public void DerivSplinePos() {
        int n;
        int f2;
        int LastFrameIndex;
        int PrevLastFrameIndex;
        int PrevPrevLastFrameIndex;

        //LastFrame=trackTranslate.keyFrameArray[trackTranslate.nbKey];

        if (2 < trackTranslate.getKeyCount()) {
            for (n = 1; n < (trackTranslate.getKeyCount() - 1); n++)
                CompDeriv(getKeyFramePosAt(n - 1),
                        getKeyFramePosAt(n + 0),
                        getKeyFramePosAt(n + 1),
                        getKeyFramePosAt(n - 1).getFramePos(),
                        getKeyFramePosAt(n + 0).getFramePos(),
                        getKeyFramePosAt(n + 1).getFramePos()
                );
            /*
            if (true) //trackLOOP)
            {
            CompDeriv(nbKey-1,nbKey,0);
            CompDeriv(nbKey-1,nbKey,n+1);

            CompDeriv(PrevLastFrame,f,f->next);
            CompDeriv(PrevLastFrame,LastFrame,f->next);

            }


            else   */
            {
                CompDerivFirst(
                        getKeyFramePosAt(0),
                        getKeyFramePosAt(1),
                        getKeyFramePosAt(2),
                        getKeyFramePosAt(0).getFramePos(),
                        getKeyFramePosAt(1).getFramePos(),
                        getKeyFramePosAt(2).getFramePos()
                );

                CompDerivLast(
                        getKeyFramePosAt(trackTranslate.getKeyCount() - 3),
                        getKeyFramePosAt(trackTranslate.getKeyCount() - 2),
                        getKeyFramePosAt(trackTranslate.getKeyCount() - 1),
                        getKeyFramePosAt(trackTranslate.getKeyCount() - 3).getFramePos(),
                        getKeyFramePosAt(trackTranslate.getKeyCount() - 2).getFramePos(),
                        getKeyFramePosAt(trackTranslate.getKeyCount() - 1).getFramePos()
                );
            }

        } else if (1 < trackTranslate.getKeyCount()) //so 2 keys
        {
            CompDerivFirst2(
                    getKeyFramePosAt(0),
                    getKeyFramePosAt(0).getFramePos(),
                    getKeyFramePosAt(1).getFramePos()
            );
            CompDerivLast2(
                    getKeyFramePosAt(0),
                    getKeyFramePosAt(0).getFramePos(),
                    getKeyFramePosAt(1).getFramePos()
            );
        }
    }

    public void DerivSplineScall() {
        int n;
        int f2;
        int LastFrameIndex;
        int PrevLastFrameIndex;
        int PrevPrevLastFrameIndex;

        //LastFrame=trackTranslate.keyFrameArray[trackTranslate.nbKey];

        if (2 < trackScaling.getKeyCount()) {
            for (n = 1; n < trackScaling.getKeyCount(); n++)
                CompDeriv(
                        getKeyFrameScallAt(0),
                        getKeyFrameScallAt(1),
                        getKeyFrameScallAt(2),
                        getKeyFrameScallAt(0).getFrameScall(),
                        getKeyFrameScallAt(1).getFrameScall(),
                        getKeyFrameScallAt(2).getFrameScall()
                );

            /*			if (trackLOOP)
            {
            CompDeriv(nbKey-1,nbKey,0);
            CompDeriv(nbKey-1,nbKey,n+1);
            }
            else
            */
            {
                CompDerivFirst
                        (
                                getKeyFrameScallAt(0),
                                getKeyFrameScallAt(1),
                                getKeyFrameScallAt(2),
                                getKeyFrameScallAt(0).getFrameScall(),
                                getKeyFrameScallAt(1).getFrameScall(),
                                getKeyFrameScallAt(2).getFrameScall()
                        );

                CompDerivLast(
                        getKeyFrameScallAt(trackScaling.getKeyCount() - 3),
                        getKeyFrameScallAt(trackScaling.getKeyCount() - 2),
                        getKeyFrameScallAt(trackScaling.getKeyCount() - 1),
                        getKeyFrameScallAt(trackScaling.getKeyCount() - 3).getFrameScall(),
                        getKeyFrameScallAt(trackScaling.getKeyCount() - 2).getFrameScall(),
                        getKeyFrameScallAt(trackScaling.getKeyCount() - 1).getFrameScall()
                );
            }
        } else if (1 < trackScaling.getKeyCount()) //so 2 keys
        {
            CompDerivFirst2(
                    getKeyFrameScallAt(0),
                    getKeyFrameScallAt(0).getFrameScall(),
                    getKeyFrameScallAt(1).getFrameScall()
            );
            CompDerivLast2(
                    getKeyFrameScallAt(0),
                    getKeyFrameScallAt(0).getFrameScall(),
                    getKeyFrameScallAt(1).getFrameScall()
            );
        }
    }

    public void DerivSplineRot() {
        //1 or 0 keys is not enought
        if (1 >= trackRotate.getKeyCount())
            return;

        // 3 or more keys
        if (2 < trackRotate.getKeyCount()) {
            for (int i = 1; i < (trackRotate.getKeyCount() - 1); i++)
                CompAB(
                        getKeyFrameRotAt(i - 1),
                        getKeyFrameRotAt(i),
                        getKeyFrameRotAt(i + 1)
                );
        }

        // first and last keys for 2 keys track and more
        CompAB(
                null,
                getKeyFrameRotAt(0),
                getKeyFrameRotAt(1)
        );
        CompAB(
                getKeyFrameRotAt(trackRotate.getKeyCount() - 2),
                getKeyFrameRotAt(trackRotate.getKeyCount() - 1),
                null);
    }

    void CompAB(KeyRotate prev, KeyRotate cur, KeyRotate next) {
        /*
        CompAB: Compute derivatives for rotation keys.
        */

        //qt_copy (cur->val._quat, &QAB);
        KeyFramer.QAB.x = cur.getqR().x;
        KeyFramer.QAB.y = cur.getqR().y;
        KeyFramer.QAB.z = cur.getqR().z;
        KeyFramer.QAB.w = cur.getqR().w;

        //qt_copy (&cur->qa, &QB);
        KeyFramer.QB.x = cur.getqA().x;
        KeyFramer.QB.y = cur.getqA().y;
        KeyFramer.QB.z = cur.getqA().z;
        KeyFramer.QB.w = cur.getqA().w;

        if (null != prev) {
            //qt_copy (&prev->val._quat, &QAA);
            KeyFramer.QAA.x = prev.getqR().x;
            KeyFramer.QAA.y = prev.getqR().y;
            KeyFramer.QAA.z = prev.getqR().z;
            KeyFramer.QAA.w = prev.getqR().w;
            //qt_copy (&prev->qa, &QA);
            KeyFramer.QA.x = prev.getqA().x;
            KeyFramer.QA.y = prev.getqA().y;
            KeyFramer.QA.z = prev.getqA().z;
            KeyFramer.QA.w = prev.getqA().w;
        }
        if (null != next) {
            //qt_copy (&next->val._quat, &QAC);
            KeyFramer.QAC.x = next.getqR().x;
            KeyFramer.QAC.y = next.getqR().y;
            KeyFramer.QAC.z = next.getqR().z;
            KeyFramer.QAC.w = next.getqR().w;
            //qt_copy (&next->qa, &QC);
            KeyFramer.QC.x = next.getqA().x;
            KeyFramer.QC.y = next.getqA().y;
            KeyFramer.QC.z = next.getqA().z;
            KeyFramer.QC.w = next.getqA().w;
        }
        if (null != prev) {
            if (2.0 * Math.PI - 0.0001 < Math.abs(QAB.w - QAA.w)) {
                //        qt_copy (&QAB, &q);
                KeyFramer.q.x = KeyFramer.QB.x;
                KeyFramer.q.y = KeyFramer.QB.y;
                KeyFramer.q.z = KeyFramer.QB.z;
                KeyFramer.q.w = KeyFramer.QB.w;
                KeyFramer.q.w = 0.0;
                //        qt_log (&q, &qm);
                Quaternion.quatLog(KeyFramer.q, KeyFramer.qm);
            } else {
                //        qt_copy (&QA, &qprev);
                KeyFramer.qprev.x = KeyFramer.QA.x;
                KeyFramer.qprev.y = KeyFramer.QA.y;
                KeyFramer.qprev.z = KeyFramer.QA.z;
                KeyFramer.qprev.w = KeyFramer.QA.w;

                //        if (qt_dotunit (&qprev, &QB) < 0.0)
                if (0.0 > qprev.dotUnit(QB))
                    //qt_negate (&qprev, &qprev);
                    KeyFramer.qprev = KeyFramer.qprev.neg();

                //qt_lndif (&qprev, &QB, &qm);
                Quaternion.quatLndif(KeyFramer.qprev, KeyFramer.QB, KeyFramer.qm);
            }
        }
        if (null != next) {
            if (2.0 * Math.PI - 0.00001 < Math.abs(QAC.w - QAB.w)) {
                //      qt_copy (&QAC, &q);
                KeyFramer.q.x = KeyFramer.QAC.x;
                KeyFramer.q.y = KeyFramer.QAC.y;
                KeyFramer.q.z = KeyFramer.QAC.z;
                KeyFramer.q.w = KeyFramer.QAC.w;
                KeyFramer.q.w = 0.0;
                //      qt_log (&q, &qp);
                Quaternion.quatLog(KeyFramer.q, KeyFramer.qp);
            } else {
                //      qt_copy (&QC, &qnext);
                KeyFramer.qnext.x = KeyFramer.QC.x;
                KeyFramer.qnext.y = KeyFramer.QC.y;
                KeyFramer.qnext.z = KeyFramer.QC.z;
                KeyFramer.qnext.w = KeyFramer.QC.w;
                //      if (qt_dotunit (&qnext, &QB) < 0.0)
                if (0.0 > qnext.dotUnit(QB))
                    //        qt_negate (&qnext, &qnext);
                    KeyFramer.qnext = KeyFramer.qnext.neg();

                //      qt_lndif (&QB, &qnext, &qp);
                Quaternion.quatLndif(KeyFramer.QB, KeyFramer.qnext, KeyFramer.qp);
            }
        }
        if (null == prev) {
            //    qt_copy (&qp, &qm);
            KeyFramer.qp.x = KeyFramer.qm.x;
            KeyFramer.qp.y = KeyFramer.qm.y;
            KeyFramer.qp.z = KeyFramer.qm.z;
            KeyFramer.qp.w = KeyFramer.qm.w;
        }
        if (null == next) {
            //    qt_copy (&qm, &qp);
            KeyFramer.qm.x = KeyFramer.qp.x;
            KeyFramer.qm.y = KeyFramer.qp.y;
            KeyFramer.qm.z = KeyFramer.qp.z;
            KeyFramer.qm.w = KeyFramer.qp.w;
        }

        KeyFramer.fp = KeyFramer.fn = 1.0;
        KeyFramer.cm = 1.0 - cur.getContinuity();
        if (null != prev && null != next) {
            KeyFramer.dt = 0.5 * (next.getFrameTime() - prev.getFrameTime());
            KeyFramer.fp = (cur.getFrameTime() - prev.getFrameTime()) / KeyFramer.dt;
            KeyFramer.fn = (next.getFrameTime() - cur.getFrameTime()) / KeyFramer.dt;
            KeyFramer.c = Math.abs(cur.getContinuity());
            KeyFramer.fp = KeyFramer.fp + KeyFramer.c - KeyFramer.c * KeyFramer.fp;
            KeyFramer.fn = KeyFramer.fn + KeyFramer.c - KeyFramer.c * KeyFramer.fn;
        }
        KeyFramer.tm = 0.5 * (1.0 - cur.getTension());
        KeyFramer.cp = 2.0 - KeyFramer.cm;
        KeyFramer.bm = 1.0 - cur.getBias();
        KeyFramer.bp = 2.0 - KeyFramer.bm;
        KeyFramer.tmcm = KeyFramer.tm * KeyFramer.cm;
        KeyFramer.tmcp = KeyFramer.tm * KeyFramer.cp;
        KeyFramer.ksm = 1.0 - KeyFramer.tmcm * KeyFramer.bp * KeyFramer.fp;
        KeyFramer.ksp = -KeyFramer.tmcp * KeyFramer.bm * KeyFramer.fp;
        KeyFramer.kdm = KeyFramer.tmcp * KeyFramer.bp * KeyFramer.fn;
        KeyFramer.kdp = KeyFramer.tmcm * KeyFramer.bm * KeyFramer.fn - 1.0;
        KeyFramer.qa.x = 0.5 * (KeyFramer.kdm * KeyFramer.qm.x + KeyFramer.kdp * KeyFramer.qp.x);
        KeyFramer.qb.x = 0.5 * (KeyFramer.ksm * KeyFramer.qm.x + KeyFramer.ksp * KeyFramer.qp.x);
        KeyFramer.qa.y = 0.5 * (KeyFramer.kdm * KeyFramer.qm.y + KeyFramer.kdp * KeyFramer.qp.y);
        KeyFramer.qb.y = 0.5 * (KeyFramer.ksm * KeyFramer.qm.y + KeyFramer.ksp * KeyFramer.qp.y);
        KeyFramer.qa.z = 0.5 * (KeyFramer.kdm * KeyFramer.qm.z + KeyFramer.kdp * KeyFramer.qp.z);
        KeyFramer.qb.z = 0.5 * (KeyFramer.ksm * KeyFramer.qm.z + KeyFramer.ksp * KeyFramer.qp.z);
        KeyFramer.qa.w = 0.5 * (KeyFramer.kdm * KeyFramer.qm.w + KeyFramer.kdp * KeyFramer.qp.w);
        KeyFramer.qb.w = 0.5 * (KeyFramer.ksm * KeyFramer.qm.w + KeyFramer.ksp * KeyFramer.qp.w);

        //  qt_exp (&qa, &qae);
        Quaternion.quatExp(KeyFramer.qa, KeyFramer.qae);

        //  qt_exp (&qb, &qbe);
        Quaternion.quatExp(KeyFramer.qb, KeyFramer.qbe);

        //  qt_mul (&QB, &qae, &cur->ds);
        Quaternion.mulQ(KeyFramer.QB, KeyFramer.qae, KeyFramer.QA);
        cur.getDs().x = KeyFramer.QA.x;
        cur.getDs().y = KeyFramer.QA.y;
        cur.getDs().z = KeyFramer.QA.z;

        //  qt_mul (&QB, &qbe, &cur->dd);
        Quaternion.mulQ(KeyFramer.QB, KeyFramer.qbe, KeyFramer.QA);
        cur.getDd().x = KeyFramer.QA.x;
        cur.getDd().y = KeyFramer.QA.y;
        cur.getDd().z = KeyFramer.QA.z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrackTranslate getTrackPos() {
        return trackTranslate;
    }

    public void setTrackPos(TrackTranslate trackTranslate) {
        this.trackTranslate = trackTranslate;
    }

    public TrackRotate getTrackRot() {
        return trackRotate;
    }

    public void setTrackRot(TrackRotate trackRotate) {
        this.trackRotate = trackRotate;
    }

    public TrackRoll getTrackRoll() {
        return trackRoll;
    }

    public void setTrackRoll(TrackRoll trackRoll) {
        this.trackRoll = trackRoll;
    }

    public TrackScaling getTrackScall() {
        return trackScaling;
    }

    public void setTrackScall(TrackScaling trackScaling) {
        this.trackScaling = trackScaling;
    }

    public TrackFov getTrackFOV() {
        return trackFOV;
    }

    public void setTrackFOV(TrackFov trackFOV) {
        this.trackFOV = trackFOV;
    }
}