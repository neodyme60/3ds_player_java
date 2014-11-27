package math.linear;

public class Vector3d {
    public double x = 0.0;
    public double y = 0.0;
    public double z = 0.0;

    public Vector3d() {
    }

    public Vector3d(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public Vector3d(Vector3d a, Vector3d b, double f) {
        x = a.x + (b.x - a.x) * f;
        y = a.y + (b.y - a.y) * f;
        z = a.z + (b.z - a.z) * f;
    }

    public Vector3d(Vector4d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vector3d(Vector3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public double dotProductV3(Vector3d v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3d scalairProductV3(Vector3d c) {
        return new Vector3d(y * c.z - z * c.y, z * c.x - x * c.z, x * c.y - y * c.x);
    }

    public Vector3d normalizeV3() {
        double l = 1.0 / Math.sqrt(x * x + y * y + z * z);
        x *= l;
        y *= l;
        z *= l;
        return this;
    }

    public Vector3d addV3(Vector3d a) {
        x += a.x;
        y += a.y;
        z += a.z;
        return this;
    }

    public Vector3d subV3(Vector3d a) {
        return new Vector3d(x - a.x, y - a.y, z - a.z);
    }

    public Vector3d lerp(Vector3d a, Vector3d b, double f) {
        x = a.x + (b.x - a.x) * f;
        y = a.y + (b.y - a.y) * f;
        z = a.z + (b.z - a.z) * f;
        return this;
    }

    public Vector3d setV3(Vector3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public Vector3d setV3Zero() {
        x = y = z = 0.0;
        return this;
    }

    public Vector3d set(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
        return this;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double normSq() {
        return (x * x + y * y + z * z);
    }

    public Vector3d divV3f(double d) {
        double dd = 1.0f / d;
        x *= dd;
        y *= dd;
        z *= dd;
        return this;
    }

    public Vector3d mulV3f(double d) {
        x *= d;
        y *= d;
        z *= d;
        return this;
    }
}

