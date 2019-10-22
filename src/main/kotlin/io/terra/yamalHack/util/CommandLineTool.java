package io.terra.yamalHack.util;

import io.terra.yamalHack.api.FaceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineTool {

    private Logger logger = LoggerFactory.getLogger(CommandLineTool.class);

    public String runTool(String argument) throws Exception {
        try {
            // Run command
            String[] command = {"python", "/home/gera/IdeaProjects/yamalHack/src/main/resources/scripts/face_detect.py", argument};
            ProcessBuilder cwlToolProcess = new ProcessBuilder(command);
            Process process = cwlToolProcess.start();

            // Read output from the process using threads
            StreamGobbler inputGobbler = new StreamGobbler(process.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
            errorGobbler.start();
            inputGobbler.start();

            // Wait for process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                inputGobbler.join();
                return inputGobbler.getContent();
            } else {
                errorGobbler.join();
                throw new Exception(errorGobbler.getContent());
            }
        } catch (IOException |InterruptedException e) {
            logger.error("Error running tool", e);
            throw new Exception("Error running tool");
        }
    }
}
