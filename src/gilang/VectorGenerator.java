package gilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yanfa.Parser;

public class VectorGenerator {

	String[] paragraphs;
	// keywords must be in lowercase!!
	List<String> keyWords;	
	float avgSentenceLength;
	List<List<Float>> vector;
	List<Integer> feature7;
	List<Integer> feature8; 
	
	
	// ---- dummy variabel -----
		String title;
		
	// ---- dummy method -----
		public void setTitle(String newTitle){
			title = newTitle;
		}
	
	public VectorGenerator(String fullText) {
		paragraphs = Parser.parseToParagraphs(fullText);
		avgSentenceLength = Analyzer.getAvgSentencesLength(fullText);
		vector = new ArrayList<>();
		keyWords = Analyzer.getKeywords(fullText);
		feature7 = getFeature7(fullText);
		feature8 = getFeature8(fullText);
	}
	
	public List<List<Float>> generateVector(){
		int j = 0;
		List<List<Float>> vector = new ArrayList<List<Float>>();
		for(String paragraph : paragraphs){
			int i = 0;
			String[] sentences = Parser.parseToSentences(paragraph);
			for(String sentence : sentences){
				List<Float> sentenceVector = vectorSentence(sentence, paragraph, i);
				sentenceVector.add((float)feature7.get(j));
				sentenceVector.add((float)feature8.get(j));
				vector.add(sentenceVector);
				
//			----- debug ------
//					System.out.println("Sentence " + i + " : " + sentence);
//					String[] words = Parser.parseToWords(sentence);
//					for(String word : words){
//						System.out.println("word : " + word);
//					}
				
				
				j++;
				i++;
			}
		}
		return vector;
	}
	
	public List<Float> vectorSentence(String sentence, String paragraph, int sentenceNumber){
		int longestSentence = Analyzer.getSentenceLength(Analyzer.getLongestSentence(paragraph, false), false);
		List<Float> sentenceVector = new ArrayList<>();
		sentenceVector.add(getFeature1(sentence, longestSentence));
		sentenceVector.add(getFeature2(sentence, paragraph));
		sentenceVector.add(getFeature3(sentence));
		sentenceVector.add(getFeature4(sentence));
		sentenceVector.add(getFeature5(title, sentence));
		sentenceVector.add(getFeature6(paragraph, sentence, sentenceNumber));
		return sentenceVector;
	}
	
	public float getFeature1(String sentence, int longestSentence){
		
//		------ debug ------
//			System.out.println("Sentence Length : " + Analizer.getSentenceLength(sentence, false) + " longest length : " + longestSentence);
		
		return (float)Analyzer.getSentenceLength(sentence, false)/longestSentence;
	}
	
	public float getFeature2(String sentence, String paragraph){
		
//		------ debug ------
//			System.out.println("position : " + Analyzer.getSentencePosition(sentence, paragraph));
		
		return (float)Analyzer.getSentencePosition(sentence, paragraph)/avgSentenceLength;
	}
	
	public float getFeature3(String sentence){
		
//		------ debug ------
//			System.out.println("numeric count : " + Analyzer.getNumericCount(sentence));
//			System.out.println("total count : " + Analyzer.getSentenceLength(sentence, true));
		
		return (float)Analyzer.getNumericCount(sentence)/(float)Analyzer.getSentenceLength(sentence, true);
	}

	public float getFeature4(String sentence){
		
//		----- debug ------
//			String[] list = {"presiden", "antasari", "ri", "menanggapi", "dpr"};
//			keyWords = Arrays.asList(list);
//			System.out.println("#katakunci : " + Analyzer.getMatchKeywords(sentence, keyWords));
		
		return (float)Analyzer.getMatchKeywordsCount(sentence, keyWords)/(float)Analyzer.getSentenceLength(sentence, true);
	}
	
	public float getFeature5(String title, String sentence){
		List<String> keywordTitle = Analyzer.getMatchKeywords(title, keyWords);
		List<String> keywordSentence = Analyzer.getMatchKeywords(sentence, keyWords);
		
//		----- debug ------
//			String[] list = {"presiden", "antasari", "ri", "menanggapi", "dpr"};
//			keyWords = Arrays.asList(list);
//			System.out.println("intersection : " + Analyzer.getIntersection(keywordTitle, keywordSentence));
//			System.out.println("union : " + Analyzer.getUnion(keywordTitle, keywordSentence));
		
		return (float)Analyzer.getIntersection(keywordTitle, keywordSentence)/(float)Analyzer.getUnion(keywordTitle, keywordSentence);
	}
	
	public float getFeature6(String paragraph, String sentence, int sentenceNumber){
//		----- debug ------
			String[] list = {"presiden", "ri", "dpr"};
			keyWords = Arrays.asList(list);
			
		List<String> currentMatch = Analyzer.getMatchKeywords(sentence, keyWords);
		List<String> anotherMatch = Analyzer.getMatchKeywords(paragraph, keyWords, sentenceNumber);
		
//		---- debug -----
//			System.out.println("current:");
//			for(String s : currentMatch){
//				System.out.println(s);
//			}
//			System.out.println("another:");
//			for(String s : anotherMatch){
//				System.out.println(s);
//			}
		
		System.out.println("intersection : " + Analyzer.getIntersection(currentMatch, anotherMatch));
		System.out.println("union : " + Analyzer.getUnion(currentMatch, anotherMatch));
		return (float)Analyzer.getIntersection(currentMatch, anotherMatch)/(float)Analyzer.getUnion(currentMatch, anotherMatch);
	}
	
	public List<Integer> getFeature7(String news){
		int n = 0;
		List<Integer> listVector = new ArrayList<>();
		listVector.add(0);
		String[] sentences = Parser.parseToSentences(news);
		for(int i = 1; i < sentences.length; i++){
			n = 0;
			for(String s : Parser.parseToWords(sentences[i])){
				if(sentences[i-1].contains(s)){
					n++;
				}
			}
			if(n > 1){
				listVector.add(1);
			}
			else{
				listVector.add(0);
			}
		}
		return listVector;
	}
	
	public List<Integer> getFeature8(String news){
		int n = 0;
		List<Integer> listVector = new ArrayList<>();
		String[] sentences = Parser.parseToSentences(news);
		for(int i = 0; i < sentences.length - 1; i++){
			n = 0;
			for(String s : Parser.parseToWords(sentences[i])){
				if(sentences[i+1].contains(s)){
					n++;
				}
			}
			if(n > 1){
				listVector.add(1);
			}
			else{
				listVector.add(0);
			}
		}
		listVector.add(0);
		return listVector;
	}
	
	public int getLabel(String sentence, String summary){
		int label = 0;
		if(summary.contains(sentence)){
			label = 1;
		}
		return label;
	}
	
	public static void main(String[] args){
		String text = "Anggota Tim 9 DPR RI, Akbar Faisal mengaku kaget, Presiden RI Susilo Bambang Yudhoyono menanggapi penyataan mantan Ketua KPK Antasari Azhar soal pengucuran dana talangan (bail out) Bank Century pada Oktober 2008. Ia menyesalkan Presiden sampai menggelar pernyataan khusus di Istana negara pada Rabu (15/8/2012) malam untuk menanggapi hal tersebut. hal tersebut. Presiden diduga secara cepat memberikan tanggapan, untuk menghindari adanya interupsi dari anggota DPR dalam rapat gabungan di DPR hari ini";
		VectorGenerator gen = new VectorGenerator(text);
		
//		---- debug ----
			gen.setTitle("Anggota Tim 9 Kasus Century Kaget Presiden Tanggapi Pernyataan Antasari");
			
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
