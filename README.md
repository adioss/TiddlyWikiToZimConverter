# TiddlyWikiToZimConverter
A basic converter of TiddlyWiki content to Zim

## Synopsis

Parse the single tiddly html page and extract it to compatible zim file hierarchy:

* look after "MainMenu"
* for each link inside 
    * create a single txt file named as the link
    * go to this page and extract each link inside
    * create a folder named as the previous txt file and for each link
        * create a single .txt and write content inside. Rmk: code is correctly formatted to work on zim

## Code Example

* mvn clean package
* java -cp tiddlyzim-1.0-SNAPSHOT-jar-with-dependencies.jar com.adioss.tiddlyzim.TiddlyToZimConverter -inputFile "c:\test\tyddly.html" -outputFolder "d:\testtest"

## Motivation

Because after each Chrome update, TiddlyWiki is no more usable. So no more [Tiddly](http://tiddlywiki.com) for me, gone to [Zim](http://zim-wiki.org/).

## Installation

    git clone https://github.com/adioss/TiddlyWikiToZimConverter.git

To edit code, import project based on maven using root pom.

## Tests

Describe and show how to run the tests with code examples.

## Contributors

## License

http://www.apache.org/licenses/LICENSE-2.0