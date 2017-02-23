/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.util.LinkedList;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ListIterator;


/**
 *   A list of postings for a given word.
 */
public class PostingsList implements Serializable 
{
    
    /** The postings list as a linked list. */
    private LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();
    private HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
    
    public PostingsList()
    {
    	this.list = list;
    	this.map = map;
    }


    /**  Number of postings in this list  */
    public int size() 
    {
    	return list.size();
    }

    /**  Returns the ith posting */
    public PostingsEntry get( int i ) 
    {
    	return list.get( i );
    }
    
    public ListIterator getIterator()
    {
    	return this.list.listIterator();
    }
    
    

    //
    //  YOUR CODE HERE
    //
    
    public void add(PostingsEntry entry)
    {
    	
    	if(this.map.isEmpty())
    	{
    		this.list.add(entry);
    		this.map.put(entry.docID, 1);
    	}
    	else if(!this.map.containsKey(entry.docID))
    	{
    		this.list.add(entry);
    		this.map.put(entry.docID, 1);
    	}
    }
}
	

			   
