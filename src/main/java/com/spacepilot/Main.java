package com.spacepilot;

import com.google.gson.Gson;
import com.spacepilot.controller.Controller;
import com.spacepilot.model.Game;
import com.spacepilot.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Main {

  // Pre-setup to start the game.
  public static void main(String[] args) {
    try (Reader input =
        new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input))
    {
      do {
        Game game = createNewGame(); // Model
        game.setOver(false); // Set the current game's status to be not over
        Controller controller = new Controller(game, reader); // Controller
        controller.play();
      } while (continuePlaying(reader));
    } catch (IOException | URISyntaxException | MidiUnavailableException |
             InvalidMidiDataException e) {
      throw new RuntimeException(e);
    }
  }
  // This runs a new game when launched

  public static Game createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/game.json")))
    {
      // convert JSON file to Game and return the Game instance
      return new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // if the user dies then asks the user if they would like to play again.
  public static boolean continuePlaying(BufferedReader reader) throws IOException {
    // print the prompt message
    View.printUserInputPrompt(
        "\nTurn & Burn! Would you like to play again?\n"
            + "Enter q to quit\n"
            + "Enter anything else to play another game\n");
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    String userInput = reader.readLine().trim().toLowerCase();
    // check if the user input was "n" and return an appropriate boolean
    if (userInput.equals("q")) {
      return false;
    } else {
      return true;
    }
  }

}
