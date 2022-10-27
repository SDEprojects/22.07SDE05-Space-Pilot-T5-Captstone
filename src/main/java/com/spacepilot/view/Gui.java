package com.spacepilot.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class Gui {

  JFrame frame;
  JPanel titlePanel, imagePanel, playGamePanel, introPanel, spacePanel;
  JTextPane intro;
  JTextArea introText;
  JLabel title;
  JButton playGameButton;

  URL titleImage = ClassLoader.getSystemClassLoader().getResource("./GUI/TitleScreen.png");
  URL spaceShip = ClassLoader.getSystemClassLoader().getResource("./GUI/SpaceShip.png");
  private static final ResourceBundle bundle = ResourceBundle.getBundle("strings");
  Font titleFont = new Font("Roboto", Font.BOLD, 50);
  Font gameStartFont = new Font("Times New Roman", Font.BOLD, 25);
  Font introFont = new Font(Font.MONOSPACED, Font.BOLD, 20);

  public static void main(String[] args) {

    new Gui();
  }

  public Gui() {

    frame = new JFrame("Space Pilot: Homebound");
    frame.setBackground(Color.black);
    frame.setSize(1000, 1000);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    titlePanel = new JPanel();
    titlePanel.setBounds(0, 100, 1000, 200);
    titlePanel.setBackground(Color.black);
    titlePanel.setOpaque(false);
    title = new JLabel("Space Pilot: Homebound");
    title.setForeground(Color.red);
    title.setFont(titleFont);
    titlePanel.add(title);

    playGamePanel = new JPanel();
    playGamePanel.setBounds(0, 800, 1000, 100);
    playGamePanel.setOpaque(false);
    playGameButton = new JButton("START GAME");
    playGameButton.setForeground(Color.red);
    playGameButton.setBackground(Color.black);
    playGameButton.setFont(gameStartFont);
//    playGameButton.setBorderPainted(false);
    playGamePanel.add(playGameButton);

    introPanel = new JPanel();
    introPanel.setBounds(0, 400, 1000, 500);
    introPanel.setOpaque(false);

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

    imagePanel = new JPanel();
    imagePanel.setBackground(Color.black);
    imagePanel.setBounds(0, 0, 1000, 1000);
    ImageIcon img = new ImageIcon(titleImage);
    img.setImage(img.getImage().getScaledInstance(1000, 1000, Image.SCALE_DEFAULT));

    imagePanel.add(new JLabel(img));

    spacePanel = new JPanel();
    spacePanel.setBackground(Color.black);
    spacePanel.setBounds(0, 200, 1000, 200);
    spacePanel.setOpaque(false);
    ImageIcon img1 = new ImageIcon(spaceShip);
    img1.setImage(img1.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));

    spacePanel.add(new JLabel(img1));

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.setBackground(Color.black);
    contentPanel.setOpaque(true);

    contentPanel.add(titlePanel);
    contentPanel.add(playGamePanel);
    contentPanel.add(introPanel);
    contentPanel.add(spacePanel);
    contentPanel.add(imagePanel);

    frame.add(contentPanel);
    frame.setVisible(true);

  }

}


