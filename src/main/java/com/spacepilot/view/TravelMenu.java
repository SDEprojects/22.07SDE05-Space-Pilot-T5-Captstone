package com.spacepilot.view;

import com.spacepilot.model.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TravelMenu {

  private Game game;
  URL iconURL = getClass().getClassLoader().getResource("GUI/Rocket.png");

  Font planetFont = new Font("Roboto", Font.BOLD, 20);

  JButton earthButton = new JButton("Earth");
  JButton jupiterButton = new JButton("Jupiter");
  JButton marsButton = new JButton("Mars");
  JButton mercuryButton = new JButton("Mercury");
  JButton moonButton = new JButton("Moon");
  JButton neptuneButton = new JButton("Neptune");
  JButton saturnButton = new JButton("Saturn");
  JButton venusButton = new JButton("Venus");

  private Consumer<String> moveListener;

  public TravelMenu(Consumer<String> moveListener) {
    setMoveListener(moveListener);

    JFrame frame = GamePlay.frame;
//    ImageIcon icon = new ImageIcon(iconURL);
//    frame.setIconImage(icon.getImage());

    JPanel midPanel = GamePlay.midPanel;
    JPanel contentPanel = GamePlay.contentPanel;
    JPanel backgroundImagePanel = GamePlay.backgroundImagePanel;
    JFrame travelFrame = new JFrame();
    travelFrame.setSize(400, 400);
    travelFrame.setVisible(true);
    travelFrame.setLocationRelativeTo(null);
    travelFrame.setResizable(false);
    travelFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    JPanel travelPanel = new JPanel();
    travelPanel.setBounds(0, 0, 400, 400);
    travelPanel.setBackground(Color.black);

    mercuryButton.setForeground(Color.blue);
    mercuryButton.setOpaque(false);
    mercuryButton.setFocusPainted(false);
    mercuryButton.setBorderPainted(false);
    mercuryButton.setContentAreaFilled(false);
    mercuryButton.setFont(planetFont);
    travelPanel.add(mercuryButton);

    mercuryButton.addActionListener(e -> {
      moveListener.accept("mercury");
//      displayPlanetIcon("mercury");
      travelFrame.setVisible(false);
      GamePlay.planetImages("mercury");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    venusButton.setForeground(Color.yellow);
    venusButton.setOpaque(false);
    venusButton.setFocusPainted(false);
    venusButton.setBorderPainted(false);
    venusButton.setContentAreaFilled(false);
    venusButton.setFont(planetFont);
    travelPanel.add(venusButton);
    venusButton.addActionListener(e -> {
      moveListener.accept("venus");
//      displayPlanetIcon("venus");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Venus");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    earthButton.setForeground(Color.green);
    earthButton.setOpaque(false);
    earthButton.setFocusPainted(false);
    earthButton.setBorderPainted(false);
    earthButton.setContentAreaFilled(false);
    earthButton.setFont(planetFont);
    travelPanel.add(earthButton);
    earthButton.addActionListener(e -> {
      moveListener.accept("earth");
//      displayPlanetIcon("earth");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Earth");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    moonButton.setForeground(Color.lightGray);
    moonButton.setOpaque(false);
    moonButton.setFocusPainted(false);
    moonButton.setBorderPainted(false);
    moonButton.setContentAreaFilled(false);
    moonButton.setFont(planetFont);
    travelPanel.add(moonButton);
    moonButton.addActionListener(e -> {
      moveListener.accept("moon");
//      displayPlanetIcon("moon");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Moon");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    marsButton.setForeground(Color.red);
    marsButton.setOpaque(false);
    marsButton.setFocusPainted(false);
    marsButton.setBorderPainted(false);
    marsButton.setContentAreaFilled(false);
    marsButton.setFont(planetFont);
    travelPanel.add(marsButton);
    marsButton.addActionListener(e -> {
      moveListener.accept("mars");
//      displayPlanetIcon("mars");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Mars");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    jupiterButton.setForeground(Color.orange);
    jupiterButton.setOpaque(false);
    jupiterButton.setFocusPainted(false);
    jupiterButton.setBorderPainted(false);
    jupiterButton.setContentAreaFilled(false);
    jupiterButton.setFont(planetFont);
    travelPanel.add(jupiterButton);
    jupiterButton.addActionListener(e -> {
      moveListener.accept("jupiter");
//      displayPlanetIcon("jupiter");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Jupiter");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    saturnButton.setForeground(Color.magenta);
    saturnButton.setOpaque(false);
    saturnButton.setFocusPainted(false);
    saturnButton.setBorderPainted(false);
    saturnButton.setContentAreaFilled(false);
    saturnButton.setFont(planetFont);
    travelPanel.add(saturnButton);
    saturnButton.addActionListener(e -> {
      moveListener.accept("saturn");
//      displayPlanetIcon("saturn");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Saturn");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    neptuneButton.setForeground(Color.blue);
    neptuneButton.setOpaque(false);
    neptuneButton.setFocusPainted(false);
    neptuneButton.setBorderPainted(false);
    neptuneButton.setContentAreaFilled(false);
    neptuneButton.setFont(planetFont);
    travelPanel.add(neptuneButton);
    neptuneButton.addActionListener(e -> {
      moveListener.accept("neptune");
//      displayPlanetIcon("neptune");
      travelFrame.setVisible(false);
      GamePlay.planetImages("Neptune");
      contentPanel.remove(midPanel);
      contentPanel.remove(backgroundImagePanel);
      frame.remove(contentPanel);
      contentPanel.add(midPanel);
      contentPanel.add(backgroundImagePanel);
      frame.add(contentPanel);
    });

    travelPanel.setLayout(new GridLayout(4, 2));
    travelFrame.add(travelPanel);
    travelFrame.setVisible(true);
  }

  //  public static void displayPlanetIcon(String iconPlanet) {
//
//    URL planetImage = GamePlay.class.getClassLoader().getResource("GUI/" + iconPlanet + ".png");
//    ImageIcon planetImg = new ImageIcon(planetImage);
//    GamePlay.frame.setIconImage(planetImg.getImage());
//
//  }
  public void setMoveListener(Consumer<String> listener) {
    this.moveListener = listener;
  }
}
