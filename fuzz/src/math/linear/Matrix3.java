package math.linear;


public class Matrix3 {
    /*
     matrix  structure
     l1=(U.x,U.y,U.x)
     l2=(V.x,V.y,V.y)
     l3=(N.x,N.y,N.z)
     */
    public double m11, m12, m13;
    public double m21, m22, m23;
    public double m31, m32, m33;

    //**************************************************
    public Matrix3(double _m11, double _m12, double _m13, double _m21, double _m22, double _m23, double _m31, double _m32, double _m33) {
        m11 = _m11;
        m12 = _m12;
        m13 = _m13;
        m21 = _m21;
        m22 = _m22;
        m23 = _m23;
        m31 = _m31;
        m32 = _m32;
        m33 = _m33;
    }

    //**************************************************
    public Matrix3(Matrix3 m) {
        m11 = m.m11;
        m12 = m.m12;
        m13 = m.m13;
        m21 = m.m21;
        m22 = m.m22;
        m23 = m.m23;
        m31 = m.m31;
        m32 = m.m32;
        m33 = m.m33;
    }

    //**************************************************
    public Matrix3(Vector3d v1, Vector3d v2, Vector3d v3) {
        m11 = v1.x;
        m12 = v1.y;
        m13 = v1.z;
        m21 = v2.x;
        m22 = v2.y;
        m23 = v2.z;
        m31 = v3.x;
        m32 = v3.y;
        m33 = v3.z;
    }

    //**************************************************
    public Matrix3() {
    }

    ;

    //**************************************************
    public static void mulM3(Matrix3 A, Matrix3 B, Matrix3 out) {
        double mam11 = A.m11 * B.m11 + A.m12 * B.m21 + A.m13 * B.m31;
        double mam12 = A.m11 * B.m12 + A.m12 * B.m22 + A.m13 * B.m32;
        double mam13 = A.m11 * B.m13 + A.m12 * B.m23 + A.m13 * B.m33;
        double mam21 = A.m21 * B.m11 + A.m22 * B.m21 + A.m23 * B.m31;
        double mam22 = A.m21 * B.m12 + A.m22 * B.m22 + A.m23 * B.m32;
        double mam23 = A.m21 * B.m13 + A.m22 * B.m23 + A.m23 * B.m33;
        double mam31 = A.m31 * B.m11 + A.m32 * B.m21 + A.m13 * B.m31;
        double mam32 = A.m31 * B.m12 + A.m32 * B.m22 + A.m33 * B.m32;
        double mam33 = A.m31 * B.m13 + A.m32 * B.m23 + A.m33 * B.m33;

        out.m11 = mam11;
        out.m12 = mam12;
        out.m13 = mam13;
        out.m21 = mam21;
        out.m22 = mam22;
        out.m23 = mam23;
        out.m31 = mam31;
        out.m32 = mam32;
        out.m33 = mam33;
    }

    //**************************************************
    //set to zero matrix3
    public Matrix3 setZeroMat3() {
        m11 = m12 = m13 = m21 = m22 = m23 = m31 = m32 = m33 = 0.0;
        return this;
    }

    //**************************************************
    //set to identity matrix3
    public Matrix3 setIdentityMat3() {
        m12 = m13 = m21 = m23 = m31 = m32 = 0.0;
        m11 = m22 = m33 = 1.0;
        return this;
    }

    //**************************************************
    //set to identity matrix3
    public Matrix3 setScallVMat3(Vector3d v) {
        m12 = m13 = m21 = m23 = m31 = m32 = 0.0;
        m11 = v.x;
        m22 = v.y;
        m33 = v.z;
        return this;
    }

    //**************************************************
    //set matrix3 rotx
    public Matrix3 setRotateXMat3(double angle) {
        double cs = Math.cos(angle);
        double sn = Math.sin(angle);
/*
        m11 = 1.0;        m12 = 0.0;       m13 = 0.0;
        m21 = 0.0;        m22 = cs;         m23 = sn;
        m31 = 0.0;        m32 = -sn;        m33 = cs;
  */
        m11 = 1.0;
        m12 = 0.0;
        m13 = 0.0;
        m21 = 0.0;
        m22 = cs;
        m23 = -sn;
        m31 = 0.0;
        m32 = sn;
        m33 = cs;
        return this;
    }

    //**************************************************
    //set matrix3 roty
    public Matrix3 setRotateYMat3(double angle) {
        double cs = (double) Math.cos(angle);
        double sn = (double) Math.sin(angle);
/*
        m11 = cs;        m12 = 0.0;        m13 = -sn;
        m21 = 0.0;        m22 = 1.0;        m23 = 0.0;
        m31 = sn;        m32 = 0.0;        m33 = cs;
        */
        m11 = cs;
        m12 = 0.0;
        m13 = sn;
        m21 = 0.0;
        m22 = 1.0;
        m23 = 0.0;
        m31 = -sn;
        m32 = 0.0;
        m33 = cs;

        return this;
    }

    //**************************************************
    //set matrix3 rotxyz
    /*
    math.linalg.Matrix3 setRotateMat3(double angle,double x,double y,double z)
    {
      double cs = (double)math.cos(angle);
      double sn = (double)math.sin(angle);
      double omcs = 1.0f-cs;
      double x2 = x*x;
      double y2 = y*y;
      double z2 = z*z;
      double xym = x*y*omcs;
      double xzm = x*z*omcs;
      double yzm = y*z*omcs;
      double xsin = x*sn;
      double ysin = y*sn;
      double zsin = z*sn;

      m11=x2*omcs+cs;  m12=xym-zsin;    m13=xzm+ysin;
      m21=xym+zsin;    m22=y2*omcs+cs;  m23=yzm-xsin;
      m31=xzm-ysin;    m32=yzm+xsin;    m33=z2*omcs+cs;
      return this;
    }*/
    //**************************************************
    //convert quaternion to matrix3

    //**************************************************
    //set matrix3 rotz
    public Matrix3 setRotateZMat3(double angle) {
        double cs = (double) Math.cos(angle);
        double sn = (double) Math.sin(angle);
/*
        m11 = cs;        m12 = sn;        m13 = 0.0;
        m21 = -sn;        m22 = cs;        m23 = 0.0;
        m31 = 0.0;        m32 = 0.0;        m33 = 1.0;
        */
        m11 = cs;
        m12 = -sn;
        m13 = 0.0;
        m21 = sn;
        m22 = cs;
        m23 = 0.0;
        m31 = 0.0;
        m32 = 0.0;
        m33 = 1.0;
        return this;
    }

    public Matrix3 setMat3FromQuat(Quaternion q) {
        double s, xs, ys, zs, wx, wy, wz, xx, xy, xz, yy, yz, zz;
        double d;

        d = q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w;
        if (d == 0.0)
            s = 1.0;
        else
            s = 2.0 / d;

        xs = q.x * s;
        ys = q.y * s;
        zs = q.z * s;
        wx = q.w * xs;
        wy = q.w * ys;
        wz = q.w * zs;
        xx = q.x * xs;
        xy = q.x * ys;
        xz = q.x * zs;
        yy = q.y * ys;
        yz = q.y * zs;
        zz = q.z * zs;
/*
        m11 = 1.0f - (yy + zz);        m21 = xy - wz;           m31 = xz + wy;
        m12 = xy + wz;                 m22 = 1.0f - (xx + zz);  m32 = yz - wx;
        m13 = xz - wy;                 m23 = yz + wx;           m33 = 1.0f - (xx + yy);
*/

        m11 = 1.0 - (yy + zz);
        m12 = xy - wz;
        m13 = xz + wy;
        m21 = xy + wz;
        m22 = 1.0 - (xx + zz);
        m23 = yz - wx;
        m31 = xz - wy;
        m32 = yz + wx;
        m33 = 1.0 - (xx + yy);

        return this;
    }

    //**************************************************
    //convert quaternion to invert matrix3
    /*
    public Matrix3 setInvFromQuatMat3(Quaternion q) {
        double x2, y2, z2, wx, wy, wz, xx, xy, xz, yy, yz, zz;

        x2 = q.x + q.x;
        y2 = q.y + q.y;
        z2 = q.z + q.z;
        wx = q.w * x2;
        wy = q.w * y2;
        wz = q.w * z2;
        xx = q.x * x2;
        xy = q.x * y2;
        xz = q.x * z2;
        yy = q.y * y2;
        yz = q.y * z2;
        zz = q.z * z2;

        m11 = 1.0f - (yy + zz);        m21 = xy - wz;        m31 = xz + wy;
        m12 = xy + wz;        m22 = 1.0f - (xx + zz);        m32 = yz - wx;
        m13 = xz - wy;        m23 = yz + wx;        m33 = 1.0f - (xx + yy);


        return this;
    }
*/
    //**************************************************
    //return matrix determinent
    public double det() {
        return (m11 * (m22 * m33 - m32 * m23) - m12 * (m21 * m33 - m31 * m23) + m13 * (m21 * m32 - m31 * m22));
    }

    //**************************************************
    /*-------------------------------------------------------------------
     in:   A=matrix 3x3
     out: inv(A) using cramer's rule: inv(A)=(a')t/det(a)
     -------------------------------------------------------------------*/
    public Matrix3 inverse(Matrix3 mr) {

        double d = det();
        if (d != 0.0) {
            mr.m11 = (m22 * m33 - m32 * m23) * d;
            mr.m12 = ((-m12 * m33) + m32 * m13) * d;
            mr.m13 = (m12 * m23 - m22 * m13) * d;
            mr.m21 = ((-m21 * m33) + m31 * m23) * d;
            mr.m22 = (m11 * m33 - m31 * m13) * d;
            mr.m23 = ((-m11 * m23) + m21 * m13) * d;
            mr.m31 = (m21 * m32 - m31 * m22) * d;
            mr.m32 = ((-m11 * m32) + m31 * m12) * d;
            mr.m33 = (m11 * m22 - m21 * m12) * d;
        } else {
            // D�terminant nul
            mr.m11 = 1.0;
            mr.m12 = 0.0;
            mr.m13 = 0.0;
            mr.m21 = 0.0;
            mr.m22 = 1.0;
            mr.m23 = 0.0;
            mr.m31 = 0.0;
            mr.m32 = 0.0;
            mr.m33 = 1.0;
        }
        return this;
    }

    //**************************************************
    public void mulV33Mat3(Vector3d in, Vector3d out) {
        double xx = in.x * m11 + in.y * m12 + in.z * m13;
        double yy = in.x * m21 + in.y * m22 + in.z * m23;
        double zz = in.x * m31 + in.y * m32 + in.z * m33;
        out.x = xx;
        out.y = yy;
        out.z = zz;
    }
    //**************************************************
}

