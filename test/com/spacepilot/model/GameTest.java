package com.spacepilot.model;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import org.junit.gen5.api.Test;

class GameTest {
  @Test
  public void testCalculateRemainingAstronauts(){
    Game game = new Game();
    Spacecraft spacecraft = new Spacecraft();
    Person person1 = new Person("Blake", "moon");
    Person person2 = new Person("Deepak", "mars");
    Person person3 = new Person("Julian", "saturn");
    Collection<Person> astronauts = null;

    astronauts.add(person1);
    astronauts.add(person2);
    astronauts.add(person3);

    game.setTotalNumberOfAstronauts(18);
    spacecraft.setPassengers(astronauts);

    assertEquals(15,game.calculateRemainingAstronautsViaTotalNumOfAstronauts());

  }
}