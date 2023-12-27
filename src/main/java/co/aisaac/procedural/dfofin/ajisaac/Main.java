package co.aisaac.procedural.dfofin.ajisaac;

public class Main {
	public static void main(String[] args) {
		// the lower the offset, the closer the rooms will be together
		// the shorter the corridors will be
		// and the larger the rooms will be

		// min node size and offset are directly related
		// the higher the min node size, the less rooms there will be

		// if offset is larger than 10
		int mapWidth = 125, mapHeight = 100, offset = 2, seed = 42;
		int minNodeSize = 6; // advise setting this to 6 or above.
		final int voide = 0, wall = 1, floor = 2, corridor = 3, door = 4;

		for (int test = 0; test < 1; test++) {
			Dungeon dungeon = new Dungeon(mapWidth, mapHeight, minNodeSize, offset, seed);
			int[][] dungeonArray = dungeon.map;
			boolean f = true;
			if (f) {
				for (int j = mapHeight - 1; j >= 0; j--) {
					String s = ""; // + j;
					for (int i = mapWidth - 1; i >= 0; i--) {
						switch (dungeonArray[i][j]) {
							// corridors painted first
							case corridor:
								s += "C";
								break;
							// doors
							case door:
								s += "D";
								break;
							// walls secondly
							case wall:
								s += ";";
								break;
							// floors
							case floor:
								s += ";";
								break;
							// void
							case voide:
								s += " ";
								break;
							default:
								break;
						}
					}
					System.out.println(s);
				}
			}
		}
	}
}
