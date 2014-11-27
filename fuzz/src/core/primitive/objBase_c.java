package core.primitive;

import keyFramer.KeyFramer;

/*
core.primitive.objBase_c	is the abstract (full virtual) base class of all Mesh,camera,light in the 3dmotor
*/

public abstract class objBase_c {
    private String name = "no name";
    private KeyFramer keyFramer = null;

    public void setKeyframer(KeyFramer _keyFramer) {
        keyFramer = _keyFramer;
    }

    public KeyFramer getKeyFramer() {
        return keyFramer;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }
}