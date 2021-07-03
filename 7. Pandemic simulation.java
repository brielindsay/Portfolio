import java.util.*;
import java.awt.geom.*;

class Population {
	public ArrayList<Simulant> sims = new ArrayList<Simulant>();
	public Random rand = new Random();

	public Population(){
        for(int i = 0; i < Main.SIMS; i++){
            sims.add(new Simulant());
        }

        // make the first one sick
        sims.get(0).sick = 1;   
	}

	/** update an entire population
	  * for each simulant update their location and illness
	  * then check each pair of simulants to see if they are in 
	  * touch with one another.  If they are, they might get sick
	  **/
	public void update(){
		for(Simulant s: sims){
            s.updateLoc();
            s.updateIllness();
        }
        // check every pair of sims to see if they are in contact, if so, pass on any sickness
        for(Simulant s1: sims){
            for(Simulant s2: sims){
                if (s1.loc.distance(s2.loc) < Simulant.SIZE){
                    s1.sick = s1.sick == 0 && s2.sick > Main.CONT_AFTER && !s1.immune && rand.nextInt(100) < Main.TRANS_RATE ? 1 : s1.sick;
                    s2.sick = s2.sick == 0 && s1.sick > Main.CONT_AFTER && !s2.immune && rand.nextInt(100) < Main.TRANS_RATE ? 1 : s2.sick;
                }
            }
        }
	}

	/** 
	  * @return  the average of the x locations of all sick simulants
	  *          answer is zero if no sims are sick
	  **/
	public double averageXOfSick(){
		double avg = 0;
		double count = 0;
		for(int i = 0;i < sims.size();i++) {
			if(sims.get(i).sick >= 1) {
			    avg = avg + sims.get(i).loc.getX();
			    count++;
			}
		}
		if(count == 0) {
			return 0;
		}
		avg = avg/count;
		return avg; //PASSED
	}

	/** 
	  * @return  the average of the y locations of all sick simulants
	  *          answer is zero if no sims are sick
	  **/
	public double averageYOfSick(){
		double avg = 0;
		double count = 0;
		for(int i = 0;i < sims.size();i++) {
			if(sims.get(i).sick >= 1) {
			avg = avg + sims.get(i).loc.getY();
			count ++;
			}
		}
		if(count == 0) {
			return 0;
		}
		avg = avg/count;
		return avg;//PASSED
	}

	/**
	  * @return  the first sim found who's is over the given point
	  *          you should use the whole area the simulant is drawn
	  *          on (i.e. center plus radius) to determine if the sim
	  *          is at this location.  Return null if no sim found.
	  **/
	public Simulant simAtLocation(Point2D loc){
		int index = 0;
		for(int i = 0;i<sims.size();i++) {
			if(loc.distance(sims.get(i).loc.getX(), sims.get(i).loc.getY())<= Main.SIM_SIZE) {
					return sims.get(i); 
			}
		}
		return null; // to be CHECKED ***
	}

	/**
	  * @param sim the simulant to find in the population
	  * @return  the simulant one index greater than
	  *          the given simulant. Return null if `sim`
	  *          not found or there is no simulant after the
	  *          found one.
	  **/
	public Simulant nextAfter(Simulant sim){
		int index = 0;
		int check = 0;
		for(int i = 0;i<sims.size();i++) {
			if(sims.get(i).equals(sim)) {
				index = i;
				check = 1;
			}
		}
		if(check == 1 && index != sims.size()-1) {
		return sims.get(index + 1);
		}
		return null; //Passed
	}
        
	/**
	  * @param sim the simulant to find in the population
	  * @return  the simulant one index less than
	  *          the given simulant. Return null if `sim`
	  *          not found or there is no simulant before the
	  *          found one.
	  **/
	public Simulant prevBefore(Simulant sim){
		int index = 0;
		int check = 0;
		for(int i = 0;i<sims.size();i++) {
			if(sims.get(i).equals(sim)) {
				index = i;
				check = 1;
			}
		}
		if(check == 1 && index != 0) {
		return sims.get(index -1);
		}
		return null; //Passed
	}

	/**
	  * @return the total number of sick simulants
	  **/
	public int numberSick(){
		int count = 0;
		for(int i = 0; i<sims.size();i++)
		if(sims.get(i).sick > 0) {
			count ++;
		}
		return count; // PASSED
	}

	/**
	  * @return An array list of all the simulants
	  *         sorted by "sickness".  Sorting order is 
	  *         determined by the `compareTo` method on
	  *         the simulants.
	  **/
	public ArrayList<Simulant> sort(){
			Collections.sort(this.sims);
			return this.sims;
	}
}


import java.awt.geom.*;
import java.util.*;

class Simulant implements Comparable<Simulant> {
	public static final int SIZE = 5;
	public Point2D loc;
	public int sick;
	public boolean immune;

	private double dir;
	private double speed;
	public Point2D homeLoc;
	public double mobility;
	private Random rand;

	public Simulant(){
		rand = new Random();
		homeLoc = new Point2D.Double(rand.nextInt(Main.WIDTH), rand.nextInt(Main.HEIGHT));
		loc = new Point2D.Double(homeLoc.getX(),homeLoc.getY());
		sick = 0;
		immune = false;
		dir = Math.random()*2*Math.PI;
		speed = Math.random();
		mobility = rand.nextInt(Main.MAX_MOVE);
	}

	/**
	 ** updates the location of the simulant on each animation frame according to
	 ** it's movement settings
	 **/
	public void updateLoc(){
		if (sick >=0){
			Point2D nLoc = (Point2D)loc.clone();
			nLoc.setLocation( nLoc.getX() + speed * Math.cos(dir)
				            , nLoc.getY() + speed * Math.sin(dir));

			if (nLoc.distance(homeLoc) > mobility || nLoc.getX() < 0 || nLoc.getY() < 0 || nLoc.getX() > Main.WIDTH || nLoc.getY() > Main.HEIGHT){
				dir = Math.random()*2*Math.PI;
			} else {
				loc = nLoc;
			}
		}
	}

	/** 
	 ** adjusts the simulant's illness status according to the rules
	 ** of the illness.  Is asymptomatic for 240 frames, then is ill
	 ** with a 3% chance of dying and then is immune if they survive
	 ** to 240 frames
	 **/
	public void updateIllness(){
		if (sick > Main.CONT_AFTER){
			if (rand.nextInt((Main.ILL_FOR - Main.CONT_AFTER)*100) < Main.DEATH_RATE){
				sick = -1;
			}
		}
		if (sick > 0){
			sick++;
		}
		if (sick > Main.ILL_FOR){
			sick = 0;
			immune = true;
		}

	}

	/** compares simulants using sickness.
	  * A simulant who is earlier in their sickness is "less than" one later in their
	  * sickness.  A simulant who is dead is "less than" any other.  A simulant who is
	  * immune is "greater than" any that is sick.  A simulant that has never been 
	  * sick is "less than" one that has been sick.
	  * @return 0 if this simulant is equal to the other, less than zero if it is less than and
	  *         greater than zero if it is greater than
	  **/
	public int compareTo(Simulant other){
		if(this.immune == true || other.sick <0 ) {//this immune or other dead
			return 1;
		}
		if(other.immune == true || this.sick < 0) {//this is dead or other immune
			return -1;
		}
		else if(this.sick == other.sick) {
			return 0;
		}
		else if (this.sick < other.sick) {
			return -1;//this is less than
		}
		else if(this.sick > other.sick) {
			return 1;//this is greater than
		}
		return 0;//returns this if they are ==
	}

}