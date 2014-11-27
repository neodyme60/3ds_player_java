package graphics.Shader.factory;

import graphics.Shader.shaderBase;
import graphics.drawing.line;
import graphics.rendering.renderList.renderListItem;
import graphics.videoBuffer_c;
import math.linear.Vector3d;

public class shaderWire extends shaderBase {
    @Override
    public void renderFace(videoBuffer_c videoBuffer, renderListItem rl) {
        int cri = 0;
        int cgi = 0;
        int cbi = 0;
        int c = 0;
        Vector3d dir = new Vector3d(0.0f, 0.0f, 1.0f);

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

        Vector3d p1 = rl.getA().getvPos3();
        Vector3d p2 = rl.getB().getvPos3();
        Vector3d p3 = rl.getC().getvPos3();

        line.drawZ(videoBuffer, p1.x / 65536.0, p1.y / 65536.0, p1.z / 65536.0, p2.x / 65536.0, p2.y / 65536.0, p2.z / 65536.0, c);
        line.drawZ(videoBuffer, p2.x / 65536.0, p2.y / 65536.0, p2.z / 65536.0, p3.x / 65536.0, p3.y / 65536.0, p3.z / 65536.0, c);
        line.drawZ(videoBuffer, p3.x / 65536.0, p3.y / 65536.0, p3.z / 65536.0, p1.x / 65536.0, p1.y / 65536.0, p1.z / 65536.0, c);
    }
}
