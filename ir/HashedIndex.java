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
import java.lang.Runtime;
import java.util.Collections;
//import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
//private int intersection = 0;
//private int phrase = 1; 
//private int ranked = 2;

//long allocatedMemory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
//long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;


/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>(); //holds the index
    private HashMap<Integer, Integer> docSize = new HashMap<Integer, Integer>(); //maps the docsize to the number of tokens it has 
    
    public HashedIndex()
    {
    	this.index = index;
    	//this.docSize = docSize;
    }


    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) 
    {
	//
	//  YOUR CODE HERE
	//
    	//if(token.toLowerCase().equals("zombie"))
    	//	System.out.println("zombie");
    	//System.out.println(token + " " + docID);
    	PostingsList doclist = this.index.get(token);  // the Postinglist returned for the string
		if (doclist == null) {
			//System.out.println("no postlist");
			doclist = new PostingsList();					//UNCOMMENT TO SHOW WORKING INDEX
			this.index.put(token, doclist);
		}
		//PostingsEntry toAdd = new PostingsEntry(docID, offset);
		//System.out.println("adding to the poatingslist");
		doclist.add(docID, offset);
		//System.out.println("done adding it to postingslist");
		
    	if(this.docSize.isEmpty() || this.docSize.get(docID) == null)/*this.docSize.isEmpty() || !this.docSize.containsKey(token)*/
    	{
    		//System.out.println("doc not in second map, so adding it and set the value to 1");
    		this.docSize.put(docID, 1);
    	}
    	else if(this.docSize.get(docID) != null)/*this.docSize.containsKey(token)*/
    	{
    		
    		int currSize = this.docSize.get(docID);
    		//System.out.println("doc exists so now value is" + currSize );
    		this.docSize.put(docID, currSize +1);
    		//System.out.println("placed the shit and now size of the hasmap is: " + this.docSize.size());
    	}
    	
    	
    	
    	
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
    	//System.out.println(queryType);
    	
    	LinkedList<String> words = query.terms;
    	//System.out.println(words);
    	// = new PostingsList();
    	//List<String> arr = new ArrayList<String>();
    	ArrayList<PostingsList> queries = new ArrayList<PostingsList>();
    	ArrayList<Integer> dfArr = new ArrayList<Integer>();
    	ArrayList<Integer> tfArr = new ArrayList<Integer>();
    	PostingsList ret;
    	
    	if(queryType == 0) //intersection query
    	{
    		for(String x : words )
    		{
    			//arr.add(x);
    			queries.add(index.get(x));			//This should be moved outside the ifs
    			
    		}
    		//System.out.println(queries.get(0));
    		
    		ret = queries.get(0); // fix to handle no size input
    		
    		if(queries.size() == 1)
    		{
    			ret = index.get(words.getFirst());   // if single word, just return its postings list
    			return ret;
    		}
    		
    		PostingsList x = queries.get(0);
    		for(int i = 1; i < queries.size(); i++)  //else loop through the postingslist of the query words and find all posting entries that match 
    		{
    			//System.out.println(i);
    			ret = new PostingsList();
    			PostingsList y = queries.get(i);
    			
    			ListIterator f1 = x.getIterator();
    			ListIterator f2 = y.getIterator();
    			
    			int countf1 = 0;
    			int countf2 = 0;
    			
    			do
    			{
    				PostingsEntry p = x.get(countf1);
    				//System.out.println(p + "p1 docID: " +p.docID);
    				PostingsEntry p2 = y.get(countf2 );
    				//System.out.println(p2+ "p2 docID" + p2.docID);
    				
    				if(p.docID == p2.docID)
    				{
    					System.out.println("Found a match");
    					ret.addQuery(p);
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
    	
    	if(queryType == 1) //phrase query
    	{
    		words = query.terms;
    		queries = new ArrayList<PostingsList>();
    		
    		for(String x : words )
    		{
    			//arr.add(x);
    			System.out.println(x);
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
    			ret = new PostingsList();				
    													//new merge PostingsList because we want a new return PostingsList that selects from the previous merged list, 
    													//stored in x, and the new list to match, y, and stores that all in ret. 
    													//x has a list, y has the other, ret stores the merged
    													//then if there are >2 then x becomes the merged list and y becomes the next query word
    			
    			PostingsList y = queries.get(i);		// get the next words posting list
    			
    			ListIterator f1 = x.getIterator();		//for iterations
    			ListIterator f2 = y.getIterator();		//for iterations
    			
    			int countf1 = 0;						//for access to elements
    			int countf2 = 0;						//for access to elements
    			
    			do
    			{
    				PostingsEntry p = x.get(countf1);											//first postingentry for x
    				System.out.println("X size is : " + queries.size());
    				//System.out.println(p + " p1 docID: " +p.docID + " offset: " + p.offsets );
    				PostingsEntry p2 = y.get(countf2 );											//posting entry for y
    				//System.out.println(p2+ " { p2 docID: " + p2.docID + " offset: " + p2.offsets );
    				
    				if(p.docID == p2.docID)
    				{
    					System.out.println("They match");
    					
    					outerloop:
    					for(int a = 0; a < p.offsets.size(); a++)
    					{
    						for(int b = 0; b < p2.offsets.size(); b++)
    						{
    							if(p.offsets.get(a) - p2.offsets.get(b) == -1)
    							{
    								System.out.println(p + " p1 docID: " +p.docID + " offset: " + p.offsets.get(a) );
    								System.out.println(p + " p2 docID: " +p2.docID + " offset: " + p2.offsets.get(b) );
    								
    								ret.addQuery(p2, p2.offsets.get(b));
    								System.out.println("Just added to merged postingsList");
    	    						//f1.next();							//increment to next postings entry
    	        					//countf1++;							//incre
    	        					//f2.next();
    	        					//countf2++;
    								break outerloop;
    							}
    							else if (p.offsets.get(a) < p2.offsets.get(b))
    	    					{
    	    						System.out.println("Offset of one is less so increment 1 to find match");
    	    						//f1.next();
    	        					//countf1++;
    	    						break;
    	    					}
    							else
    	    					{
    	    						System.out.println("Offset of two is less so increment 2 to find match");
    	    						continue;
    	    						//f2.next();
    	        					//countf2++;
    	    					}
    						}
    					 }
    					
    					 f1.next();							//increment to next postings entry
    					 countf1++;							//incre
    					 f2.next();
    					 countf2++;
    					 System.out.println("Incrementing");
    				 }
    					
    					/*if(p.offset - p2.offset == -1)			//check offset
    					{
    						ret.add(p2);						//add second posting entry for extended matches
    						System.out.println("Just added to merged postingsList");
    						f1.next();							//increment to next postings entry
        					countf1++;							//incre
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
    					*/    					
    				
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
    		//System.out.println(ret.get(0) + "ENDING");
    		return ret;
    	}
    	
    	if(queryType == 2) //ranked query
    	{
    		words = query.terms;
    		queries = new ArrayList<PostingsList>();
    		//dfArr = new ArrayList<Integer>();
    		//tfArr = new ArrayList<ArrayList<Integer>>(); //gets the term freq scores of the postings list
    		
    		for(String x : words) //get the postings list
    		{
    			//arr.add(x);
    			System.out.println(x);
    			PostingsList p = index.get(x);
    			queries.add(p);			//got the postingslist of each ter
    			//dfArr.add(p.getDF());				//got the docFreq of each term
    			//tfArr.add(p.getTF()); 				// this should be one per term. ie tf is 1 per query term
    		}
    		
    		int qLen = queries.size(); //gets the length of the query
    		
    		PostingsList unionDocList = new PostingsList();
    		for(int i = 0; i < queries.size(); i++)
    		{
    			for(PostingsEntry doc : queries.get(i).list)
    			{
    				unionDocList.list.add(doc); // make one giant doc list with all documents that the words appear in
    			}
    		}
    		
    		for(PostingsEntry doc : unionDocList.list)
    		{
    			double score = cosineSimilarity(words, doc, qLen);
    			doc.score = score;
    		}
    		
    		Collections.sort(unionDocList.list);
    		
    		
    		
    		//System.out.println(queries.get(0));
    		
    		//EYES UP HERE
    		
    		/*ret = queries.get(0);//no useful in anyway, just to silence error
    		
    		if(queries.size() == 1)
    		{
    			ret = index.get(words.getFirst()); // incase of a one word phrase query
    			
    			return ret;
    		}*/
    		
    		/*PostingsList x = queries.get(0);       // set x = to the first terms postingslist
    		for(int i = 1; i < queries.size(); i++) // go through all the postings list
    		{
    			ret = new PostingsList();				
    													//new merge PostingsList because we want a new return PostingsList that selects from the previous merged list, 
    													//stored in x, and the new list to match, y, and stores that all in ret. 
    													//x has a list, y has the other, ret stores the merged
    													//then if there are >2 then x becomes the merged list and y becomes the next query word
    			
    			PostingsList y = queries.get(i);		// get the next words posting list
    			
    			ListIterator f1 = x.getIterator();		//for iterations
    			ListIterator f2 = y.getIterator();		//for iterations
    			
    			int countf1 = 0;						//for access to elements
    			int countf2 = 0;						//for access to elements
    			
    			do
    			{
    				PostingsEntry p = x.get(countf1);											//first postingentry for x
    				System.out.println("X size is : " + queries.size());
    				//System.out.println(p + " p1 docID: " +p.docID + " offset: " + p.offsets );
    				PostingsEntry p2 = y.get(countf2 );											//posting entry for y
    				//System.out.println(p2+ " p2 docID: " + p2.docID + " offset: " + p2.offsets );
    				
    				if(p.docID == p2.docID)
    				{
    					System.out.println("They match");
    					
    					outerloop:
    					for(int a = 0; a < p.offsets.size(); a++)
    					{
    						for(int b = 0; b < p2.offsets.size(); b++)
    						{
    							if(p.offsets.get(a) - p2.offsets.get(b) == -1)
    							{
    								System.out.println(p + " p1 docID: " +p.docID + " offset: " + p.offsets.get(a) );
    								System.out.println(p + " p2 docID: " +p2.docID + " offset: " + p2.offsets.get(b) );
    								
    								ret.addQuery(p2, p2.offsets.get(b));
    								System.out.println("Just added to merged postingsList");
    	    						//f1.next();							//increment to next postings entry
    	        					//countf1++;							//incre
    	        					//f2.next();
    	        					//countf2++;
    								break oute{
    								    rloop;
    							}
    							else if (p.offsets.get(a) < p2.offsets.get(b))
    	    					{
    	    						System.out.println("Offset of one is less so increment 1 to find match");
    	    						//f1.next();
    	        					//countf1++;
    	    						break;
    	    					}
    							else
    	    					{
    	    						System.out.println("Offset of two is less so increment 2 to find match");
    	    						continue;
    	    						//f2.next();
    	        					//countf2++;
    	    					}
    						}
    					 }
    					
    					 f1.next();							//increment to next postings entry
    					 countf1++;							//incre
    					 f2.next();
    					 countf2++;
    					 System.out.println("Incrementing");
    				 }
    					
    					/if(p.offset - p2.offset == -1)			//check offset
    					{
    						ret.add(p2);						//add second posting entry for extended matches
    						System.out.println("Just added to merged postingsList");
    						f1.next();							//increment to next postings entry
        					countf1++;							//incre
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
    					/    					
    				
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
    			
    		}*/
    	//}
    		//System.out.println(ret.get(0) + "ENDING");
    		return unionDocList;
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
    
    

    public double cosineSimilarity(LinkedList<String> query, PostingsEntry doc, int qLen)//qlen is redundant
    {
    	double qtf = 0;
    	double qidf = 0;
    	double dtf = 0;
    	double didf = 0;
    	double numerator = 0;
    	double qscore = 0;
    	double dscore = 0;
    	
    	double dtfidf = 0;
    	double qtfidf = 0;
    	
    	double score = 0;
    	int numDocs = this.docSize.size();
    	
    	
    	if(qLen == 1)
    	{
    		
    		dtf = doc.offsets.size();
    		
    		PostingsList termDocs = this.index.get(query.get(0));// get the postings list
    		int docFreq = termDocs.list.size(); // size of the postingslist which is num docs that term appears in
    		//System.out.println(term + " doc freq: " + docFreq);
    		qidf = Math.log10(numDocs/docFreq) +1;
    		
    		int docLen = this.docSize.get(doc.docID); //length of the current doc 
    		score = (dtf * qidf)/docLen;
    		return score;    		
    	}
    	
    	
    	for(String term : query)
    	{
    		qtf = 1.0/qLen;
    		 // num docs in the whole data set
    		PostingsList termDocs = this.index.get(term); //postings list of the doc
    		int docFreq = termDocs.list.size(); // get the size of the postingslist which is the docfreq of the term
    		System.out.println(term + " doc freq: " + docFreq);
    		qidf = Math.log10(numDocs/docFreq) +1; //calc the idf 
    		System.out.println("query idf: " + qidf + "query tf is: " + qtf);
    		
    		dtf = doc.offsets.size(); // it gets the offsets associated with this doc, which is the amount of times it appeared in this doc.
    		int words = this.docSize.get(doc.docID); //gets the total number of words in the document
    		dtf = dtf/words; //norm of the term freq
    		System.out.println("doc term freq: " + dtf);
    		didf = qidf;  // it has to be the same since the term in the doc is the term in the query that we are calculating for
    		System.out.println("didf is : " + didf);
    		qtfidf = qtf * qidf;
    		dtfidf = dtf * didf;
    		System.out.println("qtidf is: " + qtfidf);
    		System.out.println("dtidf is: " + dtfidf);
    		
    		numerator += qtfidf * dtfidf;
    		System.out.println("numerator is: " + numerator);
    		qscore += (qtfidf * qtfidf);
    		dscore += (dtfidf * dtfidf);
    		
    		System.out.println("qscore is: " + qscore + " and dscore is: " + dscore);
    	}
    	
    	System.out.println("root of query is: " + Math.sqrt(qscore));
    	System.out.println("root of docscore is: " + Math.sqrt(dscore));
    	score = numerator;///(Math.sqrt(qscore) * Math.sqrt(dscore));
    	System.out.println("score is: " + score);
    	return score;
    }
    	

    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
