package com.spacepilot.view;

import com.spacepilot.controller.Controller;
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
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class GamePlay {

  private Game game;
  private Controller controller;

  // Adds images onto the gameplay play panel
  URL titleImage = ClassLoader.getSystemClassLoader().getResource("./GUI/TitleScreen.png");
  URL earthImage = ClassLoader.getSystemClassLoader().getResource("./GUI/Earth.png");
  URL rocketImage = ClassLoader.getSystemClassLoader().getResource("./GUI/Rocket.png");
  InputStream stream = ClassLoader.getSystemClassLoader()
      .getResourceAsStream("GUI/Dashhorizon-eZ5wg.otf");
  Font planetInfoFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
  Border black = BorderFactory.createLineBorder(Color.black);

  JPanel leftPanel, midPanel, rocketPanel, rightPanel, planetPanel, textPanel;
  JLabel currentPlanet, shipCondition, remainingDays, remainingAstronauts, passengersOnboard, totalEngineers;
  JButton loadButton, unloadButton, repairButton, mapButton, settingsButton, quitButton;

  public GamePlay(Controller controller, Game game) throws IOException, FontFormatException {
    ClassLoader loader = getClass().getClassLoader(); // This class loader can be used to load all resources for this class
    this.game = game;
    this.controller = controller;
    JFrame frame = Title.frame;

    // Calls upon the creation of each panel for the main gameplay.
    createLeftPanel();
    createRightPanel();
    createRocketPanel();
    createTextPanel();
    createMidPanel();
    createPlanetPanel();

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.setOpaque(true);

    // Add everything into the gameplay panel
    contentPanel.add(leftPanel);
    contentPanel.add(rightPanel);
    contentPanel.add(rocketPanel);
    contentPanel.add(textPanel);
    contentPanel.add(midPanel);
    contentPanel.add(planetPanel);

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
    leftPanel.setBackground(Color.white);

    currentPlanet = new JLabel("Current Planet:", SwingConstants.CENTER);
    currentPlanet.setForeground(Color.red);
    currentPlanet.setBorder(black);
    currentPlanet.setFont(planetInfoFont);
    currentPlanet.setVerticalAlignment(JLabel.TOP);
    leftPanel.add(currentPlanet);

    shipCondition = new JLabel("Ship Condition", SwingConstants.CENTER);
    shipCondition.setForeground(Color.red);
    shipCondition.setBorder(black);
    shipCondition.setFont(planetInfoFont);
    shipCondition.setVerticalAlignment(JLabel.TOP);
    leftPanel.add(shipCondition);

    remainingDays = new JLabel("Remaining Days", SwingConstants.CENTER);
    remainingDays.setForeground(Color.red);
    remainingDays.setBorder(black);
    remainingDays.setFont(planetInfoFont);
    remainingDays.setVerticalAlignment(JLabel.TOP);
    leftPanel.add(remainingDays);

    remainingAstronauts = new JLabel("Remaining Astronauts", SwingConstants.CENTER);
    remainingAstronauts.setForeground(Color.red);
    remainingAstronauts.setBorder(black);
    remainingAstronauts.setFont(planetInfoFont);
    remainingAstronauts.setVerticalAlignment(JLabel.TOP);
    leftPanel.add(remainingAstronauts);

    passengersOnboard = new JLabel("Passengers Onboard", SwingConstants.CENTER);
    passengersOnboard.setForeground(Color.red);
    passengersOnboard.setBorder(black);
    passengersOnboard.setFont(planetInfoFont);
    passengersOnboard.setVerticalAlignment(JLabel.TOP);
    leftPanel.add(passengersOnboard);

    totalEngineers = new JLabel("Total Engineers", SwingConstants.CENTER);
    totalEngineers.setForeground(Color.red);
    totalEngineers.setBorder(black);
    totalEngineers.setFont(planetInfoFont);
    totalEngineers.setVerticalAlignment(JLabel.TOP);
    leftPanel.add(totalEngineers);
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

    unloadButton = new JButton("Unload Astronauts");
    unloadButton.setForeground(Color.blue);
    unloadButton.setOpaque(false);
    unloadButton.setFont(planetInfoFont);

    repairButton = new JButton("Repair");
    repairButton.setForeground(Color.blue);
    repairButton.setOpaque(false);
    repairButton.setFont(planetInfoFont);

    mapButton = new JButton("Map");
    mapButton.setForeground(Color.blue);
    mapButton.setOpaque(false);
    mapButton.setFont(planetInfoFont);

    settingsButton = new JButton("Settings");
    settingsButton.setForeground(Color.blue);
    settingsButton.setOpaque(false);
    settingsButton.setFont(planetInfoFont);

    quitButton = new JButton("Quit");
    quitButton.setForeground(Color.blue);
    quitButton.setOpaque(false);
    quitButton.setFont(planetInfoFont);

    rightPanel.add(loadButton);
    rightPanel.add(unloadButton);
    rightPanel.add(repairButton);
    rightPanel.add(mapButton);
    rightPanel.add(settingsButton);
    rightPanel.add(quitButton);

    return rightPanel;
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
  private JPanel createMidPanel() {
    midPanel = new JPanel();
    midPanel.setBounds(213, 30, 575, 650);
    midPanel.setBackground(Color.black);
    midPanel.setOpaque(false);

    ImageIcon planetImg = new ImageIcon(earthImage);
    planetImg.setImage(planetImg.getImage().getScaledInstance(650, 650, Image.SCALE_DEFAULT));
    midPanel.add(new JLabel(planetImg));

    return midPanel;
  }

  // Creates the text panel for main game play
  private JPanel createTextPanel() {
    textPanel = new JPanel();
    textPanel.setBounds(200, 650, 600, 310);
    textPanel.setBackground(Color.black);

    return textPanel;
  }

  // Creates the main background image with the current planet
  private JPanel createPlanetPanel() {
    planetPanel = new JPanel();
    planetPanel.setBackground(Color.black);
    planetPanel.setBounds(0, 0, 1000, 1000);
    ImageIcon backgroundImg = new ImageIcon(titleImage);
    backgroundImg.setImage(
        backgroundImg.getImage().getScaledInstance(1000, 1000, Image.SCALE_DEFAULT));

    planetPanel.add(new JLabel(backgroundImg));

    return planetPanel;
  }

  public void update() {
    // populate all of the widgets with the correct values (such as remaining days)
    currentPlanet.setText("Current Planet " + game.getSpacecraft().getCurrentPlanet().getName());
    remainingDays.setText("Ship Condition " + game.getSpacecraft().getHealth());
    remainingDays.setText("Remaining Days " + game.getRemainingDays());
    remainingDays.setText("Remaining Astronauts ");
    remainingDays.setText("Passengers Onboard " + game.getSpacecraft().getPassengers());
    remainingDays.setText("Total Engineers " + game.getSpacecraft().getNumOfEngineersOnBoard());
  }

  public void startGame()
      throws MidiUnavailableException, InvalidMidiDataException, IOException, URISyntaxException {
    controller.play();
  }

}
