package student;

import java.util.*;


enum Polarity {
	POS, NEG, NEUT, NONE;
}

enum Strength {
	STRONG, WEAK, NONE;
}

public class Tweet {
	
	Polarity gPol; //Gold Polarity of the tweet (0 = neg, 2 = neutral, 4 = pos, null = not given;
	Polarity pre;
	String id; // ID of the tweet 
	String date;
	String user;
	String text;
	int order;
	Boolean annotate;
	TreeSet<String> adj;
	String componentList;

	

	public Tweet(String p, String i, String d, String u, String t) {
		switch(p){
			case("0"):
				gPol = Polarity.NEG;
			break;
			case("2"):
				gPol = Polarity.NEUT;
			break;
			case("4"):
				gPol = Polarity.POS;
			break;
			default:
				gPol = Polarity.NONE;
				break;
			} 
		pre = Polarity.NONE;
		id = i;
		date = d;
		user = u;
		text = t;
		adj = new TreeSet<String>();
		order = -1;
		annotate = false;
		componentList = i;
	}
	public boolean isMarked(){
		return annotate;
	}
	
	public void setMarked(){
		annotate = true;
	}
	public void orderVisited(int a) {
		order = a;
	}
	
	public void addNeighbour(String ID) {
		// PRE: -
		// POST: Adds a neighbour to the current tweet as part of graph structure
		adj.add(ID);
	}

	public Integer numNeighbours() {
		// PRE: -
		// POST: Returns the number of neighbours of this tweet
		return adj.size();
	}

	public void deleteAllNeighbours() {
		// PRE: -
		// POST: Deletes all neighbours of this tweet
		adj.clear();
	}

	public Vector<String> getNeighbourTweetIDs () {//like ADJs vertex.java
		// PRE: -
		// POST: Returns IDs of neighbouring tweets as vector of strings
		Iterator<String> i = adj.iterator();
		Vector<String> nId = new Vector<String>();
		while(i.hasNext()) {
			nId.add(i.next());
		}
		return nId;
	}

	public Boolean isNeighbour(String ID) {
		// PRE: -
		// POST: Returns true if ID is neighbour of the current tweet, false otherwise
		return adj.contains(ID);
	}


	public Boolean correctlyPredictedPolarity () {
		// PRE: -
		// POST: Returns true if predicted polarity matches gold, false otherwise
		if(this.gPol==this.pre) {
			return true;
		}
		return false;
	}

	public Polarity getGoldPolarity() {
		// PRE: -
		// POST: Returns the gold polarity of the tweet
		return this.gPol;
	}

	public Polarity getPredictedPolarity() {
		// PRE: -
		// POST: Returns the predicted polarity of the tweet
		return this.pre;
	}

	public void setPredictedPolarity(Polarity p) {
		// PRE: -
		// POST: Sets the predicted polarity of the tweet
		this.pre = p;
	}

	public String getID() {
		// PRE: -
		// POST: Returns ID of tweet
		return this.id;
	}

	public String getDate() {
		// PRE: -
		// POST: Returns date of tweet
		return this.date;
	}

	public String getUser() {
		// PRE: -
		// POST: Returns identity of tweeter
		return this.user;
	}

	public String getText() {
		// PRE: -
		// POST: Returns text of tweet as a single string
		return this.text;
	}

	public String[] getWords() {
		// PRE: -
		// POST: Returns tokenised text of tweet as array of strings

		if (this.getText() == null)
			return null;

		String[] words = null;

		String tmod = this.getText();
		tmod = tmod.replaceAll("@.*?\\s", "");
		tmod = tmod.replaceAll("http.*?\\s", "");
		tmod = tmod.replaceAll("\\s+", " ");
		tmod = tmod.replaceAll("[\\W&&[^\\s]]+", "");
		tmod = tmod.toLowerCase();
		tmod = tmod.trim();
		words = tmod.split("\\s");

		return words;

	}

}

package student;


import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.junit.Test;

public class TweetCollection {

	private TreeMap<String, Tweet> map;
	private TreeMap<String,Polarity> basicWords;
	private TreeMap<String,FineWords> fWords;
	private TreeMap<String,Vector<String>> links;

	public TweetCollection() {// Constructor
		map = new TreeMap<String, Tweet>();//initialises map
		basicWords = new TreeMap<String,Polarity>();//inicialises words
		fWords = new TreeMap<String,FineWords>();
		links = new TreeMap<String,Vector<String>>();
	}

	/*
	 * functions for accessing individual tweets
	 */

	public Tweet getTweetByID (String ID) {
		// PRE: -
		// POST: Returns the Tweet object that with tweet ID
		return map.get(ID);
	}

	public Integer numTweets() {
		// PRE: -
		// POST: Returns the number of tweets in this collection
		return map.size();
	}


	/*
	 * functions for accessing sentiment words
	 */

	public Polarity getBasicSentimentWordPolarity(String w) {
		// PRE: w not null, basic sentiment words already read in from file
		// POST: Returns polarity of w
		Polarity re = basicWords.get(w);
		return re;
	}

	public Polarity getFinegrainedSentimentWordPolarity(String w) {
		// PRE: w not null, finegrained sentiment words already read in from file
		// POST: Returns polarity of w
		FineWords temp = fWords.get(w);
		Polarity re = temp.fwPol;
		return re;

	}

	public Strength getFinegrainedSentimentWordStrength(String w) {
		// PRE: w not null, finegrained sentiment words already read in from file
		// POST: Returns strength of w
		FineWords temp = fWords.get(w);
		Strength re = temp.strength;
		return re;
	}

	/*
	 * functions for reading in tweets
	 * 
	 */


	public void ingestTweetsFromFile(String fInName) throws IOException, CsvException {
		// PRE: -
		// POST: Reads tweets from .csv file, stores in data structure

		// NOTES
		// Data source, file format description at http://help.sentiment140.com/for-students
		// Using opencsv reader: https://zetcode.com/java/opencsv/

		try (CSVReader reader = new CSVReader(new FileReader(fInName))) {
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) {                // nextLine[] is an array of values from the line

				Tweet tw = new Tweet(nextLine[0], // gold polarity
						nextLine[1], 				// ID
						nextLine[2], 				// date
						nextLine[4], 				// user
						nextLine[5]);				// text
				map.put(tw.id, tw);
			}
		}        
	}

	/*
	 * functions for sentiment words 
	 */

	public void importBasicSentimentWordsFromFile (String fInName) throws IOException {
		// PRE: -
		// POST: Read in and store basic sentiment words in appropriate data type

		try (BufferedReader reader = new BufferedReader(new FileReader(fInName))) {
			String curLine;
			Polarity pol;
			while ((curLine = reader.readLine()) != null) {
				String[] nextLine = curLine.split(" ", 2);
				switch(nextLine[1]){
				case("positive"):
					pol = Polarity.POS;
				break;
				case("negative"):
					pol = Polarity.NEG;
				break;
				default:
					pol = Polarity.NEUT;
					break;
				} 
				basicWords.put(nextLine[0],pol);
			}
			reader.close();
		}

	}


	public void importFinegrainedSentimentWordsFromFile (String fInName) throws IOException {
		// PRE: -
		// POST: Read in and store finegrained sentiment words in appropriate data type
		try (BufferedReader reader = new BufferedReader(new FileReader(fInName))) {
			String curLine;
			while ((curLine = reader.readLine()) != null) {
				String[] nextLine = curLine.split(" ", 6);
				nextLine[0] = nextLine[0].substring(5);//Type
				nextLine[1] = nextLine[1].substring(4);//Len
				nextLine[2] = nextLine[2].substring(6);//Word1
				nextLine[3] = nextLine[3].substring(5);//pos1
				nextLine[4] = nextLine[4].substring(9);//stemmed
				nextLine[5] = nextLine[5].substring(14);//prior Polarity
				FineWords temp = new FineWords(nextLine[0],nextLine[1],nextLine[2],nextLine[3],nextLine[4],nextLine[5]);
				fWords.put(temp.word, temp);
				//System.out.println("");
				//System.out.println(nextLine[0]);
				//System.out.println(nextLine[1]);
				//System.out.println(nextLine[2]);
				//System.out.println(nextLine[3]);
				//System.out.println(nextLine[4]);
				//System.out.println(nextLine[5]);
			} 
		}
	}


	public Boolean isBasicSentWord (String w) {
		// PRE: Basic sentiment words have been read in and stored
		// POST: Returns true if w is a basic sentiment word, false otherwise
		boolean result = basicWords.containsKey(w);
		return result;
	}

	public Boolean isFinegrainedSentWord (String w) {
		// PRE: Finegrained sentiment words have been read in and stored
		// POST: Returns true if w is a finegrained sentiment word, false otherwise
		boolean result = fWords.containsKey(w);
		return result;
	}

	public void predictTweetSentimentFromBasicWordlist () {
		Iterator<Tweet> tweetList = createIter();
		Polarity p = Polarity.NONE;
		while(tweetList.hasNext()) {
			int pos=0;
			int neg=0;
			Tweet tweet = tweetList.next();
			String[] list = tweet.getWords();
			if (list != null) {
				for(String w : list) {
					Polarity pol = basicWords.get(w);
					//System.out.println("word is " + w + " it is "+ pol);
					if(pol != null) {
						if(pol == Polarity.NEG) {
							neg++;		
						}
						if(pol == Polarity.POS) {
							pos++;
						}

					}
				}

				p = Polarity.NONE;
				if(pos > neg && pos !=0) {
					p = Polarity.POS;
				}
				else if(pos < neg && neg !=0) {
					p = Polarity.NEG;
				}
				else if(pos == neg && pos > 0) {
					p = Polarity.NEUT;
				}
			}
			tweet.setPredictedPolarity(p);
		}

	}

	public void predictTweetSentimentFromFinegrainedWordlist (Integer strongWeight, Integer weakWeight) {
		// PRE: Finegrained word sentiment already imported
		// POST: For all tweets in v, tweet annotated with predicted sentiment
		//         based on count of sentiment words in sentWords
		Iterator<Tweet> tweetList = createIter();
		Polarity p = Polarity.NONE;

		while(tweetList.hasNext()) {
			int pos=0;
			int neg=0;
			Tweet tweet = tweetList.next();
			String[] list = tweet.getWords();
			if (list != null) {
				for(String w : list) {
					FineWords fw = fWords.get(w);
					if(fw != null) {
						if(fw.fwPol == Polarity.NEG) {
							if(fw.strength == Strength.STRONG) {
								neg = neg + (1*strongWeight);	
							}
							else if(fw.strength == Strength.WEAK) {
								neg = neg + (1*weakWeight);	
							}
							else {
								neg++;
							}
						}
						if(fw.fwPol == Polarity.POS) {
							if(fw.strength == Strength.STRONG) {
								pos = pos + (1*strongWeight);	
							}
							else if(fw.strength == Strength.WEAK) {
								pos = pos + (1*weakWeight);	
							}
							else {
								pos++;
							}
						}

					}
				}
				p = Polarity.NONE;
				if(pos > neg && pos !=0) {
					p = Polarity.POS;
				}
				else if(pos < neg && neg !=0) {
					p = Polarity.NEG;
				}
				else if(pos == neg && pos > 0) {
					p = Polarity.NEUT;
				}
			}
			tweet.setPredictedPolarity(p);
		}
	}

	/*
	 * functions for inverse index
	 * 
	 */

	public Map<String, Vector<String>> importInverseIndexFromFile (String fInName) throws IOException {
		// PRE: -
		// POST: Read in and returned contents of file as inverse index
		//         invIndex has words w as key, IDs of tweets that contain w as value
		try (BufferedReader reader = new BufferedReader(new FileReader(fInName))) {
			String curLine;
			String[] temp;
			while ((curLine = reader.readLine()) != null) {
				String[] nextLine = curLine.split(" ", 2);
				temp = nextLine[1].split(",");//Len
				//System.out.println("length = " + temp.length);
				links.put(nextLine[0], new Vector<String>(Arrays.asList(temp)));
			}
		}
		return links;
	}


	/*
	 * functions for graph construction 
	 */

	public void constructSharedWordGraph(Map<String, Vector<String>> invIndex) {
		// PRE: invIndex has words w as key, IDs of tweets that contain w as value
		// POST: Graph constructed, with tweets as vertices, 
		//         and edges between them if they share a word
		Vector<String> idList;
		Tweet tweet;
		for(Map.Entry<String, Vector<String>> i : invIndex.entrySet()) {
			idList = i.getValue();
			for(String ids : idList) {
				tweet = map.get(ids);
				if(tweet != null) {
					for(String id : idList) {
						if(!id.equals(ids) && map.containsKey(id)) {
							tweet.addNeighbour(id);
						}
					}
				}
			}
		}
	}


	public Integer numConnectedComponents() {
		// PRE: -
		// POST: Returns the number of connected components

		ArrayList<String> connected = new ArrayList<String>();
		for(Tweet tweetTemp : map.values()) {
			if(!tweetTemp.isMarked()){
				tweetTemp.setMarked();
				if(!connected.contains(tweetTemp.componentList)) {
					connected.add(tweetTemp.componentList);
				}
			}
		}
		AnnotateAllFalse();
		return connected.size()-1;
	}

	public void annotateConnectedComponents() {
		// PRE: -
		// POST: Annotates graph so that it is partitioned into components

		this.AnnotateAllFalse();
		for(Tweet temp : map.values()) {
			if(!temp.isMarked()) {
				Vector<String> nei = temp.getNeighbourTweetIDs();
				temp.setMarked();
				for(int a = 0; a < nei.size();a++) {
					map.get(nei.get(a)).setMarked();
					map.get(nei.get(a)).orderVisited(a);
					map.get(nei.get(a)).componentList = temp.id;
				}
			}
		}
		this.AnnotateAllFalse();	
	}

	public void AnnotateAllFalse(){
		for(Tweet set: map.values()) {
			set.annotate = false;
		}
	}



	public Integer componentSentLabelCount(String ID, Polarity p) {
		// PRE: Graph components are identified, ID is a valid tweet
		// POST: Returns count of labels corresponding to Polarity p in component containing ID
		Tweet t = map.get(ID);
		String head = t.componentList;
		int count = 0;
		for(Tweet tempTw : map.values()) {
			if(tempTw.componentList == head && !tempTw.isMarked() && tempTw.getPredictedPolarity() == p) {
				tempTw.setMarked();
				count++;
			}
		}
		AnnotateAllFalse();
		return count;
	}


	public void propagateLabelAcrossComponent(String ID, Polarity p, Boolean keepPred) {
		// PRE: ID is a tweet id in the graph
		// POST: Labels tweets in component with predicted polarity p 
		//         (if keepPred == T, only tweets w pred polarity None; otherwise all tweets)
		String cl = map.get(ID).componentList;
		Collection<Tweet> list = map.values();
		for(Tweet temp : list) {
			if(!temp.isMarked()&& cl == temp.componentList) {
				if(!keepPred || temp.pre == Polarity.NONE) {
					temp.pre = p;
				}
			}
		}
	}



	public void propagateMajorityLabelAcrossComponents(Boolean keepPred) {
		// PRE: Components are identified
		// POST: Tweets in each component are labelled with the majority sentiment for that component
		//       Majority label is defined as whichever of POS or NEG has the larger count;
		//         if POS and NEG are both zero, majority label is NONE
		//         otherwise, majority label is NEUT
		//       If keepPred is True, only tweets with predicted label None are labelled in this way
		//         otherwise, all tweets in the component are labelled in this way
		HashMap<String, Vector<Tweet>> components = new HashMap<String,Vector<Tweet>>();
		for(Tweet s : map.values()) {
			Vector<Tweet> comp = components.get(s.componentList);
			if(comp == null) {
				comp = new Vector<Tweet>();
				comp.add(s);
				components.put(s.componentList, comp);
			}
			else {
				comp.add(s);
			}
		}
		for(Vector<Tweet> tws : components.values()) {
			int posCount = 0;
			int negCount = 0;
			for(Tweet temp : tws) {
				if(temp.pre == Polarity.NEG) {
					negCount++;
				}
				else if(temp.pre == Polarity.POS) {
					posCount++;
				}
			}
			
			Polarity major = Polarity.NONE;
			if(negCount + posCount != 0 ) {
				if(negCount>posCount) {
					major = Polarity.NEG;
				}
				else if(posCount>negCount) {
					major = Polarity.POS;
				}
				else {
					major = Polarity.NEUT;
				}
			}
			for(Tweet tw : tws) {
				if(keepPred ||tw.pre.equals(Polarity.NONE)) {
					tw.pre = major;
				}
			}
		}
	}

	/*
	 * functions for evaluation 
	 */

	public Double accuracy () {
		// PRE: -
		// POST: Calculates and returns accuracy of labelling
		double cor = 0;
		double pre = 0;
		double result = 0.0;
		Iterator<Tweet> tweetList = createIter();
		while(tweetList.hasNext()) {
			Tweet tweet = tweetList.next();
			//System.out.println("Predicted " + tweet.getPredictedPolarity());
			//System.out.println("Gold " + tweet.getGoldPolarity());
			if(tweet.getPredictedPolarity().equals(tweet.getGoldPolarity())&& !tweet.getPredictedPolarity().equals(Polarity.NONE)) {
				cor++;
				//System.out.println("Correct Plus 1 " + cor);
			}
			if(!tweet.getPredictedPolarity().equals(Polarity.NONE)&&!tweet.getGoldPolarity().equals(Polarity.NONE)) {
				pre++;
				//System.out.println("predicted Plus 1 " + pre);
			}			

		}
		if(pre !=0) {
			result = cor/pre;
		}
		//System.out.println("Correct: " + cor);
		//System.out.println("Predicted: " + pre);
		//System.out.println("Result: " + result);
		return result;
	}

	public Double coverage () {
		// PRE: -
		// POST: Calculates and returns coverage of labelling
		double predicted = 0;
		double count = 0;
		double result = 0; 
		Iterator<Tweet> gIt = createIter();
		while(gIt.hasNext()) {
			Tweet check = gIt.next();//help with tutor ***
			if(!check.gPol.equals(Polarity.NONE)) {
				count = count+ 1;
				if(!check.pre.equals(Polarity.NONE)) {
					predicted= predicted+1;
				}	
			}
		}
		if(count != 0 ) {
			result = predicted/count;
		}
		return result;
	}

	public Iterator<Tweet> createIter(){
		Iterator<Tweet> temp = map.values().iterator();
		return temp;
	}
}

package student;

public class FineWords {

	Strength strength;
	String length;
	String word;
	String pos1;
	String stemmed1;
	Polarity fwPol;

	public FineWords(String str, String len, String wor, String pos, String stem, String pol) {
		switch(str){
		case("strongsubj"):
			strength = Strength.STRONG;
		break;
		case("weaksubj"):
			strength = Strength.WEAK;
		break;
		default:
			strength = Strength.NONE;
			break;
		} 
		length = len;
		word = wor;
		pos1 = pos;
		stemmed1 = stem;
		switch(pol){
		case("negative"):
			fwPol = Polarity.NEG;
		break;
		case("neutral"):
			fwPol = Polarity.NEUT;
		break;
		case("positive"):
			fwPol = Polarity.POS;
		break;
		default:
			fwPol = Polarity.NONE;
			break;
		} 

	}
}
