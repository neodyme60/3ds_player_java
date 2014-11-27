package core.scenes.importer;

import core.scenes.scene;

public abstract class importerBaseClass extends Thread {
    protected scene scene;
    protected scene targetscene;

    public importerBaseClass(scene _targetscene) {
        targetscene = _targetscene;
    }

    public void load() {
        this.start();
    }

    public abstract int progressLoaded();
}
