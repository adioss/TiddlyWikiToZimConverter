package com.adioss.tiddlyzim;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TiddlyToZimConverter {

    private File inputFile = null;
    private Path outputFolder = null;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        TiddlyToZimConverter tiddlyToZimConverter = new TiddlyToZimConverter();
        tiddlyToZimConverter.parseParams(args);
        new Converter().apply(tiddlyToZimConverter.getInputFile(), tiddlyToZimConverter.getOutputFolder());
    }

    private void parseParams(String[] args) {
        if (args.length == 0) {
            System.out.println("TiddlyToZimConverter [PARAMETERS]");
            System.out.println("  -inputFile {the tiddly wiki html file}");
            System.out.println("  -outputFolder {the output folder that can match the Zim data folder)}");

            System.out.println("\n");
            System.out.println("Example:");
            System.out.println("TiddlyToZimConverter -inputFile \"c:\\test.html\" -outputFolder \"c:\\output\" \n\n");

            System.exit(1);
        }

        String inputFileArgument = null;
        String outputFolderArgument = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-inputFile")) {
                inputFileArgument = args[i + 1];
            } else if (arg.equals("-outputFolder")) {
                outputFolderArgument = args[i + 1];
            }
        }
        if (isNullOrEmpty(inputFileArgument)) {
            exitWithError("InputFile was not specified.");
        } else if (!Files.exists(Paths.get(inputFileArgument))) {
            exitWithError("InputFile is not correct.");
        }

        if (isNullOrEmpty(outputFolderArgument)) {
            exitWithError("OutputFolder was not specified.");
        }

        inputFile = new File(inputFileArgument);
        outputFolder = Paths.get(outputFolderArgument);
    }

    private boolean isNullOrEmpty(String inputFile) {
        return inputFile == null || "".equals(inputFile);
    }

    private void exitWithError(String message) {
        System.out.println(message);
        System.exit(1);
    }

    public File getInputFile() {
        return inputFile;
    }

    public Path getOutputFolder() {
        return outputFolder;
    }
}
