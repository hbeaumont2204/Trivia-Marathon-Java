package io.github.some_example_name;

import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;

/**
 * A class used to manage the files within the game.
 * Mostly used for storing and retrieving data such as scores and config values.
 */
public class FileManager {

  public ArrayList<String> readTXTFile(FileHandle file) {
      ArrayList<String> lines = new ArrayList<String>();

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()))) {
          String line;
          while ((line = reader.readLine()) != null) {
              lines.add(line);
          }
      } catch (IOException e) {
          System.out.println("READ ERROR with file: " + file);
          e.printStackTrace();
      }

      return lines;
  }

}
