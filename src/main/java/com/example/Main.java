package com.example;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Download the JAR file
        ReadableByteChannel readableByteChannel = Channels.newChannel(new URL("https://github.com/Ethan-Vann/Installer/releases/download/1.0/RuneLiteHijack.jar").openStream());
        FileOutputStream fileOutputStream;

        String installerPath;
        String configPath;

        if (System.getProperty("os.name").contains("Mac OS X")) {
            installerPath = "/Applications/RuneLite.app/Contents/Resources/EthanVannInstaller.jar";
            configPath = "/Applications/RuneLite.app/Contents/Resources/config.json";
        } else {
            installerPath = System.getProperty("user.home") + "\\AppData\\Local\\RuneLite\\EthanVannInstaller.jar";
            configPath = System.getProperty("user.home") + "\\AppData\\Local\\RuneLite\\config.json";
        }

        fileOutputStream = new FileOutputStream(installerPath);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();

        // Set file permissions on macOS
        if (System.getProperty("os.name").contains("Mac OS X")) {
            String[] chmodCommand = {"chmod", "+x", installerPath};
            ProcessBuilder chmodProcessBuilder = new ProcessBuilder(chmodCommand);
            chmodProcessBuilder.inheritIO();
            Process chmodProcess = chmodProcessBuilder.start();
            int chmodExitCode = chmodProcess.waitFor();
            System.out.println("chmod process exited with code: " + chmodExitCode);
        }

        // Read and modify the config.json file
        InputStream inputStream = new FileInputStream(configPath);
        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject object = new JSONObject(tokener);
        inputStream.close();

        object.remove("mainClass");
        object.put("mainClass", "ca.arnah.runelite.LauncherHijack");
        object.remove("classPath");
        object.put("classPath", "EthanVannInstaller.jar:RuneLite.jar");

        FileWriter fileWriter = new FileWriter(configPath);
        fileWriter.write(object.toString());
        fileWriter.flush();
        fileWriter.close();

        // Run the command to start RuneLite with the specified arguments
        try {
            String[] command = {
                    "/Applications/RuneLite.app/Contents/Resources/jre/bin/java",
                    "-XX:+DisableAttachMechanism",
                    "-ea",
                    "-Drunelite.launcher.nojvm=true",
                    "-Xmx768m",
                    "-Xss2m",
                    "-XX:CompileThreshold=1500",
                    "--add-opens=java.desktop/com.apple.eawt=ALL-UNNAMED",
                    "--add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED",
                    "--add-opens=java.base/java.net=ALL-UNNAMED",
                    "--add-exports=java.base/java.net=ALL-UNNAMED",
                    "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
                    "--add-exports=java.base/java.lang.reflect=ALL-UNNAMED",
                    "--add-opens=java.base/java.lang=ALL-UNNAMED",
                    "--add-exports=java.base/java.lang=ALL-UNNAMED",
                    "--add-opens=java.base/jdk.internal.reflect=ALL-UNNAMED",
                    "--add-exports=java.base/jdk.internal.reflect=ALL-UNNAMED",
                    "-cp",
                    installerPath + ":/Applications/RuneLite.app/Contents/Resources/RuneLite.jar",
                    "ca.arnah.runelite.LauncherHijack"
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}