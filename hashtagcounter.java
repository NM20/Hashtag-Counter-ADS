/*
 Nayan Mongia
 ADS Project
 UFID:40330601
 */
import java.io.*;
import java.util.*;

public class hashtagcounter {
	/* The main class of this project reads the input file  and outputs the most n popular hashtags using max-fibonacci heap*/
	public static void main(String[] args) {
		PrintStream so = System.out;

		class MaxFiboHeap {
			public Node root;
			HashMap<String, Node> hm = new HashMap<>();

		class Node {	
				Node child, sibleft ,sibright, parent;    // sibleft refers to left sibling node and sibright refers to right sibling node.
				int nodedata;
				int degree;
				public String hashname;
				boolean mark;    //represents the key value.

				public Node(int nodedata ,String hashname)// This constructor initializes left and right sibling nodes pointers and forms a circular linked list.
				{
					this.sibright = this;
					this.sibleft = this;
					this.nodedata = nodedata;
					this.degree=0;
					this.mark = false;
					this.hashname = hashname;
				} 
			}		

			public void insert(String hashname , int nodedata) // This function is used to insert new data in the max-fibonacci heap.
			{
				Node node = new Node(nodedata,hashname);
				boolean ck = hm.containsKey(hashname); 
				if(ck==false)
				{
					node.nodedata = nodedata;
					node.hashname = hashname;
					if (root != null) 
					{
						if(root.sibright==root)
						{   
							node.sibright=root;
							root.sibright=node;
							node.sibleft=root;
							root.sibleft=node;
						}
						else
						{	
							node.sibleft = root;
							node.sibright = root.sibright;
							root.sibright = node;
							node.sibright.sibleft = node;
						}
						if (node.nodedata > root.nodedata) 
						{
							root = node;
						}   
					}
					else 
					{
						root = node;
					}   
					hm.put(hashname, node);
				}  
				else
				{
					this.Increasekey(hashname,nodedata); 
				}
			}	
			public void Increasekey(String hashname,int nodedata) //This method increases the value of a node.
			{   
				Node tempnode = new Node(nodedata,hashname);
				tempnode.mark=true;
				Node tnode = null;
				tnode = hm.get(hashname);
				int temp = tnode.nodedata;
				tnode.nodedata = temp + nodedata;
				if(tnode.parent==null)
				{
					if (tnode.nodedata>root.nodedata)
					{
						root = tnode; 
					}
				}
				else
				{
					if(tnode.nodedata>tnode.parent.nodedata)
					{
						while(tempnode.mark==true)
						{			
							tnode.parent.degree=tnode.parent.degree-1;   
							tempnode = tnode.parent;
							if(tnode.sibleft==tnode && tnode.sibright==tnode)
							{

								tnode.parent.child = null;
							}
							else
							{	 
								tnode.sibleft.sibright = tnode.sibright;
								tnode.sibright.sibleft = tnode.sibleft;
								if(tnode.parent.child == tnode){
									tnode.parent.child = tnode.sibright;
								}
							}
							tnode.parent=null;
							if(root.sibright==root)
							{
								tnode.sibright=root;
								root.sibright=tnode;
								tnode.sibleft=root;
								root.sibleft=tnode;
							}
							else
							{	
								tnode.sibleft = root;
								tnode.sibright = root.sibright;
								root.sibright = tnode;
								tnode.sibright.sibleft = tnode;
							}
							if(tempnode.parent==null)
							{
								break;
							}
							if(tempnode.mark==true){
								tnode=tempnode;
							}
						}  
						tempnode.mark = true;
					}
				}
			} 	
				
			public void Removemax(int m, String op_file_name, String[] args)// It removes the max node data element from the heap.
			{
				int tempval= m;
				Node nextroot = null;
				Node orderednode = null;
				Node  temp = root;
				String result = "";
				while(m>=1)
				{
					if (root != null) 
					{
						if (root.child != null) 
						{
							Node child = root.child;
							do {
								child.parent = null;
								child = child.sibright;
							} while (child != root.child);
						}
						if(root.sibright==root)
						{
							nextroot = null;
						}
						else
						{
							nextroot = root.sibright;
						}	  
					}
					root.sibleft.sibright=root.sibright; 
					root.sibright.sibleft=root.sibleft;
					root.sibleft =  root;
					root.sibright = root;
					Node childroot = root.child;
					root.child = null;
					result+=root.hashname;
					if(m>1)
					{   
						result+=',';
					}
					hm.remove(root.hashname);
					root = meldlist(nextroot,childroot);
					pairwisecombine();
					m--;
					if(orderednode==null)
					{
						orderednode = temp;
					}
					else
					{
						orderednode.sibright.sibleft=temp;
						temp.sibright=orderednode.sibright;
						temp.sibleft=orderednode;
						orderednode.sibright=temp;
					}    
					temp=root;
				}
				
				
				if (args.length == 2) {
					printop(result, op_file_name);
				}
				else {
					showop(result);
				}
				
				
				while(tempval>=1)
				{
					insert(orderednode.hashname,orderednode.nodedata);
					orderednode=orderednode.sibright;
					tempval--;
				}
			} 
			public void pairwisecombine() //This method consolidates the heap by combining nodes of equal degree.
			{	
				HashMap<Integer, Node> pc = new HashMap<>();
				Node working = root;
				Node index = root;
				Node max = root;
				Node tIndex;
				boolean last = false;
				do{
					working=index;			 
					tIndex = index.sibright;
					if (tIndex==max)
					{
						last = true;
					}
					while(pc.containsKey(working.degree)==true)
					{	
						Node temp = pc.get(working.degree);
						pc.remove(working.degree);
						if(temp.nodedata>=working.nodedata && temp!=working)
						{
							if(working==max)
							{
								max=max.sibright;
								root = max;
							}
							working.sibright.sibleft=working.sibleft;
							working.sibleft.sibright=working.sibright;
							working.parent=temp;
							working.mark = false;
							if(temp.degree==0)
							{
								temp.child = working;
								working.sibright=working;
								working.sibleft=working;
							}
							else
							{
								working.sibright=temp.child.sibright;
								temp.child.sibright.sibleft=working;
								temp.child.sibright=working;
								working.sibleft=temp.child;
							}
							temp.child=working;
							temp.degree++;
							working=temp;
						}
						else if(temp.nodedata<working.nodedata && temp!=working)
						{
							if(temp==max)
							{
								max=max.sibright;
								root = max;
							}
							temp.sibright.sibleft=temp.sibleft;
							temp.sibleft.sibright=temp.sibright;
							temp.parent=working;
							temp.mark = false;
							temp.sibleft = null;
							temp.sibright = null;
							if(working.degree==0)
							{
								working.child = temp;
								temp.sibright=temp;
								temp.sibleft=temp;
							}
							else
							{
								temp.sibright=working.child.sibright;
								working.child.sibright.sibleft=temp;
								working.child.sibright=temp;
								temp.sibleft=working.child;
							}
							working.child=temp;
							working.degree++;
						}	
					}
					pc.put(working.degree,working);	
					if(last==true)
					{
						break;
					}
					if(index!=tIndex)
					{	
						index = tIndex.sibleft;
					}
					index = index.sibright;
				} while(index!=max);
				Node tempvar = max;
				Node maxval = max;
				do
				{
					if(tempvar.nodedata>maxval.nodedata)
					{
						maxval = tempvar;
					}
					tempvar=tempvar.sibright;		
				}
				while(tempvar!=max);
				root = maxval;
			}
		public Node meldlist(Node x, Node y) // This function is used to meld the lists when max node element is deleted from the heap.
		{
			if (x == null) {
				return y;   
			}
			if (y == null) {
				return x;
			}
			else
			{	
				Node a,b;
				a = x.sibright;
				b = y.sibright;
				x.sibright=b;
				b.sibleft=x;
				y.sibright=a;
				a.sibleft=y;

				return x;
			}
		}
		     void printop(String output, String op_file_name) // This method is used to write the string output in a file.
		    {
		    	try
		    	{
		    		Writer w = new BufferedWriter(new FileWriter(op_file_name, true));
		    		w.write(output);
		    		w.write("\n");
		    		w.close();
		    	}
		    	catch(Exception e)
		    	{
		    		so.println(" Error encountered " + e.getMessage());
		    	}
		    } 
		     
		     void showop(String output) // This method is used to write the string output in a file.
			    {
			    	try
			    	{
			    		so.println(output);
			    	}
			    	catch(Exception e)
			    	{
			    		so.println(" Error encountered " + e.getMessage());
			    	}
			    }      
		}
		MaxFiboHeap Heap = new MaxFiboHeap(); // It is used to keep track of the hashtags frequencies.
		String fname= args[0];
		String op_file_name = "blank";
		if (args.length == 2) {
			op_file_name = args[1];
		}
		if (args.length >= 3) {
			so.println("INVALID INPUT");
		}
		try{
			FileReader inputFile = new FileReader(fname);
			BufferedReader bufferReader = new BufferedReader(inputFile);  // Reads the input file
			String line;
			while ((line = bufferReader.readLine()) != null)  
			{
				char achar = line.charAt(0);
				if(achar=='#')
				{
					String[] parts = line.split(" ");
					String hname = parts[0];
					String hashname = hname.substring(1);
					String number = parts[1];
					int count = Integer.parseInt(number);
					Heap.insert(hashname,count);   	
				}
				else if(achar=='S')  // when S is encountered from 'STOP', the loop from the system exits.
				{
					return;
				}
				else
				{
					int m = Integer.parseInt(line);
					Heap.Removemax(m, op_file_name, args);
				}
			}
			bufferReader.close();	
		}catch(Exception e){
			so.println("Error encountered" + e.getMessage());                      
		}
	}
}
