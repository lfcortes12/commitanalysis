package unal.edu.co.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.lucene.document.Document;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import unal.edu.co.dao.commit.Index.IndexCommit;
import unal.edu.co.dao.commit.Index.IndexDiff;
import unal.edu.co.indexers.LsiIndexer;
import unal.edu.co.indexers.LsiJamaIndexer;
import unal.edu.co.indexers.VectorGenerator;
import unal.edu.co.service.CommitDiffService;
import unal.edu.co.service.CommitService;
import unal.edu.co.service.ConcreteDiffService;
import unal.edu.co.similarity.CosineSimilarityJama;
import Jama.Matrix;

import com.mysql.jdbc.Driver;

public class Main {
	
	static CommitDiffService service;
	static ConcreteDiffService serviceConcreteCommitDiff;
	static CommitService commitService;
	private static VectorGenerator vectorGenerator;
	private static Map<String, Reader> documents = new LinkedHashMap<String, Reader>();
	private static String projects[] = {"weka","netbeans","freemind","solr","jabref","jfreechart"};
	
	public static void setContext() throws SQLException {
		vectorGenerator = new VectorGenerator();
		Driver driver = new Driver();
		SimpleDriverDataSource datasource = new SimpleDriverDataSource(driver,"jdbc:mysql://localhost:3306/tmdb","root", "root");
		vectorGenerator.setDataSource(datasource);
		documents = new LinkedHashMap<String, Reader>();
		IndexDiff indexDiff = new IndexDiff();
		IndexCommit indexCommit = new IndexCommit();
		
		List<String> changesetlist = new ArrayList<String>();
		
		List<Document> commits = new ArrayList<Document>();
		List<String> changesets = new ArrayList<String>();
		for (String project : projects) {
			commits.addAll(indexCommit.getCommitByProjectName(project));
		}
		int i = 0;
		for (Document commit : commits) {
			String changeset =  commit.get("changeset");
			changesets.add(changeset);
			Document diff = indexDiff.searchDiff(changeset);
			if(commit != null && diff != null) {
				System.out.println("procesando commit con changeset: " + changeset);
				String message = commit.get("message");
				changeset =  commit.get("changeset");
				String diffText = diff.get("diffwords");
				if (message != null && !"".equals(message) && diffText != null && !"".equals(diffText)) {
					documents.put("C" + i, new StringReader(message));
					documents.put("C" + i + "-DIFF", new StringReader(diffText));
					changesetlist.add(changeset);
				}
			}
			i++;
		}
		indexDiff.closeWriter();
		indexCommit.closeWriter();
		try {
			printSelectedChangeSet(changesets);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void printSelectedChangeSet(List<String> changesetlist) throws FileNotFoundException {
		File file = new File("/home/fernando/Desarrollo/resultados/lsi/changesets.txt");
		PrintWriter writer = new PrintWriter(file);
		for (int j = 0; j < changesetlist.size(); j++) {
			writer.print(changesetlist.get(j));
			writer.println();
		}
		writer.flush();
		writer.close();
	}
	
	private static void prettyPrintMatrix(String legend, RealMatrix matrix,String[] documentNames, String[] words, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf(";", " ");
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%s;", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < words.length; i++) {
			writer.printf("%1s;", words[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%f;", matrix.getEntry(i, j));
			}
			writer.println();
		}
		writer.flush();
	}
	
	private static void prettyPrintMatrix(String legend, Matrix matrix,String[] documentNames, String[] words, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf(";", " ");
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%s;", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < words.length; i++) {
			writer.printf("%1s;", words[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%f;", matrix.get(i, j));
			}
			writer.println();
		}
		writer.flush();
	}

	private static void prettyPrintMatrix(String legend, Matrix matrix,String[] documentNames, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf("%s;", " ");
		for (int i = 0; i < documentNames.length; i++) {
			if(documentNames[i].contains("DIFF")) {
				writer.printf("%s;", documentNames[i]);
			}
		}
		writer.println();
		for (int i = 0; i < documentNames.length; i++) {
			if(!documentNames[i].contains("DIFF")) {
				writer.printf("%s;", documentNames[i]);
				for (int j = 0; j < documentNames.length; j++) {
					if(j%2 == 1) {
						writer.printf("%f;", matrix.get(i, j));
					}
				}
				writer.println();
			}
		}
		writer.flush();
	}
	
	private static void prettyNormalPrintMatrix(String legend, Matrix matrix,String[] documentNames, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf("%s;", " ");
		for (int i = 0; i < documentNames.length; i++) {
				writer.printf("%s;", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < documentNames.length; i++) {
				writer.printf("%s;", documentNames[i]);
				for (int j = 0; j < documentNames.length; j++) {
						writer.printf("%f;", matrix.get(i, j));
					}
				writer.println();
		}
		writer.flush();
	}
	
	public static void generateLsiIndexer() throws Exception {
		System.out.println("acabo de entrar generateLsiIndexer");
		vectorGenerator.generateVector(documents);
		LsiIndexer indexer = new LsiIndexer();
		System.out.println("acabo de crear LsiIndexer");
		RealMatrix lsiMatrix = indexer.transform(vectorGenerator.getMatrix());
		System.out.println("acabo de crear la matriz LsiIndexer");
		File file = new File("/home/fernando/Desarrollo/resultados/lsi/lsi.txt");
		prettyPrintMatrix("Latent Semantic (LSI)", lsiMatrix,vectorGenerator.getDocumentNames(), vectorGenerator.getWords(),new PrintWriter(file));
		//CosineSimilarity cosineSimilarity = new CosineSimilarity();
		//RealMatrix similarity = cosineSimilarity.transform(lsiMatrix);
		//File cosineFile = new File("/home/fernando/Desarrollo/resultados/lsi/cosinesimilarity.txt");
		//prettyPrintMatrix("Cosine Similarity (LSI)", similarity,vectorGenerator.getDocumentNames(), new PrintWriter(cosineFile));
	}
	
	public static void generateLsiJamaIndexer() throws Exception {
		System.out.println("acabo de entrar generateLsiIndexer");
		vectorGenerator.generateVector(documents);
		LsiJamaIndexer indexer = new LsiJamaIndexer();
		System.out.println("acabo de crear LsiIndexer");
		Matrix lsiMatrix = indexer.transform(vectorGenerator.getMatrix());
		System.out.println("acabo de crear la matriz LsiIndexer");
		File file = new File("/home/fernando/Desarrollo/resultados/lsi/lsi.txt");
		prettyPrintMatrix("Latent Semantic (LSI)", lsiMatrix,vectorGenerator.getDocumentNames(), vectorGenerator.getWords(),new PrintWriter(file));
		CosineSimilarityJama cosineSimilarity = new CosineSimilarityJama();
		Matrix similarity = cosineSimilarity.transformToMatrix(lsiMatrix);
		File cosineFile = new File("/home/fernando/Desarrollo/resultados/lsi/cosinesimilarity.txt");
		prettyPrintMatrix("Cosine Similarity (LSI)", similarity,vectorGenerator.getDocumentNames(), new PrintWriter(cosineFile));
		File cosineTotalFile = new File("/home/fernando/Desarrollo/resultados/lsi/cosinetotalsimilarity.txt");
		prettyNormalPrintMatrix("Cosine Similarity (LSI)", similarity,vectorGenerator.getDocumentNames(), new PrintWriter(cosineTotalFile));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Va a iniciar");
		try {
			setContext();
			generateLsiJamaIndexer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
