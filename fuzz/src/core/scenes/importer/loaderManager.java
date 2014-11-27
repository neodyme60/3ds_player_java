package core.scenes.importer;

import core.scenes.scene;

import java.util.ArrayList;
import java.util.Iterator;

public class loaderManager {
    private ArrayList<importerBaseClass> loaderList = new ArrayList<importerBaseClass>();

    public double getGlobalPercent() {
        double p = 0;
        Iterator<importerBaseClass> itr = loaderList.iterator();
        while (itr.hasNext()) {
            importerBaseClass el = itr.next();
            p += el.progressLoaded();
        }
        if (loaderList.size() > 0)
            p = p / loaderList.size();
        else
            p = 0;

        return p;
    }

    public boolean isAllLoading() {
        boolean f = true;
        Iterator<importerBaseClass> itr = loaderList.iterator();
        while (itr.hasNext() && f == true) {
            importerBaseClass el = itr.next();
            if (!el.isAlive()) {
                f = false;
                continue;
            }
        }
        return f;
    }

    public boolean addLoader(scene s, importerBaseClass imp, String codeBase) {
        if (imp != null) {
            loaderList.add(imp);
            imp.load();
        }
        return true;
    }
}
