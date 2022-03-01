package co.aisaac.procedural.dfofin.creation;

public class Chunk{
	private int size = S.chunkSize;
	private double[][] elevationData;
	private byte[][] heatData, percipData, riverData, biomeData, isLandData, isMountainData;
	private int[][] tileType;
	private int worldCoordOfChunkX, worldCoordOfChunkY;
	public Chunk(int x, int y){
		this.worldCoordOfChunkX = x;
		this.worldCoordOfChunkY = y;
		elevationData = new double[S.chunkSize][S.chunkSize];
		heatData = new byte[S.chunkSize][S.chunkSize];
		percipData = new byte[S.chunkSize][S.chunkSize];
		riverData = new byte[S.chunkSize][S.chunkSize];
		biomeData = new byte[S.chunkSize][S.chunkSize];
		isLandData = new byte[S.chunkSize][S.chunkSize];
		isMountainData = new byte[S.chunkSize][S.chunkSize];
		tileType = new int[S.chunkSize][S.chunkSize];
		
	}

	public int getX() {
		return worldCoordOfChunkX;
	}
	public int getY() {
		return worldCoordOfChunkY;
	}
	public void setIsLand(int i, int j, byte isLand) {
		isLandData[i][j] = isLand;
	}
	public void setIsMountain(int i, int j, byte isMountain) {
		isMountainData[i][j] = isMountain;
	}
	public void setElevationHeight(int i, int j, double getmountaindata) {
		elevationData[i][j] = getmountaindata;
	}
	public void setHeat(int i, int j, byte getheatdata) {
		heatData[i][j] = getheatdata;
		
	}
	public void setPercip(int i, int j, byte getpercipdata) {
		percipData[i][j] = getpercipdata;
		
	}
	public void setIsRiver(int i, int j, byte createriverdata) {
		riverData[i][j] = createriverdata;
		
	}
	public void setBiomeType(int i, int j, byte createbiomedata) {
		biomeData[i][j] = createbiomedata;
		
	}
	public void setTileType(int i, int j, int tiletype) {
		tileType[i][j] = tiletype;
		
	}
	public int getTileType(int i, int j){
		return tileType[i][j];
	}
}
