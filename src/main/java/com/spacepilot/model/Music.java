package com.spacepilot.model;

import com.spacepilot.Main;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import jm.JMC;

public final class Music implements JMC {

  private static Sequencer sequencer;

  // Handles playing music
  public static void playMusic()
      throws MidiUnavailableException, InvalidMidiDataException, IOException {

    try {
      sequencer = MidiSystem.getSequencer();
      sequencer.open();
      InputStream inputStream = Main.class.getResourceAsStream("/black-hole-audio.mp3.mid");
      sequencer.setSequence(inputStream);
      sequencer.start();
      sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
    } catch (MidiUnavailableException e) {
      throw new RuntimeException(e);
    }
  }

  // Handles stopping music
  public static void stopMusic() {
    sequencer.close();
  }

}