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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;


/**
 *   A list of postings for a given word.
 */
public class PostingsList implements Serializable 
{
    
    /** The postings list as a linked list. */
    public LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();
    private HashMap<Integer,Integer> map = new HashMap<Integer, Integer>(); // map stores the index of the docID in the list so no need for O(n) lookup
    //private HashMap<Integer,Integer> termFreq = new HashMap<Integer, Integer>(); // map stores the number of times the docID has the term: docID, #term (tf)
    
    public PostingsList()
    {
    	this.list = list;
    	this.map = map;
    	//this.termFreq = termFreq;
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
    
    public void add(int docID, int offset)
    {
    	//System.out.println("adding the entry");
    	if(this.map.isEmpty())
    	{
    		//this.list.add(entry);
    		//System.out.println("map is empty");
    		PostingsEntry newEntry = new PostingsEntry(docID, offset); // create a new Psotings Entry
    		//System.out.println("made new entry");
    		this.list.add(newEntry); // add to the list
    		//System.out.println("added to the list");
    		int index = this.list.size()-1; // get size of the  postings list (Postings List is the ll of docs that the term appears in)
    		this.map.put(docID, index); //saved where the index of the document is saved. 
    		//this.termFreq.put(docID, 1);
    		//System.out.println("added the index of the document in the list, and added the term freq");
    	}
    	else if(this.map.get(docID) == null)
    	{
    		//System.out.println("docId doesnt exist");
    		PostingsEntry newEntry = new PostingsEntry(docID, offset);
    		//System.out.println("new entry");
    		this.list.add(newEntry);
    		///System.out.println("added to list");
    		int index = this.list.size()-1;
    		this.map.put(docID, index);
    		//System.out.println("update the index where the doc is stored");
    		//this.termFreq.put(docID, 1);	
    		
    	}
    	else
    	{
    		//System.out.println("doc exists");
    		 int index = this.map.get(docID);
    		 //System.out.println("get index of the doc, prevents having to search a linked list for it");
    		 PostingsEntry doc = this.list.get(index);
    		 doc.offsets.add(offset);
    		 //System.out.println("add the offset of the repeated word in the doc to the offsets vector");
    		 //int prevfreq = this.termFreq.get(docID);
    		 //this.termFreq.put(docID, prevfreq+1);
    		 //System.out.println("since the word exists more than once, update the term frequency to: " + prevfreq+1);
    		 //System.out.println("size of the termFreq map is: " + this.termFreq.size());
    		     				 
    	}
    }
    
    public void addQuery(PostingsEntry entry, int offset)
    {
    	PostingsEntry oneOff = new PostingsEntry(entry.docID, offset);
    	this.list.add(oneOff);
    }
    public void addQuery(PostingsEntry entry)
    {
    	//PostingsEntry oneOff = new PostingsEntry(entry.docID, offset);
    	this.list.add(entry);
    }
    
   /* public ArrayList<Integer> getTF()
    {
    	ArrayList<Integer> ret = new ArrayList<Integer>; //return TF scores for each posting entry
    	int norm = 0;
    	for(int i = 0; i < this.list.size(); i++)
    	{
    		int score = this.list.get(i).score; // get the score of the posting entry, ie the number of times it shows up in the doc
    		int sqr = score * score;
    		norm += sqr;
    	}
    	norm = Math.pow(norm, .5);
    	for(int i = 0; i < this.list.size(); i++)
    	{
    		ret.add(this.list.get(i).offsets.size()); //adds in the score of the PE and //divides it by the euc norm
    	}
    	return ret; // returns the normalized tf scores of the posting list
    	
    }
    public int getDF()
    {
    	return this.list.size();
    }*/
}
	

			   
