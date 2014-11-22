
public class WordPlace {

	int article;
	int paragraph;
	int sentence;
	int word;
	
	public WordPlace(int article, int paragraph, int sentence, int word) {
		this.article = article;
		this.paragraph = paragraph;
		this.sentence = sentence;
		this.word = word;
	}

	public int getArticle() {
		return article;
	}

	public int getParagraph() {
		return paragraph;
	}

	public int getSentence() {
		return sentence;
	}

	public int getWord() {
		return word;
	}

}
