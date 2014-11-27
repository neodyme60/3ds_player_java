package core.manager;

import graphics.material.texture.textureInterface;
import graphics.material.texture.texture_c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class texturesManager {
    private static texturesManager singletonObject;
    public ArrayList<textureInterface> textureList = new ArrayList<textureInterface>();

    private texturesManager() {
        //	 Optional Code
    }

    public static synchronized texturesManager getInstance() {
        if (singletonObject == null) {
            singletonObject = new texturesManager();
        }
        return singletonObject;
    }

    public textureInterface getTextureAt(int index) {
        if (index > textureList.size())
            return null;
        return textureList.get(index);
    }

    public textureInterface getTextureByName(String textureName, int type) {
        texture_c tx = null;

        Enumeration e = Collections.enumeration(textureList);
        while (e.hasMoreElements()) {
            tx = (texture_c) e.nextElement();
            if (tx.getName() == textureName && tx.type1 == type)
                break;
            else
                tx = null;
        }
        return tx;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public int add(textureInterface t) {
        textureList.add(t);
        return textureList.indexOf(t);
    }

    public boolean isLoaded() {
        textureInterface tx = null;
        boolean ok = true;

        Enumeration e = Collections.enumeration(textureList);
        while (e.hasMoreElements()) {
            tx = (textureInterface) e.nextElement();
            if (!tx.isLoaded()) {
                ok = false;
                break;
            }
        }
        return ok;
    }
}
