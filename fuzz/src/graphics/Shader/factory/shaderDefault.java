package graphics.Shader.factory;

import graphics.Shader.shaderBase;
import graphics.rendering.rasterizer_c;
import graphics.rendering.renderList.renderListItem;
import graphics.videoBuffer_c;
import math.linear.Vector3d;

public class shaderDefault extends shaderBase {
    @Override
    public void renderFace(videoBuffer_c b, renderListItem rl) {
        //disable all
        rasterizer_c.setShadeFlat(false);
        rasterizer_c.setShadeBump(false);
        rasterizer_c.setShadeOpacity(false);
        rasterizer_c.setShadeEnvironnement(false);
        rasterizer_c.setShadeMapping(false);

        //enable some
        rasterizer_c.setShadeFlat(true);
        int cri = 0;
        int cgi = 0;
        int cbi = 0;
        int c = 0;
        Vector3d dir = new Vector3d(0.0, 0.0, 1.0);
        double dot = Math.abs(rl.getN().dotProductV3(dir));
        //build the colorRGBA
        double crf = (rl.getMat().getAmbiant().getRf() * dot);
        double cgf = (rl.getMat().getAmbiant().getGf() * dot);
        double cbf = (rl.getMat().getAmbiant().getBf() * dot);
        cri = (int) (crf * 255.0);
        if (0xff < cri) cri = 0xff;
        cgi = (int) (cgf * 255.0);
        if (0xff < cgi) cgi = 0xff;
        cbi = (int) (cbf * 255.0);
        if (0xff < cbi) cbi = 0xff;
        c = cbi | (cgi << 8) | (cri << 16) | 0xff000000;

        rasterizer_c.setAmbiantColor(c);

        if (rl.getMat().hasDiffuseChannel()) {
            rasterizer_c.setShadeMapping(true);
            rasterizer_c.setT2(rl.getMat().getDiffuseChannel().getTexture());
        }
        if (rl.getMat().hasBumpChannel()) {
            rasterizer_c.setShadeBump(true);
            rasterizer_c.setT3(rl.getMat().getBumpChannel().getTexture());
        }
        if (rl.getMat().hasOpacityChannel()) {
            rasterizer_c.setShadeOpacity(true);
            rasterizer_c.setT4(rl.getMat().getOpacityChannel().getTexture());
        }
        if (rl.getMat().hasEnvironmentChannel()) {
            rasterizer_c.setShadeEnvironnement(true);
            rasterizer_c.setT1(rl.getMat().getEnvironmentChannel().getTexture());
        }

        //rasterize !!
        rasterizer_c.v1 = rl.getA();
        rasterizer_c.v2 = rl.getB();
        rasterizer_c.v3 = rl.getC();
        rasterizer_c.b = b;
        rasterizer_c.rasterize();
    }
}
