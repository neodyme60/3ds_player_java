package math.linear;


public class Matrix4 extends Matrix3 {

    /*
    matrix  structure
      l1=(U.x,U.y,U.z),double.x;
      l2=(V.x,V.y,V.z),double.y;
      l3=(N.x,N.y,N.z),double.z;
      l4=0.0,0.0,0.0  ,1.0;
    */
    public double m14;
    public double m24;
    public double m34;
    public double m41, m42, m43, m44;

    //************************
    //constructor
    //************************
    public Matrix4(double _m11, double _m12, double _m13, double _m14, double _m21, double _m22, double _m23, double _m24,
                   double _m31, double _m32, double _m33, double _m34, double _m41, double _m42, double _m43, double _m44) {
        m11 = _m11;
        m12 = _m12;
        m13 = _m13;
        m14 = _m14;
        m21 = _m21;
        m22 = _m22;
        m23 = _m23;
        m24 = _m24;
        m31 = _m31;
        m32 = _m32;
        m33 = _m33;
        m34 = _m34;
        m41 = _m41;
        m42 = _m42;
        m43 = _m43;
        m44 = _m44;
    }

    //**************************************************
    public Matrix4(Matrix4 m) {
        m11 = m.m11;
        m12 = m.m12;
        m13 = m.m13;
        m14 = m.m14;
        m21 = m.m21;
        m22 = m.m22;
        m23 = m.m23;
        m24 = m.m24;
        m31 = m.m31;
        m32 = m.m32;
        m33 = m.m33;
        m34 = m.m34;
        m41 = m.m41;
        m42 = m.m42;
        m43 = m.m43;
        m44 = m.m44;
    }

    //**************************************************
    public Matrix4(Vector4d v1, Vector4d v2, Vector4d v3, Vector4d v4) {
        m11 = v1.x;
        m12 = v1.y;
        m13 = v1.z;
        m14 = v1.w;
        m21 = v2.x;
        m22 = v2.y;
        m23 = v2.z;
        m24 = v2.w;
        m31 = v3.x;
        m32 = v3.y;
        m33 = v3.z;
        m34 = v3.w;
        m41 = v4.x;
        m42 = v4.y;
        m43 = v4.z;
        m44 = v4.w;
    }

    //**************************************************
    public Matrix4() {
    }

    //**************************************************
    public static void mulM4(Matrix4 in1, Matrix4 in2, Matrix4 out) {
        double mam11 = in2.m11 * in1.m11 + in2.m21 * in1.m12 + in2.m31 * in1.m13 + in2.m41 * in1.m14;
        double mam12 = in2.m12 * in1.m11 + in2.m22 * in1.m12 + in2.m32 * in1.m13 + in2.m42 * in1.m14;
        double mam13 = in2.m13 * in1.m11 + in2.m23 * in1.m12 + in2.m33 * in1.m13 + in2.m43 * in1.m14;
        double mam14 = in2.m14 * in1.m11 + in2.m24 * in1.m12 + in2.m34 * in1.m13 + in2.m44 * in1.m14;

        double mam21 = in2.m11 * in1.m21 + in2.m21 * in1.m22 + in2.m31 * in1.m23 + in2.m41 * in1.m24;
        double mam22 = in2.m12 * in1.m21 + in2.m22 * in1.m22 + in2.m32 * in1.m23 + in2.m42 * in1.m24;
        double mam23 = in2.m13 * in1.m21 + in2.m23 * in1.m22 + in2.m33 * in1.m23 + in2.m43 * in1.m24;
        double mam24 = in2.m14 * in1.m21 + in2.m24 * in1.m22 + in2.m34 * in1.m23 + in2.m44 * in1.m24;

        double mam31 = in2.m11 * in1.m31 + in2.m21 * in1.m32 + in2.m31 * in1.m33 + in2.m41 * in1.m34;
        double mam32 = in2.m12 * in1.m31 + in2.m22 * in1.m32 + in2.m32 * in1.m33 + in2.m42 * in1.m34;
        double mam33 = in2.m13 * in1.m31 + in2.m23 * in1.m32 + in2.m33 * in1.m33 + in2.m43 * in1.m34;
        double mam34 = in2.m14 * in1.m31 + in2.m24 * in1.m32 + in2.m34 * in1.m33 + in2.m44 * in1.m34;

        double mam41 = in2.m11 * in1.m41 + in2.m21 * in1.m42 + in2.m31 * in1.m43 + in2.m41 * in1.m44;
        double mam42 = in2.m12 * in1.m41 + in2.m22 * in1.m42 + in2.m32 * in1.m43 + in2.m42 * in1.m44;
        double mam43 = in2.m13 * in1.m41 + in2.m23 * in1.m42 + in2.m33 * in1.m43 + in2.m43 * in1.m44;
        double mam44 = in2.m14 * in1.m41 + in2.m24 * in1.m42 + in2.m34 * in1.m43 + in2.m44 * in1.m44;

        out.m11 = mam11;
        out.m12 = mam12;
        out.m13 = mam13;
        out.m14 = mam14;
        out.m21 = mam21;
        out.m22 = mam22;
        out.m23 = mam23;
        out.m24 = mam24;
        out.m31 = mam31;
        out.m32 = mam32;
        out.m33 = mam33;
        out.m34 = mam34;
        out.m41 = mam41;
        out.m42 = mam42;
        out.m43 = mam43;
        out.m44 = mam44;
    }

    //**************************************************
    public Matrix4 setMat4(Matrix4 m) {
        m11 = m.m11;
        m12 = m.m12;
        m13 = m.m13;
        m14 = m.m14;
        m21 = m.m21;
        m22 = m.m22;
        m23 = m.m23;
        m24 = m.m24;
        m31 = m.m31;
        m32 = m.m32;
        m33 = m.m33;
        m34 = m.m34;
        m41 = m.m41;
        m42 = m.m42;
        m43 = m.m43;
        m44 = m.m44;
        return this;
    }

    //**************************************************
    public Matrix4 transposeMat4() {
        Matrix4 m = new Matrix4(this);
        m11 = m.m11;
        m12 = m.m21;
        m13 = m.m31;
        m14 = m.m41;
        m21 = m.m12;
        m22 = m.m22;
        m23 = m.m32;
        m24 = m.m42;
        m31 = m.m13;
        m32 = m.m23;
        m33 = m.m33;
        m34 = m.m43;
        m41 = m.m14;
        m42 = m.m24;
        m43 = m.m34;
        m44 = m.m44;
        return this;
    }

    public Matrix4 setMat4FromQuat(Quaternion q) {
        setIdentityMat4();
        ((Matrix3) this).setMat3FromQuat(q);
        return this;
    }

    //**************************************************
    //set to zero matrix4
    public Matrix4 setZeroMat4() {
        m11 = m22 = m33 = m12 = m22 = m22 = m13 = m23 = m33 = m14 = m24 = m34 = m41 = m42 = m43 = m44 = 0.0;
        return this;
    }

    //**************************************************
    public Matrix4 setScale3fMat4(double a, double b, double c) {
        m12 = m21 = m31 = m13 = m23 = m32 = m14 = m41 = m42 = m24 = m43 = m34 = 0.0;
        m11 = a;
        m22 = b;
        m33 = c;
        m44 = 1.0;
        return this;
    }

    //**************************************************
    //set to scall
    public Matrix4 setScallV3Mat4(Vector3d v) {
        m12 = m21 = m31 = m13 = m23 = m32 = m14 = m41 = m42 = m24 = m43 = m34 = 0.0;
        m11 = v.x;
        m22 = v.y;
        m33 = v.z;
        m44 = 1.0;
        return this;
    }

    //**************************************************
    //set to identity matrix4
    public Matrix4 setIdentityMat4() {
        m12 = m21 = m31 = m13 = m23 = m32 = m14 = m41 = m42 = m24 = m43 = m34 = 0.0;
        m11 = m22 = m33 = m44 = 1.0;
        return this;
    }

    //**************************************************
    public Vector3d getTranslate() {
        return new Vector3d(m14, m24, m34);
    }

    //**************************************************
    public Matrix4 setTranslateV3Mat4(Vector3d t) {
        m12 = m21 = m31 = m13 = m23 = m32 = m41 = m42 = m43 = 0.0;
        m11 = m22 = m33 = m44 = 1.0;
        m14 = t.x;
        m24 = t.y;
        m34 = t.z;
        return this;
    }

    //**************************************************
    public Matrix4 setTranslate3fMat4(double x, double y, double z) {
        m12 = m21 = m31 = m13 = m23 = m32 = m41 = m42 = m43 = 0.0;
        m11 = m22 = m33 = m44 = 1.0;
        m14 = x;
        m24 = y;
        m34 = z;
        return this;
    }

    /*  void mulM4(math.linalg.Matrix4 m)
    {
      double mam11=m11*m.m11	+	m21*m.m12	+	m31*m.m13	+	m41*m.m14;
      double mam12=m12*m.m11	+	m22*m.m12	+	m32*m.m13	+	m42*m.m14;
      double mam13=m13*m.m11	+	m23*m.m12	+	m33*m.m13	+	m43*m.m14;
      double mam14=m14*m.m11	+	m24*m.m12	+	m34*m.m13	+	m44*m.m14;

      double mam21=m11*m.m21	+	m21*m.m22	+	m31*m.m23	+	m41*m.m24;
      double mam22=m12*m.m21	+	m22*m.m22	+	m32*m.m23	+	m42*m.m24;
      double mam23=m13*m.m21	+	m23*m.m22	+	m33*m.m23	+	m43*m.m24;
      double mam24=m14*m.m21	+	m24*m.m22	+	m34*m.m23	+	m44*m.m24;

      double mam31=m11*m.m31	+	m21*m.m32	+	m31*m.m33	+	m41*m.m34;
      double mam32=m12*m.m31	+	m22*m.m32	+	m32*m.m33	+	m42*m.m34;
      double mam33=m13*m.m31	+	m23*m.m32	+	m33*m.m33	+	m43*m.m34;
      double mam34=m14*m.m31	+	m24*m.m32	+	m34*m.m33	+	m44*m.m34;

      double mam41=m11*m.m41	+	m21*m.m42	+	m31*m.m43	+	m41*m.m44;
      double mam42=m12*m.m41	+	m22*m.m42	+	m32*m.m43	+	m42*m.m44;
      double mam43=m13*m.m41	+	m23*m.m42	+	m33*m.m43	+	m43*m.m44;
      double mam44=m14*m.m41	+	m24*m.m42	+	m34*m.m43	+	m44*m.m44;

      m11=mam11; m12=mam12; m13=mam13; m14=mam14;
      m21=mam21; m22=mam22; m23=mam23; m24=mam24;
      m31=mam31; m32=mam32; m33=mam33; m34=mam34;
      m41=mam41; m42=mam42; m43=mam43; m44=mam44;
    }*/
    //**************************************************
    public void mulV44Mat4(Vector4d in, Vector4d out) {
        double xx = in.x * m11 + in.y * m12 + in.z * m13 + in.w * m14;
        double yy = in.x * m21 + in.y * m22 + in.z * m23 + in.w * m24;
        double zz = in.x * m31 + in.y * m32 + in.z * m33 + in.w * m34;
        double ww = in.x * m41 + in.y * m42 + in.z * m43 + in.w * m44;
        out.x = xx;
        out.y = yy;
        out.z = zz;
        out.w = ww;
    }

    //**************************************************
    public void mulV3f4Mat4(Vector3d in, double _w, Vector4d out) {
        double xx = in.x * m11 + in.y * m12 + in.z * m13 + _w * m14;
        double yy = in.x * m21 + in.y * m22 + in.z * m23 + _w * m24;
        double zz = in.x * m31 + in.y * m32 + in.z * m33 + _w * m34;
        double ww = in.x * m41 + in.y * m42 + in.z * m43 + _w * m44;
        out.x = xx;
        out.y = yy;
        out.z = zz;
        out.w = ww;
    }

    //**************************************************
    public void mulV3f3Mat4(Vector3d in, double _w, Vector3d out) {
        double xx = in.x * m11 + in.y * m12 + in.z * m13 + _w * m14;
        double yy = in.x * m21 + in.y * m22 + in.z * m23 + _w * m24;
        double zz = in.x * m31 + in.y * m32 + in.z * m33 + _w * m34;
        out.x = xx;
        out.y = yy;
        out.z = zz;
    }

    //**************************************************
    //return matrix determinent
    public double det() {
        double res = 0.0f, det;
        // 28 muls total - totally inline-expanded and factored
        // Ugly (and nearly incomprehensible) but efficient
        double mr_3344_4334 = m33 * m44 - m43 * m34;
        double mr_3244_4234 = m32 * m44 - m42 * m34;
        double mr_3243_4233 = m32 * m43 - m42 * m33;
        double mr_3144_4134 = m31 * m44 - m41 * m34;
        double mr_3143_4133 = m31 * m43 - m41 * m33;
        double mr_3142_4132 = m31 * m42 - m41 * m32;

        //submat_4x4 (msub3, mr, 0, 0);
        //res += mr._11 * det_3x3 (msub3);
        det = m22 * mr_3344_4334 - m23 * mr_3244_4234 + m24 * mr_3243_4233;
        res += m11 * det;

        //submat_4x4 (msub3, mr, 0, 1);
        //res -= mr._12 * det_3x3 (msub3);
        det = m21 * mr_3344_4334 - m23 * mr_3144_4134 + m24 * mr_3143_4133;
        res -= m12 * det;

        //submat_4x4 (msub3, mr, 0, 2);
        //res += mr._13 * det_3x3 (msub3);
        det = m21 * mr_3244_4234 - m22 * mr_3144_4134 + m24 * mr_3142_4132;
        res += m13 * det;

        //submat_4x4 (msub3, mr, 0, 3);
        //res -= mr._14 * det_3x3 (msub3);
        det = m21 * mr_3243_4233 - m22 * mr_3143_4133 + m23 * mr_3142_4132;
        res -= m14 * det;
        return res;
    }

    /**
     * ***************************************************************************
     * Routine:   submat_4x4
     * Input:     mr  - matrix (4x4) address
     * mb  - matrix (3x3) address
     * i,j - matrix coordinates
     * Output:    returns the 3x3 subset of 'mr' without column 'i' and row 'j'
     * ****************************************************************************
     */
    public void submat_4x4(Matrix3 mb, int i, int j) {
        // Unrolled - big, but very fast (one indexed jump, one unconditional jump)
        switch (i * 4 + j) {
            // i == 0
            case 0:     // 0,0
                mb.m11 = m22;
                mb.m12 = m23;
                mb.m13 = m24;
                mb.m21 = m32;
                mb.m22 = m33;
                mb.m23 = m34;
                mb.m31 = m42;
                mb.m32 = m43;
                mb.m33 = m44;
                break;
            case 1:     // 0,1
                mb.m11 = m21;
                mb.m12 = m23;
                mb.m13 = m24;
                mb.m21 = m31;
                mb.m22 = m33;
                mb.m23 = m34;
                mb.m31 = m41;
                mb.m32 = m43;
                mb.m33 = m44;
                break;
            case 2:     // 0,2
                mb.m11 = m21;
                mb.m12 = m22;
                mb.m13 = m24;
                mb.m21 = m31;
                mb.m22 = m32;
                mb.m23 = m34;
                mb.m31 = m41;
                mb.m32 = m42;
                mb.m33 = m44;
                break;
            case 3:     // 0,3
                mb.m11 = m21;
                mb.m12 = m22;
                mb.m13 = m23;
                mb.m21 = m31;
                mb.m22 = m32;
                mb.m23 = m33;
                mb.m31 = m41;
                mb.m32 = m42;
                mb.m33 = m43;
                break;

            // i == 1
            case 4:     // 1,0
                mb.m11 = m12;
                mb.m12 = m13;
                mb.m13 = m14;
                mb.m21 = m32;
                mb.m22 = m33;
                mb.m23 = m34;
                mb.m31 = m42;
                mb.m32 = m43;
                mb.m33 = m44;
                break;
            case 5:     // 1,1
                mb.m11 = m11;
                mb.m12 = m13;
                mb.m13 = m14;
                mb.m21 = m31;
                mb.m22 = m33;
                mb.m23 = m34;
                mb.m31 = m41;
                mb.m32 = m43;
                mb.m33 = m44;
                break;
            case 6:     // 1,2
                mb.m11 = m11;
                mb.m12 = m12;
                mb.m13 = m14;
                mb.m21 = m31;
                mb.m22 = m32;
                mb.m23 = m34;
                mb.m31 = m41;
                mb.m32 = m42;
                mb.m33 = m44;
                break;
            case 7:     // 1,3
                mb.m11 = m11;
                mb.m12 = m12;
                mb.m13 = m13;
                mb.m21 = m31;
                mb.m22 = m32;
                mb.m23 = m33;
                mb.m31 = m41;
                mb.m32 = m42;
                mb.m33 = m43;
                break;

            // i == 2
            case 8:     // 2,0
                mb.m11 = m12;
                mb.m12 = m13;
                mb.m13 = m14;
                mb.m21 = m22;
                mb.m22 = m23;
                mb.m23 = m24;
                mb.m31 = m42;
                mb.m32 = m43;
                mb.m33 = m44;
                break;
            case 9:     // 2,1
                mb.m11 = m11;
                mb.m12 = m13;
                mb.m13 = m14;
                mb.m21 = m21;
                mb.m22 = m23;
                mb.m23 = m24;
                mb.m31 = m41;
                mb.m32 = m43;
                mb.m33 = m44;
                break;
            case 10:    // 2,2
                mb.m11 = m11;
                mb.m12 = m12;
                mb.m13 = m14;
                mb.m21 = m21;
                mb.m22 = m22;
                mb.m23 = m24;
                mb.m31 = m41;
                mb.m32 = m42;
                mb.m33 = m44;
                break;
            case 11:    // 2,3
                mb.m11 = m11;
                mb.m12 = m12;
                mb.m13 = m13;
                mb.m21 = m21;
                mb.m22 = m22;
                mb.m23 = m23;
                mb.m31 = m41;
                mb.m32 = m42;
                mb.m33 = m43;
                break;

            // i == 3
            case 12:    // 3,0
                mb.m11 = m12;
                mb.m12 = m13;
                mb.m13 = m14;
                mb.m21 = m22;
                mb.m22 = m23;
                mb.m23 = m24;
                mb.m31 = m32;
                mb.m32 = m33;
                mb.m33 = m34;
                break;
            case 13:    // 3,1
                mb.m11 = m11;
                mb.m12 = m13;
                mb.m13 = m14;
                mb.m21 = m21;
                mb.m22 = m23;
                mb.m23 = m24;
                mb.m31 = m31;
                mb.m32 = m33;
                mb.m33 = m34;
                break;
            case 14:    // 3,2
                mb.m11 = m11;
                mb.m12 = m12;
                mb.m13 = m14;
                mb.m21 = m21;
                mb.m22 = m22;
                mb.m23 = m24;
                mb.m31 = m31;
                mb.m32 = m32;
                mb.m33 = m34;
                break;
            case 15:    // 3,3
                mb.m11 = m11;
                mb.m12 = m12;
                mb.m13 = m13;
                mb.m21 = m21;
                mb.m22 = m22;
                mb.m23 = m23;
                mb.m31 = m31;
                mb.m32 = m32;
                mb.m33 = m33;
                break;
        }
    }


    /*-------------------------------------------------------------------
        inverse( original_matrix, inverse_matrix )

         calculate the inverse of a 4x4 matrix

          -1
          A  = ___1__ adjoint A
              det A

    in:
    out:
    -------------------------------------------------------------------*/
    public int inverse(Matrix4 mr) {
        double mdet = det();
        Matrix3 mtemp = new Matrix3();

//    if (math.abs (mdet) < 0.0005f)
//        return 0;

        mdet = 1.0 / mdet;

        // row column labeling reversed for out since we transpose rows & columns
        submat_4x4(mtemp, 0, 0);
        mr.m11 = mtemp.det() * mdet;

        submat_4x4(mtemp, 0, 1);
        mr.m21 = -mtemp.det() * mdet;

        submat_4x4(mtemp, 0, 2);
        mr.m31 = mtemp.det() * mdet;

        submat_4x4(mtemp, 0, 3);
        mr.m41 = -mtemp.det() * mdet;


        submat_4x4(mtemp, 1, 0);
        mr.m12 = -mtemp.det() * mdet;

        submat_4x4(mtemp, 1, 1);
        mr.m22 = mtemp.det() * mdet;

        submat_4x4(mtemp, 1, 2);
        mr.m32 = -mtemp.det() * mdet;

        submat_4x4(mtemp, 1, 3);
        mr.m42 = mtemp.det() * mdet;


        submat_4x4(mtemp, 2, 0);
        mr.m13 = mtemp.det() * mdet;

        submat_4x4(mtemp, 2, 1);
        mr.m23 = -mtemp.det() * mdet;

        submat_4x4(mtemp, 2, 2);
        mr.m33 = mtemp.det() * mdet;

        submat_4x4(mtemp, 2, 3);
        mr.m43 = -mtemp.det() * mdet;


        submat_4x4(mtemp, 3, 0);
        mr.m14 = -mtemp.det() * mdet;

        submat_4x4(mtemp, 3, 1);
        mr.m24 = mtemp.det() * mdet;

        submat_4x4(mtemp, 3, 2);
        mr.m34 = -mtemp.det() * mdet;

        submat_4x4(mtemp, 3, 3);
        mr.m44 = mtemp.det() * mdet;

        return 1;

    }

    //**************************************************
    public void setRotateX(double angle) {

        ((Matrix3) this).setRotateXMat3(angle);
        m14 = m24 = m34 = m41 = m42 = m43 = 0.0;
        m44 = 1.0;

    }

    //**************************************************
    public void setRotateY(double angle) {

        ((Matrix3) this).setRotateYMat3(angle);
        m14 = m24 = m34 = m41 = m42 = m43 = 0.0;
        m44 = 1.0;
    }

    //**************************************************
    public void setRotateZ(double angle) {

        ((Matrix3) this).setRotateZMat3(angle);
        m14 = m24 = m34 = m41 = m42 = m43 = 0.0;
        m44 = 1.0;

    }
    //**************************************************
    /*
    public void setRotate(double angle, double x, double y, double z) {
        double cs = (double) math.cos(angle);
        double sn = (double) math.sin(angle);
        double omcs = 1.0f - cs;
        double x2 = x * x;
        double y2 = y * y;
        double z2 = z * z;
        double xym = x * y * omcs;
        double xzm = x * z * omcs;
        double yzm = y * z * omcs;
        double xsin = x * sn;
        double ysin = y * sn;
        double zsin = z * sn;

        m11 = x2 * omcs + cs;
        m12 = xym - zsin;
        m13 = xzm + ysin;
        m21 = xym + zsin;
        m22 = y2 * omcs + cs;
        m23 = yzm - xsin;
        m31 = xzm - ysin;
        m32 = yzm + xsin;
        m33 = z2 * omcs + cs;
        m41 = m42 = m43 = 0.0;
        m44 = 1.0;
    }
*/
}

