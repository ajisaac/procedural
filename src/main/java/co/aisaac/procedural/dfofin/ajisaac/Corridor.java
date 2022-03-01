package co.aisaac.procedural.dfofin.ajisaac;

import java.util.ArrayList;

class Corridor {
  ArrayList<Point> doors;
  boolean singleSpace = false,
      twoSpaces = false,
      threeOrMoreStraightLine = false,
      threeOrMoreOddShape = false;
  int x1, y1, x2, y2, size;
  // in case of two spaces or threeOrMoreStraightLine
  boolean northSouth = false, eastWest = false;

  Corridor(int x1, int y1, int x2, int y2, int size) {
    this.x1 = x1;
    this.x2 = x2;
    this.y1 = y1;
    this.y2 = y2;
    this.size = size;
    // we need to figure out north south, east west
    doors = new ArrayList<>();
    // one single space
    if (size == 1) {
      singleSpace = true;
      addDoor(x1, y1);
    }
    // two spaces
    else if (size == 2) {
      twoSpaces = true;
      if (x1 == x2 && y1 != y2) {
        addDoor(x1, y1);
        northSouth = true;
      } else if (x1 != x2 && y1 == y2) {
        addDoor(x1, y1);
        eastWest = true;
      }
    } // three spaces
    else if (size > 2) {
      // if straight line
      if (x1 == x2 || y1 == y2) {
        threeOrMoreStraightLine = true;
        if (x1 == x2 && y1 != y2) {
          addDoor(x1, y1);
          addDoor(x1, y2);
          northSouth = true;
        } else if (x1 != x2 && y1 == y2) {
          addDoor(x1, y1);
          addDoor(x2, y1);
          eastWest = true;
        }
      }
      // if odd shape
      else if (x1 != x2 && y1 != y2) {
        threeOrMoreOddShape = true;
      }
    }
  }

  private void addDoor(int x, int y) {
    doors.add(new Point(x, y));
  }

  public ArrayList<Point> getDoors() {
    return doors;
  }
}
