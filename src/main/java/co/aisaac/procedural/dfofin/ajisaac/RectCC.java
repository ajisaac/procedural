package co.aisaac.procedural.dfofin.ajisaac;

public class RectCC {
	int x1, y1, x2, y2, width, height, distance;

	public RectCC() {
	}

	public void organizeRectCC() {
		if (x1 == x2) {
			if (y2 < y1) {
				int y = y2;
				y2 = y1;
				y1 = y;
			}
		} else if (y1 == y2) {
			if (x2 < x1) {
				int x = x2;
				x2 = x1;
				x1 = x;
			}
		}
	}
}
