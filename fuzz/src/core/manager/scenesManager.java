package core.manager;

import core.scenes.exporter.saverManager;
import core.scenes.importer.importerBaseClass;
import core.scenes.importer.loaderManager;
import core.scenes.scene;

import java.util.ArrayList;

public class scenesManager {
    private static scenesManager singletonObject;
    private ArrayList<scene> sceneList = new ArrayList<scene>();

    private loaderManager lm = new loaderManager();
    private saverManager sm = new saverManager();

    /**
     * A private Constructor prevents any other class from instantiating.
     */
    private scenesManager() {
        //	 Optional Code
    }

    public static synchronized scenesManager getInstance() {
        if (singletonObject == null) {
            singletonObject = new scenesManager();
        }
        return singletonObject;
    }

    public scene getSceneAt(int i) {
        return sceneList.get(i);
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /*
        public void addScene(scene s)
        {
            sceneList.add(s);
        }
    */
    public void addScene(scene s, importerBaseClass lbc) {
        sceneList.add(s);
        lm.addLoader(s, lbc, s.getBaseUrl());
    }

    /*
        public scene addNewScene(String codeBase, String sceneName)
        {
            scene s=new scene(codeBase,sceneName);
            sceneList.add(s);
            return s;
        }
    /*
        public scene addSceneFromFile(String codeBase, String sceneName, importerBaseClass lbc)
        {
            scene s=new scene(codeBase,sceneName);
            sceneList.add(s);

            lm.addLoader(s,lbc,codeBase);
            return s;
        }
    */
    public boolean isLoading() {
        return lm.isAllLoading();
    }

    public ArrayList<scene> getSceneList() {
        return sceneList;
    }
}
