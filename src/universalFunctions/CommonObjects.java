package universalFunctions;

import java.util.Objects;

public class CommonObjects {

	public static class Position {
		private int r;
		private int c;
		
		public Position() {
			this.r = 0;
			this.c = 0;
		}
		
		public Position(int r, int c) {
			this.r = r;
			this.c = c;
		}
		
		public Position(Position p) {
			this.r = p.r;
			this.c = p.c;
		}
		
		public Position move(int[] dir) {
			return new Position(r + dir[0], c + dir[1]);
		}
		
		public int getR() {
			return r;
		}

		public void setR(int r) {
			this.r = r;
		}

		public int getC() {
			return c;
		}

		public void setC(int c) {
			this.c = c;
		}

		@Override
		public int hashCode() {
			return Objects.hash(c, r);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Position other = (Position) obj;
			return c == other.c && r == other.r;
		}

		@Override
		public String toString() {
			return String.format("(%d, %d)", r, c);
		}
	}
}
