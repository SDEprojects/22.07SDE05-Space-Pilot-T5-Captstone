package com.spacepilot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;
import com.spacepilot.model.Engineer;
import com.spacepilot.model.Game;
import com.spacepilot.model.Music;
import com.spacepilot.model.Person;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Spacecraft;
import com.spacepilot.view.View;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Controller {

  private Game game; // model, where the current state of the game is stored
  private final BufferedReader reader; // buffered reader used to read in what user enters
  private String userInput; // variable used to save user input
  private int repairCounter = 0;
  private double planetRandomizer;

  public Controller(Game game, BufferedReader reader) {
    this.game = game;
    this.reader = reader;
    this.userInput = "";
  }

  // This runs the game for initial startup
  public void play()
      throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException {
    // create and set up game environment
    setUpGame();
    // display game's introduction with flash screen and story and prompt the user to continue
    gameIntro();
    // play music
    Music.playMusic();

    // while game is not over, allow the user to continue to play
    while (!game.isOver()) {
      // print current game info
      displayGameState();
      // prompt the user to enter their next command (saved as userInput)
      getUserInput("Please enter your next command");
      // parse the user input to get their command (verb and noun)
      String[] userCommand = textParser(userInput);
      // execute their command and/or display information (e.g., list of commands, invalid command, etc.)
      nextMove(userCommand);
      checkGameResult();
    }
    Music.stopMusic(); // Close sequencer so that the program can terminate
  }

  // Creates the initial game setup of planets and astronauts randomly
  public void setUpGame() throws URISyntaxException, IOException {
    // create planets based on planets' json files and set them as the current game's planets
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

  // Prompts the user to continue after the intro.
  public void gameIntro() throws IOException {
    View.getGameTextJson();
    // display title
    View.printTitle();
    // prompt the user to press "y" to continue
    do {
      getUserInput("Enter y to continue");
    } while (!userInput.equals("y"));
    View.clearConsole(); // Note: clear console does not work on IntelliJ console
    // display introductory background story
    View.printIntro();
    // prompt the user to press "y" to continue
    do {
      getUserInput("Enter y to continue");
    } while (!userInput.equals("y"));
    View.clearConsole(); // Note: clear console does not work on IntelliJ console
    // display game instructions (how-to-play)
    View.printInstructions();
  }

  // Handles what happens if the user types in commands.
  public void nextMove(String[] command) throws IOException {
    if (command[0].equals("quit")) {
      game.setOver(true);

    } else if (command[0].equals("help")) {
      View.printInstructions();

    } else if (command[0].equals("save")) {
      saveGame(game);

    } else if (command[0].equals("continue")) {
      loadSavedGame();

    } else if (command[0].equals("go")) {
      // if the user is trying to go to the current planet
      if (command[1].equals(game.getSpacecraft().getCurrentPlanet().getName())) {
        View.printSamePlanetAlert();
      } else {
        Planet destinationPlanet = returnPlanet(command[1]);
        if (destinationPlanet == null) {
          View.printInvalidDestination();
        } else {
          String event = destinationPlanet.randomEncounter();
          Spacecraft spacecraft = game.getSpacecraft();
          if (event != null) {
            // decrement spacecraft health by 1.
            spacecraft.setHealth(spacecraft.getHealth() - 1);
            // alert the user about the event
            View.printEventAlert(event);
          }
          spacecraft.setCurrentPlanet(returnPlanet(command[1]));
          // decrement remaining days by 1 when user goes somewhere
          game.setRemainingDays(game.getRemainingDays() - 1);
          // check if the number of remaining days is less than 1
          // or if the spacecraft's health is less than 1
          if (game.getRemainingDays() < 1) {
            // if so, set the game as over
            View.ranOutOfTime();
            game.setOver(true);
          }
          if(spacecraft.getHealth() < 1){
            View.shipDestroyed();
            game.setOver(true);
          }
        }
      }

      // allows the user to talk to npc
    } else if (command[0].equals("chat")) {
      userInput = "";
      while (userInput.length() < 1) {
        View.printNPCDialoguePrompt();
        // display line below until user inputs at least one char
        getUserInput("What would you like to say to them?");
      }
      View.printNPCDialogue();

      // Allows the user to repair the ship
    } else if (command[0].equals("repair")) {
      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      int engineerCount = game.getSpacecraft().getNumOfEngineersOnBoard();
      repairShipConditions(engineerCount);

      // loading of passengers
    } else if (command[0].equals("load")) {
      if (game.getSpacecraft().getPassengers().size() >= 5){
        View.spacecraftFull();
      } else if (game.getSpacecraft().getCurrentPlanet().getArrayOfAstronautsOnPlanet().size() + game.getSpacecraft().getPassengers().size() > 5) {
        View.willPutCraftOverCapacity();
      } else {
        loadNewPassengers();
      }
      // unloading of passengers
    } else if (command[0].equals("unload")) {
      unloadPassengersOnEarth();

    } else if (command[0].equals("planets")) {
      View.printPlanets(game.getPlanets());

    } else if (command[0].equals("use-fuel")) {
      useFuel();
    } else { // invalid command message
      View.printInvalidCommandAlert();
    }
  }

  private void repairShipConditions(int engineerCount) {
    if (engineerCount == 0) {
      View.printNoEngineerAlert();
      return;
    }
    if (repairCounter < 3) {
      Engineer.repairSpacecraft(game.getSpacecraft());
      View.printRepair();

      repairCounter++;
    } else {
      View.printRepairLimit();
    }
  }

  // loading of passengers
  public void loadNewPassengers() {
    Collection<Person> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet();
    // If there are no astronauts on the planet then... print none
    if (arrayOfAstronautsOnCurrentPlanet.size() <= 0) {
      View.printNoAstronautsToLoad();
    }
    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth")) {
      View.printCannotRemovePeopleFromEarth();
    }
    if (arrayOfAstronautsOnCurrentPlanet.size() > 0 && !game.getSpacecraft().getCurrentPlanet()
        .getName().equals("Earth")) {
      game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);
      arrayOfAstronautsOnCurrentPlanet.clear();
      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      determineIfEngineerIsOnBoard();
    }
  }

  // checks for engineers
  public void determineIfEngineerIsOnBoard() {
    if (game.getSpacecraft().getNumOfEngineersOnBoard() > 0) {
      View.printYouveGotAnEngineer();
    } else {
      View.printYouHaventGotAnEngineerOnBoard();
    }
  }

  // handles unloading passengers onto earth or other.
  public void unloadPassengersOnEarth() {
    Planet currentPlanet = game.getSpacecraft().getCurrentPlanet();
    Spacecraft spacecraft = game.getSpacecraft();

    if (currentPlanet.getName().equals("Earth")) {
      currentPlanet.getArrayOfAstronautsOnPlanet().addAll(game.getSpacecraft().getPassengers());
      game.getSpacecraft().setNumOfEngineersOnBoard(0);
      spacecraft.getPassengers().clear();
    } else {
      View.printYouCantUnloadPassengersIfCurrentPlanetNotEarth();
    }
  }

  // Checks if the user has won the game yet.
  public void checkGameResult() {
    int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();

    boolean userWon = (float) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem >= (float) 4 / 4;
    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth") && userWon ){
    View.printGameOverMessage(true);
    game.setOver(true);} else {
      game.setOver(false);
    }
  }

  // Prints where the user is currently located
  public void displayGameState() {
    View.printGameState(game.calculateRemainingAstronautsViaTotalNumOfAstronauts(returnPlanet("earth")),
        game.getRemainingDays(), game.getSpacecraft().getHealth(),
        game.getSpacecraft().getCurrentPlanet().getName(),
        game.getSpacecraft().getPassengers().size());
        printFuelIfOnPlanet();
  }

  // Allows the user to load the game
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

  // allows the user to save games
  public void saveGame(Game game) throws IOException {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    FileWriter writer = new FileWriter("saved-game.json");
    writer.write(gson.toJson(game));
    writer.close();
    View.printSaveGameMessage();
  }

  /*
  HELPER METHODS
   */
  private static String[] textParser(String text) {
    String[] result = new String[2];
    String[] splitText = text.split(" ");
    String verb = splitText[0]; // First word
    String noun = splitText[splitText.length - 1]; // Last word
    result[0] = verb;
    result[1] = noun;
    return result;
  }

  public void getUserInput(String prompt) throws IOException {
    // clear previous user input
    userInput = "";
    // print the prompt message
    View.printUserInputPrompt(prompt);
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    userInput = reader.readLine().trim().toLowerCase();
  }

  // Returns an instance of the desired planet when given a planet name
  // If the desired planet by the name does not exist, returns null
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

  // Creates all possible planets
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

  public void useFuel(){
    if (game.getSpacecraft().getCurrentPlanet().getItem().equals("fuel")){
      game.setRemainingDays(game.getRemainingDays() + 2);
      game.getSpacecraft().getCurrentPlanet().setItem("");
      System.out.println("Way to fuel up!");
    } else{
      View.noFuelToUse();
    }
  }

  public void printFuelIfOnPlanet(){
    if (game.getSpacecraft().getCurrentPlanet().getItem().equals("fuel")){
      View.fuelOnPlanet();
    }
  }

}
