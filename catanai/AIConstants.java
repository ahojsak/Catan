package catanai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import catanui.BoardObject;

public interface AIConstants {
	// Structure of board components.
	public final int MAX_SIDES = 6;
	public final int NUM_ENDS = 2;
	public final int MAX_ADJ_TILES = 3;
	// Structure of overall board.
	public final int NUM_VERTICES = 54;
	public final int NUM_EDGES = 72;
	public final int NUM_TILES = 19;
	public final int NUM_VERTICES_EXP = 96;
	public final int NUM_EDGES_EXP = 144;
	public final int NUM_TILES_EXP = 37;
	public final int CEIL_X = 5;
	public final int FLOOR_X = 0;
	public final int CEIL_Y = 5;
	public final int FLOOR_Y = 0;
	public final int CEIL_Z = 3;
	public final int FLOOR_Z = -2;
	public final int CEIL_X_EXP = 7;
	public final int FLOOR_X_EXP = 0;
	public final int CEIL_Y_EXP = 7;
	public final int FLOOR_Y_EXP = 0;
	public final int CEIL_Z_EXP = 4;
	public final int FLOOR_Z_EXP = -3;
	public final int DIM_X = 0;
	public final int DIM_Y = 1;
	public final int DIM_Z = 2;
	@SuppressWarnings("serial")
	public static final HashSet<BoardCoordinate> VALID_VERTS = new HashSet<BoardCoordinate>() {{
		Stack<BoardCoordinate> toTraverse = new Stack<BoardCoordinate>();
		BoardCoordinate current, next;
		// Start at the origin.
		toTraverse.push(BoardCoordinate.ORIGIN);
		while (this.size() <= NUM_VERTICES && ! toTraverse.isEmpty()) {
			current = toTraverse.pop();
			// Add all valid adjacent vertices to the stack.
			for (int i = DIM_X; i <= DIM_Z; i++) {
				next = current.moveIn(i, true);
				if (next != null && ! this.contains(next)) toTraverse.push(next);
				else {
					next = current.moveIn(i, false);
					if (next != null && ! this.contains(next)) toTraverse.push(next);
				}
			}
			// Add the current vertex to the set of valid ones.
			add(current);
		}
	}};
	@SuppressWarnings("serial")
	public static final HashSet<BoardCoordinate> VALID_VERTS_EXP = new HashSet<BoardCoordinate>() {{
		Stack<BoardCoordinate> toTraverse = new Stack<BoardCoordinate>();
		BoardCoordinate current, next;
		// Start at the origin.
		toTraverse.push(BoardCoordinate.ORIGIN);
		while (this.size() <= NUM_VERTICES_EXP && ! toTraverse.isEmpty()) {
			current = toTraverse.pop();
			// Add all valid adjacent vertices to the stack.
			for (int i = DIM_X; i <= DIM_Z; i++) {
				next = current.moveInExp(i, true);
				if (next != null && ! this.contains(next)) toTraverse.push(next);
				else {
					next = current.moveInExp(i, false);
					if (next != null && ! this.contains(next)) toTraverse.push(next);
				}
			}
			// Add the current vertex to the set of valid ones.
			add(current);
		}
	}};
	@SuppressWarnings("serial")
	public static final Map<Integer, List<Integer>> X_GROUPS = new HashMap<Integer, List<Integer>>() {{
		put(0, new ArrayList<Integer>(Arrays.asList(25, 26, 27, 38, 39, 47, 48)));
		put(1, new ArrayList<Integer>(Arrays.asList(14, 15, 16, 28, 29, 40, 41, 49, 50)));
		put(2, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 17, 18, 30, 31, 42, 43, 51, 52)));
		put(3, new ArrayList<Integer>(Arrays.asList(0, 5, 4, 7, 19, 20, 32, 33, 44, 45, 53)));
		put(4, new ArrayList<Integer>(Arrays.asList(6, 9, 8, 11, 21, 22, 34, 35, 46)));
		put(5, new ArrayList<Integer>(Arrays.asList(10, 13, 12, 24, 23, 37, 36)));
	}};
	@SuppressWarnings("serial")
	public static final Map<Integer, List<Integer>> Y_GROUPS = new HashMap<Integer, List<Integer>>() {{
		put(0, new ArrayList<Integer>(Arrays.asList(0, 1, 2, 14, 15, 25, 26)));
		put(1, new ArrayList<Integer>(Arrays.asList(6, 5, 4, 3, 17, 16, 28, 27, 38)));
		put(2, new ArrayList<Integer>(Arrays.asList(10, 9, 8, 7, 19, 18, 30, 29, 40, 39, 47)));
		put(3, new ArrayList<Integer>(Arrays.asList(13, 12, 11, 21, 20, 32, 31, 42, 41, 49, 48)));
		put(4, new ArrayList<Integer>(Arrays.asList(24, 23, 22, 34, 33, 44, 43, 51, 50)));
		put(5, new ArrayList<Integer>(Arrays.asList(37, 36, 35, 46, 45, 53, 52)));
	}};
	@SuppressWarnings("serial")
	public static final Map<Integer, List<Integer>> Z_GROUPS = new HashMap<Integer, List<Integer>>() {{
		put(-2, new ArrayList<Integer>(Arrays.asList(1, 0, 5, 6, 9, 10, 13)));
		put(-1, new ArrayList<Integer>(Arrays.asList(2, 3, 4, 7, 8, 11, 12, 24, 14)));
		put(0, new ArrayList<Integer>(Arrays.asList(25, 15, 16, 17, 18, 19, 20, 21, 22, 23, 37)));
		put(1, new ArrayList<Integer>(Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)));
		put(2, new ArrayList<Integer>(Arrays.asList(38, 39, 40, 41, 42, 43, 44, 45, 46)));
		put(3, new ArrayList<Integer>(Arrays.asList(47, 48, 49, 50, 51, 52, 53)));
	}};
	@SuppressWarnings("serial")
	public static final Map<Integer, List<Integer>> X_GROUPS_EXP = new HashMap<Integer, List<Integer>>() {{
		put(0, new ArrayList<Integer>(Arrays.asList(46, 47, 48, 63, 64, 76, 77, 87, 88)));
		put(1, new ArrayList<Integer>(Arrays.asList(31, 32, 33, 49, 50, 65, 66, 78, 79, 89, 90)));
		put(2, new ArrayList<Integer>(Arrays.asList(18, 19, 20, 34, 35, 51, 52, 67, 68, 80, 81, 91, 92)));
		put(3, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 21, 22, 36, 37, 53, 54, 69, 70, 82, 83, 93, 94)));
		put(4, new ArrayList<Integer>(Arrays.asList(0, 5, 4, 7, 23, 24, 38, 39, 55, 56, 71, 72, 84, 85, 95)));
		put(5, new ArrayList<Integer>(Arrays.asList(6, 9, 8, 11, 25, 26, 40, 41, 57, 58, 73, 74, 86)));
		put(6, new ArrayList<Integer>(Arrays.asList(10, 13, 12, 15, 27, 28, 42, 43, 59, 60, 75)));
		put(7, new ArrayList<Integer>(Arrays.asList(14, 17, 16, 30, 29, 45, 44, 62, 61)));
	}};
	@SuppressWarnings("serial")
	public static final Map<Integer, List<Integer>> Y_GROUPS_EXP = new HashMap<Integer, List<Integer>>() {{
		put(0, new ArrayList<Integer>(Arrays.asList(0, 1, 2, 18, 19, 31, 32, 46, 47)));
		put(1, new ArrayList<Integer>(Arrays.asList(6, 5, 4, 3, 21, 20, 34, 33, 49, 48, 63)));
		put(2, new ArrayList<Integer>(Arrays.asList(10, 9, 8, 7, 23, 22, 36, 35, 51, 50, 65, 64, 76)));
		put(3, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11, 25, 24, 38, 37, 53, 52, 67, 66, 78, 77, 87)));
		put(4, new ArrayList<Integer>(Arrays.asList(17, 16, 15, 27, 26, 40, 39, 55, 54, 69, 68, 80, 79, 89, 88)));
		put(5, new ArrayList<Integer>(Arrays.asList(30, 29, 28, 42, 41, 57, 56, 71, 70, 82, 81, 91, 90)));
		put(6, new ArrayList<Integer>(Arrays.asList(45, 44, 43, 59, 58, 73, 72, 84, 83, 93, 92)));
		put(7, new ArrayList<Integer>(Arrays.asList(62, 61, 60, 75, 74, 86, 85, 95, 94)));
	}};
	@SuppressWarnings("serial")
	public static final Map<Integer, List<Integer>> Z_GROUPS_EXP = new HashMap<Integer, List<Integer>>() {{
		put(-3, new ArrayList<Integer>(Arrays.asList(1, 0, 5, 6, 9, 10, 13, 14, 17)));
		put(-2, new ArrayList<Integer>(Arrays.asList(18, 2, 3, 4, 7, 8, 11, 12, 15, 16, 30)));
		put(-1, new ArrayList<Integer>(Arrays.asList(31, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 45)));
		put(0, new ArrayList<Integer>(Arrays.asList(46, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 62)));
		put(1, new ArrayList<Integer>(Arrays.asList(47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61)));
		put(2, new ArrayList<Integer>(Arrays.asList(63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75)));
		put(3, new ArrayList<Integer>(Arrays.asList(76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86)));
		put(4, new ArrayList<Integer>(Arrays.asList(87, 88, 89, 90, 91, 92, 93, 94, 95)));
	}};
	// Development cards
	@SuppressWarnings("serial")
	public static final Map<DevCard, Integer> DEV_VP_VALUE = new HashMap<DevCard, Integer>() {{
		put(DevCard.Market, 1);
		put(DevCard.Palace, 2);
		put(DevCard.University, 2);
	}};
	@SuppressWarnings("serial")
	public static final Map<DevCard, Double> DEV_FREQ = new HashMap<DevCard, Double>() {{
		put(DevCard.Knight, 0.3);
		put(DevCard.Market, 0.1);
		put(DevCard.Palace, 0.1);
		put(DevCard.University, 0.1);
		put(DevCard.Monopoly, 0.1);
		put(DevCard.RoadBuilding, 0.15);
		put(DevCard.YearOfPlenty, 0.15);
	}};
	// Die rolls
	public final double DIE_FREQ[] = {0, 0, 1.0 / 36.0, 1.0 / 18.0, 3.0 / 36.0, 1.0 / 9.0, 5.0 / 36.0, 1.0 / 6.0, 5.0 / 36.0, 1.0 / 9.0, 3.0 / 36.0, 1.0 / 18.0, 1.0 / 36.0};
	// Scoring
	public final int VP_LONG_ROAD = 2;
	public final int VP_LARG_ARMY = 2;
	public final int VP_SETTLEMENT = 1;
	public final int VP_CITY = 2;
	// Resource constants
	@SuppressWarnings("serial")
	public static final Hashtable<TileType, Resource> TILE_RES = new Hashtable<TileType, Resource>() {{
		put(TileType.Brick, Resource.Brick);
		put(TileType.Ore, Resource.Ore);
		put(TileType.Sheep, Resource.Sheep);
		put(TileType.Timber, Resource.Timber);
		put(TileType.Wheat, Resource.Wheat);
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<Resource, BoardObject.type> RES_CONV = new Hashtable<Resource, BoardObject.type>() {{
		put(Resource.Brick, BoardObject.type.BRICK);
		put(Resource.Wheat, BoardObject.type.WHEAT);
		put(Resource.Ore, BoardObject.type.ORE);
		put(Resource.Sheep, BoardObject.type.SHEEP);
		put(Resource.Timber, BoardObject.type.WOOD);
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<BoardObject.type, Resource> RES_C_REV = new Hashtable<BoardObject.type, Resource>() {{
		put(BoardObject.type.BRICK, Resource.Brick);
		put(BoardObject.type.WHEAT, Resource.Wheat);
		put(BoardObject.type.ORE, Resource.Ore);
		put(BoardObject.type.SHEEP, Resource.Sheep);
		put(BoardObject.type.WOOD, Resource.Timber);
	}};
	public final int SETT_PAYOUT = 1;
	public final int CITY_PAYOUT = 2;
	public final int YOP_PAYOUT = 2;
	public final int DEF_SWAP_RATIO = 4;
	public final int UNK_SWAP_RATIO = 3;
	public final int SP_SWAP_RATIO = 2;
	public final int BRICK_CITY = 0;
	public final int ORE_CITY = 3;
	public final int SHEEP_CITY = 0;
	public final int TIMBER_CITY = 0;
	public final int WHEAT_CITY = 2;
	public final int BRICK_SETTLEMENT = 1;
	public final int ORE_SETTLEMENT = 0;
	public final int SHEEP_SETTLEMENT = 1;
	public final int TIMBER_SETTLEMENT = 1;
	public final int WHEAT_SETTLEMENT = 1;
	public final int BRICK_ROAD = 1;
	public final int ORE_ROAD = 0;
	public final int SHEEP_ROAD = 0;
	public final int TIMBER_ROAD = 1;
	public final int WHEAT_ROAD = 0;
	public final int BRICK_DEV = 0;
	public final int ORE_DEV = 1;
	public final int SHEEP_DEV = 1;
	public final int TIMBER_DEV = 0;
	public final int WHEAT_DEV = 1;
	// Other AI constants
	public final int LOOKAHEAD_RANGE = 0;
	public final double HEURISTIC_MULT = 1.1;
	public final int GOAL_RADIUS = 3;
	public final int MAX_PATH_LENGTH = 5;
	public final int SETTLE_CEIL = 5;
	public final int MAX_PEND_TRADE = 1;
	public final long PLAY_DELAY = 3300;
}
