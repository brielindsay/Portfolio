package assignment1;

import java.io.FileNotFoundException;

public class Analytics {
	/**
	 * An array of strings for the students results combined
	 * to split it into usable data use:
	 * for an item in the array at index i,
	 * String[] tokens = records[i].split(",");
	 * - tokens[0] = Student ID (numerical value stored as String)
	 * - tokens[1] = Start Date and Time (format DD/MM/YYYY HH:MM)
	 * - tokens[2] = End Date and Time (format DD/MM/YYYY HH:MM)
	 * - tokens[3] = Total Grade (numerical value stored as String)
	 * @note marks for each question are stored as Strings in tokens[4], tokens[5], ....
	 * - marks in first question (numerical value stored as String, but could be "-" implying non-attempt): tokens[4]
	 * - marks in second question (numerical value stored as String, but could be "-" implying non-attempt): tokens[5]
	 * ...
	 */
	public String[] records;
	public int[] weights;

	public Analytics(String filename) throws FileNotFoundException {
		records = DataReader.readData(filename);
		weights = DataReader.getWeights(filename);
	}

	/**
	 * 5 marks
	 * @return the number of questions on the quiz
	 */
	public int countQuestions() {
		return weights.length; //pass
	}	

	/**
	 * 5 marks
	 * @return the total marks for the quiz
	 */
	public int quizTotal() {
	    int markTot = 0;
		for(int i = 0; i < weights.length; i++) {
			markTot = markTot + weights[i];
		}
		return markTot; //pass
	}	

	/**
	 * 10 marks
	 * @param questionNumber
	 * @return the weight of the given question number.
	 * return 0 if questionNumber is not valid.
	 * for example, 
	 * IF THERE ARE 31 questions, any value OUTSIDE 1 to 31 is invalid.
	 * IF THERE ARE 50 questions, any value OUTSIDE 1 to 50 is invalid.
	 */
	public int getQuestionWeight(int questionNumber) {
		if(questionNumber > weights.length) {
			return 0;
		}
		if(questionNumber <= 0) {
			return 0;
		}
		return weights[questionNumber-1];//pass
	}

	/**
	 * DO NOT MODIFY
	 * PROVIDED AS A SAMPLE
	 * @return number of students who get a zero
	 */
	public int countZeroes() {
		int count = 0;
		for(int i=0; i < records.length; i++) {
			String[] tokens = records[i].split(",");
			if(Integer.parseInt(tokens[3]) == 0) {
				count++;
			}
		}
		return count;//pass
	}

	/**
	 * 10 marks
	 * @param passMark
	 * @return number of students who score at least the passing mark provided as the parameter
	 */
	public int countPasses(int passMark) {
		int count = 0;
		for(int i=0; i < records.length; i++) {
			String[] tokens = records[i].split(",");
			if(Integer.parseInt(tokens[3]) >= passMark) {
				count++;
			}
		}
		return count;//pass
	}

	/**
	 * 10 marks
	 * @return the overall average percentage marks for the quiz. 
	 * @note the mark for a given attempt is the 4th value in the record
	 */
	public double avgQuizPercent() {
		double total = 0;
		for(int i=0; i < records.length; i++) {
			String[] tokens = records[i].split(",");
			total = total + ((Double.parseDouble(tokens[3])/(double)quizTotal())*100);
			}
		return total/records.length; //pass
	}

	/**
	 * 10 marks
	 * @return the highest mark for the quiz
	 */
	public int highestMark() {
		int high = 0;
		for(int i=0; i < records.length; i++) {
			String[] tokens = records[i].split(",");
			if(Integer.parseInt(tokens[3]) >= high) {
				high = Integer.parseInt(tokens[3]);
			}
		}
		return high;//pass
	}

	/**
	 * 10 marks
	 * @param id
	 * @return the best mark for the student with given id 
	 * (return 0 if an attempt for that id doesn't exist)
	 */
	public  int bestMarkByStudent(int id) {
			int high = 0;
			for(int i=0; i < records.length; i++) {
				String[] tokens = records[i].split(",");
				if(id == Integer.parseInt(tokens[0])){
					if(Integer.parseInt(tokens[3]) > high){
						high = Integer.parseInt(tokens[3]);
					}
			}
			}
			return high;//pass
	}

	/**
	 * 10 marks
	 * @param id
	 * @return the average mark for the student with given id 
	 * (return 0 if an attempt for that id doesn't exist)
	 */
	public double avgMarkByStudentId(int id) {
		double total = 0;
		double count = 0;
		double av = 0;
			for(int i=0; i < records.length; i++) {
			    String[] tokens = records[i].split(",");
		    	if(tokens[0].equals("" + id)){
				    total = total + Double.parseDouble(tokens[3]);
		    		count++;
			    	}
		    if(count > 0)
			   av = total/count;
		}
		return av;//pass
	}

	/**
	 * 5 marks
	 * @param questionNumber
	 * @return the average percentage mark for the given question number.
	 * if questionNumber = 1, return the average percentage mark for the first question, and so on...
	 * 
	 * NOTE: some students might not attempt a particular question, indicated by a dash ("-").
	 * these values don't contribute towards attempts or average. 
	 * For example, let the following be the first entries in the array records:
	 * 
	 * records[0] = "551102,14/2/18 10:52,14/2/18 11:00,2,1,1"
	 * records[1] = "595789,14/2/18 12:27,14/2/18 14:08,1,-,1"
	 * records[2] = "463521,14/2/18 13:26,17/2/18 16:32,1,-,0"
 	 * records[3] = "610197,14/2/18 17:04,14/2/18 17:31,0,0,0"
 	 * 
 	 * If you look at Question 1 (5th comma-separate value), the marks are: "1", "-", "-" and "0".
 	 * Thus the average should be, mathematically, (1+0)/2, which should be returned as (maintaining precision) 0.5
	 */
	public double avgPercentByQuestion(int questionNumber) {
		double total = 0;
		double count = 0;
		double av = 0;
		if(questionNumber > 0 && questionNumber < weights.length + 1) {
			for(int i=0; i < records.length; i++) {
			    String[] tokens = records[i].split(",");
		    	if(!tokens[questionNumber+3].equals("-")){
				    total = total + (Double.parseDouble(tokens[questionNumber+3])/getQuestionWeight(questionNumber));
		    		count++;
			    	}
			}
		    if(count > 0) {
			   av = (total/count)*100;
		    }
		}
		return av;//pass
	}
		
	/**
	 * 10 marks
	 * @return the number of the hardest question (based on average mark).
	 * return 1 if the first question was the hardest, 2 if the second was and so on...
	 */
	public int hardestQuestion() {
		double hard = 100;
		int hardQ = 0;
		double compare = 0;
		int i = 1;
		for(i=1; i < weights.length + 1; i++) {
			compare = avgPercentByQuestion(i);
		    if(compare < hard) {
			   hard = compare;
			   hardQ = i;
			}
		}
		return hardQ; //to be completed
	}

	/**
	 * 5 marks
	 * @param id
	 * @return an array containing marks of all attempts for student with given id
	 * return an empty array if an attempt for student with given id doesn't exist.
	 */
	public int[] getStudentAttempts(int id) {
		int count = 0;
		int loc = 0;
		for(int j = 0; j < records.length;j++) {
			String[] tok = records[j].split(",");
			if(id == Integer.parseInt(tok[0])) {
				count++;
			}
		}
		int arr [] = new int [count];
		for(int i = 0; i < records.length;i++) {
			String[] tokens = records[i].split(",");
			if(id != Integer.parseInt(tokens[0])) {
				continue;
			}
			else if(id == Integer.parseInt(tokens[0])) {
				arr[loc] = Integer.parseInt(tokens[3]);
				loc++;
			}
			
		}
		return arr;//pass
	}
	
	
	/**
	 * 5 marks
	 * 
	 * @return an array containing question numbers from the hardest to easiest (based on average marks in the questions)
	 */
	public int[] getHardestToEasiest() {
		int [] arr = new int [weights.length];
		double hard = 100;
		int Q = 0;
		double compare = 0;
		int i = 1;
		for(i=1; i < weights.length + 1; i++) {
			for(int j = 1; j < weights.length; j++)
			compare = avgPercentByQuestion(j);
		    if(compare < hard) {
			   hard = compare;
			   Q = i;
			}
		    arr[weights.length-i] = Q;
		    hard = avgPercentByQuestion(i);
		   
		}
		return arr; //handed in incomplete
	}

	/**
	 * 5 marks
	 * @param idx index in array records for which the time must be returned
	 * @return time in the formats specified in the JUnit tests
	 * 
	 * there are three components in time (for our purpose) - days, hours and minutes.
	 * only those components must be included that have a non-zero value
	 * (the only exception being when the quiz starts and finishes during the same minute,
	 * in which case it should return "0 minutes").
	 * a comma separates two components, except the last two, where "and" is the separator.
	 * 
	 * see JUnit tests for more clarity
	 * 
	 */
	public String timeToDisplayByIndex(int idx) {
		String mins = "days,hours,minutes";
		for(int i=0; i < records.length; i++) {
		String[] tokens = records[i].split(",");
		String[] timeSplit = tokens[i].split("and");
		double start = Double.parseDouble(tokens[1]);
		double end = Double.parseDouble(tokens[2]);
		}
		
		return mins; //handed in incomplete 
	}
}

