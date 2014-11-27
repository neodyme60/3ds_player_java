package core.scenes.importer.format.format_3ds;

public interface MaxChunkIDs {
    // These must all be diffrent values
    static final int NULL_CHUNK = 0x0000;
    static final int UNKNOWN1 = 0x0001;
    static final int TDS_VERSION = 0x0002;
    static final int COLOR_double = 0x0010;
    static final int COLOR_BYTE = 0x0011;
    static final int CLR_BYTE_GAMA = 0x0012;
    static final int CLR_double_GAMA = 0x0013;
    static final int PRCT_INT_FRMT = 0x0030;
    static final int PRCT_FLT_FRMT = 0x0031;
    static final int MASTER_SCALE = 0x0100;

    static final int BACKGRD_BITMAP = 0x1100;
    static final int BACKGRD_COLOR = 0x1200;
    static final int USE_BCK_COLOR = 0x1201;
    static final int V_GRADIENT = 0x1300;
    static final int SHADOW_BIAS = 0x1400;
    static final int SHADOW_MAP_SIZE = 0x1420;
    static final int SHADOW_MAP_RANGE = 0x1450;
    static final int RAYTRACE_BIAS = 0x1460;
    static final int O_CONSTS = 0x1500;

    static final int GEN_AMB_COLOR = 0x2100;
    static final int FOG_FLAG = 0x2200;
    static final int FOG_BACKGROUND = 0x2210;
    static final int DISTANCE_QUEUE = 0x2300;
    static final int LAYERED_FOG_OPT = 0x2302;
    static final int DQUEUE_BACKGRND = 0x2310;

    static final int DEFAULT_VIEW = 0x3000;
    static final int VIEW_CAMERA = 0x3080;
    static final int EDIT_3DS = 0x3d3d;
    static final int MESH_VERSION = 0x3d3e;

    static final int NAMED_OBJECT = 0x4000;
    static final int OBJ_TRIMESH = 0x4100;
    static final int VERTEX_LIST = 0x4110;
    static final int VERTEX_OPTIONS = 0x4111;
    static final int FACES_ARRAY = 0x4120;
    static final int MESH_MAT_GROUP = 0x4130;
    static final int TEXT_COORDS = 0x4140;
    static final int SMOOTH_GROUP = 0x4150;
    static final int COORD_SYS = 0x4160;
    static final int MESH_COLOR = 0x4165;
    static final int MESH_TEXTURE_INFO = 0x4170;
    static final int LIGHT_OBJ = 0x4600;
    static final int LIGHT_SPOTLIGHT = 0x4610;
    static final int LIGHT_ATTENU_ON = 0x4625;
    static final int LIGHT_SPOT_SHADOWED = 0x4630;
    static final int LIGHT_LOC_SHADOW = 0x4641;
    static final int LIGHT_SEE_CONE = 0x4650;
    static final int LIGHT_SPOT_OVERSHOOT = 0x4652;
    static final int LIGHT_SPOT_ROLL = 0x4656;
    static final int LIGHT_SPOT_BIAS = 0x4658;
    static final int LIGHT_IN_RANGE = 0x4659;
    static final int LIGHT_OUT_RANGE = 0x465a;
    static final int LIGHT_MULTIPLIER = 0x465b;
    static final int CAMERA_FLAG = 0x4700;
    static final int CAMERA_RANGES = 0x4720;
    static final int MAIN_3DS = 0x4D4D;

    static final int KEY_VIEWPORT = 0x7001;
    static final int VIEWPORT_DATA = 0x7011;
    static final int VIEWPORT_DATA3 = 0x7012;
    static final int VIEWPORT_SIZE = 0x7020;

    static final int XDATA_SECTION = 0x8000;

    /*
     0xAFFF : Material block
    -----------------------
      0xA000 : Material name

      0xA010 : Ambient color
      0xA020 : Diffuse color
      0xA030 : Specular color

      0xA040 : Shininess percent
      0xA041 : Shininess strength percent

      0xA050 : Transparency percent
      0xA052 : Transparency falloff percent
      0xA053 : Reflection blur percent

      0xA081 : 2 sided
      0xA083 : Add trans
      0xA084 : Self illum
      0xA085 : Wire frame on
      0xA087 : Wire thickness
      0xA088 : Face map
      0xA08A : In tranc
      0xA08C : Soften
      0xA08E : Wire in units

      0xA100 : Render type

      0xA240 : Transparency falloff percent present
      0xA250 : Reflection blur percent present
      0xA252 : Bump map present (true percent)

      0xA200 : Texture map 1
      0xA33A : Texture map 2
      0xA210 : Opacity map
      0xA230 : Bump map
      0xA33C : Shininess map
      0xA204 : Specular map
      0xA33D : Self illum. map
      0xA220 : Reflection map
      0xA33E : Mask for texture map 1
      0xA340 : Mask for texture map 2
      0xA342 : Mask for opacity map
      0xA344 : Mask for bump map
      0xA346 : Mask for shininess map
      0xA348 : Mask for specular map
      0xA34A : Mask for self illum. map
      0xA34C : Mask for reflection map

      Sub-chunks for all maps:
        0xA300 : Mapping filename
        0xA351 : Mapping parameters
        0xA353 : Blur percent
        0xA354 : V scale
        0xA356 : U scale
        0xA358 : U offset
        0xA35A : V offset
        0xA35C : Rotation angle
        0xA360 : RGB Luma/Alpha tint 1
        0xA362 : RGB Luma/Alpha tint 2
        0xA364 : RGB tint R
        0xA366 : RGB tint G
        0xA368 : RGB tint B
    * */

    //Material block
    static final int MAT_NAME = 0xa000;
    static final int MAT_AMB_COLOR = 0xa010;
    static final int MAT_DIF_COLOR = 0xa020;
    static final int MAT_SPEC_CLR = 0xa030;
    static final int MAT_SHINE = 0xa040;
    static final int MAT_SHINE_STR = 0xa041;
    static final int MAT_ALPHA = 0xa050;
    static final int MAT_ALPHA_FAL = 0xa052;
    static final int MAT_REF_BLUR = 0xa053;
    static final int MAT_EMISSIVE = 0xa080;
    static final int MAT_TWO_SIDED = 0xa081;
    static final int MAT_SELF_ILUM = 0xa084;
    static final int MAT_WIREFRAME_ON = 0xa085;
    static final int MAT_WIRE_SIZE = 0xa087;
    static final int IN_TRANC_FLAG = 0xa08a;
    static final int MAT_SOFTEN = 0xa08c;
    static final int MAT_WIRE_ABS = 0xa08e;
    static final int MAT_SHADING = 0xa100;
/*
            sh = ReadShort();
            switch (sh)
            {
            case 0:
                mat.SetShadingType(sWireframe);
                break;
            case 1:
                mat.SetShadingType(sFlat);
                break;
            case 2:
                mat.SetShadingType(sGouraud);
                break;
            case 3:
                mat.SetShadingType(sPhong);
                break;
            case 4:
                mat.SetShadingType(sMetal);
                break;
            }
*
 */

    static final int MAT_TEXMAP = 0xa200;
    static final int MAT_OPACMAP = 0xa210;
    static final int MAT_REFLECT_MAP = 0xa220;
    static final int MAT_FALLOFF = 0xa240;
    static final int MAT_TEX_BUMP_PER = 0xa252;
    static final int MAT_TEX_BUMPMAP = 0xa230;
    static final int MAT_REFL_BLUR = 0xa250;
    static final int MAT_TEXNAME = 0xa300;
    static final int MAT_SXP_TEXT_DATA = 0xa320;
    static final int MAT_SXP_BUMP_DATA = 0xa324;
    static final int MAT_TEX2MAP = 0xa33a;
    static final int MAT_TEX_FLAGS = 0xa351;
    static final int MAT_TEX_BLUR = 0xa353;
    static final int TEXTURE_V_SCALE = 0xa354;
    static final int TEXTURE_U_SCALE = 0xa356;
    static final int MAT_BLOCK = 0xafff;

    static final int KEYFRAMES = 0xb000;
    static final int KEY_AMB_LI_INFO = 0xb001;
    static final int KEY_OBJECT = 0xb002;
    static final int KEY_CAMERA_OBJECT = 0xb003;
    static final int KEY_CAM_TARGET = 0xb004;
    static final int KEY_OMNI_LI_INFO = 0xb005;
    static final int KEY_SPOT_TARGET = 0xb006;
    static final int KEY_SPOT_OBJECT = 0xb007;
    static final int KEY_SEGMENT = 0xb008;
    static final int KEY_CURTIME = 0xb009;
    static final int KEY_HEADER = 0xb00a;
    static final int TRACK_HEADER = 0xb010;
    static final int TRACK_PIVOT = 0xb013;
    static final int BOUNDING_BOX = 0xb014;
    static final int MORPH_SMOOTH = 0xb015;
    static final int TRACK_POS_TAG = 0xb020;
    static final int TRACK_ROT_TAG = 0xb021;
    static final int TRACK_SCL_TAG = 0xb022;
    static final int KEY_FOV_TRACK = 0xb023;
    static final int KEY_ROLL_TRACK = 0xb024;
    static final int KEY_COLOR_TRACK = 0xb025;
    static final int KEY_HOTSPOT_TRACK = 0xb027;
    static final int KEY_FALLOFF_TRACK = 0xb028;
    static final int NODE_ID = 0xb030;
}
