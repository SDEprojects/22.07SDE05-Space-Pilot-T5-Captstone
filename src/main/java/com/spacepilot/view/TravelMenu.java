package com.spacepilot.view;

import com.spacepilot.controller.Controller;
import com.spacepilot.controller.GUIController;
import com.spacepilot.model.Game;
import com.spacepilot.model.Planet;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TravelMenu {

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

  public TravelMenu() {
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
    controller.moveToMercury();
    });

    venusButton.setForeground(Color.yellow);
    venusButton.setOpaque(false);
    venusButton.setFocusPainted(false);
    venusButton.setBorderPainted(false);
    venusButton.setContentAreaFilled(false);
    venusButton.setFont(planetFont);
    travelPanel.add(venusButton);
    venusButton.addActionListener(e -> {

    });

    earthButton.setForeground(Color.green);
    earthButton.setOpaque(false);
    earthButton.setFocusPainted(false);
    earthButton.setBorderPainted(false);
    earthButton.setContentAreaFilled(false);
    earthButton.setFont(planetFont);
    travelPanel.add(earthButton);
    earthButton.addActionListener(e -> {

    });

    moonButton.setForeground(Color.lightGray);
    moonButton.setOpaque(false);
    moonButton.setFocusPainted(false);
    moonButton.setBorderPainted(false);
    moonButton.setContentAreaFilled(false);
    moonButton.setFont(planetFont);
    travelPanel.add(moonButton);
    moonButton.addActionListener(e -> {

    });

    marsButton.setForeground(Color.red);
    marsButton.setOpaque(false);
    marsButton.setFocusPainted(false);
    marsButton.setBorderPainted(false);
    marsButton.setContentAreaFilled(false);
    marsButton.setFont(planetFont);
    travelPanel.add(marsButton);
    marsButton.addActionListener(e -> {

    });

    jupiterButton.setForeground(Color.orange);
    jupiterButton.setOpaque(false);
    jupiterButton.setFocusPainted(false);
    jupiterButton.setBorderPainted(false);
    jupiterButton.setContentAreaFilled(false);
    jupiterButton.setFont(planetFont);
    travelPanel.add(jupiterButton);
    jupiterButton.addActionListener(e -> {

    });

    saturnButton.setForeground(Color.magenta);
    saturnButton.setOpaque(false);
    saturnButton.setFocusPainted(false);
    saturnButton.setBorderPainted(false);
    saturnButton.setContentAreaFilled(false);
    saturnButton.setFont(planetFont);
    travelPanel.add(saturnButton);
    saturnButton.addActionListener(e -> {

    });

    neptuneButton.setForeground(Color.blue);
    neptuneButton.setOpaque(false);
    neptuneButton.setFocusPainted(false);
    neptuneButton.setBorderPainted(false);
    neptuneButton.setContentAreaFilled(false);
    neptuneButton.setFont(planetFont);
    travelPanel.add(neptuneButton);
    neptuneButton.addActionListener(e -> {

    });

    travelPanel.setLayout(new GridLayout(4, 2));
    travelFrame.add(travelPanel);

    travelFrame.setVisible(true);
  }

}
