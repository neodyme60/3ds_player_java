package graphics.material.material.factory;

import core.manager.texturesManager;
import graphics.material.channel.channelDiffuse;
import graphics.material.material.materialBase;
import graphics.material.texture.factory.textureEmpty;
import graphics.material.texture.textureBase;

public class materialMirror extends materialBase {
    public materialMirror(int width, int height, String materialName) {
        //set material name
        this.setName(materialName);

        //create texture
        textureBase tb = new textureEmpty(width, height);

        //add texture to texture manager
        texturesManager.getInstance().add(tb);

        //add texture to material diffuse channel
        channelDiffuse cd = new channelDiffuse();
        cd.setTexture(tb);

        //set diffuse material diffuse channel
        setDiffuseChannel(cd);
    }
}
