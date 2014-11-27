package core.primitive.Mesh;

import core.primitive.BoundingVolume.bbox_c;
import core.primitive.Camera.factory.camera_c;
import graphics.material.material.materialBase;
import math.geometry.Face;
import math.geometry.Vertex;
import math.linear.Matrix4;
import math.linear.Vector3d;

/*

property pWorld

global pMesh

property pMeshMR
property pAnimFlag
property pInc

global  pwaveX
global nbFace
global nbVertex
global arrayVertex
global arrayNormal

global arrayCos
global arraySin

global useToon
global UseInker
global UseToonSilhouette
--rayon
property d
--hauteur
property d10
--largeur
property d11
--slice,?
property l
--?
property d12


global toRad
global  toIndexRad
global radToDeg
global radToIndex
------------------------------
------------------------------

on beginSprite me
  pWorld = member("3d world")

  pAnimFlag = 1 -- 0 turns off animation
  pInc = 1

  me.createMesh()
end beginSprite

---------------
---------------

on createMesh me

  d=40
  --hauteur
  d10=40
  --largeur
  d11=40
  --slice,?
  l=20
  --?
  --  d12=1

  useToon=false
  UseInker=false
  UseToonSilhouette=false

  ii=1
  arrayCos=[]
  arraySin=[]
  repeat while ii<256
    arrayCos[ii]=cos(ii.double/256.0)
    arraySin[ii]=sin(ii.double/256.0)
    ii=ii+1
  end repeat


  toRad=3.1415926535897931/ 180.0
  toIndexRad=256.0/360.0
  radToDeg=180.0/3.1416
  radToIndex=1+(255.0/3.1416)
  arrayVertex=[]
  pwaveX=0
  --

  nbFace=(((180.0 / (180.0 / l)) * ((360.0 / (360.0 / l)) * 2.0))).integer

  nbVertex=l * (l + 1)

  pWorld.resetWorld()

  --** create the backdrop of sky
  pWorld.newtexture("backdrop", #fromCastmember, member("sky"))
  pWorld.camera(1).insertbackdrop(1, pWorld.texture("backdrop"),point(0,0),0)


  --************************************
  --*
  --*create model
  --*
  --************************************
  pMeshMR = pWorld.newMesh("Mball01",nbFace,nbVertex,0,4,nbVertex)



  --************texture coord
  j1 = l * (l + 1)
  i1 = 1
  d16=0.0
  repeat while  (d16 <= 180.0)
    d18= 0.0
    repeat while   (d18 < 360.0)

      d13=d18/360
      d14=d16/180


      pMeshMR.textureCoordinateList[i1]=[d13,d14]
      i1=i1+1
      d18 = d18+(360.0 / l)
    end repeat
    d16 = d16+(180.0 / l)
  end repeat

  --*******vertex
  j1 = l * (l + 1)
  i1 = 1
  d16=0.0
  repeat while  (d16 <= 180.0)
    d18= 0.0
    repeat while   (d18 < 360.0)
      d13 = d * sin(d16 * toRad) * sin(d18 * toRad)
      d14 = d10 * cos(d16 * toRad)
      d15 = d11 * sin(d16 * toRad) *cos(d18 * toRad)
      pMeshMR.vertexList[i1]=vector(d13,d14,d15)
      i1=i1+1
      d18 = d18+(360.0 / l)
    end repeat
    d16 = d16+(180.0 / l)
  end repeat
  --*******face
  l1 = 1
  i1 = 1
  d17 = 0.0
  repeat while (d17 < 180.0) --; d17 += 180D / (double)l)
    d19 = 0.0
    repeat while (d19 < 360.0) --; d19 += 360D / (double)l)
      v1 = l1
      v2 = l1 + 1
      i2=l1 + l + 1
      if (i2>j1) then
        v3=j1 - 1
      else
        v3=i2 - 1
      end if

      --face 1
      pMeshMR.face[i1].vertices  =[v1,v2,v3]
      pMeshMR.face[i1].colors = [4,1,1]
      pMeshMR.face[i1].textureCoordinates = [v1,v2,v3]


      --face 2
      i1=i1+1
      pMeshMR.face[i1].vertices=[l1,l1 + l,((l1 + l) - 1) mod (j1)]
      pMeshMR.face[i1].colors = [1,4,1]
      pMeshMR.face[i1].textureCoordinates = [l1,l1 + l,((l1 + l) - 1) mod (j1)]

      i1=i1+1
      l1=l1+1

      d19 = d19+(360.0 / l)
    end repeat
    d17 = d17+(180.0 /l)
  end repeat



  pMeshMR.colorList = [rgb(255,255,0), rgb(0, 255, 0), rgb(0,0,255), rgb(255,0,0)]

  --**build normal
  pMeshMR.generateNormals(#smooth)
  --  pMeshMR.generateNormals(#flat)
  pMeshMR.build()

  --************************************
  --*
  --*instance model
  --*
  --************************************
  pMesh = pWorld.newModel("ball", pMeshMR)

  --************************************
  --*
  --*setup create model property
  --*
  --************************************

  --create texture
  member("3D World").newTexture("phong",#fromCastMember,member("chrome"))
  pMesh.shader.emissive = rgb(250,250,250)
  pMesh.shader.texture=member("3D World").texture("phong") --chrome

  --pWorld.newshader("sphereShader", #standard)
  --pWorld.shader("sphereShader").texture  = pWorld.texture("phong")
  --pMesh.shader=pWorld.shader("sphereShader")

  pMesh.visibility = #back

  --add a mesh modifier
  pMesh.addmodifier(#meshdeform)

  --add a toon modifier
  --  pMesh.addmodifier(#toon)
  --  pMesh.toon.silhouettes=true


  --copy arrayVertex
  tt=pMesh.meshDeform.mesh[1].vertexList.count
  repeat  while tt>0
    arrayVertex[tt]=pMesh.meshDeform.mesh[1].vertexList[tt]
    tt=tt-1
  end repeat


  pwaveX=0.0

end createMesh

---------------

on exitframe me

  if (UseToon=true) then
    pMesh.toon.colorsteps=sprite(45).getValue()

    pMesh.toon.silhouettes=UseToonSilhouette
    pMesh.toon.useLineOffset=true
    pMesh.toon.lineoffset=-2
  end if

if (UseInker=true) then
    pMesh.inker.creases=0.1
  end if


  vertexSRC=pMesh.meshDeform.mesh[1].vertexList

  repeat with i=1 to vertexSRC.count
    vv=arrayVertex[i]

    d13 = (vv.x + vv.x + vv.y + vv.x + vv.x) / sprite(28).getValue()--33.0
    d14 = cos(d13 + pwaveX / sprite(30).getValue())

    d13 = (vv.y + vv.x + vv.y + vv.y + vv.y) / sprite(32).getValue()
    d14 = d14+sin(d13 + pwaveX / sprite(34).getValue())

    d13 = (vv.y + vv.y + vv.y + vv.y + vv.y) / sprite(36).getValue()
    d14 = d14+cos(d13 + pwaveX / sprite(38).getValue())

    if(d14 < 0.0) then
      d14 = -d14
    end if
    d12 = d14/1.5
    d12=d12+sprite(40).getValue()

    --    d12=1.0
    pMesh.meshDeform.mesh[1].vertexList[i]=vector(vv.x*d12,vv.y*d12,vv.z*d12)
  end repeat

  set pwaveX=pwaveX+sprite(42).getValue()
  --if (pwaveX>3.14) then
  --pwaveX=0
  --end if

  --  pMesh2.meshDeform.mesh[1].vertexList=vertexDST
  --  set planeModel.meshdeform.mesh[1].normallist=mynormallist


  --  planeModel=member("3d world").Model("ball")
  --  set myVertexlist=[]
  --  set myVertexlist=arrayVertex
  --  set myNormallist=planeModel.meshdeform.mesh[1].normallist
  --  repeat with i=1 to arrayVertex.count
  --    myvertex=getat(myVertexlist, i)
  --    set myvertex.z=myvertex.z+sin(i+pwaveX.double)*2
  --    setat myVertexlist, i, myvertex
  --    setat myNormallist, i, getnormalized(myvertex)
  --  end repeat
  --  set pwaveX=pwaveX+0.10
  --  set planeModel.meshdeform.mesh[1].vertexlist=myvertexlist
  --  set planeModel.meshdeform.mesh[1].normallist=mynormallist

end exitframe


--on exitFrame me
--   if pAnimFlag then
--     me.animateTrail()
--     pMesh.rotate(vector(0,0.25,0),#self)
--   end if
--end exitFrame



* */


public class mesh_c extends meshBase {
    //*****************************
    static Matrix4 m1 = new Matrix4();
    static Matrix4 m2 = new Matrix4();
    public Face[] arrayFace;
    public Vertex[] arrayVertex;
    public Matrix4 globalToLocalMat4;
    public Matrix4 localToGlobalMat4;
    public int nbFace;
    public int nbVertex;
    public int[] arrayFaceToRender; //index des faces a afficher
    public int nbFaceToRender;
    public int vertexTransfome; //id of vertex tranforme  0=No transforme
    public int renderingType;    //0=flat 1=wire 2=vertex
    //bounding volume
    public bbox_c boundingVolume;
    public boolean isVisible;            //bbox frustum rejection test
    public Vector3d tempv;
    public Vector3d tempvv;
    private materialBase mat;    //id du graphics.material

    //*****************************************
    public mesh_c() {
        globalToLocalMat4 = new Matrix4();
        localToGlobalMat4 = new Matrix4();

        //AAbbox
        boundingVolume = new bbox_c(); //in local objet space
        isVisible = true;

        //
        tempv = new Vector3d();
        tempvv = new Vector3d();
    }

    //*****************************************
    public final int addVertex(Vertex v) {
        arrayVertex[nbVertex++] = v;
        return nbVertex - 1;
    }

    //*****************************
    public final int addFace(Face f) {
        arrayFace[nbFace++] = f;
        return nbFace - 1;
    }

    //*****************************
    public final void mesh_c(int _nbVertex, int _nbFace) {
        nbFace = _nbFace;
        nbVertex = _nbVertex;
        arrayFace = new Face[nbFace];
        arrayFaceToRender = new int[nbFace];
        arrayVertex = new Vertex[nbFace];
    }

    //*****************************************
    public final void setFaceArray(int _nbFace) {
        nbFace = _nbFace;
        arrayFaceToRender = new int[nbFace];
        arrayFace = new Face[nbFace];
        while (0 <= --_nbFace)
            arrayFace[_nbFace] = new Face();
    }

    //*****************************************
/*
    public final void setFace(int _faceId, int i, int j, int k)
    {
        arrayFace[_faceId].set(i, j, k);
    }
*/
    //*****************************************
    public final void setVertexArray(int _nbVertex) {
        nbVertex = _nbVertex;
        arrayVertex = new Vertex[nbVertex];
        while (0 <= --_nbVertex)
            arrayVertex[_nbVertex] = new Vertex();
    }

    //*****************************************
    public final void buildFaceNormal() {
        int j = nbFace;
        Vector3d v1;
        Vector3d v2;
        Vector3d v3;

        while (0 <= --j) {
            v1 = arrayVertex[arrayFace[j].getAt(2)].getvPos0();
            v2 = arrayVertex[arrayFace[j].getAt(0)].getvPos0();
            v3 = arrayVertex[arrayFace[j].getAt(1)].getvPos0();

            Vector3d v = ((v1.subV3(v2)).scalairProductV3(v3.subV3(v2))).normalizeV3();

            arrayFace[j].setN(v);
            /*
            tempv.set(v1);     tempv.sub(v2);
            tempvv.set(v3);    tempvv.sub(v2);
            math.linear.Vector3d.scalairProduct(tempv,tempvv,arrayFace[j].n);
            */
//            arrayFace[j].getN().normalizeV3();
        }
    }

    //*****************************
    public final void buildVertexNormal() {
        for (int j = 0; j < nbVertex; j++) {
            tempv.setV3Zero();
            for (int i = 0; i < nbFace; i++)
                if (arrayFace[i].containIndex(j))
                    tempv.addV3(arrayFace[i].getN());
            tempv.mulV3f((128.0f / tempv.norm()) * 65536.0f);
            arrayVertex[j].setvN(tempv);
        }
    }

    //*****************************
    public final void toLocal() {
        for (int i = 0; i < nbVertex; i++)
            globalToLocalMat4.mulV3f3Mat4(arrayVertex[i].getvPos0(), 1.0f, arrayVertex[i].getvPos0());

        for (int i = 0; i < nbVertex; i++) {
            tempv.setV3(arrayVertex[i].getvPos0());
            arrayVertex[i].getvPos0().x = tempv.x;
            arrayVertex[i].getvPos0().z = tempv.y;
            arrayVertex[i].getvPos0().y = tempv.z;
        }

    }

    public final boolean toCameraSpace(camera_c camera) {
        int i;
        m1.setMat4(getKeyFramer().objWordlMatrix);
        Matrix4.mulM4(camera.matrixToEyeSpace, mesh_c.m1, mesh_c.m1);

        m2.setMat4(mesh_c.m1);
        Matrix4.mulM4(camera.matrixProjection, mesh_c.m2, mesh_c.m2);

        //on toCameraSpace la AABOX
        boundingVolume.transform(mesh_c.m2);

        //test bbox/frustum visibiliti in homogenoeous space
        isVisible = camera.cullBBox(boundingVolume);

        if (false == isVisible)
            return false;

        //rot all vertex normal
        for (i = 0; i < nbVertex; i++)
            //			arrayVertex[i].vN2.set(m1 .mulV(arrayVertex[i].vN));
            m1.mulV33Mat3(arrayVertex[i].getvN(), arrayVertex[i].getvN2());

        //rot all face normal
        for (i = 0; i < nbFace; i++)
            //			arrayFace[i].n2.set(m1.mulV(arrayFace[i].n));
            m1.mulV33Mat3(arrayFace[i].getN(), arrayFace[i].getN2());


        for (i = 0; i < nbVertex; i++) {
            //buid a 4D vector to map in homogenous space
            m2.mulV3f4Mat4(arrayVertex[i].getvPos0(), 1.0f, arrayVertex[i].getvPos2());
            arrayVertex[i].getvPos2().y *= 160.0f / 100.0f;
        }
        return true;
    }
    //*****************************

    public materialBase getMaterial() {
        return mat;
    }

    public void setMaterial(materialBase mat) {
        this.mat = mat;
    }
}