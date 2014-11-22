package gilang;

import java.util.ArrayList;
import java.util.List;

import yanfa.Parser;

public class VectorGenerator {

	String[] paragraphs;
	List<String> keyWords;
	float avgSentenceLength;
	List<List<Float>> vector;
	
	public VectorGenerator(String fullText) {
		paragraphs = Parser.parseToParagraphs(fullText);
		avgSentenceLength = Analizer.getAvgSentencesLength(fullText);
		vector = new ArrayList<>();
		keyWords = Analizer.getKeywords(fullText);
	}
	
	public void generateVector(){
		for(String paragraph : paragraphs){
			String[] sentences = Parser.parseToSentences(paragraph);
			for(String sentence : sentences){
				List<Float> sentenceVector = vectorSentence(sentence, paragraph);
			}
		}
	}
	
	public List<Float> vectorSentence(String sentence, String paragraph){
		int longestSentence = Analizer.getLongestSentence(paragraph).length();
		List<Float> sentenceVector = new ArrayList<>();
		sentenceVector.add(getFeature1(sentence, longestSentence));
		sentenceVector.add(getFeature2(sentence, paragraph));
		sentenceVector.add(getFeature3(sentence));
		sentenceVector.add(getFeature4(sentence));
		sentenceVector.add(getFeature5(sentence));
		sentenceVector.add(getFeature6(sentence));
		sentenceVector.add(getFeature7(sentence));
		sentenceVector.add(getFeature8(sentence));
		return sentenceVector;
	}
	
	public float getFeature1(String sentence, int longestSentence){
		return (float)Analizer.getSentenceLength(sentence)/longestSentence;
	}
	
	public float getFeature2(String sentence, String paragraph){
		return (float)Analizer.getSentencePosition(sentence, paragraph)/avgSentenceLength;
	}
	
	public float getFeature3(String sentence){
		return (float)Analizer.getNumericCount(sentence)/(float)Analizer.getSentenceLength(sentence);
	}

	public float getFeature4(String sentence){
		return (float)Analizer.getMatchKeywords(sentence, keyWords)/(float)Analizer.getSentenceLength(sentence);
	}
	
	public float getFeature5(String sentence){
		return 0;
	}
	
	public float getFeature6(String sentence){
		return 0;
	}
	
	public float getFeature7(String sentence){
		return 0;
	}
	
	public float getFeature8(String sentence){
		return 0;
	}

}
