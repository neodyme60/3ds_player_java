package graphics.rendering.renderList;

import graphics.material.material.materialBase;
import graphics.videoBuffer_c;
import math.geometry.Vertex;
import math.linear.Vector3d;

import java.util.ArrayList;

public class renderListManager {
    private static renderListManager singletonObject;
    private static int maxRenderListByPass = 3;
    private static int maxRenderListPass = 2;
    private static ArrayList<renderList> myRenderList = new ArrayList<renderList>();
    private int currentIndex1 = 0;
    private int currentIndex2 = 0;
    private int maxPoly = -1;

    public static synchronized renderListManager getInstance() {
        if (singletonObject == null) {
            singletonObject = new renderListManager();

            //add render list
            for (int i = 0; i < maxRenderListByPass * maxRenderListPass; i++)
                myRenderList.add(new renderList());
        }
        return singletonObject;
    }

    public void init(int _maxPoly) {
        maxPoly = _maxPoly;
    }

    public void reset() {
        for (int i = 0; i < myRenderList.size(); i++)
            myRenderList.get(i).reset();
    }

    /*
        public renderList getRenderList(int idRenderList, int idPass)
        {
            return myRenderList.get(idRenderList+(idPass*maxRenderListByPass));
        }
    */
    public void resetAt(int idRenderList, int idPass) {
        myRenderList.get(idRenderList + (idPass * maxRenderListByPass)).reset();
    }

    public void add(Vertex a, Vertex b, Vertex c, Vector3d n, materialBase m, int ste, int idRenderList, int idPass) {
        renderList rl = myRenderList.get(idRenderList + (idPass * maxRenderListByPass));
        renderListItem i = rl.getCurrent();

        if (i == null)
            return;

        i.getA().set(a);
        i.getB().set(b);
        i.getC().set(c);
        i.getN().setV3(n);
        i.setMat(m);
        i.setShd(ste);

        rl.add();
    }

    public ArrayList<materialBase> getMaterialList(int idRenderList, int idPass) {
        renderList rl = myRenderList.get(idRenderList + (idPass * maxRenderListByPass));

        return rl.getMaterialList();
    }

    public void render(videoBuffer_c vb, int idRenderList, int idPass) {
        renderList rl = myRenderList.get(idRenderList + (idPass * maxRenderListByPass));

        if (rl == null)
            return;

        rl.render(vb);
    }
}
