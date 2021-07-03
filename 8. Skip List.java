import java.util.*;

class SLNode {
	
	int data; // Contents of the node
	int level; // the level of this node
	SLNode next; // points to the next node on this level;
	SLNode down; // points to the node containing the duplicated data on the level below
	
	SLNode() {
		data=-1; level=0; next=null; down= null; // makes a dummy node on level 0
	}
	
	
	SLNode (int newData) { // makes a node containing data on level 0
		data= newData;
		level= 0;
		next= null;
		down= null;
	}
	
	SLNode (SLNode below) { // constructor to duplicate node below
		if (below!=null) {
			data= below.data; // duplicate data
			level= below.level+1; // level is one up
			next= null;
			down= below; // points to the node being duplicated
		}	
	}
	
		
	SLNode searchFirstExact (SLNode sList, int key) { //ToDo - P level
			// Pre : sList is valid
			// Post : returns the address of the node with the *highest* level
			// in sList whose data field matches the search term.
		if(sList.data == key) {//base case when data == key
			return sList;//returns the node (list)
		}
		
		if(sList.next != null) {
			if(!largerThan(sList.next,key)) {//if list next data isnt greater than key move to next
				if(sList.next == null) {//if null node doesn't exist in list
					return null;
				}
				return sList.searchFirstExact(sList.next, key);//moves to the next node recursively 
			}	
			else if(largerThan(sList.next,key)) {//if list next data is greater than key move down
				if(sList.down == null) {//if down is null, on lvl 0 - thus doesnt exist in list
					return null;
					}
				}
			}
		
			if(sList.down != null) {//down isnt null can move forward
				return sList.searchFirstExact(sList.down, key);//moves down
			}
				
			return null;//returns null when key doesnt exist in list
		}
	
	
				boolean largerThan(SLNode list, int key) {//helper method 
					if(list.data>key) {//if data is larger than key returns true
						return true;
					}
					return false;//returns false if data is smaller than key
				}
	
			
	SLNode searchFirstAtLeast (SLNode sList, int key) { //ToDo -- D/HD level
		
		// Pre : sList is valid
		// Post : returns the address of the node on the *highest* level in sList
		// so that all items reachable in the list from there have data value *at least*
		// the value of key, and any non-reachable item has data value strictly
		// less than the key.
		// Returns null if there is no such node.
		SLNode temp = sList.findNext(sList, key);//makes temp the value of node satisfying conditions above
		if(temp== null && sList.down!=null) {//if temp is null move down
			return sList.down.searchFirstAtLeast(sList.down, key);
		}
		if(sList.data == key) {//checks if list data equals key
			return sList;// returns list
		}
		if(temp != null) {//continues if temp isnt null
			if(sList.down == null) {//no more moving down
				return temp;
			}
			SLNode tempBelow = sList.down.searchFirstAtLeast(sList.down, key);//creates a second node
			if(temp.data-key > tempBelow.data-key) {//updates node if tempbelow is smaller
				 return tempBelow; 
			}
			return temp;
		}
		return null;
	}
				SLNode findNext(SLNode list, int key) {
					if(list.next == null) {//returns if null
						return null;
					}
					if(list.next.data >= key) {
						return list.next;
					}
					return list.next.findNext(list.next, key);
				}
	
	
	SLNode insert(SLNode sList, int toBeInserted, int topLevel) { // ToDo -- P level
		// Pre : sList is valid, and topLevel is no more than sList.level+1
		// Post : returns the first node of the list with the value toBeInserted inserted in sList
		// at level topLevel and on all levels below.
		// The returned list should be valid.
		// Details for insertion:
		// (1) if topLevel == sList.level+1, create a new level and insert
		// the node in this level
		// (2) For all other levels (below topLevel), find the place to insert: it must be in between the
		// node that is less than, and before the node that is greater than toBeInserted'
		// Do not insert if toBeInserted is already in the list.
		// (3) add the down links as appropriate..
		if(toBeInserted < 0||topLevel > sList.level+1||topLevel < 0) {
			return sList;
		}
		if(sList.level < topLevel) {
			SLNode temp = new SLNode();
			temp.level = topLevel;
			temp.next = new SLNode(toBeInserted);
			temp.next.level = topLevel;
			temp.down = sList.insert(sList, toBeInserted, topLevel-1);
			temp.next.down = temp.searchFirstExact(temp,toBeInserted);
			return temp;
		}
		if(sList.onLevel(sList, toBeInserted)) {
			return sList;
		}
		if(topLevel < sList.level) {
			sList.down = sList.down.insert(sList.down, toBeInserted, topLevel);
			return sList;
		}
		if(sList.level == 0) {
			if(sList.next == null) {
				sList.next = new SLNode(toBeInserted);
				return sList;
			}
			if(sList.next.data > toBeInserted) {
				SLNode hold = sList.next;
				sList.next = new SLNode(toBeInserted);
				sList.next.next = hold;				
				return sList;
			}
			sList.next = insert(sList.next, toBeInserted, topLevel);
			return sList;
		}
		if(sList.next == null) {
			sList.next = new SLNode(toBeInserted);
			sList.next.level = sList.level;
			sList.down = insert(sList.down,toBeInserted,topLevel-1);
			sList.next.down = sList.searchFirstExact(sList,toBeInserted);
			return sList;
		}
		if(sList.next.data > toBeInserted) {
			SLNode hold = sList.next;
			sList.next = new SLNode(toBeInserted);
			sList.next.next = hold;
			sList.next.level = sList.level;
			sList.down = insert(sList.down,toBeInserted,topLevel-1);
			sList.next.down = sList.searchFirstExact(sList,toBeInserted);
			return sList;
		}
		sList.next = insert(sList.next, toBeInserted, topLevel);
		return sList;		
	}
	

	SLNode remove(SLNode sList, int toBeRemoved) { // ToDo D/HD Level
		// Pre: Assume that each data value occurs exactly once on each level (individually).
		//      sList is valid
		if(sList == null||sList.searchFirstExact(sList, toBeRemoved) == null) {
			return sList;
		}
		// Post : returns the first node of the skip list with any nodes containing values equal to
		// toBeRemoved removed (i.e. on every level)
		//The returned list should contain all other nodes and must be valid.
		SLNode list = removeHelper(sList, toBeRemoved);
		if(list.next == null) {
			return(list.down);
		}
		return list;
	}
				SLNode removeHelper(SLNode list, int toBeRemoved) {
					if(onLevel(list,toBeRemoved)) {
						if(list.next!= null&&list.next.data == toBeRemoved) {
							list = rem(list,toBeRemoved);//remove node
							return list;
						}
						if(list.next!=null) {
							list.next = removeHelper(list.next, toBeRemoved);//moves to the next
						}
					}
					if(list.down != null) {
						list.down = removeHelper(list.down,toBeRemoved);
						
					}
					return list;
				}
				
				boolean onLevel(SLNode list,int toBeRemoved) {
					if(list.next != null) {
						if(list.next.data == toBeRemoved) {
							return true;
						}
						return onLevel(list.next,toBeRemoved);
					}
					return false;
				}
				
				SLNode rem(SLNode list,int toBeRemoved) {
					if(list.next == null) {
						return list;
					}
					if(list.next.data==toBeRemoved) {
						if(list.next.next == null) {
							list.next = null;
						}
						else{
							list.next = list.next.next;//removes node on current level
						}
					}
					else if(list.next.data !=toBeRemoved) {
						list.next = rem(list.next,toBeRemoved);
					}
					
					if(list.level==0) {
						return list;
					}
					list.down = rem(list.down,toBeRemoved);
					return list;
				}
				
				
}

 class SkipList {
	 SLNode top; // The top node in a Skip List
	 // class invariant:
	 // This defines a valid Skip List structure for SkipList;
	 // The first node on every level has data field (set to -1);
	 // All other data fields  have value at least zero;
	 // All nodes on the same level form an (increasing) sorted linked list; 
	 // All nodes are reachable from top, by following links (next or down);
	 // Any node on a non-zero level has a path to the zero layer following down links, going through
	 // nodes with strictly decreasing levels, but all with the same data;
	 // The nodes on the same level form a subset of node (data values) on the level below.
	 
	 SkipList(SLNode sList){ // Constructor
		 // Pre: sList is valid
		 top= sList;
	 }
	 
	 
	 int findLastLevel(int lvl) { //ToDo -- P level 
		 // POST: returns the value in the last node on level lvl
		 if(this.top == null||!this.validate()) {//if list is null or invalid
			 return -1;
		 }
		 
		 if(lvl < 0||lvl >this.top.level) {//base case - level is less than 0 or more than top level - returns -1 when level doesnt exsist in list
			 return -1; 
			 }	
		 return findLevelHelper(this.top,lvl);//calls helper method
	 	}
	 
	 
				 int findLevelHelper(SLNode list, int lvl) {//moves to bottom level end node
					 if(list.level != lvl) {//calls till level is equal
						 return findLevelHelper(list.down,lvl);
					 }
					 if(list.next != null) {//moves next
						return findLevelHelper(list.next,lvl);
					 }
						 return list.data;//returns end node (lvl 0)
					 }

				 
	 int[] findShortestPath(int key) { // ToDo -- CR level
		 // POST: Returns the array of node values (including dummy values) on any shortest path from
		 // the current top node to any node containing data value key on the bottom level		 
		 // Return null if no node's data value matches key.
		 // Note: in general there could be more than one shortest path. In the final
		 // testing this case will not arise.
		 if(!this.validate()||this.top==null) {
			 return null;
		 }
		return spHelper(this.top,key); //calls helper
	 }
	 
	 
			 int[] spHelper(SLNode list, int key) {//helper to find shortest path 
				 if(list.searchFirstExact(list, key)!=null) {//checks if value is in list
					 SLNode temp = list;//creates a temp node to contain data to add
					 int[] arr = new int [len(list,key)];//creates array (named arr)
					 for(int i = 0;i<arr.length;i++) {//cycles through the array 
					 	 arr[i]=temp.data;//updates value of array at i to = temps data value
						 temp = nodeAdd(list.next,key);//updates temp to the next value
					 }
				 return arr;// returns arr
				 }
				 return null;//if value doesnt exsist returns null
			 }
	 
			 
			 int len (SLNode list, int key) {//helper method to find the length of shortest path
				 if(this.top==null||!this.validate()|| list.searchFirstExact(list, key) == null) {
					 return 0;
				 }
				 if(list.data == key) {//base case - found node = key
					 return 1;
				 }
				 if(list.next != null) {//checks if next is null
					 if(largerThan(list.next,key)) {//if next data greater than key, move down
						 return 1 + len(list.down,key);
					 	}
					 return 1 + len(list.next,key);//next data is not larger than key, move across
				 	}
				  return 1 + len(list.down,key);//end of this level is reached move down
			 	}
			 
			 
			 SLNode nodeAdd(SLNode list,int key) {//helper method moves to the next node in the short path
				 if(list.data == key) {//returns when key is = to data
					 return list;
				 }
				 if(largerThan(list,key)||list.next == null) {//returns down when list data is greater than key or next is null (end of list
					 return list.down;
				 }
				 return list.next;//returns the next node
			 }
			 
	 
			 boolean largerThan(SLNode list, int key) {// helper method to find if list data is > or less than key
					if(list.data>key) {//returns true if list is larger
						return true;
					}
					return false;//returns false is key is larger
				}
	 
			 
	 int lengthShortest(int key) { // ToDo -- CR level
		 // POST: Returns the length of the shortest path from 
		 // the current top node to any node containing data value key on the bottom level.
		 // Returns 0 if there is no such node whose data value matches key
		 if(this.top != null) {
		  return this.len(this.top, key);//calls helper to find length
		 }
		 return 0;
	 }
	 
	 
	 boolean validate(){ // ToDo -- CR level
		 // POST:  Returns true if the list is valid (i.e. satisfies the class invariant).
		 // Otherwise returns false.
		 
		 /* Starting from any node a in the skip list, all nodes b, c, d, etc. reachable by following the next
		    field, i.e. b= a.next. c= b.next, d = c.next have the same level number, but have strictly increasing 
		    data values.
		   */
		 	if(this.top == null) {
		 		return false;
		 	}
		 	
			if(!inOrder(this.top)) {//calls helper to check the order of nodes, if not true returns false
				return false;
			}			
			
		 /* Starting from any node a in the skip list, all nodes b, c, d, etc. reachable by following the down
				 field, i.e. b= a.down. c= b.down, d = c.down have the same data value, but strictly decreasing
				 level numbers, and the level numbers decrease in steps of 1.
		  */
			if(!beneathHelper(this.top)) {//calls helper to check nodes data below are same as above, if not true returns false
				return false;
			}
		 
		 /* Any node which is the last node on a particular level has its next field set to null.
		  */
		int value = getToEnd(this.top);//finds the value of the last node
		if(!checkLevelEnd(this.top,value)){//calls helper to check all last nodes = null, if not true returns false
			return false;
		}
	   	 /* Any node with its level field set to 0 has its down field set to null.
	   	    Any node with its level field set to a value at least 1 has its down field set to a non-null node
		 */
	   	  if(!levelCheck(this.top)) {//calls helper to check levels of list, if level check returns false list is not valid
			 return false;
			}
	   	  
	   	 /* Dummy node not set to -1
	   	  */
	   	  if(!dummy(this.top)) {//checks all dummy values are -1
	   		  return false;
	   	  }
		 return true;//node is valid
	 }
	 		boolean dummy(SLNode list) {
	 			if(list.data == -1 && list.level == 0) {
	 				return true;
	 			}
	 			if(list.data == -1 && list.level != 0) {
	 				return dummy(list.down);
	 			}
	 			return false;
	 		}
	 
			 
			int getToEnd(SLNode list) {//helper method to get to end of a list (bottom level)
				 if(list.level!=0) {//moves down if level is not 0
					 return getToEnd(list.down);
				 }
				 if(list.next != null) {//moves next if next is not null
					 return getToEnd(list.next);
				 }
				 return list.data;//returns end node
			 }
	 
			 boolean checkLevelEnd (SLNode list, int end) {//helper method to check end node on each level is pointing to null
				 if(list.level == 0) {//level 0 contains all nodes - last one is end and points to null
					 return true;
				 }
				 if(list.data > end) {//if data is greater than end, it is not a valid list
					 return false;
				 }
				 if(list.next != null) {//moves down - not at end
					 return checkLevelEnd(list.next,end);
				 }
				 if(list.next == null) {//comes to the end of a list, moves down and continues
					 return checkLevelEnd(list.down,end);
				 }
				 return false;
			 }
	 
			 boolean levelCheck(SLNode list) {//helper method checks that nodes on levels not 0 do not point to null and 0 level nodes point to null
				 if(list.next == null) {//base case - when there is no next node
					 if(list.level == 0) {//when level is 0
						 if(list.down == null) {//this is true return true
							 return true;
						 }
						 else {
							 return false;//node on level 0 points down to something
						 }
					
					 }
					 else if(list.level > 0) {//when level is above 0
						 if(list.down != null) {//this is true, move to the next node on level same level
							 return true;
						 }
						 else {
							 return false;//node on level is pointing to null, should be pointing to node.
						 }
				 }
				 if(list.level == 0) {//when level is 0
					 if(list.down == null) {//this is true, move to the next node on level 0
						 return levelCheck(list.next);
					 }
					 else {
						 return false;//node on level 0 points down to something
					 }
				
				 }
				 }
				 else if(list.level > 0) {//when level is above 0
					 if(list.down != null) {//this is true, move to the next node on level same level
						 return levelCheck(list.next);
					 }
					 else {
						 return false;//node on level is pointing to null, should be pointing to node.
					 }
				 }
				 return false;//should only hit this point if node level is below 0, thus invalid.
			 }
			 
			 boolean beneathHelper(SLNode list) {//checks that those with level 
				 if(beneath(list) && list.down == null) {
					 return true;
				 }
				 else if(beneath(list) && beneathHelper(list.down)) {
					 return true;
				 }
				 return false; //
			 }
			 
			 boolean beneath(SLNode list) {//check all nodes beneath another have the same data
				 
				 if(list.level == 0) {
					 return true;
				 }
				 if(list.down== null) {
					 return false;
				 }
				 if(list.next == null && list.data == list.down.data) {
					 return true; 
				 }
				 else if(beneath(list.next) && list.data == list.down.data) {
					return true;
				 }
				 
				 return false;
			}
		
			boolean inOrder(SLNode list) {
				 if(list.level > 0) {//moves down to the bottom layer
					 return inOrder(list.down);
				 }
				 if(list.next == null) {//base case no next node
					 return true;
				 }
				 if(list.data < list.next.data) {//if true moves to the next
					 return inOrder(list.next);
				 }
				 return false;// returns false if nodes aren't in order
			 }
			 	
	 int traverseAndAdd(int selectedLevel) { // ToDo -- P level		 
		 // POST: Returns the sum of the non-negative-valued nodes on level equal to selectedLevel 
		 // Return 0 if selected level does not appear in the list		
		 if(!this.validate()||this.top == null) {
			 return 0;
		 }
		 return addHelper(this.top,selectedLevel);
	 }
		 
		     int addHelper(SLNode list,int selectedLevel) {
		    	 if(selectedLevel > list.level) {//level does not appear in list return 0
					 return 0;
				 }
		     	 if(selectedLevel == list.level) {//on correct level
		 			 if(list.next == null) {//no next to go to (base case)
						 return list.data +1;
					 }
					 if(selectedLevel == list.level) {
						 return list.data + addHelper(list.next,selectedLevel);
					 }
		    	 }
		     	 return addHelper(list.down,selectedLevel);
			 }
     	 
	 int[] subsequence(int start, int end ){ // ToDo D/HD level
		 // POST:  Returns the (increasing) sorted array of all nodes drawn from (bottom level of original list)
		 // such that all the nodes have data at least value start and at most value end
		 if(end<start||this.top == null||!this.validate()||this.findLastLevel(0)<start) {
			 return null;
		 }
		 
		 return subHelper(this.top,start, end);	 
	 }

	 
			 int[] subHelper(SLNode list, int start, int end) {//helper to find shortest path 
				 if(list.level !=0) {//moves down
					 return subHelper(list.down,start,end);
				 }
				 if(list.next!=null && list.data<start) {//moves across
					 return subHelper(list.next,start,end);
				 }
				 if(list.data >=start && list.next != null) {
					 SLNode temp = list;//creates a temp node to contain data to add
					 int[] arr = new int [lenSub(list, end)];//creates array (named arr)
					 for(int i = 0;i<arr.length;i++) {//cycles through the array 
					 	 arr[i]=temp.data;//updates value of array at i to = temps data value
						 temp = temp.next;//updates temp to the next value
					 }
				 return arr;// returns arr
				 }
				 return null;//if value doesnt exsist returns null
			 }
		
			 
			 int lenSub (SLNode list,int end) {//helper method to find the length of shortest path
				 if(list.data == end) {//end is found adds one and returns
					 return 1;
				 }
				 if(list.data > end) {//checks data beyond end
					 return 0;//data is beyond value of end returns 
				 }
				 if(list.next != null) {//checks if next is null
					 return 1 + lenSub(list.next,end);//adds 1 and moves to the next node
				 	}
				  return 1;//ends length
			 	}
						 
		 
	 int countAllNodes() { // ToDo -- P level ***pass base tests***
		 // POST:  Returns the total number of SLNodes in the current Skip List
		 // (The count includes all "dummy" nodes at the start of each level.)
		 if(!this.validate()) {
			 return 0;
		 }
		 return countLevel(this.top);
	 }
				 int countLevel(SLNode list) {
					 if(list.level <= 0) {
						 return countHelper(list);
					 }
					 return countHelper(list) + countLevel(list.down);
				 }
				 
				 int countHelper(SLNode list) {//helps with count all nodes
				        if(list.next != null) {
				        	return 1 + countHelper(list.next);
				        }
					 return 1;
				 }
 }

 


