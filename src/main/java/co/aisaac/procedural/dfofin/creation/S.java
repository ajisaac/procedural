package co.aisaac.procedural.dfofin.creation;

import javafx.scene.paint.Color;

// todo we want to move these to ENUM's or something
public class S {
	//basic settings
	/**
	 * must equal 2 to the X - ie 2,4,8,16,32
	 * good choices are 1024 2048 4096
	 */

	//public static final int size = 131072;
	//public static final int size = 65536;
	//public static final int size = 32768;
	//public static final int size = 16384;
	//public static final int size = 8192;
	//public static final int size = 4096;
	public static final int size = 2048;
	//public static final int size = 1024;
	//public static final int size = 512;
	//public static final int size = 256;
	//public static final int size = 128;
	//public static final int size = 64;
	//public static final int size = 32;
	//public static final int size = 16;
	public static final int chunkSize = 64;
	public static final int tileSize = 32;
	public static final long seed = 342893244;
	public static final int numOfRenderableChunksY = 3;
	public static final int numOfRenderableChunksX = 3;
	public static boolean randSeed = false;
	public static boolean loaded = false;

	public enum Direction {
		WEST,
		EAST,
		NORTH,
		SOUTH
	}

	;

	//tile types
	public static int tileGrass = 0;
	public static int tileDesert = 1;
	public static int tileMountains = 2;
	public static int tileWater = 3;
	public static int tileTree = 4;

	//desired feature percentage
	public static double desiredLandPercentage = .5;
	public static double waterLevel = .55;

	//switches for checks

	public static float min = .05f;
	public static float max = .05f;

	public static float highCut = 1f;
	public static float lowCut = 0f;

	public static float windMapOpacity = 1f;
	//map creation settings

	public static byte mountain = 2;
	public static byte water = 3;
	public static byte land = 4;
	public static byte river = 5;
	public static float landLower = -.2f;

	//biome ints
	public static byte iceCap = 0;
	public static byte tundra = 1;
	public static byte hotDesert = 2;
	public static byte shrubLand = 3;
	public static byte coldDesert = 4;
	public static byte grassland = 5;
	public static byte savanna = 6;
	public static byte borealForest = 7;
	public static byte tempDeciduous = 8;
	public static byte tropicalMontane = 9;
	public static byte tropicalDryForest = 10;
	public static byte rainForest = 11;

	//percip
	public static byte tundraPercipLow = 0;
	public static byte tundraPercipHigh = 100;
	public static byte coldDesertPercipLow = 0;
	public static byte coldDesertPercipHigh = 25;
	public static byte hotDesertPercipLow = 0;
	public static byte hotDesertPercipHigh = coldDesertPercipHigh;
	public static byte shrublandPercipLow = 0;
	public static byte shrublandPercipHigh = coldDesertPercipHigh;
	public static byte grassLandPercipLow = coldDesertPercipHigh;
	public static byte grassLandPercipHigh = 45;
	public static byte savannaPercipLow = coldDesertPercipHigh;
	public static byte savannaPercipHigh = 45;
	public static byte borealForestPercipLow = grassLandPercipHigh;
	public static byte borealForestPercipHigh = 100;
	public static byte tempDeciduousForestPercipLow = grassLandPercipHigh;
	public static byte tempDeciduousForestPercipHigh = 70;
	public static byte tropicalMontanePercipLow = tempDeciduousForestPercipHigh;
	public static byte tropicalMontanePercipHigh = 100;
	public static byte tropicalDryForestPercipLow = savannaPercipHigh;
	public static byte tropicalDryForestPercipHigh = 70;
	public static byte rainForestPercipLow = tropicalDryForestPercipHigh;
	public static byte rainForestPercipHigh = 100;

	//temps
	public static byte tundraHeatLow = 0;
	public static byte tundraHeatHigh = 15;
	public static byte coldDesertHeatLow = tundraHeatHigh;
	public static byte coldDesertHeatHigh = 35;
	public static byte shrublandHeatLow = coldDesertHeatHigh;
	public static byte shrublandHeatHigh = 70;
	public static byte hotDesertHeatLow = shrublandHeatHigh;
	public static byte hotDesertHeatHigh = 100;
	public static byte grassLandHeatLow = tundraHeatHigh;
	public static byte grassLandHeatHigh = 70;
	public static byte savannaHeatLow = grassLandHeatHigh;
	public static byte savannaHeatHigh = 100;
	public static byte borealForestHeatLow = tundraHeatHigh;
	public static byte borealForestHeatHigh = 40;
	public static byte tempDeciduousForestHeatLow = borealForestHeatHigh;
	public static byte tempDeciduousForestHeatHigh = 70;
	public static byte tropicalMontaneHeatLow = borealForestHeatHigh;
	public static byte tropicalMontaneHeatHigh = 70;
	public static byte tropicalDryForestHeatLow = tempDeciduousForestHeatHigh;
	public static byte tropicalDryForestHeatHigh = 100;
	public static byte rainForestHeatLow = tropicalMontaneHeatHigh;
	public static byte rainForestHeatHigh = 100;

	//colors
	public static Color mountainColor = Color.DARKGRAY;
	public static Color landColor = new Color(0.2f, 0.4f, 0, 1);
	public static Color waterColor = new Color(.2f, .2f, 1, 1);
	public static Color riverColor = new Color(.3f, .3f, 1, 1);
	//public static Color riverColor = new Color(Color.PINK);

	public static Color tundraColor = new Color(0.902f, 0.902f, 0.980f, 1); //lavender
	public static Color hotDesertColor = new Color(1, .271f, 0, 1);//orangered
	public static Color shrubLandColor = new Color(.502f, .502f, 0, 1);//olive
	public static Color coldDeserColor = new Color(1, 0.922f, 0.804f, 1);//blanched almond
	public static Color grasslandColor = new Color(0.855f, 0.647f, 0.125f, 1);//goldenrod
	public static Color savannaColor = new Color(0.627f, 0.322f, 0.176f, 1);//sienna
	public static Color borealForestColor = new Color(0, 0.392f, 0, 1);//dark green
	public static Color tempDeciduousColor = new Color(0.420f, 0.557f, 0.137f, 1);//olive drab
	public static Color tropicalMontaneColor = new Color(0.333f, 0.420f, 0.184f, 1);//dark olive green
	public static Color tropicalDryForestColor = new Color(0.678f, 1, 0.184f, 1);//green yellow
	public static Color rainForestColor = new Color(0, 1, 0.498f, 1);//spring green
	public static Color iceCapColor = new Color(0.439f, 0.502f, 0.565f, 1);//slate gray
	public static String gameName;
}
