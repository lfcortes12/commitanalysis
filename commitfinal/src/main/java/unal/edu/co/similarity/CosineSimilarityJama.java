package unal.edu.co.similarity;

import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import Jama.Matrix;

/**
 * Implements Cosine Similarity for a term document matrix.
 * A o B = x1*x2 + y1*y2
 * dist(A,0) = sqrt((xa-x0)^2 + (ya-y0)^2) == |A|
 * Therefore:
 * sim(A,B) = cos t = A o B/|A|x|B|  
 * 
 * @author Sujit Pal
 * @version $Revision: 21 $
 */
public class CosineSimilarityJama {
	
	public Matrix transformToMatrix(Matrix termDocumentMatrix) {
		int numDocs = termDocumentMatrix.getColumnDimension();
		Matrix similarityMatrix = new Matrix(numDocs, numDocs);
		for (int i = 0; i < numDocs; i++) {
			Matrix sourceDocMatrix = termDocumentMatrix.getMatrix(0,termDocumentMatrix.getRowDimension() - 1, i, i);
			for (int j = 0; j < numDocs; j++) {
				Matrix targetDocMatrix = termDocumentMatrix.getMatrix(0,termDocumentMatrix.getRowDimension() - 1, j, j);
				similarityMatrix.set(i, j,computeSimilarity(sourceDocMatrix, targetDocMatrix));
			}
		}
		return similarityMatrix;
	}

  public double computeSimilarity(Matrix sourceDoc, Matrix targetDoc) {
    if (sourceDoc.getRowDimension() != targetDoc.getRowDimension() ||
        sourceDoc.getColumnDimension() != targetDoc.getColumnDimension() ||
        sourceDoc.getColumnDimension() != 1) {
      throw new IllegalArgumentException(
        "Matrices are not column matrices or not of the same size");
    }
    // max col sum, only 1 col, so...
    double dotProduct = dot(sourceDoc, targetDoc);
    // sqrt of sum of squares of all elements, only one col, so...
    double eucledianDist = sourceDoc.normF() * targetDoc.normF();
    return dotProduct / eucledianDist;
  }
  
  private double dot(Matrix source, Matrix target) {
    int maxRows = source.getRowDimension();
    int maxCols = source.getColumnDimension();
    RealMatrix dotProduct = new OpenMapRealMatrix(maxRows, maxCols);
    for (int row = 0; row < maxRows; row++) {
      for (int col = 0; col < maxCols; col++) {
        dotProduct.setEntry(row, col, 
          source.get(row, col) * target.get(row, col));
      }
    }
    return dotProduct.getNorm();
  }
}
