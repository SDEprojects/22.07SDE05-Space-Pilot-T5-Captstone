package com.spacepilot.model;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Game {


  // Variables, setters, and getters.
  private boolean isOver;
  private int remainingDays;
  private Spacecraft spacecraft;
  private int totalNumberOfAstronauts;
  private List<Planet> planets;

  public Collection<Person> getSavedAstros;

  public Collection<Person> getGetSavedAstros() {
    return getSavedAstros;
  }

  public void setGetSavedAstros(Collection<Person> getSavedAstros) {
    this.getSavedAstros = getSavedAstros;
  }

  public boolean isOver() { // Getter for isOver
    return isOver;
  }

  public void setOver(boolean over) {
    isOver = over;
  }

  public int getRemainingDays() {
    return remainingDays;
  }

  public void setRemainingDays(int remainingDays) {
    this.remainingDays = remainingDays;
  }

  public Spacecraft getSpacecraft() {
    return spacecraft;
  }

  public void setSpacecraft(Spacecraft spacecraft) {
    this.spacecraft = spacecraft;
  }

  public int getTotalNumberOfAstronauts() {
    return totalNumberOfAstronauts;
  }

  public void setTotalNumberOfAstronauts(int totalNumberOfAstronauts) {
    this.totalNumberOfAstronauts = totalNumberOfAstronauts;
  }

  public List<Planet> getPlanets() {
    return planets;
  }

  public void setPlanets(List<Planet> planets) {
    this.planets = planets;
  }

  // Calculates the total number of astronauts needed
  public int calculateRemainingAstronautsViaTotalNumOfAstronauts(Planet earth) {
    int astronautsOnEarth = earth.getNumOfAstronautsOnPlanet();
    int totalNumberOfAstronauts = getTotalNumberOfAstronauts();
    int numberOfAstronautsOnSc = spacecraft.getPassengers().size();
    int remainingNumberOfAstronautsToPickUp = totalNumberOfAstronauts - numberOfAstronautsOnSc - astronautsOnEarth;
    return remainingNumberOfAstronautsToPickUp;
  }

}
