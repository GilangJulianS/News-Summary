import java.util.ArrayList;
import java.util.List;


public class WordAndLocation {
	
	String word;
	List<WordPlace> places;
	
	public WordAndLocation(String word){
		this.word = word;
		places = new ArrayList<>();
	}
	
	public WordAndLocation(String word, WordPlace wPlace){
		this.word = word;
		places = new ArrayList<>();
		places.add(wPlace);
	}
	
	public WordAndLocation(String word, List<WordPlace> places){
		this.word = word;
		this.places = places;
	}
	
	public void addWordPlace(WordPlace wPlace){
		places.add(wPlace);
	}

	public String getWord() {
		return word;	
	}

	public List<WordPlace> getPlaces() {
		return places;
	}
}
