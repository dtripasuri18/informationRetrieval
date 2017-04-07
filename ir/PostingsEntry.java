/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.io.Serializable;
import java.util.Vector;


public class PostingsEntry implements Comparable<PostingsEntry>, Serializable {
    
    public int docID;
    public int offset;
    public Vector<Integer> offsets;
    public double score;

    /**
     *  PostingsEntries are compared by their score (only relevant 
     *  in ranked retrieval).
     *
     *  The comparison is defined so that entries will be put in 
     *  descending order.
     */
    public int compareTo( PostingsEntry other ) 
    {
    	return Double.compare( other.score, score );
    }

    //
    //  YOUR CODE HERE
    //
    public PostingsEntry(int fileno, int position) 
    {
		this.docID = fileno;
		this.offsets = new Vector<Integer>();
		//System.out.println("adding fileno " + fileno + " postion " + position);
		this.offsets.add(position);
		//this.score = this.offsets.size()+1;
		//this.offset = position;
	}
    /*public PostingsEntry(PostingsEntry oneOff, int position)
    {
    	this.docID = oneOff.docID;
    	this.offset = position;
    }*/

}

    
