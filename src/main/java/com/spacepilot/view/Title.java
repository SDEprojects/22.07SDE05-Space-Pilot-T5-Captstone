package com.spacepilot.view;

import com.spacepilot.Main;
import com.spacepilot.controller.GUIController;
import com.spacepilot.model.Game;
import com.spacepilot.model.Planet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
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

public class Title {

  private Game game;
  private GUIController controller;
  public static JFrame frame;
  private GamePlay gamePlay;

  JPanel titlePanel, imagePanel, playGamePanel, introPanel, spacePanel;
  private static JPanel contentPanel = new JPanel();
  JTextPane intro;
  JLabel title;
  JButton playGameButton;

  // Provide the images to the introduction screen.
  URL titleImage = getClass().getClassLoader().getResource("GUI/TitleScreen.png");
  URL spaceShip = getClass().getClassLoader().getResource("GUI/Rocket.png");

  private static final ResourceBundle bundle = ResourceBundle.getBundle("strings");
  Font titleFont = new Font("Roboto", Font.BOLD, 50);
  Font gameStartFont = new Font("Times New Roman", Font.BOLD, 25);
  Font introFont = new Font(Font.MONOSPACED, Font.BOLD, 20);

  // This PSVM runs our GUI
  public static void main(String[] args) {

    new Title();
  }

  public Title() {

    // Creates the initial frame for everything
    frame = new JFrame("Space Pilot: Homebound");

    frame.setBackground(Color.black);
    frame.setSize(1000, 1000);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Controls just the title itself
    titlePanel = new JPanel();
    titlePanel.setBounds(0, 100, 1000, 200);
    titlePanel.setBackground(Color.black);
    titlePanel.setOpaque(false);
    title = new JLabel("Space Pilot: Homebound");
    title.setForeground(Color.red);
    title.setFont(titleFont);
    titlePanel.add(title);

    // Controls the start game button
    playGamePanel = new JPanel();
    playGamePanel.setBounds(0, 800, 1000, 100);
    playGamePanel.setOpaque(false);
    playGameButton = new JButton("START GAME");
    playGameButton.setForeground(Color.red);
    playGameButton.setBackground(Color.black);
    playGameButton.setFont(gameStartFont);
    playGamePanel.add(playGameButton);

    // Sets up the panel for the intro text
    introPanel = new JPanel();
    introPanel.setBounds(0, 400, 1000, 500);
    introPanel.setOpaque(false);

    // Changes information about the text itself
    intro = new JTextPane();
    intro.setBounds(0, 400, 1000, 500);
    intro.setFont(introFont);
    intro.setBackground(Color.black);
    intro.setForeground(Color.orange);
    intro.setOpaque(false);
    intro.setEditable(false);

    StyledDocument doc = intro.getStyledDocument();
    SimpleAttributeSet center = new SimpleAttributeSet();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
    doc.setParagraphAttributes(0, doc.getLength(), center, false);
    intro.setText(bundle.getString("introduction"));

    introPanel.add(intro);

    // Sets up the background image
    imagePanel = new JPanel();
    imagePanel.setBackground(Color.black);
    imagePanel.setBounds(0, 0, 1000, 1000);

//    URL titleImage = getClass().getClassLoader().getResource("./GUI/TitleScreen.png");
    ImageIcon img = new ImageIcon(titleImage);
    img.setImage(img.getImage().getScaledInstance(1000, 1000, Image.SCALE_DEFAULT));

    imagePanel.add(new JLabel(img));

    spacePanel = new JPanel();
    spacePanel.setBackground(Color.black);
    spacePanel.setBounds(0, 200, 1000, 200);
    spacePanel.setOpaque(false);
    ImageIcon img1 = new ImageIcon(spaceShip);
    img1.setImage(img1.getImage().getScaledInstance(100, 250, Image.SCALE_DEFAULT));

    spacePanel.add(new JLabel(img1));

//    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.setBackground(Color.black);
    contentPanel.setOpaque(true);

    // Adds everything onto the GUI
    contentPanel.add(titlePanel);
    contentPanel.add(playGamePanel);
    contentPanel.add(introPanel);
    contentPanel.add(spacePanel);
    contentPanel.add(imagePanel);

    frame.add(contentPanel);

    playNewGame();
    frame.setVisible(true);
  }

  public void playNewGame() {

    // Allows the users to hit the play button
    playGameButton.addActionListener(e -> {

      game = Main.createNewGame();
      controller = new GUIController(game);
      frame.remove(contentPanel);

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
                                       updateGamePlayScreen();
                                     }

                                   }
                                 }
        );
        updateGamePlayScreen();
      } catch (IOException | FontFormatException | MidiUnavailableException | URISyntaxException |
               InvalidMidiDataException | InterruptedException ex) {
        throw new RuntimeException(ex);
      }
    });

  }


  public void updateGamePlayScreen() {
    String remainingDays = String.valueOf(game.getRemainingDays());
    String condition = String.valueOf(game.getSpacecraft().getHealth());
    String planet = game.getSpacecraft().getCurrentPlanet().getName();
    String remainingAstronauts = String.valueOf(
        game.calculateRemainingAstronautsViaTotalNumOfAstronauts(
            controller.returnPlanet("Earth")));
    String passengersOnboard = String.valueOf(game.getSpacecraft().getPassengers().size());
    String engineersOnboard = String.valueOf(game.getSpacecraft().getNumOfEngineersOnBoard());
    String dialogue = game.getDialogue();
    gamePlay.update(remainingDays, condition, planet, remainingAstronauts, passengersOnboard,
        engineersOnboard, dialogue);

  }

}