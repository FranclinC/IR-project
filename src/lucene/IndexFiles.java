package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;

import GUI.IndexGUI;

public class IndexFiles {
  
  PorterStemmer stemmer;
  StopWords sw;
  
  private String[] indexPath = {
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_00",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_01",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_10",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/index/index_11"
  };
  
  private String[] docsPath = {
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/Documents/docs_00",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/Documents/docs_01",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/Documents/docs_10",
			"/Users/franclincabral/Documents/CIN/MineraçãoWeb/IR-project/Documents/docs_11",
  };
  
  
  public void indexFiles() {
	  boolean create = true;
	  Date start = new Date();
	  
	  try {
		  for (int i = 0; i < indexPath.length; i++) {
			  if (i == 1 || i == 2) {
				  createFiles(docsPath[0], docsPath[i],i);
			  }
			  
			  if (i == 3) {
				  createFiles(docsPath[2], docsPath[i],1);
			  }
			  
			  Path docDir = Paths.get(docsPath[i]);
			  if (!Files.isReadable(docDir)) {
				  print ("Document directory '" + docDir.toAbsolutePath() + "'...");
				  System.exit(1);
			  }
			  
			  print("Indexing to directory '" + indexPath[i] + "'...");
			  
			  Directory dir = FSDirectory.open(Paths.get(indexPath[i]));
			  Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
			  IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			  
			  if (create) {
				  iwc.setOpenMode(OpenMode.CREATE);
			  } else {
				  iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			  }
			  
			  IndexWriter writer = new IndexWriter(dir, iwc);
			  indexDocs(writer, docDir);
			  
			  writer.close();
		  }
		  Date end = new Date();
		  print(end.getTime() - start.getTime() + " total milliseconds");
	  } catch (IOException e) {
		  print(" caught a" + e.getClass() + "\n with message: " + e.getMessage());
	  }
  }
  
  private void print(String str) {
		IndexGUI.setToContent(str);
  }
  
  private void createFiles(String from, String to, int code) {
	  String word, text;
	  char [] w = new char[501];
	  int i = 1;
	  
	  File dir = new File(from);
	  for (File file : dir.listFiles()) {
		  try {
			  text = "";
			  FileInputStream in = new FileInputStream(file);
			  while (true) {
				  word = "";
				  int ch = in.read();
				  if (Character.isLetter((char) ch)) {
					  int j = 0;
					  
					  while (true) {
						  ch = Character.toLowerCase((char) ch);
						  w[j] = (char) ch;
						  if (j < 500) {
							  j++;
						  }
						  ch = in.read();
						  
						  if (!Character.isLetter((char) ch)) {
							  for (int c = 0; c < j; c++) {
								  word += w[c];
							  }
							  if (code == 1) {
								  stemmer = new PorterStemmer();
								  stemmer.setCurrent(word);
								  stemmer.stem();
								  text += stemmer.getCurrent();
								  text += ' ';
								  break;
							  } else if (code == 2) {
								  sw = new StopWords();
								  if (!sw.isStopword(word)) {
									  text += word;
									  text += ' ';
								  }
								  break;
							  }
						  }
					  }
				  }
				  if (ch < 0) {
					  break;
				  }
				  
			  }
			  
			  FileOutputStream out = new FileOutputStream(to + "/" + String.format("%03d", i));
			  new PrintStream(out).println(text);
			  i++;
			  out.close();
			  in.close();
		  } catch (FileNotFoundException ie) {
			  System.out.println("error reading " + file);
			  break;
		  } catch (IOException e) {
			  System.out.println("file " + file + " not found");
			  break;
		  }
	  }
	  
  }

  /**
   * Indexes the given file using the given writer, or if a directory is given,
   * recurses over files and directories found under the given directory.
   * 
   * NOTE: This method indexes one document per input file.  This is slow.  For good
   * throughput, put multiple documents into your input file(s).  An example of this is
   * in the benchmark module, which can create "line doc" files, one document per line,
   * using the
   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
   * >WriteLineDocTask</a>.
   *  
   * @param writer Writer to the index where the given file/dir info will be stored
   * @param path The file to index, or the directory to recurse into to find files to index
   * @throws IOException If there is a low-level I/O error
   */
  static void indexDocs(final IndexWriter writer, Path path) throws IOException {
    if (Files.isDirectory(path)) {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          try {
            indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
          } catch (IOException ignore) {
            // don't index files that can't be read.
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } else {
      indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
    }
  }

  /** Indexes a single document */
  static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
    try (InputStream stream = Files.newInputStream(file)) {
      // make a new, empty document
      Document doc = new Document();
      
      // Add the path of the file as a field named "path".  Use a
      // field that is indexed (i.e. searchable), but don't tokenize 
      // the field into separate words and don't index term frequency
      // or positional information:
      Field pathField = new StringField("path", file.toString(), Field.Store.YES);
      doc.add(pathField);
      
      // Add the last modified date of the file a field named "modified".
      // Use a LongPoint that is indexed (i.e. efficiently filterable with
      // PointRangeQuery).  This indexes to milli-second resolution, which
      // is often too fine.  You could instead create a number based on
      // year/month/day/hour/minutes/seconds, down the resolution you require.
      // For example the long value 2011021714 would mean
      // February 17, 2011, 2-3 PM.
      doc.add(new LongPoint("modified", lastModified));
      
      // Add the contents of the file to a field named "contents".  Specify a Reader,
      // so that the text of the file is tokenized and indexed, but not stored.
      // Note that FileReader expects the file to be in UTF-8 encoding.
      // If that's not the case searching for special characters will fail.
      doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
      
      if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
        // New index, so we just add the document (no old document can be there):
        System.out.println("adding " + file);
        writer.addDocument(doc);
      } else {
        // Existing index (an old copy of this document may have been indexed) so 
        // we use updateDocument instead to replace the old one matching the exact 
        // path, if present:
        System.out.println("updating " + file);
        writer.updateDocument(new Term("path", file.toString()), doc);
      }
    }
  }
}
