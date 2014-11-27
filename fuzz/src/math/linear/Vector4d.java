package math.linear;

public class Vector4d extends Vector3d {
    public double w = 0;

    public Vector4d() {
    }

    public Vector4d(Vector4d a, Vector4d b, double f) {
        x = a.x + (b.x - a.x) * f;
        y = a.y + (b.y - a.y) * f;
        z = a.z + (b.z - a.z) * f;
        w = a.w + (b.w - a.w) * f;
    }

    public Vector4d(double _x, double _y, double _z, double _w) {
        x = _x;
        y = _y;
        z = _z;
        w = _w;
    }

    public Vector4d(Vector3d v, double f) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = f;
    }

    public Vector4d(Vector4d v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
    }

    public Vector4d mulV4f(double a) {
        x *= a;
        y *= a;
        z *= a;
        w *= a;
        return this;
    }

    public Vector4d normalizeV4() {
        double l = 1.0 / Math.sqrt(x * x + y * y + z * z + w * w);
        x *= l;
        y *= l;
        z *= l;
        w *= l;
        return this;
    }

    public Vector4d addV4(Vector4d a) {
        x += a.x;
        y += a.y;
        z += a.z;
        w += a.w;
        return this;
    }

    public Vector4d subV4(Vector4d a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;
        w -= a.w;
        return this;
    }

    public Vector4d setV4(Vector4d a, Vector4d b, double f) {
        x = a.x + (b.x - a.x) * f;
        y = a.y + (b.y - a.y) * f;
        z = a.z + (b.z - a.z) * f;
        w = a.w + (b.w - a.w) * f;
        return this;
    }

    public Vector4d setV4(Vector3d v, double f) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = f;
        return this;
    }

    public Vector4d setV4(Vector4d v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
        return this;
    }

    public Vector4d setV4Zero() {
        x = y = z = w = 0.0;
        return this;
    }

    public Vector4d setV4(double _x, double _y, double _z, double _w) {
        x = _x;
        y = _y;
        z = _z;
        w = _w;
        return this;
    }
}

