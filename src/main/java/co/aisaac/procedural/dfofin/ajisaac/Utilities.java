package co.aisaac.procedural.dfofin.ajisaac;

import java.util.ArrayList;
import java.util.Random;

public class Utilities {

  Random rand;
  int mapWidth, mapHeight;

  Utilities(int mapWidth, int mapHeight, Random random) {
    this.rand = random;
    this.mapHeight = mapHeight;
    this.mapWidth = mapWidth;
  }

  boolean ArrayIsEmpty(int[][] map) {
    for (int j = mapHeight - 1; j >= 0; j--) {
      for (int i = mapWidth - 1; i >= 0; i--) {
        if (map[i][j] != 0) {
          return false;
        }
      }
    }
    return true;
  }

  // prints the array to screen
  void printArray(int[][] map) {
    for (int j = mapHeight - 1; j >= 0; j--) {
      String s = "";
      for (int i = mapWidth - 1; i >= 0; i--) {
        if (map[i][j] == 0) {
          s += ".";
        } else {
          s += map[i][j];
        }
      }
      System.out.println(s);
    }
    System.out.println();
    System.out.println();
  }

  // check if the array is filled up by any one character
  boolean landMassFillsUpArray(int[][] map) {
    int t = map[0][0];
    for (int j = mapHeight - 1; j >= 0; j--) {
      for (int i = mapWidth - 1; i >= 0; i--) {
        if (map[i][j] != t) {
          return false;
        }
      }
    }
    return true;
  }

  // initializes an array with 0
  int[][] initializeArray(int mapWidth, int mapHeight) {
    int[][] map = new int[mapWidth][mapHeight];
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        map[i][j] = 0;
      }
    }

    return map;
  }

  // returns a complete copy of an array
  int[][] copyArray(int[][] map) {
    int[][] newLM = initializeArray(mapWidth, mapHeight);
    for (int j = mapHeight - 1; j >= 0; j--) {
      for (int i = mapWidth - 1; i >= 0; i--) {
        newLM[i][j] = map[i][j];
      }
    }
    return newLM;
  }

  // removes any part of one array that might be contained in another array
  int[][] purgeFromArray(int[][] purge, int[][] map) {
    int[][] newLM = initializeArray(mapWidth, mapHeight);
    for (int j = mapHeight - 1; j >= 0; j--) {
      for (int i = mapWidth - 1; i >= 0; i--) {
        newLM[i][j] = map[i][j];
        if (purge[i][j] != 0) {
          newLM[i][j] = 0;
        }
      }
    }
    return newLM;
  }

  // combines two arrays into one
  int[][] mergeArrays(int[][] map, int[][] map2) {
    int[][] newLM = initializeArray(mapWidth, mapHeight);
    for (int j = mapHeight - 1; j >= 0; j--) {
      for (int i = mapWidth - 1; i >= 0; i--) {
        if (map[i][j] != 0 || map2[i][j] != 0) {
          newLM[i][j] = 1;
        }
      }
    }
    return newLM;
  }

  // flood fills one land mass onto another array
  void floodFillToNewArray(int x, int y, int[][] map, int[][] returns) {
    // if we've hit part of the land mass
    if (map[x][y] != 0) {
      returns[x][y] = 1;
      if (x - 1 >= 0) {
        if (map[x - 1][y] != 0 && returns[x - 1][y] != 1) {
          floodFillToNewArray(x - 1, y, map, returns);
        }
      }
      if (x + 1 < mapWidth) {
        if (map[x + 1][y] != 0 && returns[x + 1][y] != 1) {
          floodFillToNewArray(x + 1, y, map, returns);
        }
      }
      if (y - 1 >= 0) {
        if (map[x][y - 1] != 0 && returns[x][y - 1] != 1) {
          floodFillToNewArray(x, y - 1, map, returns);
        }
      }
      if (y + 1 < mapHeight) {
        if (map[x][y + 1] != 0 && returns[x][y + 1] != 1) {
          floodFillToNewArray(x, y + 1, map, returns);
        }
      }
    }
  }

  ArrayList<Corridor> getCorridors(int[][] corridorMap) {
    int[][] lmA = copyArray(corridorMap);
    ArrayList<Corridor> cor = new ArrayList<>();
    for (int j = mapHeight - 1; j >= 0; j--) {
      for (int i = mapWidth - 1; i >= 0; i--) {
        // for each point in our corridor array
        if (lmA[i][j] == 1) {
          // if we find a corridor, fill it out and return it as array
          int[][] tempC = initializeArray(mapWidth, mapHeight);
          floodFillToNewArray(i, j, lmA, tempC);
          // now figure out the corridor by iterating through the returned array
          int x1 = mapWidth - 1, x2 = 0, y1 = mapHeight - 1, y2 = 0, size = 0;
          // figure out the general shape, aiming to figure out if it is a line
          for (int l = mapHeight - 1; l >= 0; l--) {
            for (int k = mapWidth - 1; k >= 0; k--) {
              // x = k, y = l
              if (tempC[k][l] == 1) {
                size += 1;
                if (k < x1) {
                  x1 = k;
                }
                if (k > x2) {
                  x2 = k;
                }
                if (l < y1) {
                  y1 = l;
                }
                if (l > y2) {
                  y2 = l;
                }
              }
            }
          }
          Corridor c = new Corridor(x1, y1, x2, y2, size);
          purgeFromArray(tempC, lmA);
          cor.add(c);
        }
      }
    }
    return cor;
  }

  // gets a random landmass as an array
  int[][] getRandomLandmass(int[][] origMap) {
    boolean randomPointIsInLandmass = false;
    Point pointA = null;
    while (!randomPointIsInLandmass) {
      pointA = new Point(rand.nextInt(mapWidth), rand.nextInt(mapHeight));
      if (origMap[pointA.x][pointA.y] != 0) {
        randomPointIsInLandmass = true;
      }
    }
    int[][] map = initializeArray(mapWidth, mapHeight);
    floodFillToNewArray(pointA.x, pointA.y, origMap, map);
    return map;
  }

  int[][] rectCCToArray(RectCC r) {
    int[][] ret = initializeArray(mapWidth, mapHeight);
    for (int x = r.x1; x <= r.x2; x++) {
      for (int y = r.y1; y <= r.y2; y++) {
        ret[x][y] = 1;
      }
    }
    return ret;
  }
}
