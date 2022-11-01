package com.spacepilot.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;
import com.spacepilot.model.Game;
import com.spacepilot.model.Music;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Spacecraft;
import com.spacepilot.view.View;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class GUIController {

  private Game game;
  private int repairCounter = 0;
  private double planetRandomizer;

  public GUIController(Game game){
    this.game = game;
  }

  public void play() throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException {
    // create and set up game environment
    setUpGame();

    // play music
    //    Music.playMusic();

    // while game is not over, allow the user to continue to play
//        while (!game.isOver()) {
//            // print current game info
//            //displayGameState();
//            // prompt the user to enter their next command (saved as userInput)
//
//            checkGameResult();
//        }
    //  Music.stopMusic(); // Close sequencer so that the program can terminate
  }

  public void saveGame(Game game) throws IOException {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    FileWriter writer = new FileWriter("saved-game.json");
    writer.write(gson.toJson(game));
    writer.close();
    View.printSaveGameMessage();
  }

  public void loadSavedGame() {
    try (Reader reader = Files.newBufferedReader(Paths.get("./saved-game.json"))) {
      Game savedGame = new Gson().fromJson(reader, Game.class);
      if (savedGame != null) { // if there is a saved game data
        game = savedGame;
        View.printLoadGameResult(true);
      } else {
        View.printLoadGameResult(false);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public void setUpGame() throws URISyntaxException, IOException {
    game.setPlanets(createPlanets());

    placeFuelRandomly(game);
    // for each planet in the current game
    for (Planet planet : game.getPlanets()) {
      // place random number of astronauts on each planet
      planet.placeAstronauts(planet);
      // and increment the number of total astronauts by the number of astronauts on each planet
      game.setTotalNumberOfAstronauts(
          game.getTotalNumberOfAstronauts() + planet.getNumOfAstronautsOnPlanet());
    }
    // create a new spacecraft instance for the current game, using data from a .json file
    game.setSpacecraft(createSpacecraft());
    // set the current spacecraft's current planet to be Earth
    game.getSpacecraft().setCurrentPlanet(returnPlanet("earth"));
  }

  public static List<Planet> createPlanets() throws URISyntaxException, IOException {
    List<Planet> planets = new ArrayList<>();
    List<String> planetNames = new ArrayList<>();
    planetNames.add("/planets/mercury.json");
    planetNames.add("/planets/venus.json");
    planetNames.add("/planets/earth.json");
    planetNames.add("/planets/moon.json");
    planetNames.add("/planets/mars.json");
    planetNames.add("/planets/jupiter.json");
    planetNames.add("/planets/saturn.json");
    planetNames.add("/planets/neptune.json");


    for (String planetPath : planetNames) {
      try (Reader reader = new InputStreamReader(
          Main.class.getResourceAsStream(planetPath))) {
        planets.add(new Gson().fromJson(reader, Planet.class));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return planets;
  }

  public void placeFuelRandomly(Game game){
    Random rng = new Random();

    int planetRandomizer = rng.nextInt(game.getPlanets().size() - 1);

    Planet randomPlanet = game.getPlanets().get(planetRandomizer);

    randomPlanet.setItem("fuel");
  }

  public Spacecraft createSpacecraft() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        Main.class.getResourceAsStream("/spacecraft.json"))) {
      // convert JSON file to Spacecraft
      return new Gson().fromJson(reader, Spacecraft.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Planet returnPlanet(String destination) {
    // capitalize the destination
    String planetName =
        destination.substring(0, 1).toUpperCase() + destination.substring(1).toLowerCase();
    for (Planet planet : game.getPlanets()) {
      if (planet.getName().equals(planetName)) {
        return planet;
      }
    }
    return null;
  }

  public void useFuel(){
    if (game.getSpacecraft().getCurrentPlanet().getItem().equals("fuel")){
      game.setRemainingDays(game.getRemainingDays() + 2);
      game.getSpacecraft().getCurrentPlanet().setItem("");
      System.out.println("Way to fuel up!");
    } else{
      View.noFuelToUse();
    }
  }

  public void checkGameResult() {
    int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();

    boolean userWon = (float) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem >= (float) 4 / 4;
    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth") && userWon ){
      View.printGameOverMessage(true);
      game.setOver(true);}
  }
}

