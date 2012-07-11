package unal.edu.co.dao.commit.analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class DiffAnalyzer extends Analyzer {

	private Set<Object> javaStopSet; 
	@SuppressWarnings("unused")
	private Set<Object> englishStopSet; 
	public static final String[] JAVA_STOP_WORDS = {
		"public","private","protected","interface","abstract","implements",
		"extends","null","new" ,"switch","case", "default" ,"synchronized" ,
		"do", "if", "else","break","continue", "this",  "assert" ,
		"for","instanceof", "transient", "final", "static" ,"void",  
		"catch","try","throws","throw",	"class", "finally", "return" ,
		"const" , "native", "super", "while", "import" , "package" ,"true", "false",
		"a", "b", "c", "d", "e", "f", "g", "h", "i","j", "k", "l", "m", "n", "o", "p", "q", "r",
	    "s", "t", "u", "v", "w", "x", "y", "z","*","-","/","+", "a", "an", "and", "are", "as", "at", "be", "but", "by",
	    "for", "if", "in", "into", "is", "it",
	    "no", "not", "of", "on", "or", "s", "such",
	    "t", "that", "the", "their", "then", "there", "these",
	    "they", "this", "to", "was", "will", "with"
	};
	
	private static final String[] ENGLISH_STOP_WORDS = {
	    "a", "an", "and", "are", "as", "at", "be", "but", "by",
	    "for", "if", "in", "into", "is", "it",
	    "no", "not", "of", "on", "or", "s", "such",
	    "t", "that", "the", "their", "then", "there", "these",
	    "they", "this", "to", "was", "will", "with"
	  };
	
	public DiffAnalyzer(){
		super();
		javaStopSet = StopFilter.makeStopSet(Version.LUCENE_36,JAVA_STOP_WORDS);
		englishStopSet = StopFilter.makeStopSet(Version.LUCENE_36,ENGLISH_STOP_WORDS);
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream result = new StandardTokenizer(Version.LUCENE_36,reader);  
        return new StopFilter(Version.LUCENE_36,new LowerCaseFilter(Version.LUCENE_36,result),javaStopSet);
	}
	
}
