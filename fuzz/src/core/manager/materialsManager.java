package core.manager;

import core.scenes.exporter.saverManager;
import core.scenes.importer.loaderManager;
import graphics.material.material.factory.materialDefault;
import graphics.material.material.materialBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class materialsManager {
    private static materialsManager singletonObject;
    private ArrayList<materialBase> materialList = new ArrayList<materialBase>();

    private loaderManager lm = new loaderManager();
    private saverManager sm = new saverManager();

    /**
     * A private Constructor prevents any other class from instantiating.
     */
    private materialsManager() {
        //	 Optional Code
    }

    public static synchronized materialsManager getInstance() {
        if (singletonObject == null) {
            singletonObject = new materialsManager();
        }
        return singletonObject;
    }

    public void setMaterialAt(int i, materialBase m) {
        materialList.set(i, m);
    }

    public materialBase getMaterialAt(int i) {
        return materialList.get(i);
    }

    public materialDefault getMaterialByName(String i) {
        materialDefault m = null;
        Enumeration e = Collections.enumeration(materialList);
        while (e.hasMoreElements()) {
            m = (materialDefault) e.nextElement();
            if (i.equals(m.getName()))
                break;
            else
                m = null;
        }
        return m;
    }

    public boolean isLoading() {
        return lm.isAllLoading();
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void addMaterial(materialDefault m) {
        materialList.add(m);
    }

}
