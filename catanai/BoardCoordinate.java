package catanai;

public class BoardCoordinate implements AIConstants {
	public static final BoardCoordinate ORIGIN = new BoardCoordinate(0, 0, 0);
	private int _x, _y, _z;
	
	public BoardCoordinate(int x, int y, int z) {
		_x = x;
		_y = y;
		_z = z;
	}
	
	public int x() {
		return _x;
	}
	
	public int y() {
		return _y;
	}
	
	public int z() {
		return _z;
	}
	
	// distance: Returns minimum number of edges between this and other.
	public int distance(BoardCoordinate other) {
		int dx = Math.abs(this.x() - other.x());
		int dy = Math.abs(this.y() - other.y());
		int dz = Math.abs(this.z() - other.z());
		return dx + dy + dz;
	}
	
	// validMove: Returns whether one can move in the given dimension and direction from this.
	// Throws an IllegalArgumentException if the given dimension is not valid.
	private boolean validMove(int dim, boolean dir) {
		switch (dim) {
		case DIM_X:
			if (dir) return this.x() < CEIL_X && (this.distance(ORIGIN) % 2 == 0);
			else return this.x() > FLOOR_X && (this.distance(ORIGIN) % 2 != 0);
		case DIM_Y:
			if (dir) return this.y() < CEIL_Y && (this.distance(ORIGIN) % 2 != 0);
			else return this.y() > FLOOR_Y && (this.distance(ORIGIN) % 2 == 0);
		case DIM_Z:
			if (dir) return this.z() < CEIL_Z && (this.distance(ORIGIN) % 2 == 0);
			else return this.z() > FLOOR_Z && (this.distance(ORIGIN) % 2 != 0);
		default:
			throw new IllegalArgumentException("Dimensionality out of bounds!");
		}
	}
	
	private boolean validMoveExp(int dim, boolean dir) {
		switch (dim) {
		case DIM_X:
			if (dir) return this.x() < CEIL_X_EXP && (this.distance(ORIGIN) % 2 == 0);
			else return this.x() > FLOOR_X_EXP && (this.distance(ORIGIN) % 2 != 0);
		case DIM_Y:
			if (dir) return this.y() < CEIL_Y_EXP && (this.distance(ORIGIN) % 2 != 0);
			else return this.y() > FLOOR_Y_EXP && (this.distance(ORIGIN) % 2 == 0);
		case DIM_Z:
			if (dir) return this.z() < CEIL_Z_EXP && (this.distance(ORIGIN) % 2 == 0);
			else return this.z() > FLOOR_Z_EXP && (this.distance(ORIGIN) % 2 != 0);
		default:
			throw new IllegalArgumentException("Dimensionality out of bounds!");
		}
	}
	
	// moveIn: Returns the BoardCoordinate adjacent this in the given dimension and direction.
	// Returns null if the new coordinate cannot exist on the board.
	// Throws IllegalArgumentException if the dimension is not valid.
	public BoardCoordinate moveIn(int dim, boolean dir) {
		if (! validMove(dim, dir)) return null;
		int offset = (dir)? 1:-1;
		switch (dim) {
		case DIM_X:
			return new BoardCoordinate(_x + offset, _y, _z);
		case DIM_Y:
			return new BoardCoordinate(_x, _y + offset, _z);
		case DIM_Z:
			return new BoardCoordinate(_x, _y, _z + offset);
		default:
			throw new IllegalArgumentException("Dimensionality out of bounds!");
		}
	}
	
	public BoardCoordinate moveInExp(int dim, boolean dir) {
		if (! validMoveExp(dim, dir)) return null;
		int offset = (dir)? 1:-1;
		switch (dim) {
		case DIM_X:
			return new BoardCoordinate(_x + offset, _y, _z);
		case DIM_Y:
			return new BoardCoordinate(_x, _y + offset, _z);
		case DIM_Z:
			return new BoardCoordinate(_x, _y, _z + offset);
		default:
			throw new IllegalArgumentException("Dimensionality out of bounds!");
		}
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof BoardCoordinate) && this.x() == ((BoardCoordinate) other).x() && 
		this.y() == ((BoardCoordinate) other).y() && this.z() == ((BoardCoordinate) other).z();
	}
	
	@Override
	public String toString() {
		return "(" + Integer.toString(_x) + ", " + Integer.toString(_y) + ", " + Integer.toString(_z) + ")";
	}
	
	@Override
	public int hashCode() {
		int x_val = this.x() % 256;
		int y_val = this.y() % 256;
		int z_val = this.z() % 256;
		return z_val + (y_val * 256) + (x_val * 256 * 256);
	}
}
