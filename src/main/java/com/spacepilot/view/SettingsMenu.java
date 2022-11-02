package com.spacepilot.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SettingsMenu {

  Font settingsFont = new Font("Roboto", Font.BOLD, 20);

  JButton musicOn = new JButton("Music On");
  JButton musicOff = new JButton("Music Off");
  JButton volumeUp = new JButton("Volume +");
  JButton volumeDown = new JButton("Volume -");
  JButton save = new JButton("Save");
  JButton help = new JButton("Help");

  public SettingsMenu() {
    JFrame settingsFrame = new JFrame();
    settingsFrame.setSize(300, 200);
    settingsFrame.setVisible(true);
    settingsFrame.setLocationRelativeTo(null);
    settingsFrame.setResizable(false);
    settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    JPanel settingsPanel = new JPanel();
    settingsPanel.setBounds(0, 0, 300, 200);
    settingsPanel.setBackground(Color.black);

    musicOn.setForeground(Color.green);
    musicOn.setOpaque(false);
    musicOn.setFocusPainted(false);
    musicOn.setFont(settingsFont);
    settingsPanel.add(musicOn);

    musicOff.setForeground(Color.red);
    musicOff.setOpaque(false);
    musicOff.setFocusPainted(false);
    musicOff.setFont(settingsFont);
    settingsPanel.add(musicOff);

    volumeUp.setForeground(Color.green);
    volumeUp.setOpaque(false);
    volumeUp.setFocusPainted(false);
    volumeUp.setFont(settingsFont);
    settingsPanel.add(volumeUp);

    volumeDown.setForeground(Color.red);
    volumeDown.setOpaque(false);
    volumeDown.setFocusPainted(false);
    volumeDown.setFont(settingsFont);
    settingsPanel.add(volumeDown);

    save.setForeground(Color.green);
    save.setOpaque(false);
    save.setFocusPainted(false);
    save.setFont(settingsFont);
    settingsPanel.add(save);

    help.setForeground(Color.red);
    help.setOpaque(false);
    help.setFocusPainted(false);
    help.setFont(settingsFont);
    settingsPanel.add(help);

    settingsPanel.setLayout(new GridLayout(3, 2));
    settingsFrame.add(settingsPanel);

    settingsFrame.setVisible(true);
  }

}
