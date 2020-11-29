package df.snowrunner.profilecopy;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This contains a view on the savegame file. Named after the savegame filename of SnowRunner.
 */
public class CompleteSave {

  private int money;
  private int xp;
  private int rank;
  private int numberOfTrucks;
  private String fileName;
  private String path;
  private Boolean firstGarage;
  private String lastLoaded;
  private JsonNode profile;
  private JsonNode root;
  private JsonNode sslValue;
  private String objectName;

  /**
   * Constructs the CompleteSave by loading the associated savegame.
   * @param path the path to the savegame file.
   * @param fileName savegame file name is passed seperately as it is also the json node name.
   * @throws Throwable at error.
   */
  public CompleteSave(String path, String fileName) throws Throwable {
    this.path = path;
    this.fileName = fileName;

    // Read file.
    byte[] encoded = Files.readAllBytes(Paths.get(path + fileName));
    String jsonString = new String(encoded, StandardCharsets.UTF_8);

    // Parse file.
    ObjectMapper mapper = new ObjectMapper();
    root = mapper.readTree(jsonString);
    if (root.get("cfg_version").asText() != "1") {
      throw new IllegalStateException("Configversion is not 1!");
    }
    
    // Populate fields.
    objectName = fileName.substring(0, fileName.indexOf("."));
    sslValue = root.get(objectName).get("SslValue");
    firstGarage = sslValue.get("isFirstGarageDiscovered").asInt() == 1;
    lastLoaded = sslValue.get("lastLoadedLevel").asText();
    profile = sslValue.get("persistentProfileData");
    numberOfTrucks = profile.get("trucksInWarehouse").size();
    money = profile.get("money").asInt();
    xp = profile.get("experience").asInt();
    rank = profile.get("rank").asInt();
  }

  /**
   * Returns a fixed length formatted string of the savegame.
   * @return fixed length formatted representational string.
   */
  public String formatted() {
    return String.format("|%-17.17s|%17.17s|%3dr|%8dxp|%8d$|%4dgtrs|", fileName, lastLoaded, rank, xp, money, numberOfTrucks);
  }

  /**
   * Returns the path plus filename.
   * @return path plus filename.
   */
  public String getFilePathAndFileName() {
    return path + fileName;
  }

  /**
   * Injects `other` profiledata into our JsonNode tree. Other is not changed, `this` is. The internal fields are dirty after calling this method.
   * @param other the source to get the profile data from.
   */
  public void injectProfileFrom(CompleteSave other) {
    ((ObjectNode) sslValue).set("persistentProfileData", other.profile);
  }
  
  /**
   * Overwrites the safegame with the data of the root node.
   */
  public void overwriteSave() throws Throwable {
    try (PrintStream out = new PrintStream(new FileOutputStream(path + fileName))) {
      out.print(root.toString());
    }
  }

  /**
   * Human readable representation of the savegame involved.
   */
  @Override
  public String toString() {
    return fileName + " has a profile rank of " + rank + " that earned " + xp + " expericene with " + money +"$ in the wallet, and " + numberOfTrucks
      + " trucks in garage. " + (firstGarage ? "This file can host the " + lastLoaded + " multiplayer game." : "This file is unable to host the " + lastLoaded + " multiplayer game!") ;
  }

}
