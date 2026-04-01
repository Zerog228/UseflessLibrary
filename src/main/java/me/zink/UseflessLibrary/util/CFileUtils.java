package me.zink.UseflessLibrary.util;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

public class CFileUtils {

    /**
     * Copies resources from selected folder in the project '/resources' to selected folder in '/plugins/plugin_resources'
     * @param maxDepth Max depth for iterator to search files
     * @param forceCopy Copy file if it's already exists
     * */
    public static void copyResources(Plugin plugin, String from, String to, int maxDepth, boolean forceCopy){
        try{
            //Create needed directories
            new File(plugin.getDataFolder().getAbsoluteFile() + File.separator + to).mkdirs();

            URI uri = CFileUtils.class.getResource("/" + from + "/").toURI();
            Path myPath;
            if (uri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                myPath = fileSystem.getPath("/" + from + "/");
            } else {
                myPath = Paths.get(uri);
            }
            Stream<Path> walk = Files.walk(myPath, maxDepth);

            for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
                Path pathToFile = it.next();

                //Skipping directories
                if(pathToFile.getFileName().toString().contains(".")){
                    File destination = new File(plugin.getDataFolder().getAbsoluteFile() + File.separator + to + File.separator + pathToFile.getFileName());
                    if (!destination.exists() || forceCopy) {
                        copyInputStreamToFile(plugin.getResource(pathToFile.toString().replaceFirst("/", "")), destination);
                    }
                }
            }
            walk.close();
        }catch (Exception e){
            System.err.println("Exception on file copying!");
            e.printStackTrace();
        }
    }

    public static void copyResources(File fromDirectory, File toDirectory, boolean deepSearch, boolean forceCopy){
        try{
            toDirectory.mkdirs();

            for(File file : fromDirectory.listFiles()){
                if(file.isDirectory() && deepSearch){
                    copyResources(fromDirectory, toDirectory, deepSearch, forceCopy);
                }

                if(!file.exists() || forceCopy){
                    copyFileUsingChannel(file, new File(toDirectory.getAbsolutePath() + File.separator + file.getName()));
                }
            }
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private static void copyFileUsingChannel(File source, File dest){
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel(); FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        catch (Exception ignored){
            System.err.println("Failed to copy file using channel!");
        }
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
