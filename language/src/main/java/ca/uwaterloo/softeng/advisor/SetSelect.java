package ca.uwaterloo.softeng.advisor;

/**
 * Constraint satisfaction solver.  Store a matrix where columns represent
 * constraints and rows represent items that satisfy constraints.  Each
 * element is a either zero for a non-match or non-zero for a match with
 * higher values representing a higher preference for that match.  Finds
 * a maximum covering of constraints where an item can satisfy maximum one
 * constraint.
 */
class SetSelect {
	// stores sets as array of {0,1+} array
	final int rows;
	final int cols;
	int[][] setArray;
	int[] rowCount;
	int[] colCount;

	/**
	 * Creates empty matrix and also row and column counts.
	 *
	 * @param r number of rows
	 * @param c number of columns
	 */
	public SetSelect(int r, int c) {
		rows = r;
		cols = c;
		setArray = new int[rows][cols];
		rowCount = new int[rows];
		colCount = new int[cols];
	}

	/**
	 * Sets a match with given priority between item in row and constraint
	 * in column.
	 *
	 * @param row item row number
	 * @param col constraint column number
	 * @param priority preference for this match
	 */
	public void set(int row, int col, int priority) {
		setArray[row][col] = 1 + priority;
		rowCount[row] += 1;
		colCount[col] += 1;
	}

	/**
	 * Choose one element from each row to maximize column coverage. i.e.
	 * does one-to-one matching for maximum coverage of columns.
	 */
	public void solve() {
		boolean solved;
		do {
			boolean changes;
			do {
				changes = false;
				// iterate through cols
				for(int col=0; col<cols; col++) {
					// if uniquely satisfied rqmt
					if(colCount[col] == 1) {
						int row = firstRow(col);
						if(rowCount[row] != 1) {
							// must check for other columns uniquely matched
							// by this row that might have higher priority
							int maxCol = firstMaxCol(row, col, true);
							selectElement(row, maxCol);
							// not sure if next line is needed
							// col = -1; // will get ++ed
							changes = true;
						}
					}
				}
				// iterate through rows
				for(int row=0; row<rows; row++) {
					// if uniquely matched course
					if(rowCount[row] == 1) {
						int col = firstCol(row);
						if(colCount[col] != 1) {
							// must check for other rows with unique matches
							// to this column that might have higher priority
							int maxRow = firstMaxRow(row, col, true);
							selectElement(maxRow, col);
							// not sure if next line is needed
							// row = -1; // will get ++ed
							changes = true;
						}
					}
				}
			} while(changes);

			// break-tie (if needed)
			solved = true;
			for(int col=0; col<cols && solved; col++) {
				if(colCount[col] > 1) {
					int row = firstMaxRow(firstRow(col), col, false);
					selectElement(row, col);
					solved = false;
				}
			}
		} while(!solved);

		return;
	}

	/**
	 * Retrieves first non-zero element from a row.
	 * @param row row index
	 * @return element value
	 */
	public int element(int row) {
		return firstCol(row);
	}

	/**
	 * Finds first non-zero element in column.
	 * @param col column number
	 * @return row number of first non-zero element
	 */
	public int firstRow(int col) {
		int row = 0;
		while(row<rows && setArray[row][col]<1) {
			row++;
		}
		return row;
	}

	/**
	 * Finds the first row with the maximum value for that column.
	 * @param column index of column to search
	 * @param maxRow index of a row with the value to beat
	 * @param rowCountOne if true, rows with counts>1 are ignored
	 * @param row index of first max element
	 */
	private int firstMaxRow(int maxRow, int col, boolean rowCountOne) {
		for(int row = 0; row<rows; row++) {
			if((setArray[row][col] > setArray[maxRow][col]) &&
					(!rowCountOne || rowCount[row] == 1)) {
				maxRow = row;
			}
		}
		return maxRow;
	}

	/**
	 * Finds first non-zero element in row.
	 * @param row rowumn number
	 * @return col number of first non-zero element
	 */
	private int firstCol(int row) {
		// find col
		int col=0;
		while((col < cols) && (setArray[row][col] < 1)) {
			col++;
		}
		return col;
	}

	/**
	 * Finds the first column with the maximum value for that row.
	 * @param row index of row to search
	 * @param maxCol index of a column with the value to beat
	 * @param colCountOne if true, columns with counts>1 are ignored
	 * @param column index of first max element
	 */
	private int firstMaxCol(int row, int maxCol, boolean colCountOne) {
		for(int col = 0; col<cols; col++) {
			if((setArray[row][col] > setArray[row][maxCol]) &&
					(!colCountOne || colCount[col] == 1)) {
				maxCol = col;
			}
		}
		return maxCol;
	}

	/**
	 * Selects element from matrix by zeroing all others in same row or
	 * column.
	 * @param row row number of selected element
	 * @param col col number of selected element
	 */
	private void selectElement(int row, int col) {
		if(rowCount[row] > 1) {
			// remove other col matches for course
			for(int j=0; j<cols && rowCount[row]>1; j++) {
				if((setArray[row][j] >= 1) && (j!=col)) {
					setArray[row][j] = 0;
					colCount[j] -= 1;
					rowCount[row] -= 1;
				}
			}
		}
		if(colCount[col] > 1) {
			// remove other rows matching that col
			for(int i=0; i<rows && colCount[col] > 1; i++) {
				if((setArray[i][col] >= 1) && (i!=row)) {
					setArray[i][col] = 0;
					colCount[col] -= 1;
					rowCount[i] -= 1;
				}
			}
		}
	}

}
