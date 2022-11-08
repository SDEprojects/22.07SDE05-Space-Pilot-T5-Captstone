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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
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
  URL skeletonImage = getClass().getClassLoader().getResource("GUI/Skeleton.png");

  URL happyAstronautImage = getClass().getClassLoader().getResource("GUI/Astronaut.png");

  private static JPanel skeletonPanel, astronautWinPanel;

  private static JPanel skeletonPanel;

  private static JFrame frame = new JFrame("Space Pilot: Homebound");
  private static JPanel contentPanel = new JPanel();


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
//    while (!game.isOver()) {
//
////      Music.stopMusic(); // Close sequencer so that the program can terminate
//    }

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

    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth") && game.calculateRemainingAstronautsViaTotalNumOfAstronauts(returnPlanet("earth")) == 0) {
      game.setDialogue(View.winMessage());
      Music.stopMusic();
      createWinFrame();
    } if (game.getRemainingDays() < 1) {
      game.setDialogue(View.ranOutOfTime());
      Music.stopMusic();
      createLoseFrame();
    } if (game.getSpacecraft().getHealth() < 1) {
      game.setDialogue(View.shipDestroyed());
      Music.stopMusic();
      createLoseFrame();
    }
  }

  public void createLoseFrame () {

    createFrameForLoss();

    JPanel titlePanel = new JPanel();
    titlePanel.setBounds(0, 50, 800, 200);
    titlePanel.setBackground(Color.black);
    titlePanel.setOpaque(false);
    JLabel mainTitle = new JLabel("Space Pilot: Homebound");
    mainTitle.setForeground(Color.red);
    mainTitle.setFont(titleFont);
    titlePanel.add(mainTitle);

    JPanel losePanel = new JPanel();
    losePanel.setBounds(0, 300, 800, 200);
    losePanel.setOpaque(false);

    // Changes information about the text itself
    JTextPane loss = new JTextPane();
    loss.setBounds(0, 300, 800, 200);
    loss.setFont(lossFont);
    loss.setBackground(Color.black);
    loss.setForeground(Color.red);
    loss.setOpaque(true);
    loss.setEditable(false);

    StyledDocument doc = loss.getStyledDocument();
    SimpleAttributeSet center = new SimpleAttributeSet();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
    doc.setParagraphAttributes(0, doc.getLength(), center, false);
    if (game.getRemainingDays() < 1) {
      loss.setText(View.ranOutOfTime());
    } if (game.getSpacecraft().getHealth() < 1) {
      loss.setText(View.shipDestroyed());
    }

    losePanel.add(loss);

    JPanel playGamePanel = new JPanel();
    playGamePanel.setBounds(100, 600, 600, 50);
    playGamePanel.setOpaque(false);
    JButton playGameButton = new JButton("Start Game");
    playGameButton.setForeground(Color.red);
    playGameButton.setBackground(Color.black);
    playGameButton.setFont(gameStartFont);

    JButton quitGameButton = new JButton("Quit");
    quitGameButton.setForeground(Color.red);
    quitGameButton.setBackground(Color.black);
    quitGameButton.setFont(gameStartFont);

    playGamePanel.add(playGameButton);
    playGamePanel.add(quitGameButton);
    playGamePanel.setLayout(new GridLayout(1, 2));

    playGameButton.addActionListener(e -> {
      Title title = new Title();
      title.playNewGame();
    });

    quitGameButton.addActionListener(e -> {
      System.exit(0);
    });

    contentPanel.setLayout(null);
    contentPanel.setBackground(Color.black);
    contentPanel.setOpaque(true);


    contentPanel.add(titlePanel);
    contentPanel.add(losePanel);
    contentPanel.add(playGamePanel);
    contentPanel.add(skeletonPanel);

    frame.add(contentPanel);
    frame.setVisible(true);

  }

  public void createFrameForLoss() {

    public void checkGameResult () throws InterruptedException {
      int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
      int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();

      boolean userWon =
          (float) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem >= (float) 4 / 4;
      if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth") && userWon) {
        game.setDialogue(View.printGameOverMessage(true));
        Music.stopMusic();
        createWinFrame();
      } else if (game.getRemainingDays() < 1) {
        game.setDialogue(View.ranOutOfTime());
        Music.stopMusic();
        createLoseFrame();
      } else if (game.getSpacecraft().getHealth() < 1) {
        game.setDialogue(View.shipDestroyed());
        Music.stopMusic();
        createLoseFrame();
      }
    }

    public void createLoseFrame () {

    createFrameForLoss();

      JPanel titlePanel = new JPanel();
      titlePanel.setBounds(0, 50, 800, 200);
      titlePanel.setBackground(Color.black);
      titlePanel.setOpaque(false);
      JLabel mainTitle = new JLabel("Space Pilot: Homebound");
      mainTitle.setForeground(Color.red);
      mainTitle.setFont(titleFont);
      titlePanel.add(mainTitle);

      JPanel losePanel = new JPanel();
      losePanel.setBounds(0, 300, 800, 200);
      losePanel.setOpaque(false);

      // Changes information about the text itself
      JTextPane loss = new JTextPane();
      loss.setBounds(0, 300, 800, 200);
      loss.setFont(lossFont);
      loss.setBackground(Color.black);
      loss.setForeground(Color.red);
      loss.setOpaque(true);
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


    frame.setBackground(Color.black);
    frame.setSize(800, 800);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



      JPanel playGamePanel = new JPanel();
      playGamePanel.setBounds(100, 600, 600, 50);
      playGamePanel.setOpaque(false);
      JButton playGameButton = new JButton("Start Game");
      playGameButton.setForeground(Color.red);
      playGameButton.setBackground(Color.black);
      playGameButton.setFont(gameStartFont);

      JButton quitGameButton = new JButton("Quit");
      quitGameButton.setForeground(Color.red);
      quitGameButton.setBackground(Color.black);
      quitGameButton.setFont(gameStartFont);

      playGamePanel.add(playGameButton);
      playGamePanel.add(quitGameButton);
      playGamePanel.setLayout(new GridLayout(1, 2));

      playGameButton.addActionListener(e -> {
        Title title = new Title();
        title.playNewGame();
      });

      quitGameButton.addActionListener(e -> {
        System.exit(0);
      });

      contentPanel.setLayout(null);
      contentPanel.setBackground(Color.black);
      contentPanel.setOpaque(true);

      contentPanel.add(titlePanel);
      contentPanel.add(losePanel);
      contentPanel.add(playGamePanel);
      contentPanel.add(skeletonPanel);

      frame.add(contentPanel);
      frame.setVisible(true);

    }

  public void createFrameForLoss() {

    frame.setBackground(Color.black);
    frame.setSize(800, 800);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    skeletonPanel = new JPanel();
    skeletonPanel.setBackground(Color.black);
    skeletonPanel.setBounds(0, 0, 800, 800);
    ImageIcon img = new ImageIcon(skeletonImage);
    img.setImage(img.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT));

    skeletonPanel.add(new JLabel(img));

  }


  public void createWinFrame () {
    createFrameForWin();

    JPanel titlePanel = new JPanel();
    titlePanel.setBounds(0, 50, 800, 200);
    titlePanel.setBackground(Color.black);
    titlePanel.setOpaque(false);
    JLabel titleText = new JLabel("Space Pilot: Homebound");
    titleText.setForeground(Color.red);
    titleText.setFont(titleFont);
    titlePanel.add(titleText);

    JPanel winPanel = new JPanel();
    winPanel.setBounds(0, 300, 800, 300);
    winPanel.setOpaque(false);

    // Changes information about the text itself
    JTextPane win = new JTextPane();
    win.setBounds(0, 300, 800, 300);
    win.setFont(lossFont);
    win.setBackground(Color.black);
    win.setForeground(Color.green);
    win.setOpaque(false);
    win.setEditable(false);

    StyledDocument doc = win.getStyledDocument();
    SimpleAttributeSet center = new SimpleAttributeSet();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
    doc.setParagraphAttributes(0, doc.getLength(), center, false);
    win.setText(View.winMessage());

    winPanel.add(win);

    JPanel playGamePanel = new JPanel();
    playGamePanel.setBounds(100, 600, 600, 50);
    playGamePanel.setOpaque(false);
    JButton playGameButton = new JButton("START GAME");
    playGameButton.setForeground(Color.red);
    playGameButton.setBackground(Color.black);
    playGameButton.setFont(gameStartFont);

    JButton quitGameButton = new JButton("Quit");
    quitGameButton.setForeground(Color.red);
    quitGameButton.setBackground(Color.black);
    quitGameButton.setFont(gameStartFont);
    playGamePanel.add(playGameButton);
    playGamePanel.add(quitGameButton);
    playGamePanel.setLayout(new GridLayout(1, 2));

    playGameButton.addActionListener(e -> {
      Title title = new Title();
      title.playNewGame();
    });

    quitGameButton.addActionListener(e -> {
      System.exit(0);
    });

    contentPanel.setLayout(null);
    contentPanel.setBackground(Color.black);
    contentPanel.setOpaque(true);

    contentPanel.add(titlePanel);
    contentPanel.add(winPanel);
    contentPanel.add(playGamePanel);
    contentPanel.add(astronautWinPanel);

    frame.add(contentPanel);
    frame.setVisible(true);

  }

  public void createFrameForWin() {

    frame.setBackground(Color.black);
    frame.setSize(800, 800);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    astronautWinPanel = new JPanel();
    astronautWinPanel.setBackground(Color.black);
    astronautWinPanel.setBounds(0, 0, 800, 800);
    ImageIcon img = new ImageIcon(happyAstronautImage);
    img.setImage(img.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT));

    astronautWinPanel.add(new JLabel(img));

    public void createWinFrame () {
      createFrame();

      JPanel titlePanel = new JPanel();
      titlePanel.setBounds(0, 50, 500, 100);
      titlePanel.setBackground(Color.black);
      titlePanel.setOpaque(false);
      JLabel titleText = new JLabel("Space Pilot: Homebound");
      titleText.setForeground(Color.red);
      titleText.setFont(titleFont);
      titlePanel.add(titleText);

      JPanel winPanel = new JPanel();
      winPanel.setBounds(0, 300, 800, 200);
      winPanel.setOpaque(false);

      // Changes information about the text itself
      JTextPane win = new JTextPane();
      win.setBounds(0, 300, 800, 200);
      win.setFont(lossFont);
      win.setBackground(Color.black);
      win.setForeground(Color.green);
      win.setOpaque(false);
      win.setEditable(false);

      StyledDocument doc = win.getStyledDocument();
      SimpleAttributeSet center = new SimpleAttributeSet();
      StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
      doc.setParagraphAttributes(0, doc.getLength(), center, false);
      win.setText(View.printGameOverMessage(true));

      winPanel.add(win);

      JPanel playGamePanel = new JPanel();
      playGamePanel.setBounds(100, 600, 600, 50);
      playGamePanel.setOpaque(false);
      JButton playGameButton = new JButton("START GAME");
      playGameButton.setForeground(Color.red);
      playGameButton.setBackground(Color.black);
      playGameButton.setFont(gameStartFont);

      JButton quitGameButton = new JButton("Quit");
      quitGameButton.setForeground(Color.red);
      quitGameButton.setBackground(Color.black);
      quitGameButton.setFont(gameStartFont);
      playGamePanel.add(playGameButton);
      playGamePanel.add(quitGameButton);
      playGamePanel.setLayout(new GridLayout(1, 2));

      playGameButton.addActionListener(e -> {
        Title title = new Title();
        title.playNewGame();
      });

      quitGameButton.addActionListener(e -> {
        System.exit(0);
      });

      contentPanel.setLayout(null);
      contentPanel.setBackground(Color.black);
      contentPanel.setOpaque(true);

      contentPanel.add(titlePanel);
      contentPanel.add(winPanel);
      contentPanel.add(playGamePanel);

      frame.add(contentPanel);
      frame.setVisible(true);

    }

    public void createFrame() {
      frame = new JFrame("Space Pilot: Homebound");


      frame.setBackground(Color.black);
      frame.setSize(800, 800);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

  public Game getGame () {
    return game;
  }
  }

  public Game getGame () {
    return game;
  }
}