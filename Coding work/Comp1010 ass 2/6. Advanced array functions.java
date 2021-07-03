package assignment2template;

//no tests provided. write your own junit tests
public class Polygon {
	public Point[] points;//create array

	/**
	 * create an instance copy of the array p into the instance variable points.
	 * also, for each item of p, create an instance copy of that item into the corresponding item in array points.
	 * Thus, p and points should NOT refer to the same Point[] instance.
	 * Also, for all i, p[i] and points[i] should NOT refer to the same Point instance.
	 * @param p
	 */
	public Polygon(Point[] p) {
		Point [] points = new Point [p.length];
		for(int i = 0;i < p.length;i++) {
			points[i].x = p[i].x;
			points[i].y = p[i].y;
		}
	}
	
	/**
	 * 
	 * @return the circumference of the polygon, that is the sum of
	 * distance between adjacent points. note that the first and last items are adjacent too.
	 */
	public double circumference() {
		double dis = 0;
		for(int i = 0;i < points.length;i++) {
			dis = dis + points[i].distance(points[i+1]);
		}
		dis = dis + points[0].distance(points[points.length-1]);
		return dis;
	}

	/**
	 * 
	 * @return the distance of the longest edge/line of the polygon.
	 */
	public double longestSide() {
		double lo = 0;
		for(int i = 0; i < points.length;i++) {
			if(points[i].distance(points[i+1])>= lo) {
				lo = points[i].distance(points[i+1]);
			}
		}
		if(points[0].distance(points[points.length-1])>= lo) {
			lo = points[0].distance(points[points.length-1]);
		}
		
		return lo; 
	}
}

package assignment2template;

public class Point {
	//create variables
	public int x;
	public int y;
	//constructors
	public Point() {
		this.x = 0;
		this.y = 0;
	}//passed
	public Point(int x, int y) {
		if(x<0) {
		    this.x = 0;
		}
		else if(x>=0) {
			this.x = x;
		}
		if(y<0) {
			this.y = 0;
		}
		else if(y>=0) {
			this.y = y;
		}
	}//passed
	public Point(Point whatever) {
		this.x = whatever.x;
		this.y = whatever.y;
		
	}//passed
	public double distance(Point a) {
		double xDis2 = 0;
		double yDis2 = 0;
		if(this.x > a.x) {
			xDis2 = Math.pow(this.x - a.x,2);
		}
		else if(this.x < a.x) {
			xDis2 = Math.pow(a.x - this.x,2);
		}
		if(this.y > a.y) {
			yDis2 = Math.pow(this.y - a.y,2);
		}
		else if(this.y < a.y) {
			yDis2 = Math.pow(a.y - this.y,2);
		}
		return Math.sqrt(xDis2 + yDis2);
		}//passed
	
	public Point midPoint(Point a) {
		int xLoc = (this.x + a.x)/2;
		int yLoc = (this.y + a.y)/2;
		return new Point(xLoc,yLoc);
	}//passed
	
	public Point shift(Point a) {
		int xLoc = this.x + a.x;
		int yLoc = this.y + a.y;
		return new Point(xLoc,yLoc);
	}//Passed
	
	public Point shift(int x, int y) {
		int xLoc = this.x + x;
		int yLoc = this.y + y;
		return new Point(xLoc,yLoc);
	}//passed
	
	public boolean equals(Point a) {
	if(this.x == a.x && this.y == a.y) {
	return true;
	}
	else
	return false;
	}//passed
	
	public String toString() {
		return '('+ String.valueOf(this.x) + ','+ String.valueOf(this.y)+ ')';
	}//passed
	
	public String getPathToOrigin() {
		String path = "";
		if(this.x <= 0 && this.y <= 0) {
			return this.toString();
		}
			if(this.x>this.y) {
				path = path + this.toString() + " -> ";
		        this.x = this.x - 1;
		        }
			else{
				path = path + this.toString() + " -> ";
				this.y = this.y - 1;
				}
		return path + this.getPathToOrigin();
	}//passed
	
	public Point[] getPointsOnPathToOrigin() {
		String path = this.getPathToOrigin();
		String pTo[] = path.split(" ");
		Point [] arr = new Point [(pTo.length/2) +1];//pTo.length/2];
		int pLoc = 0;
		for(int i = 0; i < pTo.length;i++) {
			if(pTo[i].charAt(0) == '(' ) {
		    arr[pLoc] = new Point(Character.getNumericValue(pTo[i].charAt(1)),Character.getNumericValue(pTo[i].charAt(3)));
		    System.out.print(arr[pLoc]);
		    pLoc++;
			}
		}
		return arr;
	}

	
		

	
}

package assignment2template;

public class Line {
	public Point a, b;

	public Line(Point _a, Point _b) {
		this.a = _a;
		this.b = _b;
	}

	public double length() {
		return a.distance(b);
	}

	/**
	 * 
	 * @return an array containing all points on the line segment such that for every point in the array,
	 * both x and y are integers
	 * For example, if there is a line from (1,1) to (5,3), the only other point for which both x and y are 
	 * integers is (3,2). Thus the method should return the array containing (1,1), (3,2) and (5,3)
	 * The order of points in the array returned should be from the point with lower x 
	 * (use Point a as the starting point in case a.x == b.x).
	 * 
	 * Hence if a = (5,1) and b=(1,3), the method returns an array such that 
	 * first item is (1,3), second is (3,2) and third is (5,1)
	 */
	public Point[] getPointsOnCrosshair() {
		int len = Math.abs(this.a.y-this.b.y);//length of array
		int rise = (this.b.y-this.a.y)/(len);//increase of x for every new point
		int run = (this.b.x-this.a.x)/(len);//increase of y for every new point
		Point [] Points = new Point[len+1];//create the final array to return
		for(int i = 0; i < Points.length;i++) {
			Points[i]= new Point(this.a.x,this.a.y);
			this.a.x = this.a.x + run;
			this.a.y = this.a.y + rise;
			}
		
		
	return Points;//not complete - hardcoded.
	}
}
	
