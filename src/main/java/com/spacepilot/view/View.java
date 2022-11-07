package com.spacepilot.view;

import com.google.gson.Gson;
import com.spacepilot.Main;
import com.spacepilot.model.GameText;
import com.spacepilot.model.Planet;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Random;

public class View {

  // Sets in game text colors
  public static GameText gameText;
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";

  // Allows the program to read the game-text file.
  public static void getGameTextJson() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        Main.class.getResourceAsStream("/game-text.json"))
    ) {
      // convert JSON file to model.GameText
      gameText = new Gson().fromJson(reader, GameText.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Cleans extra text on the console.
  public static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void printTitle() {
    System.out.println(ANSI_BLUE);
    for (String line : gameText.getTitle()) {
      System.out.println(line);
    }
    System.out.println(ANSI_RESET);
    System.out.println();
  }

  // Prints introduction
  public static void printIntro() {
    System.out.println();
    for (String line : gameText.getIntroduction()) {
      System.out.println(line);
    }
    System.out.println();
  }

  // prints instructions
  public static void printInstructions() {
    System.out.println();
    for (String line : gameText.getInstructions()) {
      System.out.println(line);
    }
    System.out.println();
  }

  // Gives the user current information about their gameplay.
  public static void printGameState(int remainingAstro, int remainingDays, int shipHealth,
      String planetName, int numOfPassengersOnboard) {
    System.out.println();
    System.out.println("Current Planet: " + planetName);
    System.out.println("Ship's Condition: " + shipHealth);
    System.out.println("Number of Remaining Astronauts: " + remainingAstro);
    System.out.println("Number of Remaining Days: " + remainingDays);
    System.out.println("Number of Passengers Onboard: " + numOfPassengersOnboard);
    System.out.println();
  }

  public static void printUserInputPrompt(String prompt) {
    System.out.println(prompt);
  }

  // Prints if the user loads a saved game.
  public static void printLoadGameResult(boolean savedGameExists) {
    System.out.println();
    if (savedGameExists) {
      System.out.println(ANSI_GREEN + "Previous game data successfully loaded" + ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Failed - previous game data does not exist" + ANSI_RESET);
    }
    System.out.println();
  }

  // Prompts the user that they were saved.
  public static void printSaveGameMessage() {
    System.out.println();
    System.out.println(ANSI_BLUE + "SAVED GAME DATA" + ANSI_RESET);
    System.out.println();
  }

  // Prompts the user if they are already on a planet they are trying to move to.
  public static String printSamePlanetAlert() {
    return "System: You are already there";
  }

  // Prints if no engineer are available
  public static String printNoEngineerAlert() {

   return "There are no engineers on board! You need some Engineers!";

  }

  // Prints an event if you have been damaged, and tells you what event.
  public static String printEventAlert(String event) {
    return "MISSION CONTROL: \n\nYour spacecraft has been damaged by \n" + event + ".";
  }

  // Tells the user when they pick up engineers
  public static String printYouveGotAnEngineer() {
    return "You have got at least 1 engineer on board...\n"
        + "and they've got the ability to repair the spacecraft!";
  }

  // Tells the user they cannot repair if they do not have engineers if prompted to repair
  public static String printYouHaventGotAnEngineerOnBoard() {
    return "You don't have any engineers on board...\n"
        + "thus, you cannot repair the spacecraft.";
  }

  // Unsure when this prompt would be displayed.
  public static void printNPCDialoguePrompt() {
    System.out.println();
    System.out.println("The passengers don't seem to be doing well...");
  }

  // Picks a random quote from passengers to be displayed
  public static void printNPCDialogue() {
    Random random = new Random();
    int randomIntInArrayRange = random.nextInt(7);
    System.out.println("Passenger: " + gameText.getNpcDialogue()[randomIntInArrayRange]);
  }

  // prints the gameover message
  public static String printGameOverMessage(boolean userWon) {
    System.out.println();
    if (userWon) {
      return "Congratulations! YOU WON! You have saved the Astronauts. Enjoy Retirement!" ;
    } else {
      return "You Failed!!!";
    }

  }

  // tells the user when they make an invalid command
  public static void printInvalidCommandAlert() {
    System.out.println();
    System.out.println(
        ANSI_RED + "Invalid Command! Please use HELP command to see available commands"
            + ANSI_RESET);
    System.out.println();
  }

  // Tells the user if they cannot go to a certain destination (Or you know... almost anywhere)
  public static void printInvalidDestination() {
    System.out.println();
    System.out.println(ANSI_RED + "Sorry, you cannot go there." + ANSI_RESET);
    System.out.println();
  }

  // Tells the user when there is nobody to load
  public static String printNoAstronautsToLoad() {
    return "There aren't any astronauts to rescue on this planet.";
  }

  // Cannot load people from Earth
  public static String printCannotRemovePeopleFromEarth() {
       return "All passengers dropped off on Earth must remain there, as planet Earth is their final destination.";
  }

  // You can only unload on Earth
  public static String printYouCantUnloadPassengersIfCurrentPlanetNotEarth() {
    return "Passengers can only be dropped off on Earth.";
  }

  // You have repaired your spacecraft
  public static String printRepair() {
    return "Spacecraft repair was successful.";
  }

  // You can repair only twice in each round.
  public static String printRepairLimit() {
    return "Sorry, you cannot use the repair command\n"
        + "more than three times per round of the game.";
  }

  // print all planets that can be traveled to
  public static void printPlanets(List<Planet> planets) {

    for (Planet planet : planets) {
      System.out.println(planet.getName());
    }

  }

  public static String spacecraftFull() {
      return  "The spacecraft is full, \nunload on earth to continue rescuing more astronauts";

  }

  public static String willPutCraftOverCapacity() {
    return "Loading will put your ship over capacity, \nunload on earth and return";
  }

  public static String shipDestroyed() {
    return "Your ship has been destroyed!"
        + "\n\n You failed!";
  }

  public static String ranOutOfTime() {
        return "You did not rescue the necessary amount \nof astronauts in the given time! "
            + "\n\n You failed!";
  }

  public static void noFuelToUse() {
    System.out.println();
    System.out.println("There is now fuel to use!");
    System.out.println();
  }

  public static void fuelOnPlanet() {
    System.out.println();
    System.out.println(ANSI_BLUE
        + "Look There is fuel, type in \"use-fuel\" to fuel up and extend your mission time by 2 days!"
        + ANSI_RESET);
    System.out.println();
  }

  public static String printPassengersLoaded() {

  return "The Passengers have been loaded!";

  }

  public static String printUnloadSuccessful(){
    return "Passengers were unloaded successfully";
  }

  public static String printShipAtFullHealth(){
    return "Cannot repair, \nShip is at full health";
  }
}
