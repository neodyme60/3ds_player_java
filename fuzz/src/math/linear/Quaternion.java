package math.linear;


public class Quaternion extends Vector4d {

    /*-------------------------------------------------------------------
    //square a quaternion
    in:
    out:
    -------------------------------------------------------------------*/
    public static Quaternion q = new Quaternion();

    /*-------------------------------------------------------------------
    in: 4 value for quaternion
    out: create a quaternion by the for values
    -------------------------------------------------------------------*/
    public Quaternion(double _x, double _y, double _z, double _w) {
        x = _x;
        y = _y;
        z = _z;
        w = _w;
    }

    /*-------------------------------------------------------------------
    in: 1 double &  1 vector
    out: create a quaternion by vector & double
    -------------------------------------------------------------------*/
    public Quaternion(Vector3d _v, double _w) {
        x = _v.x;
        y = _v.y;
        z = _v.z;
        w = _w;
    }

    /*-------------------------------------------------------------------
    in: double ptr
    out: create a quaternion by the 4 double pointed by ptr !!no check 4 ptr!=null
    -------------------------------------------------------------------*/
    public Quaternion(double _v[]) {
        x = _v[0];
        y = _v[1];
        z = _v[2];
        w = _v[3];
    }

    /*-------------------------------------------------------------------
    in:
    out:  create identity quat
    -------------------------------------------------------------------*/
    public Quaternion(Vector4d v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
    }

    /*-------------------------------------------------------------------
    in:
    out:  create identity quat
    -------------------------------------------------------------------*/
    public Quaternion() {
        x = y = z = 0.0;
        w = 1.0;
    }

    /*-------------------------------------------------------------------
    //Compute equivalent quaternion from [angle,axis] representation
    in: angle in rad, and axis
    out:  create from euler representation
    -------------------------------------------------------------------*/
    public Quaternion(double angle, Vector3d axis) {
        double halfAngle = angle * 0.5;
        double s = Math.sin(halfAngle);
        x = axis.x * s;
        y = axis.y * s;
        z = axis.z * s;
        w = Math.cos(halfAngle);
    }

    //***************************************************
    public static void mulQ(Quaternion a, Quaternion b, Quaternion out) {
        double _w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;
        double _x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y;
        double _y = a.w * b.y + a.y * b.w + a.z * b.x - a.x * b.z;
        double _z = a.w * b.z + a.z * b.w + a.x * b.y - a.y * b.x;

        out.setQ(_x, _y, _z, _w);

    }

    //***************************************************
    public static Quaternion quatSlerp(double time, Quaternion a, Quaternion b, double spin) {
        double k1, k2;                    /* interpolation coefficions. */
        double angle;                  /* angle between A and B */
        double angleSpin;            /* angle between A and B plus spin. */
        double sin_a, cos_a;    /* sine, cosine of angle */
        int flipk2;                /* use negation of k2. */

//    cos_a = Qdotunit( a,b );
        cos_a = a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
        if (cos_a < 0.0) {
            cos_a = -cos_a;
            flipk2 = -1;
        } else
            flipk2 = 1;

        if ((1.0 - cos_a) < 0.0001f) {
            k1 = 1.0f - time;
            k2 = time;
        } else {                /* normal case */
            angle = Math.acos(cos_a);
            sin_a = Math.sin(angle);
            angleSpin = angle + spin * Math.PI;
            k1 = Math.sin(angle - time * angleSpin) / sin_a;
            k2 = Math.sin(time * angleSpin) / sin_a;
        }
        k2 *= flipk2;

        return new Quaternion((double) (k1 * a.x + k2 * b.x), (double) (k1 * a.y + k2 * b.y), (double) (k1 * a.z + k2 * b.z), (double) (k1 * a.w + k2 * b.w));
    }

    //***************************************************
    public static void quatSlerpLong(double time, Quaternion a, Quaternion b, Quaternion out, double spin) {
        double k1, k2;                    /* interpolation coefficions. */
        double angle;                    /* angle between A and B */
        double angleSpin;                /* angle between A and B plus spin. */
        double sin_a, cos_a;                    /* sine, cosine of angle */

//    cos_a = Qdotunit( a,b );
        cos_a = a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;

        if (1.0f - Math.abs(cos_a) < 0.00001f) {
            k1 = 1.0f - time;
            k2 = time;
        } else {                /* normal case */
            angle = Math.acos(cos_a);
            sin_a = Math.sin(angle);
            angleSpin = angle + spin * Math.PI;
            k1 = Math.sin(angle - time * angleSpin) / sin_a;
            k2 = Math.sin(time * angleSpin) / sin_a;
        }
        out.x = (double) (k1 * a.x + k2 * b.x);
        out.y = (double) (k1 * a.y + k2 * b.y);
        out.z = (double) (k1 * a.z + k2 * b.z);
        out.w = (double) (k1 * a.w + k2 * b.w);
    }

    //***************************************************
    public static void fromMat3(Matrix3 mat, Quaternion out) {
        /*
        qt_frommat: convert rotation matrix to quaternion.
        */
        double tr, s;
        Quaternion q = new Quaternion();
        //    static int nxt[3] = {Y, Z, X};

        tr = mat.m11 + mat.m22 + mat.m33;
        if (tr > 0.0) {
            s = (double) Math.sqrt(tr + 1.0);
            q.w = s / 2.0;
            s = 0.5 / s;
            q.x = (mat.m23 - mat.m32) * s;
            q.y = (mat.m31 - mat.m13) * s;
            q.z = (mat.m12 - mat.m21) * s;
        } else {
            //i = 0;//X
            if (mat.m22 > mat.m11) {
                //i = 1;//Y;
                if (mat.m33 > mat.m22) {
                    //  i = 2;//Z;
                    // j = 0;//nxt[i];
                    // k = 1;//nxt[j];
                    s = (double) Math.sqrt((mat.m33 - (mat.m11 + mat.m22)) + 1.0);
                    q.z = s / 2.0;
                    if (s != 0.0) s = 0.5 / s;
                    q.w = (mat.m12 - mat.m21) * s;
                    q.x = (mat.m31 + mat.m13) * s;
                    q.y = (mat.m32 + mat.m23) * s;
                } else { //i=Y=1
                    //j = 2;//nxt[i];
                    //k = 0;//nxt[j];
                    s = Math.sqrt((mat.m22 - (mat.m33 + mat.m11)) + 1.0);
                    q.y = s / 2.0;
                    if (s != 0.0) s = 0.5 / s;
                    q.w = (mat.m31 - mat.m13) * s;
                    q.z = (mat.m23 + mat.m31) * s;
                    q.x = (mat.m21 + mat.m12) * s;

                }
            } else {//i=0
                //j = 1;//nxt[i];
                //k = 2;//nxt[j];
                s = Math.sqrt((mat.m11 - (mat.m22 + mat.m33)) + 1.0);
                q.x = s / 2.0;
                if (s != 0.0) s = 0.5 / s;
                q.w = (mat.m23 - mat.m32) * s;
                q.y = (mat.m12 + mat.m21) * s;
                q.z = (mat.m13 + mat.m31) * s;

            }
        }

        out.w = q.w;
        out.x = q.x;
        out.y = q.y;
        out.z = q.z;
    }

    //***************************************************
    public static void quatExp(Quaternion q, Quaternion qOut) /* Calculate quaternion`s exponent. */ {
        double d, d1;
        d = (Math.sqrt(q.x * q.x + q.y * q.y + q.z * q.z));
        if (d > 0.0)
            d1 = Math.sin(d) / d;
        else
            d1 = 1.0;
        qOut.w = (double) Math.cos(d);
        qOut.x = q.x * d1;
        qOut.y = q.y * d1;
        qOut.z = q.z * d1;
    }

    //***************************************************
    public static void quatLog(Quaternion q, Quaternion qOut)    /* Calculate quaternion`s logarithm. */ {
        double d;
        d = Math.sqrt(q.x * q.x + q.y * q.y + q.z * q.z);
        if (q.w != 0.0)
            d = Math.atan(d / q.w);
        else
            d = Math.PI * 0.5;
        qOut.w = 0.0;
        qOut.x = q.x * d;
        qOut.y = q.y * d;
        qOut.z = q.z * d;
    }

    //***************************************************
    // qinv:  Form multiplicative inverse of q
    public static void quatInv(Quaternion q, Quaternion qq) {
        double l;
        l = (q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w);
        if (l != 0.0)
            l = 1.0 / l;
        else
            l = 1.0;
        if (l == 0.0)
            l = 1.0;
        qq.x = -q.x * l;
        qq.y = -q.y * l;
        qq.z = -q.z * l;
        qq.w = q.w * l;
    }

    //***************************************************
    // Calculate logarithm of the relative rotation from p to q
    public static void quatLndif(Quaternion qa, Quaternion qb, Quaternion qOut) {
        Quaternion inv = new Quaternion();
        Quaternion dif = new Quaternion();
        double d, d1;
        double s;

        Quaternion.quatInv(qa, inv);            /* inv = -p; */
        Quaternion.mulQ(inv, q, dif);        /* dif = -p*q */

        d = (double) Math.sqrt(dif.x * dif.x + dif.y * dif.y + dif.z * dif.z);
        s = qa.x * qb.x + qa.y * qb.y + qa.z * qb.z + qa.w * qb.w;
        if (s != 0.0)
            d1 = Math.atan(d / s);
        else
            d1 = Math.PI * 0.5;
        if (d != 0.0)
            d1 /= d;
        qOut.w = 0.0;
        qOut.x = dif.x * d1;
        qOut.y = dif.y * d1;
        qOut.z = dif.z * d1;
    }

    //***************************************************
    public Quaternion add(Quaternion q) {
        x += q.x;
        y += q.y;
        z += q.z;
        w += q.w;
        return this;
    }

    //***************************************************
    public Quaternion setQ(Quaternion q) {
        x = q.x;
        y = q.y;
        z = q.z;
        w = q.w;
        return this;
    }

    //***************************************************
    public Quaternion setQ(double _x, double _y, double _z, double _w) {
        x = _x;
        y = _y;
        z = _z;
        w = _w;
        return this;
    }

    //***************************************************
    public Quaternion sub(Quaternion q) {
        x -= q.x;
        y -= q.y;
        z -= q.z;
        w -= q.w;
        return this;
    }

    //***************************************************
    public Quaternion mulf(double f) {
        x *= f;
        y *= f;
        z *= f;
        w *= f;
        return this;
    }

    //***************************************************
    public double dot(Quaternion q)  //dot unit product
    {
        return (x * q.x + y * q.y + z * q.z + w * q.w);
    }

    //***************************************************
    public double dotUnit(Quaternion q)  //dot unit product
    {
        return (x * q.x + y * q.y + z * q.z + w * q.w);
    }

    /**
     * ************************************************
     * quaternion conjuge
     * !!! must be unitaire
     * *************************************************
     */
    public Quaternion conj() {
        x = -x;
        y = -y;
        z = -z; //w=w
        return this;
    }

    //***************************************************
    public Quaternion neg() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
        return this;
    }

    /*-------------------------------------------------------------------
    //normaliz this
    in:
    out
    -------------------------------------------------------------------*/
    public Quaternion qnormalize() {
        double l, c;
        l = x * x + y * y + z * z + w * w;
        if (l < 0.001)
            return setIdentity();
        return mulf(1.0 / l);
    }

    /*-------------------------------------------------------------------
    in:
    out:  set this to identity
    -------------------------------------------------------------------*/
    public Quaternion setIdentity() {
        x = y = z = 0.0;
        w = 1.0;
        return this;
    }

    Quaternion sq() {
        q.w = w * w;
        q.w -= q.dot(q);
        q.x = x * 2.0 * q.w;
        q.y = y * 2.0 * q.w;
        q.z = z * 2.0 * q.w;

        return (Quaternion) setV4(q);
    }

    //***************************************************
    //convert quaternion to angle axis
    public void qToAngleAxis(double angle, Vector3d axis) {
        double length2 = x * x + y * y + z * z;
        double ll = 1.0 / Math.sqrt(length2);

        if (length2 > 0.0f) {
            axis = new Vector3d(x * ll, y * ll, z * ll);
            angle = (double) (2.0 * Math.acos(w));
        } else {
            // angle is 0 (mod 2*pi), so any axis will do
            axis.x = 1.0;
            axis.y = 0.0;
            axis.z = 0.0;
            angle = 0.0;
        }
    }
    //***************************************************
    //***************************************************
    //***************************************************

}