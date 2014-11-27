package core.scenes.importer.format.format_3ds;

public class format_3ds_chunk_c {
    //main chunk
    public static final int CHUNK_PRIMARY = 0x4d4d; //Primary chunk

    //sub main  chunk
    public static final int CHUNK_VERSION = 0x0002;
    public static final int CHUNK_MESHVERSION = 0x3D3E;  // this is the start of the editor config
    public static final int CHUNK_EDIT3DS = 0x3D3D;  // this is the start of the editor config
    public static final int CHUNK_KEYF3DS = 0xB000;  // this is the start of the keyframer config

    //>------ sub defines of CHUNK_EDIT3DS
    public static final int CHUNK_EDIT_MATERIAL = 0xAFFF;
    public static final int CHUNK_EDIT_CONFIG1 = 0x0100;
    public static final int CHUNK_EDIT_CONFIG2 = 0x3E3D;
    public static final int CHUNK_EDIT_VIEW_P1 = 0x7012;
    public static final int CHUNK_EDIT_VIEW_P2 = 0x7011;
    public static final int CHUNK_EDIT_VIEW_P3 = 0x7020;
    public static final int CHUNK_EDIT_VIEW1 = 0x7001;
    public static final int CHUNK_EDIT_BACKGR = 0x1200;
    public static final int CHUNK_EDIT_AMBIENT = 0x2100;
    public static final int CHUNK_EDIT_OBJECT = 0x4000;

    public static final int CHUNK_EDIT_UNKNW01 = 0x1100;
    public static final int CHUNK_EDIT_UNKNW02 = 0x1201;
    public static final int CHUNK_EDIT_UNKNW03 = 0x1300;
    public static final int CHUNK_EDIT_UNKNW04 = 0x1400;
    public static final int CHUNK_EDIT_UNKNW05 = 0x1420;
    public static final int CHUNK_EDIT_UNKNW06 = 0x1450;
    public static final int CHUNK_EDIT_UNKNW07 = 0x1500;
    public static final int CHUNK_EDIT_UNKNW08 = 0x2200;
    public static final int CHUNK_EDIT_UNKNW09 = 0x2201;
    public static final int CHUNK_EDIT_UNKNW10 = 0x2210;
    public static final int CHUNK_EDIT_UNKNW11 = 0x2300;
    public static final int CHUNK_EDIT_UNKNW12 = 0x2302;
    public static final int CHUNK_EDIT_UNKNW13 = 0x3000;

    //>------ sub defines of EDIT_OBJECT
    public static final int CHUNK_OBJ_TRIMESH = 0x4100;
    public static final int CHUNK_OBJ_LIGHT = 0x4600;
    public static final int CHUNK_OBJ_CAMERA = 0x4700;

    public static final int CHUNK_OBJ_UNKNWN01 = 0x4010;
    public static final int CHUNK_OBJ_UNKNWN02 = 0x4012; //>>---- Could be shadow

    //>------ sub defines of OBJ_CAMERA
    public static final int CHUNK_CAM_UNKNWN01 = 0x4710;
    public static final int CHUNK_CAM_UNKNWN02 = 0x4720;

    //>------ sub defines of OBJ_LIGHT
    public static final int CHUNK_LIT_OFF = 0x4620;
    public static final int CHUNK_LIT_SPOT = 0x4610;
    public static final int CHUNK_LIT_UNKNWN01 = 0x465A;

    //>------ sub defines of OBJ_TRIMESH
    public static final int CHUNK_TRI_VERTEXL = 0x4110;
    public static final int CHUNK_TRI_FACEL2 = 0x4111;
    public static final int CHUNK_TRI_FACEL1 = 0x4120;
    public static final int CHUNK_TRI_SMOOTH = 0x4150;
    public static final int CHUNK_TRI_LOCAL = 0x4160;
    public static final int CHUNK_TRI_VISIBLE = 0x4165;


    public static final int CHUNK_OBJECT_ = 0x4000;
    public static final int CHUNK_MESH = 0x4100;
    public static final int CHUNK_VERTEX = 0x4110;
    public static final int CHUNK_POLYGON = 0x4120;


    //>>------ sub defs of CHUNK_KEYF3DS

    public static final int CHUNK_KEYF_UNKNWN01 = 0xB009;
    public static final int CHUNK_KEYF_UNKNWN02 = 0xB00A;
    public static final int CHUNK_KEYF_FRAMES = 0xB008;
    public static final int CHUNK_KEYF_OBJDES = 0xB002;

    //>>------  these define the different colorRGBA chunk types
    public static final int CHUNK_COL_RGB = 0x0010;
    public static final int CHUNK_COL_TRU = 0x0011;
    public static final int CHUNK_COL_UNK = 0x0013;

    //>>------ defines for viewport chunks

    public static final int CHUNK_TOP = 0x0001;
    public static final int CHUNK_BOTTOM = 0x0002;
    public static final int CHUNK_LEFT = 0x0003;
    public static final int CHUNK_RIGHT = 0x0004;
    public static final int CHUNK_FRONT = 0x0005;
    public static final int CHUNK_BACK = 0x0006;
    public static final int CHUNK_USER = 0x0007;
    public static final int CHUNK_CAMERA = 0x0008; // 0xFFFF is the actual code read from file
    public static final int CHUNK_LIGHT = 0x0009;
    public static final int CHUNK_DISABLED = 0x0010;
    public static final int CHUNK_BOGUS = 0x0011;

}
