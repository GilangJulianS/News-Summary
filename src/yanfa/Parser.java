package yanfa;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Parser {

	private String string;
	private String cleanString;
	private String[] words;
	private String[] sentences;
	private String[] paragraphs;
	private WordAndLocation[] wordAndLocation;
	
	public Parser(String string){
		this.string = string;
		cleanString = deleteStopWords(string);
		paragraphs = parseToParagraphs(cleanString);
		sentences = parseToSentences(cleanString);
		words = parseToWords(cleanString);
		wordAndLocation = getAllWordLocation(string);
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

	public static String deleteStopWords(String s){
		String[] stopWords = {"ke", "di", "yang", "pada", "untuk", "juga", "akan","dan","kalau","Yang"};
		for(String sWords : stopWords){
			s = s.replace(" "+sWords+" ", "");
		}
		return s;
	}
	
	public static String[] parseToWords(String s){
		String delims = "[ ,.'\\\"\n/]+";
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
	
	public static String getTitle(String fullText){
		// ----- stub -----
			return "Anggota  Tim  9  Kasus  Century  Kaget Presiden Tanggapi Pernyataan Antasari";
	}
	
	public static void main(String[] args){
		Parser parser = new Parser("Forum Indonesia untuk Transparansi Anggaran (Fitra) telah menduga PT "
				+ "Ghalia Indonesia Printing tak akan berhasil menyelesaikan tender naskah ujian nasional."
				+ "Koordinator Investigasi dan Advokasi Uchok Sky Khadafi menilai proses tender perusahaaan "
				+ "tersebut ganjil. \"Dari awal saya sudah menduga ini bermasalah,\" katanya saat dihubungi, Ahad, 14 "
				+ "April 2013.\n"
				+ "Uchok menyebutkan, dalam lelang, Ghalia menawarkan harga yang lebih tinggi, Rp "
				+ "22,8 miliar. Sedangkan perusahaan lainnya, PT Aneka Ilmu memberi menawarkan Rp 17 miliar, PT "
				+ "Jasuindo Tiga Perkasa Rp 21,1 miliar, dan PT Balebat Dedikasi Prima Rp 21,6 miliar. Namun "
				+ "kementerian Pendidikan dan Kebudayaan tetap memenangkan perusahaan tersebut. Selain itu, "
				+ "Ghalia ternyata tak hanya mengikuti satu paket lelang. Perusahaan itu juga ikut dalam lelang tiga "
				+ "paket lainnya.\n"
				+ "Menurut Uchok, ini merupakan bukti Ghalia tak mempertimbangkan kapasitas "
				+ "perusahannya. \"Yang penting menang, dan akhirnya bermasalah,\" ujarnya. PT Ghalia Indonesia "
				+ "Printing adalah perusahaan yang mencetak naskah ujian untuk 11 provinsi. Provinsi tersebut yakni "
				+ "Kalimantan Selatan, Kalimantan Timur, Sulawesi Utara, Sulawesi Tengah, Sulawesi Selatan, "
				+ "Sulawesi Tenggara, Bali, Nusa Tenggara Barat, Nusa Tenggara Timur, Gorontalo, dan Sulawesi "
				+ "Barat. Ghalia mengaku kesulitan memasukkan naskah ke boks per sekolah hingga membuat ujian "
				+ "nasional tingkat SMA, MA, dan SMK untuk ke-11 provinisi tersebut ditunda. \"Kalau mencetak,"
				+ "kami sudah selesai, tapi ketika memasukan ke boks per sekolah, itu yang kami kesulitan,\" kata "
				+ "Direktur Ghalia, Hamzah Lukman.\n"
				+ "Ujian nasional awalnya akan diselenggarakan serentak Senin "
				+ "besok, 15 April 2013. Karena terlambat, jadwal untuk Senin, yakni ujian Bahasa Indonesia, "
				+ "dipindah pekan depan. Untuk Selasa, yakni Bahasa Inggris dan Fisika/Ekonomi, ditunda 23 April "
				+ "2013. Sementara itu, untuk mata pelajaran Matematika yang seharusnya Rabu, 17 April, digeser "
				+ "ke hari Jumat, 19 April 2013."
				+ "");
		parser.printWordAndLocation();
	}
}
