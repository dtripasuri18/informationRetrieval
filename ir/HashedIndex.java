/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012-14
 */  


package ir;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.lang.Math;
//import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
//private int intersection = 0;
//private int phrase = 1; 
//private int ranked = 2;




/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>();
    
    public HashedIndex()
    {
    	this.index = index;
    }


    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) 
    {
	//
	//  YOUR CODE HERE
	//
    	PostingsList idx = this.index.get(token);
		if (idx == null) {
			
			idx = new PostingsList();
			this.index.put(token, idx);
		}
		PostingsEntry toAdd = new PostingsEntry(docID, offset);
		idx.add(toAdd);
		
    	
    	
    	
    }


    /**
     *  Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
	// 
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
    	
    	Iterator<String> ret = index.keySet().iterator();
    	return ret;
    }


    /**
     *  Returns the postings for a specific term, or null
     *  if the term is not in the index.
     */
    public PostingsList getPostings( String token ) {
	// 
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
    	PostingsList ret = this.index.get(token);
    
    	return ret;
    }


    /**
     *  Searches the index for postings matching the query.
     */
    public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
	// 			System.out.print(word);

	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
    	System.out.println(queryType);
    	
    	LinkedList<String> words = query.terms;
    	//System.out.println(words);
    	// = new PostingsList();
    	//List<String> arr = new ArrayList<String>();
    	ArrayList<PostingsList> queries = new ArrayList<PostingsList>();
    	PostingsList ret;
    	
    	if(queryType == 0)
    	{
    		for(String x : words )
    		{
    			//arr.add(x);
    			queries.add(index.get(x));			
    			
    		}
    		System.out.println(queries.get(0));
    		ret = queries.get(0);
    		
    		if(queries.size() == 1)
    		{
    			ret = index.get(words.getFirst());
    			return ret;
    		}
    		
    		PostingsList x = queries.get(0);
    		for(int i = 1; i < queries.size(); i++)
    		{
    			System.out.println(i);
    			ret = new PostingsList();
    			PostingsList y = queries.get(i);
    			
    			ListIterator f1 = x.getIterator();
    			ListIterator f2 = y.getIterator();
    			
    			int countf1 = 0;
    			int countf2 = 0;
    			
    			do
    			{
    				PostingsEntry p = x.get(countf1);
    				System.out.println(p + "p1 docID: " +p.docID);
    				PostingsEntry p2 = y.get(countf2 );
    				System.out.println(p2+ "p2 docID" + p2.docID);
    				
    				if(p.docID == p2.docID)
    				{
    					System.out.println("I got here");
    					ret.add(p);
    					f1.next();
    					countf1++;
    					f2.next();
    					countf2++;
    				}
    				else if(p.docID < p2.docID)
    				{
    					f1.next();
    					countf1++;
    				}
    				else
    				{
    					f2.next();
    					countf2++;
    				}
    			}while(f1.hasNext() && f2.hasNext());  
    			
    			x = ret;
    		}
    		//System.out.println(ret.get(0));
    		return ret;
    	}	
    	
    	if(queryType == 1)
    	{
    		queries = new ArrayList<PostingsList>();
    		
    		for(String x : words )
    		{
    			//arr.add(x);
    			queries.add(index.get(x));			
    			
    		}
    		//System.out.println(queries.get(0));
    		
    		ret = queries.get(0);//no useful in anyway, just to silence error
    		
    		if(queries.size() == 1)
    		{
    			ret = index.get(words.getFirst()); // incase of a one word phrase query
    			return ret;
    		}
    		
    		PostingsList x = queries.get(0);       // set x = to the first terms postingslist
    		for(int i = 1; i < queries.size(); i++) // go through all the postings list
    		{
    			ret = new PostingsList();				//new merge PostingList
    			PostingsList y = queries.get(i);
    			
    			ListIterator f1 = x.getIterator();		//for iterations
    			ListIterator f2 = y.getIterator();		//for iterations
    			
    			int countf1 = 0;						//for access to elements
    			int countf2 = 0;						//for access to elements
    			
    			do
    			{
    				PostingsEntry p = x.get(countf1);											//first postingentry for x
    				System.out.println(p + " p1 docID: " +p.docID + " offset: " + p.offset );
    				PostingsEntry p2 = y.get(countf2 );											//posting entry for y
    				System.out.println(p2+ " p2 docID: " + p2.docID + " offset: " + p2.offset );
    				
    				if(p.docID == p2.docID)
    				{
    					System.out.println("They match");
    					if(p.offset - p2.offset == -1)			//check offset
    					{
    						ret.add(p2);						//add second posting entry for etended matches
    						System.out.println("Just added to merged postingsList");
    						f1.next();
        					countf1++;
        					f2.next();
        					countf2++;
    					} 
    					else if (p.offset < p2.offset)
    					{
    						System.out.println("Offset of one is less so increment 1 to find match");
    						f1.next();
        					countf1++;

    					}
    					else
    					{
    						System.out.println("Offset of two is less so increment 2 to find match");
    						
    						f2.next();
        					countf2++;
    					}
    					    					
    				}
    				else if(p.docID < p2.docID)
    				{
    					f1.next();
    					countf1++;
    				}
    				else
    				{
    					f2.next();
    					countf2++;
    				}
    			}while(f1.hasNext() && f2.hasNext());
    			
    			x = ret;
    			System.out.print
    		}
    		System.out.println(ret.get(0) + "ENDING");
    		return ret;
    	}	
    		/*for(int i = 0; i < queries.size()-1; i++ )
    		{
    			PostingsList x = queries.get(i);
    			System.out.println(x);
    			PostingsList y = queries.get(i+1);
    			System.out.println(y);
    			
    			ListIterator f1 = x.getIterator();
    			ListIterator f2 = y.getIterator();
    			
    			int tmp = f1.next().docID;
    			System.out.println(f1);
    			System.out.println(f2);
    			System.out.println(f2.next());
    			System.out.println(f2.next());
    			System.out.println(f1);
    			System.out.println(f2);
    			PostingsEntry tmp = f1.next().PostingsEntry;
    			
    			
    			
    			    			
    			do{
    				System.out.print(f1);
    				System.out.print(f2);
    				
    				PostingsEntry p = f1.next();
    				PostingsEntry p2 = f2.next();
    				
    				System.out.print(f1);
    				System.out.print(f2);
    				
    				if(p.docID == p2.docID)
    				{
    					ret.add(p);
    					f1.next();
    					f2.next();
    				}
    				else if(p.docID == p2.docID)
    				{
    					f1.next();
    				}
    				else
    				{
    					f2.next();
    				}
    				
    			}while(f1.hasNext() && f2.hasNext());
    		}
    			return ret;
    		}
    	}*/
    	
    	
    	PostingsList idx = index.get(words.getFirst());
    	/*for (String x : words) {
			//Set<String> answer = new HashSet<String>();
			//String word = _word.toLowerCase();
    		int count = 0;
			idx = index.get(x);
			if (idx != null) {
				for (PostingsEntry entry : idx) {
					count++;//answer.add(files.get(entry.docID));
				}
			}
			//System.out.print(x);
			//for (String f : answer) {
				//System.out.print(" " + f);
			//}
			//System.out.println(count + " results");
		}*/
    	
	return idx;
    }


    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
