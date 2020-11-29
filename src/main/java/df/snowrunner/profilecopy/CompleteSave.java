package df.snowrunner.profilecopy;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CompleteSave {

  public Integer money;
  public Integer xp;
  public Integer rank;
  public Integer numberOfTrucks;
  public String filename;
  public Boolean firstGarage;
  public String lastLoaded;

  
  public CompleteSave(String path, String fileName) throws Throwable {
    
    // Read file.
    byte[] encoded = Files.readAllBytes(Paths.get(path + fileName));
    String jsonString = new String(encoded, StandardCharsets.UTF_8);

    // Parse file.
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(jsonString);
    if (node.get("cfg_version").asText()!="1") {
      throw new Exception("Configversion is not 1!");
    }
    
    // Populate fields.
    this.filename = fileName;
    String objectName = fileName.substring(0, fileName.indexOf("."));
    JsonNode sslValue = node.get(objectName).get("SslValue");
    firstGarage = sslValue.get("isFirstGarageDiscovered").asInt() == 1;
    lastLoaded = sslValue.get("lastLoadedLevel").asText();
    JsonNode profile = sslValue.get("persistentProfileData");
    numberOfTrucks = profile.get("trucksInWarehouse").size();
    money = profile.get("money").asInt();
    xp = profile.get("experience").asInt();
    rank = profile.get("rank").asInt();
  }

  @Override
  public String toString() {
    return filename + " has a profile rank of " + rank.toString() + " that earned " + xp.toString() + " expericene with " + money.toString() +"$ in the wallet, and " + numberOfTrucks
      + " trucks in garage. " + (firstGarage ? "This file can host the " + lastLoaded + " multiplayer game." : "This file is unable to host the " + lastLoaded + " multiplayer game!") ;
  }

}
