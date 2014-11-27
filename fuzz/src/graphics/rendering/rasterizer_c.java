package graphics.rendering;

import graphics.Color.colorRGBA;
import graphics.material.texture.textureInterface;
import graphics.videoBuffer_c;
import math.geometry.Vertex;

public class rasterizer_c {
    public final static int FRACBITS = 16;
    public static Vertex v1 = null;
    public static Vertex v2 = null;
    public static Vertex v3 = null;
    public static videoBuffer_c b = null;
    private static String version = "Rasterizer engine v1.0";
    private static Vertex v4;
    private static int temp_y;
    private static double temp_f;
    //triangle interpolation
    private static int grad1 = 0;
    private static int grad2 = 0;
    private static int grad3 = 0;
    private static int grad4 = 0;
    private static int start;
    private static int xStart = 0;
    private static int xEnd = 0;
    private static int xStart2 = 0;
    private static int xEnd2 = 0;
    private static int d1;
    private static int d2;
    private static int h1;
    private static int h2;
    private static int h3;
    private static int i;
    private static int j;
    private static int t;
    private static Vertex[] v = new Vertex[4];
    private static int right_x;
    //static double right_x,left_x;
    private static int left_x;
    private static int temp_x;
    private static int y;
    private static int x;
    private static int xs;
    //static double xs,xe;
    //static double height;
    private static int xe;
    //texture1
    private static int right_tx;
    private static int right_ty;
    private static int const_tx;
    private static int const_ty;
    private static int dx_left;
    private static int dy_left;
    //static double dx_left,dy_left;
    private static int dx_right;
    private static int dy_right;
    //static double dx_right,dy_right;
    private static int left_tx;
    private static int left_ty;
    private static int tx_left;
    private static int ty_left;
    private static int txs;
    private static int tys;
    private static int tx;
    private static int ty;
    //texture2
    private static int right_tx2;
    private static int right_ty2;
    private static int const_tx2;
    private static int const_ty2;
    private static int dx_left2;
    private static int dy_left2;
    private static int dx_right2;
    private static int dy_right2;
    private static int left_tx2;
    private static int left_ty2;
    private static int tx_left2;
    private static int ty_left2;
    private static int txs2;
    private static int tys2;
    private static int tx2;
    private static int ty2;
    //texture4 //opacity
    private static int right_tx4;
    private static int right_ty4;
    private static int const_tx4;
    private static int const_ty4;
    private static int dx_left4;
    private static int dy_left4;
    private static int dx_right4;
    private static int dy_right4;
    private static int left_tx4;
    private static int left_ty4;
    private static int tx_left4;
    private static int ty_left4;
    private static int txs4;
    private static int tys4;
    private static int tx4;
    private static int ty4;
    //zbuffer
    private static double left_z;
    private static double right_z;
    private static double left_z2;
    private static double right_z2;
    private static double const_z;
    private static double z_left;
    private static double dxz_right2;
    private static double dyz_right2;
    private static double zx_left2;
    private static double zy_left2;
    private static double zxs2;
    private static double zys2;
    private static double zss;
    private static double zs;
    private static double z;
    private static double y1i;
    private static double y2i;
    private static double y3i;
    //shading	option
    private static boolean shadeBump = false;
    private static boolean shadeFlat = true;
    private static boolean shadeEnvironnement = false;
    private static boolean shadeMapping = true;
    private static boolean shadeOpacity = false;
    private static boolean shadeIdBuffer = false;
    private static boolean shadeAmbiant = false;
    //shading param
    private static int ambiantColor = 0x50607000;    //colorRGBA for flat shading
    //static	texture_c	texturePhong;			//for phong texture mapping
    //static	texture_c	textureMapping;		//for texture mapping
    private static int objId;                                        //objet	id in	objet	list for id buffer
    private static int matId;                                        //graphics.material Id	in graphics.material	list for id buffer

    //temp variable for rendering
    private static int phongColor;
    private static int mappingColor;
    private static int opacityColor;
    private static int scanLengt;
    private static int offsetStart;
    private static int doit;
    private static int scanStart;
    private static int scanEnd;
    //	static double scanStart,scanEnd;

    private static textureInterface t1;    //env
    private static textureInterface t2;    //map
    private static textureInterface t3;    //bump
    private static textureInterface t4;    //opacity

    private static void setObjId(int i) {
        rasterizer_c.objId = i;
    }

    private static void setMatId(int i) {
        rasterizer_c.matId = i;
    }
/*
    public static void rasterize(videoBuffer_c b, Vertex v1, Vertex v2, Vertex v3)
    {
        int height;
        int coef;
        int tt;
        double ztt;

        //
        //Y	sort with bubble sort
        //
        if (v1.getvPos3().y > v2.getvPos3().y) //swap 0 & 1
        {
            v4 = v1;            v1 = v2;            v2 = v4;
        }
        if ( v2.getvPos3().y  >  v3.getvPos3().y) //swap 1 & 2
        {
            v4 = v2;            v2 = v3;            v3 = v4;
        }
        if ( v1.getvPos3().y >  v2.getvPos3().y) //swap 0 & 1
        {
            v4 = v1;            v1 = v2;            v2 = v4;
        }

        int xx1=(int) (v1.getvPos3().x*(1<<FRACBITS));
        int yy1=(int) (v1.getvPos3().y*(1<<FRACBITS));
        int xx2=(int) (v2.getvPos3().x*(1<<FRACBITS));
        int yy2=(int) (v2.getvPos3().y*(1<<FRACBITS));
        int xx3=(int) (v3.getvPos3().x*(1<<FRACBITS));
        int yy3=(int) (v3.getvPos3().y*(1<<FRACBITS));

        //check height
        height = Math.abs(yy3 - yy1)>>FRACBITS;
        if (height==0)
            return;

        left_x = xx2;

        //magic	coef
        coef = (yy2-yy1) / height;

        //poly incrment
        tt = (xx3-xx1)>>FRACBITS;
        right_x =  xx1 + (tt*coef);

        //zbuffer	au point 4 & 2
        left_z = v2.getvPos3().z;
        ztt = v3.getvPos3().z - v1.getvPos3().z;
        right_z = v1.getvPos3().z + (coef * ztt/(1<<FRACBITS));

        if (right_x < left_x)
        {
            //for	scanline
            temp_x = right_x;
            right_x = left_x;
            left_x = temp_x;

            //for	zbuffer
            temp_f = right_z;
            right_z = left_z;
            left_z = temp_f;
        }

        //zbuffer
        const_z = (right_z - left_z) /(right_x - left_x);
        
        start = b.xSize * (yy1 >> FRACBITS);

        height = (yy2-yy1)>>FRACBITS;
        if (height > 0)
        {
            //start x for scanline
            xs =xx1;
            xe =xx1;

            //zbuffer
            z_left = (left_z - v1.getvPos3().z) / (double)height;
            zs = v1.getvPos3().z;
            
            //****************
            //les increments sur les Y
            //pour la 1ere partie du triangle
            //
            //****************

            dx_left = (left_x - xx1) / height;
            dx_right = (right_x - xx1) / height;

            for (y =0;y<height; y++)
            {
                scanLengt = (xe-xs)>> FRACBITS;
                
                //zbuffer
                z = zs;
                
                offsetStart = start + (xs >> FRACBITS);
                
                while (scanLengt >= 0)
                {
                    if (b.zBuffer[offsetStart] >z)    //need to update pixel ?
                    {
                        //update zbuffer
                        b.zBuffer[offsetStart] = z;

                        int myColor =0xffffff;

//                        myColor = getAmbiantColor();

                        b.getPixelBufferPrevious()[offsetStart] = myColor;
                    }
                    //scanline increment
                    offsetStart++;

                    //zbuffer increment
                    z += const_z;

                    scanLengt--;
                }

                //
                //next line increment
                //

                //screen
                xs += dx_left;
                xe += dx_right;
                //new y screen start
                start += b.xSize;

                //zbuffer
                zs += z_left;
            }
        }

        height =(yy3 - yy2)>>FRACBITS;

        if (height > 0)
        {
            dx_right = (xx3 - right_x) / height;
            dx_left = (xx3 - left_x) / height;

            //start for x scanline
            xs =  left_x;
            xe =  right_x;

             //zbuffer
            zs = left_z;
            z_left = (v3.getvPos3().z - left_z) / (double)height;

            for (y =0; y<height; y++)
            {
                scanLengt = (xe - xs)>> FRACBITS;
                
                //zbuffer	start	for	scanline
                z = zs;
                
                offsetStart = start + (xs>> FRACBITS);
                while (scanLengt >= 0)
                {
                    if (b.zBuffer[offsetStart] > z)
                    {
                        //update zbuffer
                        b.zBuffer[offsetStart] = z;

                        int myColor=0xffffff;

//                        myColor = getAmbiantColor();

                        b.getPixelBufferPrevious()[offsetStart] = myColor;
                    }
                    //scanline increment
                    offsetStart++;

                    //zbuffer increment
                    z += const_z;

                    scanLengt--;
                }

                //
                //next line increment
                //

                //screen
                xs += dx_left;
                xe += dx_right;

                start += b.xSize;

                //zbuffer
                zs += z_left;
            }
        }
    }
    */


    public static void rasterize() {
        //
        //Y	sort with bubble sort
        //
        if (v1.getvPos3().y > v2.getvPos3().y) //swap 0 & 1
        {
            v4 = v1;
            v1 = v2;
            v2 = v4;
        }
        if (v2.getvPos3().y > v3.getvPos3().y) //swap 1 & 2
        {
            v4 = v2;
            v2 = v3;
            v3 = v4;
        }
        if (v1.getvPos3().y > v2.getvPos3().y) //swap 0 & 1
        {
            v4 = v1;
            v1 = v2;
            v2 = v4;
        }

        double coef;
        int height;
/*
        v1.getvPos3().x/=65536.0;
        v1.getvPos3().y/=65536.0;
//        v1.getvPos3().z/=65536.0;

        v2.getvPos3().x/=65536.0;
        v2.getvPos3().y/=65536.0;
//        v2.getvPos3().z/=65536.0;

        v3.getvPos3().x/=65536.0;
        v3.getvPos3().y/=65536.0;
//        v3.getvPos3().z/=65536.0;
*/
        left_x = (int) (v2.getvPos3().x);
        long h = ((int) v3.getvPos3().y >> FRACBITS) - ((int) v1.getvPos3().y >> FRACBITS);
        if (h == 0) return;

        //magic	coef
        coef = ((double) (((int) v2.getvPos3().y >> FRACBITS) - ((int) v1.getvPos3().y >> FRACBITS))) / (double) h;

        //poly incrment
        double tt = (double) ((int) v3.getvPos3().x) - ((int) v1.getvPos3().x);
        right_x = (int) v1.getvPos3().x + (int) (coef * tt);

        //texture1 coord au point 4 & 2
        if (isShadeEnvironnement() == true) {
            left_tx = (int) v2.getvN2().x;
            tt = (double) ((int) v3.getvN2().x) - ((int) v1.getvN2().x);
            right_tx = (int) v1.getvN2().x + (int) (coef * tt);
            left_ty = (int) v2.getvN2().y;
            tt = (double) ((int) v3.getvN2().y) - ((int) v1.getvN2().y);
            right_ty = (int) v1.getvN2().y + (int) (coef * tt);
        }

        //texture2 coord au point 4 & 2
        if (isShadeMapping() == true) {

            v1.getvM2().x = v1.getvM().x * t2.getWidth() * 65536;
            v2.getvM2().x = v2.getvM().x * t2.getWidth() * 65536;
            v3.getvM2().x = v3.getvM().x * t2.getWidth() * 65536;
            v1.getvM2().y = v1.getvM().y * t2.getHeight() * 65536;
            v2.getvM2().y = v2.getvM().y * t2.getHeight() * 65536;
            v3.getvM2().y = v3.getvM().y * t2.getHeight() * 65536;

            left_tx2 = (int) v2.getvM2().x;
            tt = (double) ((int) v3.getvM2().x) - ((int) v1.getvM2().x);
            right_tx2 = (int) v1.getvM2().x + (int) (coef * tt);
            left_ty2 = (int) v2.getvM2().y;
            tt = (double) ((int) v3.getvM2().y) - ((int) v1.getvM2().y);
            right_ty2 = (int) v1.getvM2().y + (int) (coef * tt);
        }

        if (isShadeMapping() == true) {
            left_tx4 = (int) v2.getvM().x;
            tt = (double) ((int) v3.getvM().x) - ((int) v1.getvM().x);
            right_tx4 = (int) v1.getvM().x + (int) (coef * tt);
            left_ty4 = (int) v2.getvM().y;
            tt = (double) ((int) v3.getvM().y) - ((int) v1.getvM().y);
            right_ty4 = (int) v1.getvM().y + (int) (coef * tt);
        }


        //zbuffer	au point 4 & 2
        left_z = v2.getvPos3().z;
        tt = v3.getvPos3().z - v1.getvPos3().z;
        right_z = v1.getvPos3().z + (coef * tt);

        if (right_x < left_x) {
            //for	scanline
            temp_x = right_x;
            right_x = left_x;
            left_x = temp_x;

            //for	phong	mapping
            if (isShadeEnvironnement()) {
                temp_x = right_tx;
                right_tx = left_tx;
                left_tx = temp_x;
                temp_x = right_ty;
                right_ty = left_ty;
                left_ty = temp_x;
            }

            //for	texture	mapping
            if (isShadeMapping()) {
                temp_x = right_tx2;
                right_tx2 = left_tx2;
                left_tx2 = temp_x;
                temp_x = right_ty2;
                right_ty2 = left_ty2;
                left_ty2 = temp_x;
            }

            //for	texture	opacity
            if (isShadeOpacity()) {
                temp_x = right_tx4;
                right_tx4 = left_tx4;
                left_tx4 = temp_x;
                temp_x = right_ty4;
                right_ty4 = left_ty4;
                left_ty4 = temp_x;
            }

            //for	zbuffer
            temp_f = right_z;
            right_z = left_z;
            left_z = temp_f;
        }

        //
        //les increment constant pour tout le triangle sur les X
        //
        //

        //environnement
        const_tx = (int) ((1 << FRACBITS) * (double) (right_tx - left_tx) / (double) ((right_x - left_x)));
        const_ty = (int) ((1 << FRACBITS) * (double) (right_ty - left_ty) / (double) ((right_x - left_x)));

        //mapping
        const_tx2 = (int) ((1 << FRACBITS) * (double) (right_tx2 - left_tx2) / (double) ((right_x - left_x)));
        const_ty2 = (int) ((1 << FRACBITS) * (double) (right_ty2 - left_ty2) / (double) ((right_x - left_x)));

        const_tx4 = (int) ((1 << FRACBITS) * (double) (right_tx4 - left_tx4) / (double) ((right_x - left_x)));
        const_ty4 = (int) ((1 << FRACBITS) * (double) (right_ty4 - left_ty4) / (double) ((right_x - left_x)));

        //zbuffer
        const_z = ((double) (right_z - left_z) / (double) (right_x - left_x)) * 65536.0;

        start = b.xSize * ((int) (v1.getvPos3().y + 0.5) >> FRACBITS);

        height = ((int) v2.getvPos3().y >> FRACBITS) - ((int) v1.getvPos3().y >> FRACBITS);
        if (height > 0) {
            //start x for scanline
            xs = (int) v1.getvPos3().x;
            xe = (int) v1.getvPos3().x;

            //****************
            //les increments sur les Y
            //pour la 1ere partie du triangle
            //
            //****************

            dx_left = (left_x - (int) v1.getvPos3().x) / height;
            dx_right = (right_x - (int) v1.getvPos3().x) / height;

            //zbuffer
            z_left = (left_z - v1.getvPos3().z) / (double) height;
            zs = v1.getvPos3().z;

            //environnement mapping
            if (isShadeEnvironnement()) {
                tx_left = (left_tx - (int) v1.getvN2().x) / height;
                ty_left = (left_ty - (int) v1.getvN2().y) / height;
                txs = (int) v1.getvN2().x;
                tys = (int) v1.getvN2().y;
            }

            //texture mapping
            if (isShadeMapping()) {
                tx_left2 = (left_tx2 - (int) v1.getvM2().x) / height;
                ty_left2 = (left_ty2 - (int) v1.getvM2().y) / height;
                txs2 = (int) v1.getvM2().x;
                tys2 = (int) v1.getvM2().y;
            }

            if (isShadeOpacity()) {
                tx_left4 = (left_tx4 - (int) v1.getvM().x) / height;
                ty_left4 = (left_ty4 - (int) v1.getvM().y) / height;
                txs4 = (int) v1.getvM().x;
                tys4 = (int) v1.getvM().y;
            }


            for (y = (int) v1.getvPos3().y >> FRACBITS; y < (int) v2.getvPos3().y >> FRACBITS; y++) {
                //				doit=1;

                scanStart = xs >> FRACBITS;
                scanEnd = xe >> FRACBITS;


                if (y >= b.ySize)
                    break;

                //zbuffer	start	for	scanline
                z = zs;

                //texture1 start for scanline
                if (isShadeEnvironnement()) {
                    tx = txs;
                    ty = tys;
                }

                //texture2 start for scanline
                if (isShadeMapping()) {
                    tx2 = txs2;
                    ty2 = tys2;
                }

                //texture2 start for scanline
                if (isShadeOpacity()) {
                    tx4 = txs4;
                    ty4 = tys4;
                }

                scanLengt = scanEnd - scanStart;
                offsetStart = start + scanStart;

                while ((scanLengt--) > 0) {
                    if (z < b.zBuffer[offsetStart])    //need to update pixel ?
                    {
                        //update zbuffer
                        b.zBuffer[offsetStart] = z;

                        //update id buffer
                        if (isShadeIdBuffer())
                            b.idBuffer[offsetStart] = (objId << FRACBITS) + matId;

                        //get Environnement myColor
                        if (isShadeEnvironnement()) {
                            int ttx = (((tx >> FRACBITS) + 127) & 255);
                            int tty = (((ty >> FRACBITS) + 127) & 255);
                            phongColor = t1.getDataBuffer()[ttx + (tty * t1.getWidth())];
                        }

                        //get texture	mapping myColor
                        if (isShadeMapping()) {
                            mappingColor = t2.getDatai(tx2, ty2, 0);
                        }
                        if (isShadeOpacity()) {
//                            opacityColor = t4.getDataBuffer()[(((tx4 >> FRACBITS)) ) + (((ty4 >> FRACBITS)) ) * t4.getWidth()];
//                            opacityColor=opacityColor & 0xff;
                        }


                        int myColor = 0;

                        if (isShadeFlat())
                            myColor = getAmbiantColor();

                        if (isShadeEnvironnement())
                            myColor = colorRGBA.add(phongColor, myColor);

                        if (isShadeMapping())
//                            myColor =  colorRGBA.add( mappingColor,myColor);
                            myColor = mappingColor;


                        if (isShadeOpacity()) {
//                            myColor = colorRGBA.transparency(b.getPixelBufferPrevious()[offsetStart], myColor,255-opacityColor);
                        }

                        b.getPixelBufferCurrent()[offsetStart] = myColor;
                    }

                    //scanline increment
                    offsetStart++;

                    //phong increment
                    if (isShadeEnvironnement()) {
                        tx += const_tx;
                        ty += const_ty;
                    }

                    //mapping increment
                    if (isShadeMapping()) {
                        tx2 += const_tx2;
                        ty2 += const_ty2;
                    }

                    //mapping increment
                    if (isShadeOpacity()) {
                        tx4 += const_tx4;
                        ty4 += const_ty4;
                    }

                    //zbuffer increment
                    z += const_z;
                }
                //****************
                //update Y index
                //
                //
                //****************

                //scanline increment
                xs += dx_left;
                xe += dx_right;

                //phong increment
                if (isShadeEnvironnement()) {
                    txs += tx_left;
                    tys += ty_left;
                }

                //mapping increment
                if (isShadeMapping()) {
                    txs2 += tx_left2;
                    tys2 += ty_left2;
                }

                if (isShadeOpacity()) {
                    txs4 += tx_left4;
                    tys4 += ty_left4;
                }

                zs += z_left;

                //new y screen start
                start += b.xSize;
            }
        }

        height = ((int) v3.getvPos3().y >> FRACBITS) - ((int) v2.getvPos3().y >> FRACBITS);
        if (height > 0) {
            dx_right = ((int) v3.getvPos3().x - right_x) / height;
            dx_left = ((int) v3.getvPos3().x - left_x) / height;

            //start for x scanline
            xs = (int) left_x;
            xe = (int) right_x;

            //****************
            //les increments sur les Y
            //
            //
            //****************

            //phong mapping
            if (isShadeEnvironnement()) {
                tx_left = ((int) v3.getvN2().x - left_tx) / height;
                ty_left = ((int) v3.getvN2().y - left_ty) / height;
                txs = left_tx;
                tys = left_ty;
            }

            //texture mapping
            if (isShadeMapping()) {
                tx_left2 = ((int) v3.getvM2().x - left_tx2) / height;
                ty_left2 = ((int) v3.getvM2().y - left_ty2) / height;
                txs2 = left_tx2;
                tys2 = left_ty2;
            }
            if (isShadeOpacity()) {
                tx_left4 = ((int) v3.getvM().x - left_tx4) / height;
                ty_left4 = ((int) v3.getvM().y - left_ty4) / height;
                txs4 = left_tx4;
                tys4 = left_ty4;
            }

            //zbuffer
            zs = left_z;
            z_left = (v3.getvPos3().z - left_z) / (double) height;

            for (y = (int) v2.getvPos3().y >> FRACBITS; y < (int) v3.getvPos3().y >> FRACBITS; y++) {
                doit = 1;

                scanStart = xs >> FRACBITS;
                scanEnd = xe >> FRACBITS;

                //				if (y<0) doit=0;

                if (y >= b.ySize)
                    break;

                //zbuffer	start	for	scanline
                z = zs;

                //phong texture start for scanline
                if (isShadeEnvironnement()) {
                    tx = txs;
                    ty = tys;
                }

                //texture mapping start for scanline
                if (isShadeMapping()) {
                    tx2 = txs2;
                    ty2 = tys2;
                }
                if (isShadeOpacity()) {
                    tx4 = txs4;
                    ty4 = tys4;
                }

                //clip x test	rejection
                //				if (doit==1)
                {
                    scanLengt = scanEnd - scanStart;
                    offsetStart = start + scanStart;

                    while ((scanLengt--) > 0) {
                        if (b.zBuffer[offsetStart] > z) {
                            //update zbuffer
                            b.zBuffer[offsetStart] = z;

                            //update id buffer
                            if (b.useIdBuffer)
                                b.idBuffer[offsetStart] = (objId << FRACBITS) + matId;

                            //get phong colorRGBA
                            if (isShadeEnvironnement())
                                phongColor = t1.getDataBuffer()[(((tx >> FRACBITS) + 127) & 255) + (((ty >> FRACBITS) + 127) & 255) * t1.getWidth()];

                            //get texture	mapping colorRGBA
                            if (isShadeMapping())
                                mappingColor = t2.getDatai(tx2, ty2, 0);

                            if (isShadeOpacity()) {
//                                opacityColor = t4.getDataBuffer()[(((tx4 >> FRACBITS)) & 255) + (((ty4 >> FRACBITS)) & 255) * t4.getWidth()];
//                                opacityColor=opacityColor & 0xff;
                            }

                            int myColor = 0;

                            if (isShadeFlat())
                                myColor = getAmbiantColor();

                            if (isShadeEnvironnement())
                                myColor = colorRGBA.add(phongColor, myColor);

                            if (isShadeMapping())
//                                myColor =  colorRGBA.add( mappingColor,myColor);
                                myColor = mappingColor;

                            if (isShadeOpacity()) {
//                                myColor= colorRGBA.transparency(b.getPixelBufferPrevious()[offsetStart],myColor,255-opacityColor);
                            }

                            b.getPixelBufferCurrent()[offsetStart] = myColor;
                        }

                        //scanline increment
                        offsetStart++;

                        //phong increment
                        if (isShadeEnvironnement()) {
                            tx += const_tx;
                            ty += const_ty;
                        }

                        //mapping increment
                        if (isShadeMapping()) {
                            tx2 += const_tx2;
                            ty2 += const_ty2;
                        }

                        if (isShadeOpacity()) {
                            tx4 += const_tx4;
                            ty4 += const_ty4;
                        }

                        //zbuffer increment
                        z += const_z;
                    }
                }

                //****************
                //update Y index
                //
                //
                //****************

                //scanline increment
                xs += dx_left;
                xe += dx_right;

                //phong increment
                if (isShadeEnvironnement()) {
                    txs += tx_left;
                    tys += ty_left;
                }
                //mapping incrment
                if (isShadeMapping()) {
                    txs2 += tx_left2;
                    tys2 += ty_left2;
                }
                if (isShadeOpacity()) {
                    txs4 += tx_left4;
                    tys4 += ty_left4;
                }

                zs += z_left;

                start += b.xSize;
            }
        }
    }

    public static boolean isShadeBump() {
        return shadeBump;
    }

    public static void setShadeBump(boolean shadeBump) {
        rasterizer_c.shadeBump = shadeBump;
    }

    public static boolean isShadeFlat() {
        return shadeFlat;
    }

    public static void setShadeFlat(boolean shadeFlat) {
        rasterizer_c.shadeFlat = shadeFlat;
    }

    public static boolean isShadeEnvironnement() {
        return shadeEnvironnement;
    }

    public static void setShadeEnvironnement(boolean shadePhong) {
        rasterizer_c.shadeEnvironnement = shadePhong;
    }

    public static boolean isShadeMapping() {
        return shadeMapping;
    }

    public static void setShadeMapping(boolean shadeMapping) {
        rasterizer_c.shadeMapping = shadeMapping;
    }

    public static boolean isShadeOpacity() {
        return shadeOpacity;
    }

    public static void setShadeOpacity(boolean shadeOpacity) {
        rasterizer_c.shadeOpacity = shadeOpacity;
    }

    public static boolean isShadeIdBuffer() {
        return shadeIdBuffer;
    }

    public static void setShadeIdBuffer(boolean shadeIdBuffer) {
        rasterizer_c.shadeIdBuffer = shadeIdBuffer;
    }

    public static textureInterface getT1() {
        return t1;
    }

    public static void setT1(textureInterface t1) {
        rasterizer_c.t1 = t1;
    }

    public static textureInterface getT2() {
        return t2;
    }

    public static void setT2(textureInterface t2) {
        rasterizer_c.t2 = t2;
    }

    public static textureInterface getT3() {
        return t3;
    }

    public static void setT3(textureInterface t3) {
        rasterizer_c.t3 = t3;
    }

    public static textureInterface getT4() {
        return t4;
    }

    public static void setT4(textureInterface t4) {
        rasterizer_c.t4 = t4;
    }

    public static int getAmbiantColor() {
        return ambiantColor;
    }

    public static void setAmbiantColor(int _ambiantColor) {
        ambiantColor = _ambiantColor;
    }
}