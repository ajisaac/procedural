package co.aisaac.procedural.dfofin.ajisaac; // test

import java.util.ArrayList;
import java.util.Random;

/**
 * @author aaron
 */
public class Dungeon {
	int mapWidth, mapHeight, minNodeSize;
	int[][] map, corridorMap;
	int voide = 0;
	int wall = 1;
	int floor = 2;
	int corridor = 3;
	int door = 4;
	BSPNode rootNode;
	private static ArrayList<BSPNode> nodes;
	private static ArrayList<Corridor> corridors;
	private static Random rand;
	private Utilities util;

	Dungeon(int mapWidth, int mapHeight, int minNodeSize, int offset, int seed) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.minNodeSize = minNodeSize;
		map = new int[mapWidth][mapHeight];
		corridorMap = new int[mapWidth][mapHeight];
		rand = new Random();
		nodes = new ArrayList<>();
		util = new Utilities(mapWidth, mapHeight, rand);

		// create root node
		rootNode = new BSPNode(0, 0, mapWidth - 1, mapHeight - 1, nodes, minNodeSize, 1, offset, rand);
		// partition everything out
		rootNode.partition();

		// ArrayList nodes only contains the nodes that need rooms
		// createRoomsByteMap will take all those nodes and give them a rectangle
		map = createRoomsByteMap(nodes, mapWidth, mapHeight);

		// util.printArray(map);
		map = connectEverything(map);
	}

	Dungeon(int mapWidth, int mapHeight, int minNodeSize, int offset) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.minNodeSize = minNodeSize;
		map = new int[mapWidth][mapHeight];
		corridorMap = new int[mapWidth][mapHeight];
		rand = new Random();
		nodes = new ArrayList<>();
		util = new Utilities(mapWidth, mapHeight, rand);

		// create root node
		rootNode = new BSPNode(0, 0, mapWidth - 1, mapHeight - 1, nodes, minNodeSize, 1, offset, rand);
		// partition everything out
		rootNode.partition();

		// ArrayList nodes only contains the nodes that need rooms
		// createRoomsByteMap will take all those nodes and give them a rectangle
		map = createRoomsByteMap(nodes, mapWidth, mapHeight);

		// util.printArray(map);
		map = connectEverything(map);
	}

	// creates an array of rooms from nodes
	private int[][] createRoomsByteMap(ArrayList<BSPNode> nodess, int mapWidth, int mapHeight) {
		int[][] map = new int[mapWidth][mapHeight];

		// initialize the map as a blank slate of voidness, represented by 0
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				map[i][j] = 0; // VOID
			}
		}

		for (int i = 0; i < nodess.size(); i++) {
			BSPNode node = nodess.get(i);
			Rect r = node.room;
			for (int j = r.x1; j <= r.x2; j++) {
				for (int k = r.y1; k <= r.y2; k++) {
					map[j][k] = floor;
				}
			}
		}
		// now our map of ints should be good to go
		return map;
	}

	// connects all the rooms in the map to each other
	private int[][] connectEverything(int[][] map) {
		// int[][] newMap = util.initializeArray(mapWidth, mapHeight);

		int[][] origMap = util.copyArray(map);
		// get a room from map - lma
		int[][] lmA = util.getRandomLandmass(origMap);
		// newMap = util.mergeArrays(newMap, lmA);
		// remove lma from oldMap
		origMap = util.purgeFromArray(lmA, origMap);

		while (!util.ArrayIsEmpty(origMap)) {
			// util.printArray(origMap);
			// expand lma in all directions to find shortest path to any lmb
			LandMass lmB = hitFirst(lmA, origMap, rand);
			if (lmB == null) {
				break;
			}
			// connect lmb to lma and add corridor on lma
			lmA = util.mergeArrays(lmB.getLandMassArray(), lmA);
			// remove lmb from origMap
			origMap = util.purgeFromArray(lmB.getLandMassArrayWithoutRect(), origMap);
		}
		// just corridors is an array of just the corridor sections of the map
		int[][] justCorridors = util.purgeFromArray(map, lmA);
		// Now we're going to go through and make a list of the corridor objects
		corridors = util.getCorridors(justCorridors);

		ArrayList<Point> doors = new ArrayList<>();
		for (Corridor cor : corridors) {
			// System.out.println(cor.x1 + ", " + cor.y1 + ", " + cor.x2 + ", " + cor.y2);
			for (Point p : cor.getDoors()) {
				doors.add(p);
			}
		}

		int[][] corridorsAndDoors = util.copyArray(justCorridors);
		for (Point p : doors) {
			corridorsAndDoors[p.x][p.y] = 4;
		}
    /*util.printArray(lmA);
    util.printArray(map);
    util.printArray(justCorridors);
    util.printArray(corridorsAndDoors);*/
		// we get all of the floors as 2's
		int[][] f = util.copyArray(map);
		// if it is a corridor, paint over f
		for (int j = mapHeight - 1; j >= 0; j--) {
			for (int i = mapWidth - 1; i >= 0; i--) {
				if (justCorridors[i][j] == 1) {
					f[i][j] = 3;
				}
				if (corridorsAndDoors[i][j] == 4) {
					f[i][j] = 4;
				}
			}
		}

		ArrayList<Corridor> sCorridors = new ArrayList<>();
		for (Corridor c : corridors) {
			// if the corridor is 4 or longer and straight line
			if (c.size > 3 && c.threeOrMoreStraightLine == true) {
				if (c.northSouth) {
					System.out.println("NorthSouth");
					boolean corridorRight = true;
					// x doesn't change y does
					for (int i = c.x1; i < c.x2; i++) {
						for (int j = c.y1; j <= c.y2; j++) {
							if (j + 1 >= mapWidth) {
								corridorRight = false;
							} else if (f[i][j + 1] != 0) {
								corridorRight = false;
							}
						}
					}
					if (corridorRight) {
						// because the map is flipped somewhere in the process, or
						// because we're reading arrays backwards, -1 is really to the right
						sCorridors.add(new Corridor(c.x1 - 1, c.y1, c.x2 - 1, c.y2, c.size));
					}
				} else if (c.eastWest) {
					System.out.println("EastWest");
					boolean corridorSouth = true;
					// x doesn't change y does
					for (int i = c.x1; i < c.x2; i++) {
						for (int j = c.y1; j <= c.y2; j++) {

							if (i - 1 <= 0) {
								corridorSouth = false;
							} else if (f[i - 1][j] != 0) {
								corridorSouth = false;
							}
						}
					}
					if (corridorSouth) {
						System.out.println("True");
						// because the map is flipped somewhere in the process, or
						// because we're reading arrays backwards, -1 is really to the right
						sCorridors.add(new Corridor(c.x1, c.y1 + 1, c.x2, c.y2 + 1, c.size));
					}
				}
			}
		}

		// our new map of our new corridors and doors
		int[][] sCorridorsAndDoors = util.initializeArray(mapWidth, mapHeight);
		// get all the new doors from the secondary corridors
		for (Corridor cor : sCorridors) {
			// paint the corridors
			for (int i = cor.x1; i <= cor.x2; i++) {
				for (int j = cor.y1; j <= cor.y2; j++) {
					sCorridorsAndDoors[i][j] = 3;
				}
			}
			for (Point p : cor.getDoors()) {
				sCorridorsAndDoors[p.x][p.y] = 4;
			}
		}
		// util.printArray(sCorridorsAndDoors);

		for (int j = mapHeight - 1; j >= 0; j--) {
			for (int i = mapWidth - 1; i >= 0; i--) {
				if (sCorridorsAndDoors[i][j] != 0) {
					f[i][j] = sCorridorsAndDoors[i][j];
				}
			}
		}
		// get all the corridors as a map

		// util.printArray(f);

		return f;
	}

	private LandMass hitFirst(int[][] self, int[][] orig, Random rand) {
		RectCC r = null;

		// get all the points
		ArrayList<Point> points = new ArrayList<>();
		for (int j = mapHeight - 1; j >= 0; j--) {
			for (int i = mapWidth - 1; i >= 0; i--) {
				if (self[i][j] != 0) {
					points.add(new Point(i, j));
				}
			}
		}

		int sDistance = 0; // the current shortest distance

		// we'll go through every point in the landmass
		for (int i = 0; i < points.size(); i++) {
			// lets get a point that is in the lmB landmass
			Point p = points.get(i);

			// return a rectCC if we successfully hit something
			RectCC N = expandPoint(p, self, orig, "N");
			RectCC S = expandPoint(p, self, orig, "S");
			RectCC E = expandPoint(p, self, orig, "E");
			RectCC W = expandPoint(p, self, orig, "W");

			RectCC shortRect = getShortestRect(N, S, E, W);

			// if we found a shorter corridor, lets make it r
			if (shortRect != null) {
				// if the first time we've found a decent corridor
				if (sDistance == 0) {
					sDistance = shortRect.distance;
					r = shortRect;
				}
				// else if this is the shortest distance,
				else if (sDistance > shortRect.distance) {
					sDistance = shortRect.distance;
					r = shortRect;
				}
			}
		}
		// create a landmass
		if (r != null) {
			Point p = null;
			r.organizeRectCC();
			for (int x = r.x1; x <= r.x2; x++) {
				for (int y = r.y1; y <= r.y2; y++) {
					if (orig[x][y] != 0) {
						p = new Point(x, y);
					}
				}
			}
			if (p != null) {
				LandMass l = new LandMass(mapWidth, mapHeight, r, p, orig, rand);
				return l;
			}
			return null;
		}
		return null;
	}

	private RectCC getShortestRect(RectCC N, RectCC S, RectCC E, RectCC W) {
		// check to see if N is shortest
		int shortest = 0;
		int n = 999999999, w = 999999999, e = 999999999, s = 999999999;
		if (N != null) {
			n = N.distance;
		}
		if (S != null) {
			s = S.distance;
		}
		if (E != null) {
			e = E.distance;
		}
		if (W != null) {
			w = W.distance;
		}
		// if n is greatest
		shortest = n;
		if (w < shortest) {
			shortest = w;
		}
		if (s < shortest) {
			shortest = s;
		}
		if (e < shortest) {
			shortest = e;
		}

		if (n == shortest) {
			return N;
		} else if (e == shortest) {
			return E;
		} else if (w == shortest) {
			return W;
		} else if (s == shortest) {
			return S;
		}

		return null;
	}

	private RectCC expandPoint(Point p, int[][] self, int[][] orig, String dir) {
    /*start at p, go north and if we only hit void, keep going north
    until we hit a non void on cantCorridor or lmA*/
		int y = p.y;
		int x = p.x;
		int dirIntX = 0, dirIntY = 0;
		if (null != dir)
			switch (dir) {
				case "N":
					dirIntY = 1;
					break;
				case "S":
					dirIntY = -1;
					break;
				case "E":
					dirIntX = 1;
					break;
				case "W":
					dirIntX = -1;
					break;
				default:
					break;
			}

		boolean hitSelf = false;
		while (!hitSelf) {
			if (null != dir) // if we hit the north wall
				switch (dir) {
					case "N":
						if (y + 1 == mapHeight) {
							return null;
						}
						break;
					case "S":
						if (y - 1 == 0) {
							return null;
						}
						break;
					case "E":
						if (x + 1 == mapWidth) {
							return null;
						}
						break;
					case "W":
						if (x - 1 == 0) {
							return null;
						}
						break;
					default:
						break;
				}

			if (x + dirIntX < 0 || x + dirIntX >= mapWidth) {
				return null;
			}
			if (y + dirIntY < 0 || y + dirIntY >= mapHeight) {
				return null;
			}

			// if we hit ourself
			if (self[x + dirIntX][y + dirIntY] != 0) {
				return null;
			}
			// if cantCorridor contains something, return a rectCC
			if (orig[x + dirIntX][y + dirIntY] != 0) {
				RectCC r = new RectCC();
				r.x1 = p.x;
				r.y1 = p.y;
				r.x2 = x + dirIntX;
				r.y2 = y + dirIntY;
				r.organizeRectCC();
				int xx = Math.abs(r.x2 - r.x1);
				int yy = Math.abs(r.y2 - r.y1);
				if (xx > yy) {
					r.distance = xx;
				}
				if (yy > xx) {
					r.distance = yy;
				}
				return r;
			}

			switch (dir) {
				case "N":
					y += 1;
					break;
				case "S":
					y -= 1;
					break;
				case "E":
					x += 1;
					break;
				case "W":
					x -= 1;
					break;
				default:
					break;
			}
		}
		return null;
	}
}
