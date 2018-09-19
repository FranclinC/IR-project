package lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;

import GUI.SearchGUI;

/** Simple command-line based search demo. */
public class SearchFiles {

	private String[] index = {
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_00",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_01",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_10",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_11"
	};

	private PorterStemmer stemmer;
	private StopWords sw;
	private IndexSearcher searcher;
	private boolean next, previous;
	private int start, end;
	private ScoreDoc[] hits;
	private int numTotalHits, hitsPerPage;
	private Query query;
	private String field = "contents";
	
	public void search(String input, int sourceCode) {
		
		String line = input.trim();

		if(line.length() != 0) {
			
			// Se foi passado algo no input
			String[] splitLine = line.split("\\s");

			if(sourceCode == 0) {
				lastAdjust(line, sourceCode);
			}
			if(sourceCode == 1) {
				line = stemming(splitLine);
				lastAdjust(line, sourceCode);
			} 
			if(sourceCode == 2) {
				line = stopword(splitLine);
				if(line.length() != 0)
					lastAdjust(line, sourceCode);
				else {
					String[] fail = {"No matching documents, try again..."};
					sendToPrint(fail);
				}
			} 
			if(sourceCode == 3) {
				line = stsw(splitLine);
				if(line.length() != 0)
					lastAdjust(line, sourceCode);
				else {
					String[] fail = {"No matching documents, try again..."};
					sendToPrint(fail);
				}
			}
		}

	}
	
	private void lastAdjust(String line, int sourceCode) {
		hitsPerPage = 10;
		try {
			// Base escolhida para a pesquisa
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index[sourceCode])));
//			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index[sourceCode])));
			searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
			QueryParser parser = new QueryParser(field, analyzer);

			query = parser.parse(line.trim());

			doPagingSearch();

			//TODO: reader.close();

		} catch(IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void doPagingSearch() throws IOException {
		// Collect all the docs
		TopDocs results = searcher.search(query, 20 * hitsPerPage);
		hits = results.scoreDocs;
		numTotalHits = (int) results.totalHits;
		start = 0;
		end = Math.min(numTotalHits, hitsPerPage);
		listDocs(start, end);
	}
	
	private void listDocs(int start, int end) throws IOException {
		String[] printList = new String[end];
		String matches = "Searching for: " + query.toString(field) + '\n' + numTotalHits + " total matching documents";
		printMatches(matches);
		int j = 0;
		
		for(int i = start; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String path = doc.get("path");
			if (path != null) {
				printList[j] = (i+1) + ". " + path;
				j++;
				String title = doc.get("title");
				if (title != null) {
					printList[j] = "   Title: " + doc.get("title");
					j++;
				}
			} else {
				printList[j] = (i+1) + ". " + "No path for this document";
				j++;
			}
		}
		sendToPrint(printList);
		j = 0;
		
		if(numTotalHits >= end) {
			next = true;
			previous = true;
		} else {
			next = false;
			previous = false;
		}
	}
	
	public void previousPage() {
		if(previous) {
			start = Math.max(0, start - hitsPerPage);
			end = Math.min(numTotalHits, start + hitsPerPage);
			try {
				listDocs(start, end);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void nextPage() {
		if(next)
			if(start + hitsPerPage < numTotalHits) {
				start += hitsPerPage;
				end = Math.min(numTotalHits, start + hitsPerPage);
				try {
					listDocs(start, end);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	private String stemming(String[] words) {
		String newStr = "";
		stemmer = new PorterStemmer();

		for (int i = 0; i < words.length; i++) {
			stemmer.setCurrent(words[i]);
			stemmer.stem();
			newStr += stemmer.getCurrent();
			newStr += ' ';
		}
		return newStr;
	}

	private String stopword(String[] words) {
		String newStr = "";
		sw = new StopWords();

		for (int i = 0; i < words.length; i++) {
			if(!sw.isStopword(words[i])) {
				newStr += words[i];
				newStr += ' ';
			}
		}
		return newStr;
	}
	
	private String stsw(String[] words) {
		String newStr = "";
		stemmer = new PorterStemmer();
		sw = new StopWords();

		for (int i = 0; i < words.length; i++) {
			if(!sw.isStopword(words[i])) {
				stemmer.setCurrent(words[i]);
				stemmer.stem();
				newStr += stemmer.getCurrent();
				newStr += ' ';
			}
		}
		return newStr;
	}

	private void printMatches(String str) {
		SearchGUI.setMatches(str);
	}

	private void sendToPrint(String[] array) {
		SearchGUI.setToContent(array);
	}
}

