package co.aisaac.procedural.dfofin.ajisaac;

import java.util.ArrayList;
import java.util.Random;

public class BSPNode {

	// SETTINGS////t
	int smallestPartitionSize;
	int minNodeSize;
	double maxPartitionSizeRatio = 1;
	int vertOffset;
	int horiOffset;
	int level;
	int offset;

	// SETTINGS///
	Orientation splitOrientation;
	int nodeWidth, nodeHeight, x1, x2, y1, y2;
	ArrayList nodes;
	BSPNode left, right;
	Rect room = null;

	static Random rand;

	public enum Orientation {
		HORIZONTAL,
		VERTICAL
	}

	public BSPNode(
		int x1,
		int y1,
		int x2,
		int y2,
		ArrayList nodes,
		int minNodeSize,
		int level,
		int offset,
		Random rand) {

		this.rand = rand;
		this.level = level += 1;
		this.minNodeSize = minNodeSize;
		smallestPartitionSize = minNodeSize * 2;
		this.nodes = nodes;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		nodeWidth = x2 - x1;
		nodeHeight = y2 - y1;
		vertOffset = minNodeSize - offset;
		horiOffset = minNodeSize - offset;
		this.offset = offset;
	}

	// splits this node into two halves, then we'll create nodes
	// for those two children, then we partition for each of those
	// recursively till we've reached desired results
	public void partition() {
		// choose the axis along which we'll partition this node
		if ((double) nodeWidth / (double) nodeHeight > maxPartitionSizeRatio) {
			splitOrientation = Orientation.HORIZONTAL;
		} else if ((double) nodeHeight / (double) nodeWidth > maxPartitionSizeRatio) {
			splitOrientation = Orientation.VERTICAL;
		} else if (rand.nextBoolean()) {
			splitOrientation = Orientation.HORIZONTAL;
		} else {
			splitOrientation = Orientation.VERTICAL;
		}

		// Split the Node.
		if (splitOrientation == Orientation.HORIZONTAL) {
			// Pick the location along the XAxis (between the LeftEdge and RightEdge)
			// at which we will split.
			int splitLocation = x1 + homogenizedRandomValue(nodeWidth);

			// Create our two child nodes
			left = new BSPNode(x1, y1, splitLocation, y2, nodes, minNodeSize, this.level, offset, rand);
			right = new BSPNode(splitLocation, y1, x2, y2, nodes, minNodeSize, this.level, offset, rand);

			// partition child nodes if either is not yet too small
			if (weShouldSplit(splitLocation - x1)) { // if we should split the left node
				left.partition();
			} else {
				left.room = getRoom(left);
				nodes.add(left);
			}
			if (weShouldSplit(x2 - splitLocation)) {
				right.partition();
			} else {
				right.room = getRoom(right);
				nodes.add(right);
			}

		} else // Vertical split
		{
			// Pick the location along the YAxis (between the TopEdge and BottomEdge)
			// at which we will split.
			int splitLocation = y1 + homogenizedRandomValue(nodeHeight);

			// Create our two (Left = upper and Right = lower) child nodes
			left = new BSPNode(x1, y1, x2, splitLocation, nodes, minNodeSize, this.level, offset, rand);
			right = new BSPNode(x1, splitLocation, x2, y2, nodes, minNodeSize, this.level, offset, rand);

			// partition child nodes if either is not yet too small
			if (weShouldSplit(splitLocation - y1)) {
				left.partition();
			} else {
				left.room = getRoom(left);
				nodes.add(left);
			}
			if (weShouldSplit(y2 - splitLocation)) {
				right.partition();
			} else {
				right.room = getRoom(right);
				nodes.add(right);
			}
		}
	}

	private boolean weShouldSplit(double partitionSize) {
		return (partitionSize > smallestPartitionSize);
	}

	// lets get a split location that isn't right up on either wall
	private int homogenizedRandomValue(int width) {
		int num = 0;
		while (num < minNodeSize || num > width - minNodeSize) {
			num = rand.nextInt(width);
		}
		return num;
	}

	private Rect getRoom(BSPNode node) {
		int verticalOffset = node.nodeHeight - node.vertOffset;
		int horizontalOffset = node.nodeWidth - node.horiOffset;
		int y1Offset = rand.nextInt(verticalOffset),
			y2Offset = rand.nextInt(verticalOffset - y1Offset),
			x1Offset = rand.nextInt(horizontalOffset),
			x2Offset = rand.nextInt(horizontalOffset - x1Offset);

		Rect r = new Rect();
		r.y1 = node.y1 + y1Offset;
		r.y2 = node.y2 - y2Offset;
		r.x1 = node.x1 + x1Offset;
		r.x2 = node.x2 - x2Offset;
		r.width = r.x2 - r.x1;
		r.height = r.y2 - r.y1;
		return r;
	}
}
