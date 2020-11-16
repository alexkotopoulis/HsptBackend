package hspt.backend;

public class Word {
	String id;
	String word;
	String meaning;
	String type;
	int pass=0;
	int fail=0;
	static int avgPass=0;
	
	public Word(String word, String meaning, String type) {
		this.word=word;
		this.meaning=meaning;
		this.type=type;
	}
	
	public Word(String [] line) {
		this.id= line[0];
		this.word=line[1];
		this.meaning=line[3];
		this.type=line[2];
	}
	
	public String[] toArray() {
		return new String[] {id, word, type, meaning, ""+pass, ""+fail};
	}
	
	public String[] toUserStatsArray() {
		return new String[] {id, word, ""+pass, ""+fail};
	}
	
	public String toString() {
		return word;
	}
	
	// 0: fail; 1: rare; 2: pass
	public int getCategory() {
		if (pass < fail) 
			return 0;
		else if (pass <= avgPass)
			return 1;
		else
			return 2;

	}

	public String getWord() {
		return word;
	}

	public String getMeaning() {
		return meaning;
	}
	
	public int getTotalPass() {
		return this.pass;
	}
	
	public int getTotalFail() {
		return this.fail;
	}
	
	
}
