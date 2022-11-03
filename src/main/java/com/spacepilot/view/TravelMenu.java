package com.spacepilot.view;

import com.spacepilot.controller.Controller;
import com.spacepilot.controller.GUIController;
import com.spacepilot.model.Game;
import com.spacepilot.model.Planet;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TravelMenu extends JFrame{

  private GUIController controller;
  private Game game;
  private Planet planet;
  private GamePlay gamePlay;

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
//      gamePlay.planetImages("mercury");
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
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
      travelFrame.setVisible(false);
    });

    travelPanel.setLayout(new GridLayout(4, 2));
    travelFrame.add(travelPanel);

    travelFrame.setVisible(true);
  }

  public void setMoveListener(Consumer<String> listener) {
    this.moveListener = listener;
  }
}
