package graphics;

import core.manager.scenesManager;
import core.scenes.scene;
import graphics.tni.filter_c;

public class videoBuffer_c {
    public int[] swapData = null;
    public int xSize;
    public int ySize;
    public double xSizef;
    public double ySizef;
    public double xAlfeSizef;
    public double yAlfeSizef;
    public int swapEffectId;
    public double[] zBuffer = null;
    public int[] idBuffer = null;
    public int[] sBuffer;
    //some flag for rendering
    public boolean useIdBuffer = false;
    public int postFilterId = 0;        //no filter
    public int swapEffetId;
    public Object s = new Object();
    private int[] bufferPrevious = null;
    private int[] bufferCurrent = null;

    public videoBuffer_c() {
        useIdBuffer = false;
        xSize = ySize = 0;
        swapEffetId = 0;
        postFilterId = 0;
        swapEffectId = 0;
    }

    public int[] getPixelBufferPrevious() {
        return bufferCurrent;
    }

    public int[] getPixelBufferCurrent() {
        return bufferCurrent;
    }

    public void swapPixelBuffer() {
        int[] t = bufferPrevious;
        bufferPrevious = bufferCurrent;
        bufferCurrent = t;
    }

    /*
    int x: x size
    int y: y size
    swap effect: 0=nothing 1= zoom table
    bool : id bufferPrevious for picking
    int: if using post processing filter, result image is in bufferCurrent
             postFilter=filter id, 0 means no filter
    */

    public void setSize(int x, int y) {
        xSize = x;
        ySize = y;

        xSizef = (double) x;
        ySizef = (double) y;

        xAlfeSizef = xSizef / 2.0;
        yAlfeSizef = ySizef / 2.0;
    }

    public void setPostFilter(int b) {
        postFilterId = b;
    }

    public void setSwapFx(int swapId) {
        swapEffectId = swapId;
    }

    public void setIdBuffer(boolean b) {
        useIdBuffer = b;
    }

    public void init()//int x,int y,int swapId,boolean bufferIdB, int postFilter)
    {
        bufferPrevious = new int[xSize * ySize];
        bufferCurrent = new int[xSize * ySize];

//        if (0 != postFilterId)
//            bufferCurrent = new int[xSize * ySize];

        //alloc zbuffer
        zBuffer = new double[xSize * ySize];

        //clear zbuffer
        clearZBuffer();

        //alloc & clear idbuffer
        if (useIdBuffer) {
            idBuffer = new int[xSize * ySize];
            clearIdBuffer();
        }

        //
        //setup swapping page eefect
        //
        switch (swapEffectId) {
            case 0:
                //simple swapping effect so nothing to do
                break;

            case 1:
                //double bufferPrevious
                //buil swap table, in this case it is a zoom
                swapData = new int[xSize * ySize];
                //      bufferCurrent=new int[x*y];
                int index = 0;
                for (int yy = 0; yy < ySize; yy++)
                    for (int xx = 0; xx < xSize; xx++) {
                        int xs;
                        int ys;
                        xs = (int) (((double) (xx - xSize / 2)) * 0.7) + xSize / 2;
                        ys = (int) (((double) (yy - ySize / 2)) * 0.7) + ySize / 2;
                        swapData[index++] = xs + (ys * xSize);
                    }
                break;
        }
    }

    //********************************************************
    //do post prod filter if there is one
    public final void postProd() {
        if (0 != postFilterId) {
            switch (postFilterId) {
                case 1:
                    filter_c.filter01(this);
                    break;
                case 2:
                    filter_c.filter02(this);
                    break;
                case 3:
                    filter_c.filter03(this);
                    break;
                case 4:
                    filter_c.filter04(this);
                    break;
            }
        }
    }

    //********************************************************
    public final void swap(int scene_id) {
        //clear zbuffer
        clearZBuffer();

        //clear idbuffer
        if (useIdBuffer)
            clearIdBuffer();


        scene s = scenesManager.getInstance().getSceneAt(scene_id);
        clear(s.getAmbiant().geti());

//        swapPixelBuffer();

//        clear(0);

        //do swapping page effect
        switch (swapEffectId) {
            case 0:
            case 1:
                //               clear(0xff000000);
                break;
//        case 1:
//                  sBuffer=bufferPrevious;
//                  bufferPrevious=bufferCurrent;
//                  bufferCurrent=sBuffer;
//                  for(int i=0;i<xSize*ySize;i++)
//                    bufferPrevious[i]=bufferCurrent[swapData[i]];
//            break;
        }
    }

    //********************************************************
    public final void clearZBuffer() {
        //java.util.util.Arrays.fill();

        double value = Double.MAX_VALUE;
        int size = (xSize * ySize) - 1;
        int cleared = 1;
        int index = 1;
        zBuffer[0] = value;
        while (cleared < size) {
            System.arraycopy(zBuffer, 0, zBuffer, index, cleared);
            size -= cleared;
            index += cleared;
            cleared <<= 1;
        }
        System.arraycopy(zBuffer, 0, zBuffer, index, size);

    }

    //********************************************************
    public final void clearIdBuffer() {
//		for(int i=0;i<xSize*ySize;i++)
//			idBuffer[i]=(int)-1; //fill with no id

        int value = -1;
        int size = (xSize * ySize) - 1;
        int cleared = 1;
        int index = 1;
        idBuffer[0] = value;
        while (cleared < size) {
            System.arraycopy(idBuffer, 0, idBuffer, index, cleared);
            size -= cleared;
            index += cleared;
            cleared <<= 1;
        }
        System.arraycopy(idBuffer, 0, idBuffer, index, size);
    }

    //********************************************************
    public final void clear(int value) {
        int size = (xSize * ySize) - 1;
        int cleared = 1;
        int index = 1;
        bufferCurrent[0] = value;

        while (cleared < size) {
            System.arraycopy(bufferCurrent, 0, bufferCurrent, index, cleared);
            size -= cleared;
            index += cleared;
            cleared <<= 1;
        }
        System.arraycopy(bufferCurrent, 0, bufferCurrent, index, size);

    }
/*
    public static void bytefill(byte[] array, byte value)
	{
		int len = array.length;
		if (len > 0)
			array[0] = value;
		for (int i = 1; i < len; i += i)
			System.arraycopy( array, 0, array, i,((len - i) < i) ? (len - i) : i);
	}
*/

    public int[] getBufferPrevious() {
        return bufferPrevious;
    }
}

