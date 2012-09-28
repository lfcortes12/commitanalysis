package unal.edu.co.indexers;

import org.apache.commons.math.linear.RealMatrix;

import Jama.Matrix;


/**
 * Uses Latent Semantic Indexing to find word associations between docs. 
 * Idea is to find the intersections of the words found in each document
 * and score them accordingly. We use SVD to accomplish this. We first
 * decompose the word frequency vector into the three parts, then multiply
 * the three components back to get our transformed matrix.
 */
public class LsiJamaIndexer {

	public Matrix transform(RealMatrix matrix) {
		Matrix matrixJama = new Matrix(matrix.getData());
		Jama.SingularValueDecomposition svdJama = new Jama.SingularValueDecomposition(matrixJama);
		Matrix wordVector = svdJama.getU();
		Matrix sigma = svdJama.getS();
		Matrix documentVector = svdJama.getV();
		int k = (int) Math.floor(Math.sqrt(matrix.getColumnDimension()));
		Matrix reducedWordVector = wordVector.getMatrix(0,wordVector.getRowDimension() - 1, 0, k - 1);
		Matrix reducedSigma = sigma.getMatrix(0, k - 1, 0, k - 1);
		Matrix reducedDocumentVector = documentVector.getMatrix(0,documentVector.getRowDimension() - 1, 0, k - 1);
		Matrix weights = reducedWordVector.times(reducedSigma).times(reducedDocumentVector.transpose());
		for (int j = 0; j < weights.getColumnDimension(); j++) {
			double sum = sum(weights.getMatrix(0,weights.getRowDimension() - 1, j, j));
			for (int i = 0; i < weights.getRowDimension(); i++) {
				if (sum > 0.0D) {
					weights.set(i, j,Math.abs((weights.get(i, j)) / sum));
				} else {
					weights.set(i, j, 0.0D);
				}
			}
		}
		return weights;
	}
	
	private double sum(Matrix colMatrix) {
		double sum = 0.0D;
		for (int i = 0; i < colMatrix.getRowDimension(); i++) {
			sum += colMatrix.get(i, 0);
		}
		return sum;
	}
}
