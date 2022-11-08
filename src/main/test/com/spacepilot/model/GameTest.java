package com.spacepilot.model;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import com.spacepilot.controller.Controller;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.Collection;
import org.junit.gen5.api.Test;

class GameTest {
  Game game = new Game();

  private BufferedReader reader;
  Controller controller = new Controller(game , reader = new BufferedReader(Reader.nullReader()));
  Spacecraft spacecraft = new Spacecraft();
  Planet planet = new Planet("earth", "asteroid", 5, 100, 2000);

  Person person1 = new Person("Blake", "moon");
  Person person2 = new Person("Deepak", "mars");
  Person person3 = new Person("Julian", "saturn");
  Collection<Person> astronauts;


  @Test
  public void testCalculateRemainingAstronauts(){
    astronauts.add(person1);
    astronauts.add(person2);
    astronauts.add(person3);

    game.setTotalNumberOfAstronauts(18);
    spacecraft.setPassengers(astronauts);

    assertEquals(15,game.calculateRemainingAstronautsViaTotalNumOfAstronauts(planet));

  }

  @Test
  public void testUnloadPassengers(){
    astronauts.add(person1);
    astronauts.add(person2);
    astronauts.add(person3);

    game.setTotalNumberOfAstronauts(10);
    spacecraft.setPassengers(astronauts);

  }
}