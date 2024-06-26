package de.orat.math.view.euclidview3d;

import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import org.jogamp.vecmath.Vector4f;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Composite;
import org.jzy3d.plot3d.primitives.EuclidPart;
import org.jzy3d.plot3d.primitives.EuclidVBO2;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_AMBIENT;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_DIFFUSE;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_SPECULAR;
import static org.lwjgl.assimp.Assimp.aiGetMaterialColor;
import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

/**
 * An object Loader using Assimp through lwjgl. 
 * 
 * Can load COLLADA and object files for now.
 * 
 * @author Dominik Scharnagl
 */
public class ObjectLoader {
    
    private static ObjectLoader loader;
    private List<String> lastNames = null;
    
    private ObjectLoader(){
        lastNames = new ArrayList<>();
    }
    
    public static ObjectLoader getLoader(){
        if(loader == null){
            loader = new ObjectLoader();
        }
        return loader;
    }
    
    /**
     * Add a COLLADA (.dae) File Object to the Scene.
     * 
     * @param path the path to the COLLADA File
     * @return 
    */
    public EuclidPart getCOLLADA(String path){
        List<EuclidVBO2> objects = getParts(path);
        for(EuclidVBO2 o: objects){
            o.setWireframeDisplayed(false);
        }
        //Combine Objects into one composite
        return new EuclidPart(objects);
    }
    
    /**
     * Add a Wavefront (.obj) File Object to the Scene.
     * 
     * @param path the path to the COLLADA File
     * @return 
    */
    public EuclidPart getWavefront(String path){
        List<EuclidVBO2> objects = getParts(path);
        for(EuclidVBO2 o: objects){
            o.setWireframeDisplayed(false);
        }
        //Combine Objects into one composite
        return new EuclidPart(objects);
    }
    
    public EuclidPart getWavefront(String path, List<String> names){
        lastNames = names;
        List<EuclidVBO2> objects = getParts(path);
        for(EuclidVBO2 o: objects){
            o.setWireframeDisplayed(false);
        }
        //Combine Objects into one composite
        return new EuclidPart(objects);
    }
    
    /**
     * @param path
     * @return 
     * @throws IllegalArgumentException if the path is not found
     */ 
    private List<EuclidVBO2> getParts(String path){
        //Load COLLADA files (creates a seperate thread)
        System.out.println("path="+path);
        // getResource() == null bei Ausführung in Euclid3dView
        URL url = this.getClass().getResource(path);
        if (url != null){
            String completePath = url.getPath();
            System.out.println("complete path="+completePath);
        } else {
            System.out.println("getResource()==null");
        }
        String filePath = ResourceManager.extract(path);
        System.out.println("filePath="+filePath);
        AIScene aiScene = aiImportFile(filePath, 0);
        
        if (aiScene == null){
            System.out.println("getParts() failed for path \""+path+"\"!");
            throw new IllegalArgumentException("Path \""+path+"\" not found!");
        }
        
        //process Materials
        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials);
        }
        //Get the Meshes from the File
        PointerBuffer aiMeshes = aiScene.mMeshes();
        AIMesh[] meshes = new AIMesh[aiScene.mNumMeshes()];
        List<EuclidVBO2> objects = new ArrayList<>();
        //Make objects from the vertices from the 
        for(int i = 0; i < aiScene.mNumMeshes();i++){
            meshes[i] = AIMesh.create(aiMeshes.get(i));
            if(checkIfNotSkeletonBox(meshes[i].mName().dataString())){
                lastNames.add(meshes[i].mName().dataString());
                List<Float> vertices = new ArrayList<>();
                processVertices(meshes[i], vertices);
                objects.add(getVBOObject(vertices, materials.get(meshes[i].mMaterialIndex()))); 
            }
        }
        return objects;
    }
    
    /**
     * Dont add Boxes to the mesh
     * @param string the input string
     * @return if the input string is Box01 or Box02 or nothing
     */
    private boolean checkIfNotSkeletonBox(String string){
        if(string.equals("Box01")){
            return false;
        }
        if(string.equals("Box02")){
            return false;
        }
        if(string.equals("LeftToe")){
           return false; 
        }
        if(string.equals("RightToe")){
           return false; 
        }
        return true;
    }
    /**
     * Get the object from the vertices
     * @param vertices the vertieces
     * @param material the Material of the object
     * @return the combined object
     */
    private EuclidVBO2 getVBOObject(List<Float> vertices, Material material){   
        //translate the Floats to an array
        float[] verticesFloat = new float[vertices.size()];
        for(int i = 0; i < vertices.size(); i++){
            verticesFloat[i]  = vertices.get(i);         
        }
        //set up and return the object
        EuclidVBO2 vbo = new EuclidVBO2(verticesFloat, 3);
        vbo.setMaterialAmbiantReflection(new Color(material.getAmbient().x, material.getAmbient().y, material.getAmbient().z));
        vbo.setMaterialDiffuseReflection(new Color(material.getDiffuse().x, material.getDiffuse().y, material.getDiffuse().z));
        vbo.setMaterialSpecularReflection(new Color(material.getSpecular().x, material.getSpecular().y, material.getSpecular().z));
        Color color = new Color((material.getAmbient().x+material.getDiffuse().x+material.getSpecular().x)*1/3,
                                (material.getAmbient().y+material.getDiffuse().y+material.getSpecular().y)*1/3,
                                (material.getAmbient().z+material.getDiffuse().z+material.getSpecular().z)*1/3, 1.0f);
        vbo.setColor(color);
        vbo.setPolygonWireframeDepthTrick(false);
        return vbo;
    }
    
    /**
     * Process the vertices from an aim#Mesh to get float value of the vertices
     * @param aiMesh the aiMesh with the vertices
     * @param vertices the list where the vertices will be stored
     */
    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    } 
    
    /**
     * Process the Materials in a assimp loaded object
     * @param aiMaterial the aiMaterial from the assimp object
     * @param materials the list where the materials will be added
     */
    private void processMaterial(AIMaterial aiMaterial, List<Material> materials){
        AIColor4D colour = AIColor4D.create();
        
        //set ambient value of the material
        Vector4f ambient = new Vector4f(0,0,0,0);
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }
        
        //set the diffuse value of the material
        Vector4f diffuse = new Vector4f(0,0,0,0);
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }
       
        //set the specular value of the material
        Vector4f specular = new Vector4f(0,0,0,0);
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }
       
        //combine the values
        Material material = new Material(ambient, diffuse, specular, 1.0f);
        materials.add(material);
    }
    
}
