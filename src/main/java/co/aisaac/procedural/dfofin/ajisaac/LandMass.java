package co.aisaac.procedural.dfofin.ajisaac; /*
                      * To change this license header, choose License Headers in Project Properties.
                      * To change this template file, choose Tools | Templates
                      * and open the template in the editor.
                      */

import java.util.Random;

/** @author aaron */
public class LandMass {
  static Utilities util;
  RectCC r = null;
  int[][] lmWoRect, lmWRect;

  public LandMass(int mapWidth, int mapHeight, RectCC r, Point p, int[][] orig, Random rand) {
    util = new Utilities(mapWidth, mapHeight, rand);
    lmWoRect = util.initializeArray(mapWidth, mapHeight);
    lmWRect = util.initializeArray(mapWidth, mapHeight);

    util.floodFillToNewArray(p.x, p.y, orig, lmWoRect);
    int[][] rect = util.rectCCToArray(r);
    lmWRect = util.mergeArrays(rect, lmWoRect);
  }

  public int[][] getLandMassArray() {
    return lmWRect;
  }

  public int[][] getLandMassArrayWithoutRect() {
    return lmWoRect;
  }
}
