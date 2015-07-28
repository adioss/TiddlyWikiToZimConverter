/**
 * Copyright 2015 Adrien PAILHES
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adioss.tiddlyzim;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converter {
    private final FileHelper fileHelper;

    public Converter() {
        fileHelper = new FileHelper();
    }

    /**
     * Apply conversion from Tiddly wiki html page to Zim compatible format
     *
     * @param inputFile    the input Tiddly wiki html page
     * @param outputFolder the output folder
     * @throws IOException if {@code inputFile} can not be parsed by {@link Jsoup#parse(java.io.File, java.lang.String)}
     *                     or if can not create {@code outputFolder}
     */
    public void apply(File inputFile, Path outputFolder) throws IOException {
        Map<String, String> contents = new HashMap<>();
        Map<String, String> titles = new HashMap<>();
        Document doc = Jsoup.parse(inputFile, "UTF-8");
        Elements div = doc.getElementsByTag("div");
        for (Element element : div) {
            String title = element.attr("title");
            String key = escapeString(title);
            titles.put(key, title);
            contents.put(key, element.text());
        }
        fileHelper.createDirectory(outputFolder);
        String mainMenu = contents.get("MainMenu");
        List<String> mainMenuLinks = extractLinks(mainMenu);
        for (String mainMenuLink : mainMenuLinks) {
            String mainMenuLinkContent = contents.get(mainMenuLink);
            if (mainMenuLinkContent == null || "".equals(mainMenuLinkContent)) {
                continue;
            }
            Path subDirectoryPath = createSubDirectory(outputFolder, mainMenuLink);
            if (subDirectoryPath == null) {
                continue;
            }
            String newMainMenuLinkContent = "";
            List<String> subMenuLinks = extractLinks(mainMenuLinkContent);
            for (String subMenuLink : subMenuLinks) {
                String escapeString = escapeString(subMenuLink);
                String subMenuContent = contents.get(escapeString);
                createPage(escapeString, titles.get(escapeString), replaceBraceForCode(subMenuContent), subDirectoryPath);
                newMainMenuLinkContent += "[[+" + titles.get(escapeString) + "|" + titles.get(escapeString) + "]]\n";
            }
            createPage(mainMenuLink, titles.get(mainMenuLink), newMainMenuLinkContent, outputFolder);
        }
    }

    List<String> extractLinks(String content) {
        if (content == null || "".equals(content)) {
            return null;
        }
        List<String> result = new ArrayList<>();
        content = content.replaceAll("\n", "");
        String[] menuEntries = content.split("\\[\\[");
        for (String menuEntry : menuEntries) {
            if (isValid(menuEntry)) {
                continue;
            }
            String[] split = menuEntry.split("\\|");
            if (split.length == 1) {
                result.add(menuEntry.replaceAll("]]", ""));
            } else {
                result.add(split[1].replaceAll("]]", ""));
            }
        }
        return result;
    }

    private void createPage(String pageCode, String pageTitle, String pageContent, Path root) {
        String pattern = "Content-Type: text/x-zim-wiki\n" + "Wiki-Format: zim 0.4\n" + "Creation-Date: 2015-07-20T11:58:44+02:00\n" + "\n" + "====== "
                + pageTitle + " ======" + "\n" + pageContent;
        File file = fileHelper.createTxtFile(pageCode, root);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            bufferedWriter.write(pattern);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Page can not be created: " + pageCode);
        }
    }

    private Path createSubDirectory(Path root, String mainMenuLink) {
        File subDirectory = fileHelper.createFile(escapeString(mainMenuLink), root);
        Path subDirectoryPath;
        try {
            subDirectoryPath = Paths.get(subDirectory.getPath());
            fileHelper.createDirectory(subDirectoryPath);
        } catch (Exception e) {
            System.out.println("Page can not be created: " + subDirectory.getPath());
            return null;
        }
        return subDirectoryPath;
    }

    private String replaceBraceForCode(String subMenuContent) {
        if (subMenuContent == null || "".equals(subMenuContent)) {
            return subMenuContent;
        }
        return subMenuContent.replaceAll("\\{\\{\\{", "{{{code: lang=\"java\" linenumbers=\"True\"");
    }

    private boolean isValid(String menuEntry) {
        String escapedString = escapeString(menuEntry);
        return "".equals(escapedString) || escapedString.startsWith("*");
    }

    private String escapeString(String menuEntry) {
        return menuEntry.trim().replaceAll("\\[\\[", "").replaceAll("]]", "").replaceAll("\\*", "").replaceAll(" ", "_");
    }
}
