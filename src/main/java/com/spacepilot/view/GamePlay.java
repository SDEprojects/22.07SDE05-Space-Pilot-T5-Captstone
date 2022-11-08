package com.spacepilot.view;

import com.spacepilot.controller.GUIController;
import com.spacepilot.model.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Consumer;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GamePlay {

  private Game game;
  private GUIController controller;

  private Consumer<String> moveListener;

  // Adds images onto the gameplay play panel
  URL titleImage = getClass().getClassLoader().getResource("GUI/TitleScreen.png");
  URL rocketImage = getClass().getClassLoader().getResource("GUI/Rocket.png");

  InputStream stream = getClass().getClassLoader()
      .getResourceAsStream("GUI/Dashhorizon-eZ5wg.otf");
  Font planetInfoFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
  Border black = BorderFactory.createLineBorder(Color.black);

  static JFrame frame;
  JPanel currentPlanetPanel, shipConditionPanel, remainingDaysPanel, remainingAstronautsPanel, passengersOnboardPanel, engineersOnboardPanel;
  JPanel leftPanel;
  static JPanel midPanel;
  JPanel rocketPanel;
  JPanel rightPanel;
  static JPanel backgroundImagePanel;
  JPanel textPanel;
  static JPanel contentPanel;
  JLabel currentPlanet, shipCondition, remainingDays, remainingAstronauts, passengersOnboard, engineersOnboard;
  JLabel planetLabel, shipConditionLabel, remainingDaysLabel, remainingAstronautsLabel, passengersOnboardLabel, engineersOnboardLabel;
  JButton loadButton, unloadButton, repairButton, travelButton, settingsButton, quitButton;
  JTextPane description;
  private TravelMenu travelMenu;

  public GamePlay(GUIController controller, Game game)
      throws IOException, FontFormatException, MidiUnavailableException, InvalidMidiDataException, URISyntaxException, InterruptedException {
    ClassLoader loader = getClass().getClassLoader(); // This class loader can be used to load all resources for this class
    this.game = game;
    this.controller = controller;
    controller.play();
    frame = Title.frame;


    // Calls upon the creation of each panel for the main gameplay.

    createLeftPanel();
    createRightPanel();
    createRocketPanel();
    createTextPanel();
    createMidPanel();
    createBackgroundImagePanel();


    contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.setBackground(Color.lightGray);

    // Add everything into the gameplay panel

    contentPanel.add(leftPanel);
    contentPanel.add(rightPanel);
    contentPanel.add(rocketPanel);
    contentPanel.add(textPanel);
    contentPanel.add(midPanel);
    contentPanel.add(backgroundImagePanel);

    frame.add(contentPanel);

    frame.setVisible(true);
  }

  /*
 Sets up the left panel on game play to display the information for the current planet, ship
 condition, remaining days, remaining astronauts, passengers onboard, and total engineers
 */
  private JPanel createLeftPanel() {
    leftPanel = new JPanel();
    leftPanel.setBounds(0, 200, 200, 600);
    leftPanel.setBackground(Color.black);

    currentPlanetPanel = new JPanel();
    currentPlanetPanel.setBounds(0, 200, 200, 100);
    currentPlanetPanel.setBackground(Color.white);
    currentPlanetPanel.setBorder(black);

    currentPlanet = new JLabel("Current Planet", SwingConstants.CENTER);
    currentPlanet.setForeground(Color.red);
    currentPlanet.setFont(planetInfoFont);
    currentPlanet.setVerticalAlignment(JLabel.TOP);

    planetLabel = new JLabel("", SwingConstants.CENTER);
    planetLabel.setForeground(Color.orange);
    planetLabel.setFont(planetInfoFont);
    planetLabel.setVerticalAlignment(JLabel.TOP);
    currentPlanetPanel.add(currentPlanet);
    currentPlanetPanel.add(planetLabel);
    currentPlanetPanel.setLayout(new GridLayout(2, 1));

    shipConditionPanel = new JPanel();
    shipConditionPanel.setBounds(0, 300, 200, 100);
    shipConditionPanel.setBackground(Color.white);
    shipConditionPanel.setBorder(black);

    shipCondition = new JLabel("Ship Condition", SwingConstants.CENTER);
    shipCondition.setForeground(Color.red);
    shipCondition.setFont(planetInfoFont);
    shipCondition.setVerticalAlignment(JLabel.TOP);

    shipConditionLabel = new JLabel("", SwingConstants.CENTER);
    shipConditionLabel.setText("");
    shipConditionLabel.setForeground(Color.orange);
    shipConditionLabel.setFont(planetInfoFont);
    shipConditionLabel.setVerticalAlignment(JLabel.TOP);

    shipConditionPanel.add(shipCondition);
    shipConditionPanel.add(shipConditionLabel);
    shipConditionPanel.setLayout(new GridLayout(2, 1));

    remainingDaysPanel = new JPanel();
    remainingDaysPanel.setBounds(0, 400, 200, 100);
    remainingDaysPanel.setBackground(Color.white);
    remainingDaysPanel.setBorder(black);

    remainingDays = new JLabel("Remaining Days", SwingConstants.CENTER);
    remainingDays.setForeground(Color.red);
    remainingDays.setFont(planetInfoFont);
    remainingDays.setVerticalAlignment(JLabel.TOP);

    remainingDaysLabel = new JLabel("", SwingConstants.CENTER);
    remainingDaysLabel.setText("");
    remainingDaysLabel.setForeground(Color.orange);
    remainingDaysLabel.setFont(planetInfoFont);
    remainingDaysLabel.setVerticalAlignment(JLabel.TOP);

    remainingDaysPanel.add(remainingDays);
    remainingDaysPanel.add(remainingDaysLabel);
    remainingDaysPanel.setLayout(new GridLayout(2, 1));

    remainingAstronautsPanel = new JPanel();
    remainingAstronautsPanel.setBounds(0, 500, 200, 100);
    remainingAstronautsPanel.setBackground(Color.white);
    remainingAstronautsPanel.setBorder(black);
    remainingAstronauts = new JLabel("Remaining Astronauts", SwingConstants.CENTER);
    remainingAstronauts.setForeground(Color.red);
    remainingAstronauts.setFont(planetInfoFont);
    remainingAstronauts.setVerticalAlignment(JLabel.TOP);

    remainingAstronautsLabel = new JLabel("", SwingConstants.CENTER);
    remainingAstronautsLabel.setText("");
    remainingAstronautsLabel.setForeground(Color.orange);
    remainingAstronautsLabel.setFont(planetInfoFont);
    remainingAstronautsLabel.setVerticalAlignment(JLabel.TOP);

    remainingAstronautsPanel.add(remainingAstronauts);
    remainingAstronautsPanel.add(remainingAstronautsLabel);
    remainingAstronautsPanel.setLayout(new GridLayout(2, 1));

    passengersOnboardPanel = new JPanel();
    passengersOnboardPanel.setBounds(0, 600, 200, 100);
    passengersOnboardPanel.setBackground(Color.white);
    passengersOnboardPanel.setBorder(black);
    passengersOnboard = new JLabel("Passengers Onboard", SwingConstants.CENTER);
    passengersOnboard.setForeground(Color.red);
    passengersOnboard.setFont(planetInfoFont);
    passengersOnboard.setVerticalAlignment(JLabel.TOP);

    passengersOnboardLabel = new JLabel("", SwingConstants.CENTER);
    passengersOnboardLabel.setText("");
    passengersOnboardLabel.setForeground(Color.orange);
    passengersOnboardLabel.setFont(planetInfoFont);
    passengersOnboardLabel.setVerticalAlignment(JLabel.TOP);

    passengersOnboardPanel.add(passengersOnboard);
    passengersOnboardPanel.add(passengersOnboardLabel);
    passengersOnboardPanel.setLayout(new GridLayout(2, 1));

    engineersOnboardPanel = new JPanel();
    engineersOnboardPanel.setBounds(0, 700, 200, 100);
    engineersOnboardPanel.setBackground(Color.white);
    engineersOnboardPanel.setBorder(black);
    engineersOnboard = new JLabel("Engineers Onboard", SwingConstants.CENTER);
    engineersOnboard.setForeground(Color.red);
    engineersOnboard.setFont(planetInfoFont);
    engineersOnboard.setVerticalAlignment(JLabel.TOP);

    engineersOnboardLabel = new JLabel("", SwingConstants.CENTER);
    engineersOnboardLabel.setText("");
    engineersOnboardLabel.setForeground(Color.orange);
    engineersOnboardLabel.setFont(planetInfoFont);
    engineersOnboardLabel.setVerticalAlignment(JLabel.TOP);

    engineersOnboardPanel.add(engineersOnboard);
    engineersOnboardPanel.add(engineersOnboardLabel);
    engineersOnboardPanel.setLayout(new GridLayout(2, 1));

    leftPanel.add(currentPlanetPanel);
    leftPanel.add(shipConditionPanel);
    leftPanel.add(remainingDaysPanel);
    leftPanel.add(remainingAstronautsPanel);
    leftPanel.add(passengersOnboardPanel);
    leftPanel.add(engineersOnboardPanel);

    leftPanel.setLayout(new GridLayout(6, 1));

    return leftPanel;
  }

  /*
  Below here is the controls for the right panel to include the load, unload, repair, map,
  settings, and quit buttons.
    */
  private JPanel createRightPanel() {
    rightPanel = new JPanel();
    rightPanel.setBounds(800, 200, 200, 600);
    rightPanel.setBackground(Color.black);
    rightPanel.setOpaque(false);
    rightPanel.setLayout(new GridLayout(6, 1));

    loadButton = new JButton("Load Astronauts");
    loadButton.setForeground(Color.blue);
    loadButton.setOpaque(false);
    loadButton.setFont(planetInfoFont);
    loadFunctionality();

    unloadButton = new JButton("Unload Astronauts");
    unloadButton.setForeground(Color.blue);
    unloadButton.setOpaque(false);
    unloadButton.setFont(planetInfoFont);
    unloadFunctionality();

    repairButton = new JButton("Repair");
    repairButton.setForeground(Color.blue);
    repairButton.setOpaque(false);
    repairButton.setFont(planetInfoFont);
    repairFunctionality();

    travelButton = new JButton("Travel");
    travelButton.setForeground(Color.blue);
    travelButton.setOpaque(false);
    travelButton.setFont(planetInfoFont);
    travelButton.addActionListener(e -> {
      travelMenu = new TravelMenu(moveListener);
    });

    settingsButton = new JButton("Settings");
    settingsButton.setForeground(Color.blue);
    settingsButton.setOpaque(false);
    settingsButton.setFont(planetInfoFont);
    settingsButton.addActionListener(e -> {
      new SettingsMenu();
    });

    quitButton = new JButton("Quit");
    quitButton.setForeground(Color.blue);
    quitButton.setOpaque(false);
    quitButton.setFont(planetInfoFont);
    quitGame();

    rightPanel.add(loadButton);
    rightPanel.add(unloadButton);
    rightPanel.add(repairButton);
    rightPanel.add(travelButton);
    rightPanel.add(settingsButton);
    rightPanel.add(quitButton);

    return rightPanel;
  }

  private void loadFunctionality() {
    loadButton.addActionListener(e -> {
      controller.loadNewPassengers();
      update(String.valueOf(game.getRemainingDays()),
          String.valueOf(game.getSpacecraft().getHealth()),
          String.valueOf(game.getSpacecraft().getCurrentPlanet().getName()),
          String.valueOf(game.calculateRemainingAstronautsViaTotalNumOfAstronauts(
              controller.returnPlanet("earth"))),
          String.valueOf(game.getSpacecraft().getPassengers().size()),
          String.valueOf(game.getSpacecraft().getNumOfEngineersOnBoard()), game.getDialogue());
    });
  }

  private void unloadFunctionality() {
    unloadButton.addActionListener(e -> {
      controller.unloadPassengersOnEarth();
      update(String.valueOf(game.getRemainingDays()),
          String.valueOf(game.getSpacecraft().getHealth()),
          String.valueOf(game.getSpacecraft().getCurrentPlanet().getName()),
          String.valueOf(game.calculateRemainingAstronautsViaTotalNumOfAstronauts(
              controller.returnPlanet("earth"))),
          String.valueOf(game.getSpacecraft().getPassengers().size()),
          String.valueOf(game.getSpacecraft().getNumOfEngineersOnBoard()), game.getDialogue());
    });
  }

  private void repairFunctionality() {
    repairButton.addActionListener(e -> {
      controller.repairShipConditions(Integer.parseInt(engineersOnboardLabel.getText()));
      update(String.valueOf(game.getRemainingDays()),
          String.valueOf(game.getSpacecraft().getHealth()),
          String.valueOf(game.getSpacecraft().getCurrentPlanet().getName()),
          String.valueOf(game.calculateRemainingAstronautsViaTotalNumOfAstronauts(
              controller.returnPlanet("earth"))),
          String.valueOf(game.getSpacecraft().getPassengers().size()),
          String.valueOf(game.getSpacecraft().getNumOfEngineersOnBoard()), game.getDialogue());
    });
  }

  private void quitGame() {
    quitButton.addActionListener(e -> {
      System.exit(0);
    });
  }

  private JPanel createRocketPanel() {
    rocketPanel = new JPanel();
    rocketPanel.setBackground(Color.black);
    rocketPanel.setBounds(225, 75, 575, 300);
    rocketPanel.setOpaque(false);

    // Adds a rocket to the background, for fun.
    ImageIcon rocketImg = new ImageIcon(rocketImage);
    rocketImg.setImage(rocketImg.getImage().getScaledInstance(100, 250, Image.SCALE_DEFAULT));
    rocketPanel.add(new JLabel(rocketImg));

    return rocketPanel;
  }

  // Creates the game play panel to give the user a visual representation as to what is going on.
  private void createMidPanel() {
    midPanel = new JPanel();
    midPanel.setBounds(213, 30, 575, 650);
    midPanel.setBackground(Color.black);
    midPanel.setOpaque(false);
    planetImages("earth");

  }

  public static void planetImages(String planet) {
    midPanel.removeAll();
    URL planetImage = GamePlay.class.getClassLoader().getResource("GUI/" + planet + ".png");
    ImageIcon planetImg = new ImageIcon(planetImage);

    planetImg.setImage(planetImg.getImage().getScaledInstance(575, 575, Image.SCALE_DEFAULT));
    midPanel.add(new JLabel(planetImg));

  }

  // Creates the text panel for main game play
  public JPanel createTextPanel() {
    textPanel = new JPanel();
    textPanel.setBounds(200, 700, 600, 310);
    textPanel.setBackground(Color.black);

    description = new JTextPane();
    description.setBounds(200, 700, 600, 310);
    description.setFont(planetInfoFont);
    description.setBackground(Color.black);
    description.setForeground(Color.white);
    description.setOpaque(false);
    description.setEditable(false);

    StyledDocument doc = description.getStyledDocument();
    SimpleAttributeSet center = new SimpleAttributeSet();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
    doc.setParagraphAttributes(0, doc.getLength(), center, false);

    textPanel.add(description);

    return textPanel;
  }

  // Creates the main background image with the current planet
  private JPanel createBackgroundImagePanel() {
    backgroundImagePanel = new JPanel();
    backgroundImagePanel.setBackground(Color.black);
    backgroundImagePanel.setBounds(0, 0, 1000, 1000);
    ImageIcon backgroundImg = new ImageIcon(titleImage);
    backgroundImg.setImage(
        backgroundImg.getImage().getScaledInstance(1000, 1000, Image.SCALE_DEFAULT));

    backgroundImagePanel.add(new JLabel(backgroundImg));
    return backgroundImagePanel;
  }

  // TODO update this so it takes no parameters
  public void update(String days, String condition, String planet, String remainingAstronauts,
      String passengersOnboard, String engineersOnboard, String dialogue) {
    remainingDaysLabel.setText("" + days);
    shipConditionLabel.setText("" + condition);
    planetLabel.setText("" + planet);
    remainingAstronautsLabel.setText("" + remainingAstronauts);
    passengersOnboardLabel.setText("" + passengersOnboard);
    if (Integer.parseInt(engineersOnboard) > 0){
      engineersOnboardLabel.setText("Yes");
    }else if(Integer.parseInt(engineersOnboard) <1 ){
      engineersOnboardLabel.setText("No");
    }
    description.setText(dialogue);
  }

  public void setMoveListener(Consumer<String> moveListener) {
    this.moveListener = moveListener;
  }

}