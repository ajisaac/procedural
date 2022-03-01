package co.aisaac.procedural.dfofin.creation;

import java.util.Random;

public class Creation {

	//lower = more zoomed in
	private double landScale = 0.00009;
	private double mountainScale = .00006;
	private double heatScale = .0006;
	double riverScale = .001;
	double riverScale2 = .006;
	double riverPers = .4;
	double riverPers2 = .45;

	//threshold for mountains
	private double mountainHeight = .86;

	Random rand = new Random();
	OpenSimplexNoise o, oi, oRiver;

	public Creation() {
		long seed = rand.nextLong();
		o = new OpenSimplexNoise(seed);
		oRiver = new OpenSimplexNoise(rand.nextLong());
		System.out.println("Seed: " + seed);

	}

	public Chunk createChunk(int i, int j) {
		Chunk ch = new Chunk(i, j);
		for (int k = 0; k < S.chunkSize; k++) {
			for (int l = 0; l < S.chunkSize; l++) {
				byte island = isLand(((i * S.chunkSize) + k), (j * S.chunkSize) + l);
				byte ismountain = isMountain(((i * S.chunkSize) + k), (j * S.chunkSize) + l);
				double getmountaindata = getMountainData(((i * S.chunkSize) + k), (j * S.chunkSize) + l);
				byte getheatdata = getHeatData(((i * S.chunkSize) + k), (j * S.chunkSize) + l);
				byte getpercipdata = getPercipData(((i * S.chunkSize) + k), (j * S.chunkSize) + l);
				byte createriverdata = createRiverData((i * S.chunkSize) + k, (j * S.chunkSize) + l, getmountaindata,
													   getpercipdata);
				byte createbiomedata = createBiomeData(getheatdata, getpercipdata);
				ch.setIsLand(k, l, island);
				ch.setIsMountain(k, l, ismountain);
				ch.setElevationHeight(k, l, getmountaindata);
				ch.setHeat(k, l, getheatdata);
				ch.setPercip(k, l, getpercipdata);
				ch.setIsRiver(k, l, createriverdata);
				ch.setBiomeType(k, l, createbiomedata);
				if (island == 0 || createriverdata == 1) {//water
					ch.setTileType(k, l, S.tileWater);
				} else if (ismountain == 1) {//mountain
					ch.setTileType(k, l, S.tileMountains);
				} else if (createbiomedata == S.coldDesert || createbiomedata == S.hotDesert) {//desert
					ch.setTileType(k, l, S.tileDesert);
				} else if (createbiomedata == S.borealForest || createbiomedata == S.rainForest ||
						   createbiomedata == S.tempDeciduous || createbiomedata == S.tropicalDryForest) {//trees
					ch.setTileType(k, l, S.tileTree);
				} else
					ch.setTileType(k, l, S.tileGrass);

			}
		}
		return ch;
	}

	public byte isLand(int i, int j) {
		if (sumOctaveLandData(i, j, 10, .5, landScale) + .5 > S.waterLevel)
			return 1;
		else
			return 0;
	}
	private double sumOctaveLandData(double i, double j, int iter, double pers, double scale) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; ++k) {
			fnt += o.eval(i * scale, j * scale) * amp;
			maxAmp += amp;
			amp *= pers;
			scale *= 2.4;

		}
		fnt /= maxAmp;
		return fnt;

	}

	private byte isMountain(int i, int j) {
		double num = 0;
		num = sumOctaveMounData(i, j, 8, .53, mountainScale) + .5;
		if (num < .5)
			num = -num + 1;

		num = (num - .5) * 2;
		num = -num + 1;

		if (num > mountainHeight) {
			return 1;
		} else
			return 0;

	}
	private double getMountainData(int i, int j) {
		double num = 0;
		num = sumOctaveMounData(i, j, 8, .53, mountainScale) + .5;
		if (num < .5)
			num = -num + 1;

		num = (num - .5) * 2;
		num = -num + 1;

		return num;
	}
	private double sumOctaveMounData(double i, double j, int iter, double pers, double scale) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; ++k) {
			fnt += o.eval(i * scale, j * scale * 1.5) * amp;
			maxAmp += amp;
			amp *= pers;
			scale *= 2.4;

		}
		fnt /= maxAmp;
		return fnt;

	}

	public byte getHeatData(int i, int j) {
		double num;
		double hm;
		num = sumOctaveHeatData(i, j, 10, .6, heatScale) + .5;
		hm = normalizeIntToFloat(j % S.size, 0, S.size);
		hm += ((num - .5) * .2);

		if (hm > .5)
			hm = 1 - hm;
		return (byte) (Math.round(normalizeFloatToFloat((float) hm, 0, .5f, 0, 1) * 100));
	}
	public static float normalizeFloatToFloat(float j, float sourceMin, float sourceMax, float destMin,
											  float destMax) {
		return (destMax - destMin) / (sourceMax - sourceMin) * (j - sourceMax) + destMax;
	}
	public static float normalizeIntToFloat(int num, int sourceMin, int sourceMax) {
		return (float) ((1 / ((double) sourceMax - (double) sourceMin)) * (double) num);
	}
	private double sumOctaveHeatData(double i, double j, int iter, double pers, double scale) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; ++k) {
			fnt += o.eval(i * scale, j * scale) * amp;
			maxAmp += amp;
			amp *= pers;
			scale *= 2.4;

		}
		fnt /= maxAmp;
		return fnt;

	}

	private byte getPercipData(int i, int j) {
		//how wavy and spread out the colors will get. the closer to 0, the less wavy
		//the closer to 1, the more all over the place they get
		double distortion = .6;
		//how noisy is the map
		//closer to 0, blobier, closer to 1 jagged
		double pers = .6;
		//zoom percentage
		double scale = .0009;
		double num = 0;
		byte bnum = 0;
		double pm;

		num = (sumOctavePercipData(i, j, 8, pers, scale));

		if ((j / S.size) % 2 == 0) {
			pm = (float) normalizeIntToFloat(j % S.size, 0, S.size);
		} else
			pm = (float) normalizeIntToFloat(-(j % S.size) + S.size, 0, S.size);

		pm += ((num) * distortion);

		bnum = (byte) (Math.round((pm) * 100));
		if (bnum < 0) {
			bnum = 0;
		}
		if (bnum > 99) {
			bnum = 99;
		}
		return bnum;
	}
	private double sumOctavePercipData(double i, double j, int iter, double pers, double scale) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; ++k) {
			fnt += o.eval(i * scale, j * scale) * amp;
			maxAmp += amp;
			amp *= pers;
			scale *= 2;

		}
		fnt /= maxAmp;
		return fnt;

	}

	public byte createRiverData(int i, int j, double elevation, byte precip) {

		double riverNum = sumOctaveRiverData(i, j, 8, riverPers, riverScale) + .5;
		double riverSmallNum = sumOctaveRiverData2(i, j, 8, riverPers2, riverScale2) + .5;
		if (riverNum < .5)
			riverNum = -riverNum + 1;

		riverNum = (riverNum - .5) * 2;
		riverNum = -riverNum + 1;
		if (riverSmallNum < .5)
			riverSmallNum = -riverSmallNum + 1;

		riverSmallNum = (riverSmallNum - .5) * 2;
		riverSmallNum = -riverSmallNum + 1;

		double percip = ((double) precip) / 100;
		byte river = 0;
		percip = -(percip) + 1;
		if (riverNum > .92 + (.03 * percip) + (.05 * elevation) ||
			(riverNum > .73 + (.27 * percip) && riverSmallNum > .93 + (.06 * percip))) {
			river = 1;
		}
		return river;
	}
	private double sumOctaveRiverData(double i, double j, int iter, double pers, double scale) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; ++k) {
			fnt += oRiver.eval(i * scale, j * scale) * amp;
			maxAmp += amp;
			amp *= pers;
			scale *= 2.4;

		}
		fnt /= maxAmp;
		return fnt;

	}
	private double sumOctaveRiverData2(double i, double j, int iter, double pers, double scale) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; ++k) {
			fnt += oRiver.eval(i * scale, j * scale) * amp;
			maxAmp += amp;
			amp *= pers;
			scale *= 2.4;

		}
		fnt /= maxAmp;
		return fnt;

	}
	private byte createBiomeData(byte heat, byte precip) {
		//default settings

		if ((heat >= S.tundraHeatLow && heat < S.tundraHeatHigh) &&
			(precip >= S.tundraPercipLow && precip < S.tundraPercipHigh)) {
			return S.tundra;
		} else if ((heat >= S.coldDesertHeatLow && heat < S.coldDesertHeatHigh) &&
				   (precip >= S.coldDesertPercipLow && precip < S.coldDesertPercipHigh)) {
			return S.coldDesert;
		} else if ((heat >= S.hotDesertHeatLow && heat <= S.hotDesertHeatHigh) &&
				   (precip >= S.hotDesertPercipLow && precip < S.hotDesertPercipHigh)) {
			return S.hotDesert;
		} else if ((heat >= S.shrublandHeatLow && heat < S.shrublandHeatHigh) &&
				   (precip >= S.shrublandPercipLow && precip < S.shrublandPercipHigh)) {
			return S.shrubLand;
		} else if ((heat >= S.grassLandHeatLow && heat < S.grassLandHeatHigh) &&
				   (precip >= S.grassLandPercipLow && precip < S.grassLandPercipHigh)) {
			return S.grassland;
		} else if ((heat >= S.savannaHeatLow && heat <= S.savannaHeatHigh) &&
				   (precip >= S.savannaPercipLow && precip < S.savannaPercipHigh)) {
			return S.savanna;
		} else if ((heat >= S.borealForestHeatLow && heat < S.borealForestHeatHigh) &&
				   (precip >= S.borealForestPercipLow && precip < S.borealForestPercipHigh)) {
			return S.borealForest;
		} else if ((heat >= S.tempDeciduousForestHeatLow && heat < S.tempDeciduousForestHeatHigh) &&
				   (precip >= S.tempDeciduousForestPercipLow && precip < S.tempDeciduousForestPercipHigh)) {
			return S.tempDeciduous;
		} else if ((heat >= S.tropicalMontaneHeatLow && heat < S.tropicalMontaneHeatHigh) &&
				   (precip >= S.tropicalMontanePercipLow && precip < S.tropicalMontanePercipHigh)) {
			return S.tropicalMontane;
		} else if ((heat >= S.tropicalDryForestHeatLow && heat <= S.tropicalDryForestHeatHigh) &&
				   (precip >= S.tropicalDryForestPercipLow && precip < S.tropicalDryForestPercipHigh)) {
			return S.tropicalDryForest;
		} else if ((heat >= S.rainForestHeatLow && heat <= S.rainForestHeatHigh) &&
				   (precip >= S.rainForestPercipLow && precip < S.rainForestPercipHigh)) {
			return S.rainForest;
		} else
			return S.iceCap;
	}

	private double sumOctaveElevData(double i, double j, int iter, double pers, double freq) {

		double fnt = 0;
		double maxAmp = 0;
		double amp = 1;
		for (int k = 0; k < iter; k++) {
			fnt += o.eval(i * freq, j * freq) * amp;
			maxAmp += amp;
			amp *= pers;
			freq *= 2;

		}
		fnt /= maxAmp;
		return fnt;

	}
}
