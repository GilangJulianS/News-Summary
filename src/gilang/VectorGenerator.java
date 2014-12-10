package gilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yanfa.Parser;

public class VectorGenerator {

	String[] paragraphs;
	List<String> keyWords;
	float avgSentenceLength;
	List<List<Float>> vector;
	
	public VectorGenerator(String fullText) {
		paragraphs = Parser.parseToParagraphs(fullText);
		avgSentenceLength = Analyzer.getAvgSentencesLength(fullText);
		vector = new ArrayList<>();
		keyWords = Analyzer.getKeywords(fullText);
	}
	
	public List<List<Float>> generateVector(){
		List<List<Float>> vector = new ArrayList<List<Float>>();
		for(String paragraph : paragraphs){
			int i = 1;
			String[] sentences = Parser.parseToSentences(paragraph);
			for(String sentence : sentences){
				List<Float> sentenceVector = vectorSentence(sentence, paragraph);
				vector.add(sentenceVector);
				
				//debug
//					System.out.println("Sentence " + i + " : " + sentence);
//					String[] words = Parser.parseToWords(sentence);
//					for(String word : words){
//						System.out.println("word : " + word);
//					}
				
				
				
				i++;
			}
		}
		return vector;
	}
	
	public List<Float> vectorSentence(String sentence, String paragraph){
		int longestSentence = Analyzer.getSentenceLength(Analyzer.getLongestSentence(paragraph, false), false);
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
//		debug
//			System.out.println("Sentence Length : " + Analizer.getSentenceLength(sentence, false) + " longest length : " + longestSentence);
		return (float)Analyzer.getSentenceLength(sentence, false)/longestSentence;
	}
	
	public float getFeature2(String sentence, String paragraph){
//		debug
//			System.out.println("position : " + Analyzer.getSentencePosition(sentence, paragraph));
		return (float)Analyzer.getSentencePosition(sentence, paragraph)/avgSentenceLength;
	}
	
	public float getFeature3(String sentence){
//		debug
//			System.out.println("numeric count : " + Analyzer.getNumericCount(sentence));
//			System.out.println("total count : " + Analyzer.getSentenceLength(sentence, true));
		return (float)Analyzer.getNumericCount(sentence)/(float)Analyzer.getSentenceLength(sentence, true);
	}

	public float getFeature4(String sentence){
//		debug
//			String[] list = {"presiden", "antasari", "ri", "menanggapi", "dpr"};
//			keyWords = Arrays.asList(list);
//			System.out.println("#katakunci : " + Analyzer.getMatchKeywords(sentence, keyWords));
		return (float)Analyzer.getMatchKeywords(sentence, keyWords)/(float)Analyzer.getSentenceLength(sentence, true);
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
	
	public static void main(String[] args){
		String text = "Anggota  Tim  9  Dewan  Perwakilan  Rakyat RI,  Akbar  Faisal  mengaku  kaget, Presiden RI Susilo Bambang Yudhoyono menanggapi penyataan mantan Ketua KPK Antasari Azhar soal pengucuran dana talangan (bail out) Bank Century pada  Oktober  2008.  Ia  menyesalkan Presiden sampai  menggelar  pernyataan khusus di Istana negara pada Rabu (15/8/2012) malam untuk menanggapi hal tersebut.";
		VectorGenerator gen = new VectorGenerator(text);
		List<List<Float>> vector = gen.generateVector();
		int par = 1;
		for(List<Float> list : vector){
			System.out.println("paragraph " + par);
			int sen = 1;
			for(Float f : list){
				System.out.println(sen + " : " + f);
				sen++;
			}
			par++;
		}
	}

}
