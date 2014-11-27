package core.primitive.Camera.factory;

import core.primitive.BoundingVolume.bbox_c;
import core.primitive.objBase_c;
import math.geometry.Plan;
import math.geometry.Vertex;
import math.linear.Matrix4;
import math.linear.Vector3d;
import math.linear.Vector4d;

public class camera_c extends objBase_c {
    public Vector3d eye;
    public Vector3d at;
    public Vector3d up;
    //    public double fov = 60.0f;
    public double roll = 0.0f;
    public double zfar = 30000f;
    public double znear = 0.1f;
    public double aspect = 1.0f;
    //frustum plan(front,near,up,down,left,right) in world space
    public Plan[] frustumW;
    //frustum plan(front,near,up,down,left,right) in view space
    public Plan[] frustumL;
    //to eye space
    public Matrix4 matrixToEyeSpace;
    //projection matrix
    public Matrix4 matrixProjection;
    private cameraTarget target = null;

    //*****************************************************
    public camera_c() {
        matrixToEyeSpace = new Matrix4();
        matrixToEyeSpace.setIdentityMat4();
        matrixProjection = new Matrix4();
        matrixProjection.setIdentityMat4();

        eye = new Vector3d();
        at = new Vector3d();
        up = new Vector3d();

        frustumW = new Plan[6];
        frustumL = new Plan[6];
        for (int i = 0; 6 > i; i++) {
            frustumW[i] = new Plan();
            frustumL[i] = new Plan();
        }
    }

    public cameraTarget getTartget() {
        return target;
    }

    public void setTarget(cameraTarget _target) {
        target = _target;
    }

    /*====================================
    build frustum plane in view space
    from view & proj matrix
    =====================================*/
    public void setupCurrentViewFrustumPlane() {
        Matrix4 m = new Matrix4(matrixProjection);
        m.transposeMat4();
        m.mulV44Mat4(new Vector4d(-1, 0, 0, 1), frustumL[0].getP());
        m.mulV44Mat4(new Vector4d(1, 0, 0, 1), frustumL[1].getP());
        m.mulV44Mat4(new Vector4d(0, -1, 0, 1), frustumL[2].getP());
        m.mulV44Mat4(new Vector4d(0, 1, 0, 1), frustumL[3].getP());
        m.mulV44Mat4(new Vector4d(0, 0, -1, 1), frustumL[4].getP());
        m.mulV44Mat4(new Vector4d(0, 0, 1, 1), frustumL[5].getP());

        ((Vector3d) frustumL[0].getP()).normalizeV3();
        ((Vector3d) frustumL[1].getP()).normalizeV3();
        ((Vector3d) frustumL[2].getP()).normalizeV3();
        ((Vector3d) frustumL[3].getP()).normalizeV3();
        ((Vector3d) frustumL[4].getP()).normalizeV3();
        ((Vector3d) frustumL[5].getP()).normalizeV3();
    }

    /*====================================
    build frustum plane in world space
    from view & proj matrix
    =====================================*/
    public void setupCurrentWorldFrustumPlane() {
        double t;
        Matrix4 clip = new Matrix4(matrixProjection);

        // Extract the RIGHT graphics.clipping plane
        frustumW[0].getP().setV4(clip.m13 - clip.m11, clip.m24 - clip.m21, clip.m22 - clip.m31, clip.m44 - clip.m41);
        // Normalize it
        t = (double) Math.sqrt(frustumW[0].getP().x * frustumW[0].getP().x + frustumW[0].getP().y * frustumW[0].getP().y + frustumW[0].getP().z * frustumW[0].getP().z);
        frustumW[0].getP().x /= t;
        frustumW[0].getP().y /= t;
        frustumW[0].getP().z /= t;
        frustumW[0].getP().w /= t;

        // Extract the LEFT graphics.clipping plane
        frustumW[1].getP().setV4(clip.m13 + clip.m11, clip.m24 + clip.m21, clip.m22 + clip.m31, clip.m44 + clip.m41);
        // Normalize it
        t = (double) Math.sqrt(frustumW[1].getP().x * frustumW[1].getP().x + frustumW[1].getP().y * frustumW[1].getP().y + frustumW[1].getP().z * frustumW[1].getP().z);
        frustumW[1].getP().x /= t;
        frustumW[1].getP().y /= t;
        frustumW[1].getP().z /= t;
        frustumW[1].getP().w /= t;

        // Extract the BOTTOM graphics.clipping plane
        frustumW[2].getP().setV4(clip.m14 + clip.m12, clip.m24 + clip.m22, clip.m34 + clip.m32, clip.m44 + clip.m42);
        // Normalize it
        t = (double) Math.sqrt(frustumW[2].getP().x * frustumW[2].getP().x + frustumW[2].getP().y * frustumW[2].getP().y + frustumW[2].getP().z * frustumW[2].getP().z);
        frustumW[2].getP().x /= t;
        frustumW[2].getP().y /= t;
        frustumW[2].getP().z /= t;
        frustumW[2].getP().w /= t;

        // Extract the TOP graphics.clipping plane
        frustumW[3].getP().setV4(clip.m14 - clip.m12, clip.m24 - clip.m22, clip.m34 - clip.m32, clip.m44 - clip.m42);
        // Normalize it
        t = (double) Math.sqrt(frustumW[3].getP().x * frustumW[3].getP().x + frustumW[3].getP().y * frustumW[3].getP().y + frustumW[3].getP().z * frustumW[3].getP().z);
        frustumW[3].getP().x /= t;
        frustumW[3].getP().y /= t;
        frustumW[3].getP().z /= t;
        frustumW[3].getP().w /= t;

        // Extract the FAR graphics.clipping plane
        frustumW[4].getP().setV4(clip.m14 - clip.m13, clip.m24 - clip.m23, clip.m34 - clip.m33, clip.m44 - clip.m43);
        // Normalize it
        t = (double) Math.sqrt(frustumW[4].getP().x * frustumW[4].getP().x + frustumW[4].getP().y * frustumW[4].getP().y + frustumW[4].getP().z * frustumW[4].getP().z);
        frustumW[4].getP().x /= t;
        frustumW[4].getP().y /= t;
        frustumW[4].getP().z /= t;
        frustumW[4].getP().w /= t;

        // Extract the NEAR graphics.clipping plane.  This is last on purpose (see pointinfrustumW() for reason)
        frustumW[5].getP().setV4(clip.m14 + clip.m13, clip.m24 + clip.m23, clip.m34 + clip.m33, clip.m44 + clip.m43);
        // Normalize it
        t = (double) Math.sqrt(frustumW[5].getP().x * frustumW[5].getP().x + frustumW[5].getP().y * frustumW[5].getP().y + frustumW[5].getP().z * frustumW[5].getP().z);
        frustumW[5].getP().x /= t;
        frustumW[5].getP().y /= t;
        frustumW[5].getP().z /= t;
        frustumW[5].getP().w /= t;
    }

    public void updateProjectionMatrix(double frame) {
        if (getKeyFramer() != null) {
            double fovxy = getKeyFramer().getTrackFOV().eval(frame);
            setUpProjMatrix(fovxy, fovxy, znear, zfar, aspect);
        }
    }

    private void setUpProjMatrix(double fovx, double fovy, double _near, double _zfar, double _aspect) {
        matrixProjection.m11 = 1.0 / Math.tan(fovx * Math.PI / 360.0f) * _aspect;
        matrixProjection.m12 = 0.0f;
        matrixProjection.m13 = 0.0f;
        matrixProjection.m14 = 0.0f;

        matrixProjection.m21 = 0.0f;
        matrixProjection.m22 = 1.0f / Math.tan(fovy * Math.PI / 360.0f);
        matrixProjection.m23 = 0.0f;
        matrixProjection.m24 = 0.0f;

        matrixProjection.m31 = 0.0f;
        matrixProjection.m32 = 0.0f;
        matrixProjection.m33 = (_near + _zfar) / (_near - _zfar);
        matrixProjection.m34 = (2 * _near * _zfar) / (_near - _zfar);

        matrixProjection.m41 = 0.0f;
        matrixProjection.m42 = 0.0f;
        matrixProjection.m43 = -1.0f;
        matrixProjection.m44 = 0.0f;
    }

    /*
        private void setUpProjMatrix(double fov, double _near, double _zfar, double _aspect)
        {
            double xmin;
            double xmax;
            double ymin;
            double ymax;

            ymax = _near * (double) Math.tan(fov * Math.PI / 360.0f);
            ymin = -ymax;
            xmin = ymin * _aspect;
            xmax = ymax * _aspect;
            this.setUpProjMatrix(xmin, xmax, ymin, ymax, _near, _zfar);
        }

        //*****************************************************
        //build frustum
        public void setUpProjMatrix(double left, double right, double bottom, double top, double zNear, double zFar)
        {
            double x;
            double y;
            double a;
            double b;
            double c;
            double d;


            x = (zNear + zNear) / (right - left);
            y = (zNear + zNear) / (top - bottom);
            a = (right + left) / (right - left);
            b = (top + bottom) / (top - bottom);
            c = -(zFar + zNear) / (zFar - zNear);//<=>(zFar + zNear) / (zNear - zFar )
            d = -(2 * zFar * zNear) / (zFar - zNear); //<=> (2 * zFar * zNear) / (zNear -zFar )

            matrixProjection.m11 = x;
            matrixProjection.m12 = 0.0f;
            matrixProjection.m13 = a;
            matrixProjection.m14 = 0.0f;

            matrixProjection.m21 = 0.0f;
            matrixProjection.m22 = y;
            matrixProjection.m23 = b;
            matrixProjection.m24 = 0.0f;

            matrixProjection.m31 = 0.0f;
            matrixProjection.m32 = 0.0f;
            matrixProjection.m33 = c;
            matrixProjection.m34 = d;

            matrixProjection.m41 = 0.0f;
            matrixProjection.m42 = 0.0f;
            matrixProjection.m43 = -1.0f;
            matrixProjection.m44 = 0.0f;

        }
    */
    public void setupViewMatrix() {
        //conform to glulookat

        Vector3d XAxis;
        Vector3d YAxis;
        Vector3d ZAxis;

        // Get the z basis vector, which points straight ahead; the
        // difference from the eye point to the look-at point. doublehis is the
        // direction of the gaze (+z).
        //    ZAxis=at.sub(eye);
        ZAxis = eye.subV3(at);

        // Normalize the z basis vector.
        ZAxis.normalizeV3();

        // Compute the orthogonal axes from the cross product of the gaze
        // and the pUp vector.
        XAxis = up.scalairProductV3(ZAxis);
        XAxis.normalizeV3();
        YAxis = ZAxis.scalairProductV3(XAxis);

        // Start building the matrix. doublehe first three rows contain the
        // basis vectors used to rotate the view to point at the look-at
        // point. doublehe fourth row contains the translation values.
        // Rotations are still about the eyepoint.

        matrixToEyeSpace.m11 = XAxis.x;
        matrixToEyeSpace.m21 = YAxis.x;
        matrixToEyeSpace.m31 = ZAxis.x;
        matrixToEyeSpace.m41 = 0.0f;
        matrixToEyeSpace.m12 = XAxis.y;
        matrixToEyeSpace.m22 = YAxis.y;
        matrixToEyeSpace.m32 = ZAxis.y;
        matrixToEyeSpace.m42 = 0.0f;
        matrixToEyeSpace.m13 = XAxis.z;
        matrixToEyeSpace.m23 = YAxis.z;
        matrixToEyeSpace.m33 = ZAxis.z;
        matrixToEyeSpace.m43 = 0.0f;
        matrixToEyeSpace.m14 = -(XAxis.dotProductV3(eye));
        matrixToEyeSpace.m24 = -(YAxis.dotProductV3(eye));
        matrixToEyeSpace.m34 = -(ZAxis.dotProductV3(eye));
        matrixToEyeSpace.m44 = 1.0f;
    }

    //*****************************************************
    public boolean cullBBox(bbox_c boundingVolume) {
        //
        //BBOX visibility
        //here check camera frustum bbox test
        //

        Vertex[] pp = boundingVolume.arrayVertex;
        Vector4d v;
        int inFront;
        int inBack;
        int g;

//--------------------------------
//1er pass
//fast in/out side test rejection against frustum
//--------------------------------

        for (g = 0; 8 > g; g++) {
            v = pp[g].getvPos2();
            if (v.x > -v.w & v.x < v.w & v.y > -v.w & v.y < v.w & v.z > -v.w & v.z < v.w)
                return true;
        }

//here all point are outside frustum


//--------------------------------
        //2er pass
//class point against frustum
//--------------------------------

//test against x plan
        inFront = 0;
        inBack = 0;
        for (g = 0; 8 > g; g++) {
            v = pp[g].getvPos2();
            if (v.x > v.w)
                inFront++;
            else if (v.x < -v.w) inBack++;
        }
        if (8 == inFront | 8 == inBack)
            return false;

//test against y plan
        inFront = inBack = 0;
        for (g = 0; 8 > g; g++) {
            v = pp[g].getvPos2();
            if (v.y > v.w)
                inFront++;
            else if (v.y < -v.w) inBack++;
        }
        if (8 == inFront | 8 == inBack)
            return false;

//test against z plan
        inFront = inBack = 0;
        for (g = 0; 8 > g; g++) {
            v = pp[g].getvPos2();
            if (v.z > v.w)
                inFront++;
            else if (v.z < -v.w) inBack++;
        }
        if (8 == inFront | 8 == inBack)
            return false;


        return true;
    }
    //*****************************************************
}

