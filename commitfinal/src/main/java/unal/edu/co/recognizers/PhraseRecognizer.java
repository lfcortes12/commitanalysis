package unal.edu.co.recognizers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Combines co-located words into phrases by looking up a table of
 * common phrases.
 * @author Sujit Pal
 * @version $Revision$
 */
public class PhraseRecognizer implements IRecognizer {

  @SuppressWarnings("unused")
  private final Log log = LogFactory.getLog(getClass());
  
  private JdbcTemplate jdbcTemplate;
  @SuppressWarnings("unused")
  private Map<String,String> phraseMap;
  
  public PhraseRecognizer(DataSource dataSource) {
    super();
    setDataSource(dataSource);
  }
  
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }
  
  public void init() throws Exception { /* :NOOP: */ }

  public List<Token> recognize(List<Token> tokens) {
    List<Token> extractedTokens = new ArrayList<Token>();
    try {
	    int numTokens = tokens.size();
	    TOKEN_LOOP:
	    for (int i = 0; i < numTokens; i++) {
	      Token token = tokens.get(i);
	      TokenType type = token.getType();
	      if (type != TokenType.WORD) {
	        // we don't care about phrases that begin with types other than word
	        extractedTokens.add(token);
	        continue;
	      }
	      String word = token.getValue();
	      
	      List<Map<String,Object>> rows = jdbcTemplate.queryForList(
	        "select coc_phrase, coc_num_words from my_colloc where coc_lead_word = ?", 
	        (Object[])(new String[] {StringUtils.lowerCase(word)}));
	      for (Map<String,Object> row : rows) {
	        String phrase = (String) row.get("COC_PHRASE");
	        int numWords = (Integer) row.get("COC_NUM_WORDS");
	        if (numTokens > i + numWords) {
	          // we don't want to look beyond the actual size of the sentence
	          String inputPhrase = getInputPhrase(tokens, i, numWords);
	          if (phrase.equals(inputPhrase)) {
	            extractedTokens.add(new Token(phrase + "|||" + numWords, TokenType.PHRASE));
	            // move the pointer forward (numWords - 1)
	            i += (numWords - 1);
	            continue TOKEN_LOOP;
	          }
	        }
	      }
	      // if we came this far, then there is no phrase starting at
	      // this position...pass through
	      extractedTokens.add(token);
	    }
    } catch (CannotGetJdbcConnectionException ex) {
    	 System.out.println("Ocurrio un problema al conectarse a la base de datos");
    }
    return extractedTokens;
  }

  @SuppressWarnings("unused")
private String getInputPhrase(List<Token> tokens, int start, int length) {
    List<Token> tokenSublist = tokens.subList(start, start + length);
    StringBuilder phraseBuilder = new StringBuilder();
    for (Token token : tokenSublist) {
      TokenType type = token.getType();
      phraseBuilder.append(token.getValue());
    }
    return phraseBuilder.toString();
  }
}
