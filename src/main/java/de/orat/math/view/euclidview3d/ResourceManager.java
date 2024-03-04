package de.orat.math.view.euclidview3d;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Diese Implementierung ist ein Workaround. Besser sollte es wie folgt gehen:
 * 
 * If the resources are packed in another data file (e.g. a .jar file) and you 
 * don't want to extract them to disk, you must use aiImportFileEx. The last 
 * argument is an AIFileIO structure that can be used to define a virtual 
 * filesystem. The callbacks you setup there will be used by Assimp while 
 * parsing a scene. I've successfully used it in the past and you can nicely 
 * implement extraction/decompression to memory buffers, avoiding expensive IO.
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class ResourceManager {

    // Stores paths to files with the global jarFilePath as the key
    private static Map<String, String> fileCache = new HashMap<>();

    /**
     * Extract the specified resource from inside the jar to the local file system.
     * 
     * @param jarFilePath absolute path to the resource
     * @return full file system path if file successfully extracted, else null on error
     */
    public static String extract(String jarFilePath){

        if(jarFilePath == null)
            return null;

        // See if we already have the file
        if(fileCache.containsKey(jarFilePath))
            return fileCache.get(jarFilePath);

        // Alright, we don't have the file, let's extract it
        try {
            // Read the file we're looking for
            InputStream fileStream = ResourceManager.class.getResourceAsStream(jarFilePath);

            // Was the resource found?
            if(fileStream == null){
                System.out.println("jarFilePath \""+jarFilePath+"\" not found!");
                return null;
            }
            
            // Grab the file name
            String[] chopped = jarFilePath.split("\\/");
            String fileName = chopped[chopped.length-1];

            // Create our temp file (first param is just random bits)
            File tempFile = File.createTempFile("asdf", fileName);

            // Set this file to be deleted on VM exit
            tempFile.deleteOnExit();

            // Create an output stream to barf to the temp file
            OutputStream out = new FileOutputStream(tempFile);

            // Write the file to the temp file
            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            // Store this file in the cache list
            fileCache.put(jarFilePath, tempFile.getAbsolutePath());

            // Close the streams
            fileStream.close();
            out.close();

            // Return the path of this sweet new file
            return tempFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}