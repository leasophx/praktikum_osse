package de.hfu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import de.hfu.Queue;
import de.hfu.Util;


public class AppTest 
{
   
    
    @Test     
    public void testQueue() {
    	Queue queue = new Queue(3);
    	
    	   assertEquals(queue.queue.length,3);  
		   queue.enqueue(4);
		   queue.dequeue();
    	
    }
    
    @Test
    public void testUtil() {
    	
    	assertEquals(Util.istErstesHalbjahr(5),true);
		assertEquals(Util.istErstesHalbjahr(8),false);
		
		Util.istErstesHalbjahr(3);
    }
  
}
