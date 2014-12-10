package yanfa;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Parser {

	private String string;
	private String cleanString;
	private String news;
	private String summary;
	private String[] words;
	private String[] sentences;
	private String[] paragraphs;
	private WordAndLocation[] wordAndLocation;
	
	public Parser(String string){
		this.string = string;
		news = getNewsInString(string);
		summary = getSummaryInString(string);
		
		cleanString = deleteStopWords(getNewsInString(string));
		paragraphs = parseToParagraphs(cleanString);
		sentences = parseToSentences(cleanString);
		words = parseToWords(cleanString);
		wordAndLocation = getAllWordLocation(getNewsInString(string));
	}
	
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String[] getSentences() {
		return sentences;
	}

	public String[] getParagraphs() {
		return paragraphs;
	}

	public String[] getWords() {
		return words;
	}
	
	public WordAndLocation[] getWordAndLocation() {
		return wordAndLocation;
	}
	
	public static String getTitleInString(String s){
		String tempString = s.substring(0, s.indexOf("\n"));
		return tempString;
	}
	
	public static String getNewsInString(String s){
		String tempString = s.substring(s.indexOf(">>>>>") + 6, s.indexOf("\\Ringkasan\\") - 1);
		return tempString;
	}
	
	public static String getSummaryInString(String s){
		String tempString = s.substring(s.indexOf("\\Ringkasan\\") + 13, s.length() - 2);
		return tempString;
	}

	public static String deleteStopWords(String s){
		String[] stopWords = parseToWords(readStream("Dataset/stopwords.txt"));
		for(String sWords : stopWords){
			s = s.replace(" "+sWords+" ", "");
		}
		return s;
	}
	
	public static String[] parseToWords(String s){
		String delims = "[\n .\'\\\"/()]|, |.\n";
		return s.split(delims);
	}
	
	public static String[] parseToSentences(String s){
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		List<String> tempList = new ArrayList<>();
		iterator.setText(s);
		int start = iterator.first();
		for (int end = iterator.next();
		    end != BreakIterator.DONE;
		    start = end, end = iterator.next()) {
			tempList.add(s.substring(start, end));
		}
		return tempList.toArray(new String[tempList.size()]);
	}
	
	public static String[] parseToParagraphs(String s){
		String delims = "[\n]+";
		return s.split(delims);
	}
	
	public static WordAndLocation[] getAllWordLocation(String s){
		List<WordAndLocation> word = new ArrayList<>();
		boolean found;
		int i = 0, j = 0, k = 0, l = 0, notifier;
		for(String par : parseToParagraphs(s)){
			k = 0;
			for(String sen : parseToSentences(par)){
				l = 0;
				for(String sWord : parseToWords(sen)){
					found = false;
					notifier = 0;
					for (WordAndLocation w : word){
						if(sWord.equalsIgnoreCase(w.getWord())){
							found = true;
						}
						else if (!sWord.equalsIgnoreCase(w.getWord()) && !found){
							notifier ++;
						}
					}
					if(!found){
						word.add(new WordAndLocation(sWord, new WordPlace(i,j,k,l)));
					}
					else{
						word.get(notifier).addWordPlace(new WordPlace(i, j, k, l));
					}
					l++;
				}
				k++;
			}
			j++;
		}
		return word.toArray(new WordAndLocation[word.size()]); 
	}
	
	public void printParagraphs(){
		for(String s : paragraphs){
			System.out.println(s);
		}
	}
	
	public void printSentences(){
		for(String s : sentences){
			System.out.println(s);
		}
	}
	
	public void printWords(){
		for(String s : words){
			System.out.println(s);
		}
	}

	public void printWordAndLocation(){
		for(WordAndLocation w : wordAndLocation){
			System.out.println("Kata \'"+w.getWord()+"\' ada di : ");
			for(WordPlace wPlace : w.getPlaces()){
				System.out.println("Artikel: "+(wPlace.getArticle()+1)+ " Paragraf: "+(wPlace.getParagraph()+1) + " Kalimat: " + 
						(wPlace.getSentence()+1) + " Kata ke: " + (wPlace.getWord()+1));
			}
		}
	}
	
	private static String readStream(String namaFile) {
		File file = new File(namaFile);
		StringBuilder sb = new StringBuilder(512);
		try{
			FileInputStream fileStream = new FileInputStream(file);
		    try {
		        Reader r = new InputStreamReader(fileStream, "UTF-8");
		        int c = 0;
		        while ((c = r.read()) != -1) {
		            sb.append((char) c);
		        }
		        r.close();
		    } catch (IOException e) {
		        throw new RuntimeException(e);
		    }
		    fileStream.close();
		}catch(Exception e){
			System.out.println("error : " + e);
		}
	    return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println("Title :\n"+ Parser.getTitleInString(readStream("Dataset/King001.txt")));
		System.out.println("News :\n"+ Parser.getNewsInString(readStream("Dataset/King001.txt")));
		System.out.println("Summary :\n"+ Parser.getSummaryInString(readStream("Dataset/King001.txt"))+ "\n");
		Parser parser = new Parser(readStream("Dataset/King001.txt"));
		parser.printWordAndLocation();
		for(String s : Parser.parseToParagraphs(Parser.getNewsInString(readStream("Dataset/King001.txt")))){
			System.out.print("tes : " + s);
		}
	}
}
