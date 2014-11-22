package gilang;

import java.util.ArrayList;
import java.util.List;

import yanfa.Parser;
import yanfa.WordAndLocation;

public class Analizer {
	
	static int avgSentencesLength = -1;

	public Analizer() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getSentenceLength(String sentences){
		String[] words = Parser.parseToWords(sentences);
		return words.length;
	}
	
	public static String getLongestSentence(String paragraph){
		int maxLength = Integer.MIN_VALUE;
		int idx = -1;
		String[] sentences = Parser.parseToSentences(paragraph);
		for(int i=0; i<sentences.length; i++){
			if(sentences[i].length() > maxLength)
				idx = i;
		}
		return sentences[idx];
	}
	
	public static float getAvgSentencesLength(String fullText){
		if(avgSentencesLength == -1){	
			int sum = 0;
			String[] paragraphs = Parser.parseToParagraphs(fullText);
			for(String paragraph : paragraphs){
				String[] sentences = Parser.parseToSentences(paragraph);
				sum += sentences.length;
			}
			return (float)sum/(float)paragraphs.length;
		}
		else{
			return avgSentencesLength;
		}
	}
	
	public static int getSentencePosition(String sentence, String paragraph){
		String[] sentences = Parser.parseToSentences(paragraph);
		for(int i=0; i<sentences.length; i++){
			if(sentences[i].contains(sentence))
				return i;
		}
		return -1;
	}
	
	public static List<String> getKeywords(String fullText){
		WordAndLocation[] WALs = Parser.getAllWordLocation(fullText);
		WordAndLocation temp;
		for(int i=0; i<WALs.length; i++){
			for(int j=i+1; j<WALs.length; j++){
				if(WALs[i].getCounter() < WALs[j].getCounter()){
					temp = WALs[i];
					WALs[i] = WALs[j];
					WALs[j] = temp;
				}
			}
		}
		List<String> keyWords = new ArrayList<String>();
		for(int i=0; i<5; i++){
			keyWords.add(WALs[i].getWord().toLowerCase());
		}
		return keyWords;
	}
	
	public static int getMatchKeywords(String sentences, List<String> keyWords){
		String[] words = Parser.parseToWords(sentences);
		int counter = 0;
		for(String word : words){
			if(keyWords.contains(word.toLowerCase()))
				counter++;
		}
		return counter;
	}
	
	public static int getNumericCount(String sentence){
		int count = 0;
		String[] words = Parser.parseToWords(sentence);
		for(String word : words){
			if(isNumber(word))
				count++;
		}
		return count;
	}
	
	private static boolean isNumber(String word){
		try{
			Float.parseFloat(word);
			return true;
		}catch(NumberFormatException e){
			if(word.indexOf('/') != word.lastIndexOf('/') || word.indexOf('-') != word.lastIndexOf('-'))
				return false;
			return true;
					
		}
	}

}
