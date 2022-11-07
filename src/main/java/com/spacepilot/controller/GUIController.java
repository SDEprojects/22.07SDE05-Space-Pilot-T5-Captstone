package com.spacepilot.controller;


import static com.spacepilot.Main.createNewGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;

import com.spacepilot.model.Engineer;
import com.spacepilot.model.Game;

import com.spacepilot.model.Music;
import com.spacepilot.model.Person;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Spacecraft;

import com.spacepilot.view.GamePlay;
import com.spacepilot.view.Title;

import com.spacepilot.view.View;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Window;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.ResourceBundle;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GUIController {

  private Game game;
  private int repairCounter = 0;


  Font titleFont = new Font("Roboto", Font.BOLD, 50);
  Font gameStartFont = new Font("Times New Roman", Font.BOLD, 25);


  Font lossFont = new Font(Font.MONOSPACED, Font.BOLD, 20);

  private static final ResourceBundle bundle = ResourceBundle.getBundle("strings");


  public GUIController(Game game){
    this.game = game;
  }

  public void play()
      throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
    // create and set up game environment
    setUpGame();

//     play music
    Music.playMusic();

    checkGameResult();
    game.setDialogue(bundle.getString("intro"));
    while (!game.isOver()) {

//      Music.stopMusic(); // Close sequencer so that the program can terminate
    }
  }

    public void saveGame (Game game) throws IOException {
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      FileWriter writer = new FileWriter("saved-game.json");
      writer.write(gson.toJson(game));
      writer.close();
      View.printSaveGameMessage();
    }

    public void loadSavedGame () {
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

    public void setUpGame () throws URISyntaxException, IOException {
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

    public static List<Planet> createPlanets () throws URISyntaxException, IOException {
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

    public void placeFuelRandomly (Game game){
      Random rng = new Random();

      int planetRandomizer = rng.nextInt(game.getPlanets().size() - 1);

      Planet randomPlanet = game.getPlanets().get(planetRandomizer);

      randomPlanet.setItem("fuel");
    }

    public Spacecraft createSpacecraft () {
      // create a reader
      try (Reader reader = new InputStreamReader(
          Main.class.getResourceAsStream("/spacecraft.json"))) {
        // convert JSON file to Spacecraft
        return new Gson().fromJson(reader, Spacecraft.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Planet returnPlanet (String destination){
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

    public void repairShipConditions ( int engineerCount){
      if (engineerCount == 0) {
        game.setDialogue(View.printNoEngineerAlert());
        return;
      } else if (game.getSpacecraft().getHealth() == 3) {
        game.setDialogue(View.printShipAtFullHealth());
      } else if (repairCounter > 3) {
        game.setDialogue(View.printRepairLimit());
      } else if (repairCounter < 3) {
        Engineer.repairSpacecraft(game.getSpacecraft());
        game.setDialogue(View.printRepair());

        repairCounter++;
      }
    }

    public void loadNewPassengers () {
      Collection<Person> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
          .getArrayOfAstronautsOnPlanet();

      if (arrayOfAstronautsOnCurrentPlanet.size() <= 0) {
        game.setDialogue(View.printNoAstronautsToLoad());

      } else if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth")) {
        game.setDialogue(View.printCannotRemovePeopleFromEarth());
      } else if (game.getSpacecraft().getPassengers().size() >= 5) {
        game.setDialogue(View.spacecraftFull());
      } else if (game.getSpacecraft().getCurrentPlanet().getArrayOfAstronautsOnPlanet().size()
          + game.getSpacecraft().getPassengers().size() > 5) {
        game.setDialogue(View.willPutCraftOverCapacity());
      } else {
        game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);
        arrayOfAstronautsOnCurrentPlanet.clear();
        game.getSpacecraft().typeAndNumOfPassengersOnBoard();
        determineIfEngineerIsOnBoard();
        game.setDialogue(View.printPassengersLoaded());
      }

    }

    public void unloadPassengersOnEarth () {
      Planet currentPlanet = game.getSpacecraft().getCurrentPlanet();
      Spacecraft spacecraft = game.getSpacecraft();

      if (currentPlanet.getName().equals("Earth")) {
        currentPlanet.getArrayOfAstronautsOnPlanet().addAll(game.getSpacecraft().getPassengers());
        game.getSpacecraft().setNumOfEngineersOnBoard(0);
        spacecraft.getPassengers().clear();
        game.setDialogue(View.printUnloadSuccessful());
      } else {
        game.setDialogue(View.printYouCantUnloadPassengersIfCurrentPlanetNotEarth());
      }
    }

    public void determineIfEngineerIsOnBoard () {
      if (game.getSpacecraft().getNumOfEngineersOnBoard() > 0) {
        game.setDialogue(View.printYouveGotAnEngineer());
      } else {
        game.setDialogue(View.printYouHaventGotAnEngineerOnBoard());
      }
    }

    public void useFuel () {
      if (game.getSpacecraft().getCurrentPlanet().getItem().equals("fuel")) {
        game.setRemainingDays(game.getRemainingDays() + 2);
        game.getSpacecraft().getCurrentPlanet().setItem("");
        System.out.println("Way to fuel up!");
      } else {
        View.noFuelToUse();
      }
    }

    public void checkGameResult () throws InterruptedException {
      int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
      int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();

      boolean userWon =
          (float) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem >= (float) 4 / 4;
      if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth") && userWon) {
        game.setDialogue(View.printGameOverMessage(true));
        createWinFrame();
      } else if (game.getRemainingDays() < 1) {
        game.setDialogue(View.ranOutOfTime());
        createLoseFrame();
      } else if (game.getSpacecraft().getHealth() < 1) {
        game.setDialogue(View.shipDestroyed());
        createLoseFrame();
      }


    }

  public static void main(String[] args) {
    Game game = new Game();
    GUIController controller = new GUIController(game);

    controller.createLoseFrame();

  }

    public void createLoseFrame () {
      JFrame frame = new JFrame("Space Pilot: Homebound");

      frame.setBackground(Color.black);
      frame.setSize(700, 700);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      JPanel titlePanel = new JPanel();
      titlePanel.setBounds(0, 50, 500, 100);
      titlePanel.setBackground(Color.black);
      titlePanel.setOpaque(false);
      JLabel title = new JLabel("Space Pilot: Homebound");
      title.setForeground(Color.red);
      title.setFont(titleFont);
      titlePanel.add(title);

      JPanel playGamePanel = new JPanel();
      playGamePanel.setBounds(0, 400, 800, 100);
      playGamePanel.setOpaque(false);
      JButton playGameButton = new JButton("START GAME");
      playGameButton.setForeground(Color.red);
      playGameButton.setBackground(Color.black);
      playGameButton.setFont(gameStartFont);
//    playGameButton.setBorderPainted(false);
      playGamePanel.add(playGameButton);
      playGameButton.addActionListener(e -> {
        GamePlay gamePlay;
        game = createNewGame();
        GUIController controller = new GUIController(game);
        Title newTitle = new Title();

        try {

          gamePlay = new GamePlay(controller, game);

          gamePlay.setMoveListener(new Consumer<String>() {
                                     @Override
                                     public void accept(String location) {
                                       if (location.equals(
                                           game.getSpacecraft().getCurrentPlanet().getName())) {
                                         game.setDialogue(View.printSamePlanetAlert());
                                       } else {
                                         game.setDialogue("You are now on " + location);
                                         Planet newPlanet = controller.returnPlanet(
                                             location.toUpperCase());
                                         controller.getGame().getSpacecraft()
                                             .setCurrentPlanet(newPlanet);
                                         game.setRemainingDays(game.getRemainingDays() - 1);
                                         game.randomEvents();

                                         try {
                                           controller.checkGameResult();

                                         } catch (InterruptedException ex) {
                                           throw new RuntimeException(ex);
                                         }
                                         newTitle.updateGamePlayScreen();
                                       }
                                     }
                                   }

          );
          newTitle.updateGamePlayScreen();
        } catch (IOException | FontFormatException | MidiUnavailableException | URISyntaxException |
                 InvalidMidiDataException | InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      });

      JPanel quitGamePanel = new JPanel();
      quitGamePanel.setBounds(0, 400, 500, 50);
      quitGamePanel.setOpaque(false);
      JButton quitGameButton = new JButton("Quit");
      quitGameButton.setForeground(Color.red);
      quitGameButton.setBackground(Color.black);
      quitGameButton.setFont(gameStartFont);
//    playGameButton.setBorderPainted(false);
      quitGamePanel.add(quitGameButton);
      quitGameButton.addActionListener(e -> {
          System.exit(0);
      });

      JPanel losePanel = new JPanel();
      losePanel.setBounds(0, 400, 1000, 500);
      losePanel.setOpaque(false);

      // Changes information about the text itself
      JTextPane loss = new JTextPane();
      loss.setBounds(0, 400, 1000, 500);
      loss.setFont(lossFont);
      loss.setBackground(Color.black);
      loss.setForeground(Color.orange);
      loss.setOpaque(false);
      loss.setEditable(false);

      StyledDocument doc = loss.getStyledDocument();
      SimpleAttributeSet center = new SimpleAttributeSet();
      StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
      doc.setParagraphAttributes(0, doc.getLength(), center, false);
      if (game.getRemainingDays() < 1) {
        loss.setText(View.ranOutOfTime());
      } else if (game.getSpacecraft().getHealth() < 1) {
        loss.setText(View.shipDestroyed());
      }

      losePanel.add(loss);

      frame.add(titlePanel);
      frame.add(playGamePanel);
      frame.add(quitGamePanel);
      frame.add(losePanel);

      frame.setVisible(true);

    }

    public void createWinFrame () {
      JFrame frame = new JFrame("Space Pilot: Homebound");

      frame.setBackground(Color.black);
      frame.setSize(500, 500);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      JPanel titlePanel = new JPanel();
      titlePanel.setBounds(0, 50, 500, 100);
      titlePanel.setBackground(Color.black);
      titlePanel.setOpaque(false);
      JLabel title = new JLabel("Space Pilot: Homebound");
      title.setForeground(Color.red);
      title.setFont(titleFont);
      titlePanel.add(title);

      JPanel playGamePanel = new JPanel();
      playGamePanel.setBounds(0, 400, 500, 50);
      playGamePanel.setOpaque(false);
      JButton playGameButton = new JButton("START GAME");
      playGameButton.setForeground(Color.red);
      playGameButton.setBackground(Color.black);
      playGameButton.setFont(gameStartFont);
//    playGameButton.setBorderPainted(false);
      playGamePanel.add(playGameButton);
      playGameButton.addActionListener(e -> {
        GamePlay gamePlay;
        Game newGame = createNewGame();
        GUIController newController = new GUIController(newGame);
        Title newTitle = new Title();

        try {

          gamePlay = new GamePlay(newController, newGame);

          gamePlay.setMoveListener(new Consumer<String>() {
                                     @Override
                                     public void accept(String location) {
                                       if (location.equals(
                                           newGame.getSpacecraft().getCurrentPlanet().getName())) {
                                         newGame.setDialogue(View.printSamePlanetAlert());
                                       } else {
                                         newGame.setDialogue("You are now on " + location);
                                         Planet newPlanet = newController.returnPlanet(
                                             location.toUpperCase());
                                         newController.getGame().getSpacecraft()
                                             .setCurrentPlanet(newPlanet);
                                         newGame.setRemainingDays(newGame.getRemainingDays() - 1);
                                         newGame.randomEvents();

                                         try {
                                           newController.checkGameResult();

                                         } catch (InterruptedException ex) {
                                           throw new RuntimeException(ex);
                                         }
                                         newTitle.updateGamePlayScreen();
                                       }
                                     }
                                   }
          );
          newTitle.updateGamePlayScreen();
        } catch (IOException | FontFormatException | MidiUnavailableException | URISyntaxException |
                 InvalidMidiDataException | InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      });

      JPanel quitGamePanel = new JPanel();
      quitGamePanel.setBounds(0, 400, 500, 50);
      quitGamePanel.setOpaque(false);
      JButton quitGameButton = new JButton("Quit");
      quitGameButton.setForeground(Color.red);
      quitGameButton.setBackground(Color.black);
      quitGameButton.setFont(gameStartFont);
//    playGameButton.setBorderPainted(false);
      quitGamePanel.add(quitGameButton);
      quitGameButton.addActionListener(e -> {
        System.exit(0);
      });

      JPanel winPanel = new JPanel();
      winPanel.setBounds(0, 400, 1000, 500);
      winPanel.setOpaque(false);

      // Changes information about the text itself
      JTextPane win = new JTextPane();
      win.setBounds(0, 400, 1000, 500);
      win.setFont(lossFont);
      win.setBackground(Color.black);
      win.setForeground(Color.orange);
      win.setOpaque(false);
      win.setEditable(false);

      StyledDocument doc = win.getStyledDocument();
      SimpleAttributeSet center = new SimpleAttributeSet();
      StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
      doc.setParagraphAttributes(0, doc.getLength(), center, false);
      win.setText(View.printGameOverMessage(true));

      winPanel.add(win);

      frame.add(titlePanel);
      frame.add(playGamePanel);
      frame.add(quitGamePanel);
      frame.add(winPanel);

      frame.setVisible(true);

    }

    public Game getGame () {
      return game;
    }


  }

