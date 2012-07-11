package unal.edu.co.dao.commit.analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

public class CommitAnalyzer extends Analyzer {

	private Set<Object> englishStopSet; 
	
	private static final String[] ENGLISH_STOP_WORDS = {
	    "a", "an", "and", "are", "as", "at", "be", "but", "by",
	    "for", "if", "in", "into", "is", "it",
	    "no", "not", "of", "on", "or", "s", "such",
	    "t", "that", "the", "their", "then", "there", "these",
	    "they", "this", "to", "was", "will", "with"
	  };
	
	public CommitAnalyzer(){
		super();
		englishStopSet = StopFilter.makeStopSet(Version.LUCENE_36,ENGLISH_STOP_WORDS);
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		//TokenStream result = new StandardTokenizer(Version.LUCENE_36,reader);  
		return new PorterStemFilter(new StopFilter(Version.LUCENE_36,new LowerCaseTokenizer(Version.LUCENE_36,reader), englishStopSet));
        //return new StopFilter(Version.LUCENE_36,new LowerCaseFilter(Version.LUCENE_36,result),englishStopSet);
	}
	
}
