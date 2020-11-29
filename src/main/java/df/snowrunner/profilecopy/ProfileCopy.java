package df.snowrunner.profilecopy;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ProfileCopy {

  public static void main(String[] args) {
    CompleteSave source, destination;
    Integer i = 0;
    try {
      String path = findSavegameFolder();
      List<CompleteSave> saves = openAllSavegames(path);
      for (CompleteSave cs: saves) {
        System.out.format("%2d %s\n", i++, cs.formatted());
      }
    } catch(Throwable t) {
      System.out.println("An error occured!");
      t.printStackTrace();
    }
  }
  
  /**
   * Returns a list of CompleteSave's found in the supplied path.
   * @param path path to the directory containing the files.
   * @return list of CompleteSave's instantiated per found file in the path.
   */
  private static List<CompleteSave> openAllSavegames(String path) throws Throwable {
    List<CompleteSave> saves = new ArrayList<>();
    File file = new File(path);
    String[] candidates = file.list(new FilenameFilter() {
      @Override
      public boolean accept(File current, String name) {
        return  ( name.matches("^CompleteSave[0-9]?\\.dat$") );
      }
    });
    if (candidates.length == 0) {
      throw new Exception("No savegames found!");
    }
    for (String name: candidates) {
      saves.add(new CompleteSave(path, name));
    }
    return saves;
  }

  /**
   * Returns the SnowRunner savegame folder. This folder contains the
   * "CompleteSave.dat" files.
   * 
   * @return Path to the path containing the savegames of SnowRunner.
   * @throws Throwable at error.
   */
  public static String findSavegameFolder() throws Throwable {
    String storage = findWindowsMyDocuments() + "\\My Games\\SnowRunner\\base\\storage";
    File file = new File(storage);
    String[] candidates = file.list(new FilenameFilter() {
      @Override
      public boolean accept(File current, String name) {
        return  ( (new File(current, name).isDirectory()) &&
                  (name.length() == 32) &&
                  (name.matches("[a-z0-9]*")) );
      }
    });
    if (candidates.length != 1) {
      throw new Exception("Multiple candidates " + Arrays.toString(candidates) + " found in " + storage);
    }
    return storage + "\\" + candidates[0] + "\\";
  }

  /**
   * Returns the My Document path under windows by spawning a process using the query abilities of the Windows exacutable "reg".
   * @return The path.
   * @throws Throwable at error.
   */
  public static String findWindowsMyDocuments() throws Throwable {
    // From: https://stackoverflow.com/questions/9677692/getting-my-documents-path-in-java
    Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
    p.waitFor();

    InputStream in = p.getInputStream();
    byte[] b = new byte[in.available()];
    in.read(b);
    in.close();

    String line = new String(b);
    return line.split("\\s\\s+")[4];
  }

}

