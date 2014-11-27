package graphics.rendering.renderList;

import graphics.Shader.factory.shaderDefault;
import graphics.Shader.factory.shaderFlat;
import graphics.Shader.factory.shaderWire;
import graphics.Shader.shaderInterface;
import graphics.material.material.materialBase;
import graphics.shading.ShadingTypeEnum;
import graphics.videoBuffer_c;

import java.util.ArrayList;

public class renderList {
    private ArrayList<renderListItem> renderListList = new ArrayList<renderListItem>();
    private int currentIndex = 0;

    private shaderFlat sf = new shaderFlat();
    private shaderWire sw = new shaderWire();
    private shaderDefault sa = new shaderDefault();

    public renderList() {
        alloc(5000);
    }

    public ArrayList<materialBase> getMaterialList() {
        ArrayList<materialBase> mm = new ArrayList<materialBase>();

        //scan all
        for (int i = 0; i < currentIndex; i++) {
            renderListItem r = renderListList.get(i);
            materialBase mb = r.getMat();
//            if (mb.getClass().equals(materialMirror.class))
            if (!mm.contains(mb))
                mm.add(mb);
        }
        return mm;
    }


    public void render(videoBuffer_c vb) {
        //render all in one pass
        for (int i = 0; i < currentIndex; i++) {
            renderListItem r = renderListList.get(i);
            shaderInterface si = null;

            //choose shader
            switch (r.shd) {
                case ShadingTypeEnum.flat:
                    si = sf;
                    break;
                case ShadingTypeEnum.gouraud:
                    break;
                case ShadingTypeEnum.lambert:
                    break;
                case ShadingTypeEnum.phong:
                    break;
                case ShadingTypeEnum.texture_mapping_inear:
                    break;
                case ShadingTypeEnum.wire:
                    si = sw;
                    break;
                case ShadingTypeEnum.autodetect:
                    si = sa;
                    break;
            }

            //shader rendering
            if (si != null)
                si.renderFace(vb, r);
        }
    }

    /*
        public ArrayList<renderListItem> getRenderListList()
        {
            return renderListList;
        }
    */
    public renderListItem getCurrent() {
        return renderListList.get(currentIndex);
    }

    public void setRenderListList(ArrayList<renderListItem> renderListList) {
        this.renderListList = renderListList;
    }

    public void reset() {
        currentIndex = 0;
    }

    public void add() {
        currentIndex++;
        if (currentIndex >= renderListList.size())
            alloc(1000);
    }

    public void alloc(int s) {
        for (int i = 0; i < s; i++)
            renderListList.add(new renderListItem());
    }
}
