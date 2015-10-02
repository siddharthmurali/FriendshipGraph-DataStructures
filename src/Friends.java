//Karthik Nair Section 13
//Siddharth Murali Section 02

import java.util.*;
import java.io.*;



class Neighbor {
	public int vertexNum;
	public Neighbor next;
	public Neighbor(int vnum, Neighbor nbr) {
		this.vertexNum = vnum;
		next = nbr;
	}
}

class Vertex {
	String name, school;
	Neighbor adjList;
	int dfsnumber = 0;
	int previous = 0;
	boolean visited;

	Vertex(String name, String school, Neighbor neighbors) {
		this.name = name;
		this.school = school;
		this.adjList = neighbors;
	}
}


public class Friends {

	private static final Readable String = null;
	Vertex[] adjLists;
	String clique = " ";
	Vertex[] subgraph;



	public static void main(String[] args) throws FileNotFoundException{

		//Choose Graph File
		Friends friends = new Friends();
		Scanner sc1 = new Scanner(System.in);
		System.out.println("Input a proper Graph File");
		String graphFile = sc1.nextLine();	
		Vertex[] adjLists = friends.build(graphFile);
		//friends.printer(adjLists);

		System.out.println("Choose one of the options below: ");
		System.out.println("1. Shortest Intro Chain ");
		System.out.println("2. Cliques at a School");
		System.out.println("3. Connectors");
		System.out.println("4. Quit");

		int option = sc1.nextInt();
		System.out.println(option);

		while(option != 1 && option != 2 && option != 3 && option != 4){
			System.out.println("Enter a valid option: ");
			option = sc1.nextInt();
		}


		while(option != 4){

			if(option == 1){

				System.out.println("Enter first name: ");
				String intro = sc1.next();
				System.out.println("Enter second name: ");
				String outro = sc1.next();
				Vertex[] graph1 = adjLists;
				friends.shortestPath(graph1,intro,outro);
				//friends.shortestPath(intro, other, adjLists);

			} 
			if(option == 2){
				System.out.println("Enter the School you would like to know about: ");
				System.out.print(sc1.nextLine());
				String school = sc1.nextLine().toLowerCase();
				friends.clique(adjLists, school);		
			}

			if(option == 3){
				friends.connectors(adjLists);
			}
			System.out.println("Enter next option from 1 - 4");
			option = sc1.nextInt();
		}
	}

	public Vertex[] build(String file) throws FileNotFoundException{

		Vertex[] adjLists;
		Scanner sc3;

		if(file.contains("txt")){
			sc3 = new Scanner(new File(file));
		}else{
			sc3 = new Scanner(file);
		}
		
		if(file.contains("Clique")){
			sc3.nextLine();
		}
		

		adjLists = new Vertex[Integer.parseInt(sc3.nextLine())];

		// read vertices
		for (int v=0; v < adjLists.length; v++) {
			String line = sc3.nextLine();
			String name = line.substring(0, line.indexOf('|'));

			int x = line.indexOf('|');
			String school;

			if(line.charAt(x+1) == 'y'){
				school = line.substring(x+3);
			}else{
				school = "noschool";
			}

			adjLists[v] = new Vertex(name, school, null);
		}

		// read edges
		while (sc3.hasNext()) {

			String line = sc3.next();
			int v1 = indexForName(line.substring(0, line.indexOf('|')), adjLists);
			int v2 = indexForName(line.substring(line.indexOf('|')+1), adjLists);

			// add v2 to front of v1's adjacency list and
			// add v1 to front of v2's adjacency list
			adjLists[v1].adjList = new Neighbor(v2, adjLists[v1].adjList);
			adjLists[v2].adjList = new Neighbor(v1, adjLists[v2].adjList);

		}

		return adjLists;

	}

	private int indexForName(String name, Vertex[] graph) {
		for (int v=0; v < graph.length; v++) {
			if (graph[v].name.equals(name)) {
				return v;
			}
		}
		return -1;		
	}


	public ArrayList<String> shortestPath(Vertex[] subgraph, String intro, String outro){
		Vertex[] subgraph1=subgraph;
		boolean found1 = false;
		boolean found2 = false;

		int sour = 0;
		int destt = 0;
		
		
		ArrayList<String> shortpath = new ArrayList<String>();

		if (intro.equals(outro)) // same name
		{

			return null;

		}
		if (subgraph1.length == 0) // empty list/graph
		{
			System.out.println("empty");
			return null;
		}

		for (int i = 0; i < subgraph1.length; i++) // goes through adjLL to
			// see if source & dest
			// are in the graph
		{
			if (subgraph1[i].name.equals(intro)) // find the first name in adjLL
			{
				found1 = true;
				sour = i; // index of source

			}
			if (subgraph1[i].name.equals(outro)) // find the first name in adjLL
			{
				found2 = true;
				destt = i; // index of dest
			}
		}

		Vertex begin = subgraph1[sour]; // entire line
		Vertex end = subgraph1[destt];
		Vertex current = null;
		

		if (found1 && found2) {
			Queue<Vertex> q = new Queue<Vertex>();
			q.enqueue(begin);

			HashMap<Vertex, Boolean> visit = new HashMap<Vertex, Boolean>();
			HashMap<Vertex, Vertex> prev = new HashMap<Vertex, Vertex>();

			visit.put(begin, true);

			while (!q.isEmpty()) {
				// System.out.println("deq: " +q.peek().name);
				current = q.dequeue();

				if (current.equals(destt)) {
					
					break;
				} else {
					
					Neighbor nbr = current.adjList; // first neighbor
					//	System.out.println("neighbor: "
					//			+ adjLists[nbr.vertexNum].name);

					while (nbr != null) {
						Vertex nextt = subgraph1[(nbr).vertexNum]; // vertex of
						// the
						// neighbor

						if (!visit.containsKey(nextt)) // checks if visited
						{
							// System.out.println("enq: " +nextt.name);
							q.enqueue(nextt);

							visit.put(nextt, true);

							// System.out.println("prev: "+)
							prev.put(nextt, current);
							// System.out.println("current: "+current.name);
						}
						nbr = nbr.next; // grab next neighbor
					}

				}
			}

			shortpath.add(end.name);
			for (Vertex v = end; v != null; v = prev.get(v)) {
				shortpath.add(v.name);
			}
		} else // if found1 or found2 is false - means it's not in the graph
		{
			System.out.println("one of the names entered is not in the graph");
		}

		// for (int i = 0; i < shortpath.size()-1 ; i++)
		int counter = 0;
		for (int i = shortpath.size() - 1; i > 0; i--) // goes through backwards
		{
			if (i == shortpath.size() - 1) {
				System.out.print(shortpath.get(i));
			} else {
				System.out.print("--" + shortpath.get(i));
				counter ++;

			}
	
		}
		
		if(counter == 0){
			System.out.println();
			System.out.println("There is no connection between the two");
		}
		
		System.out.println();

		//System.out.println(shortpath.get(0));
		//System.out.println(shortpath.size());
		
		

		return shortpath;
	}


	public void clique(Vertex[] adjLists, String school) throws FileNotFoundException{
		boolean[] visited = new boolean[adjLists.length];
		for (int v=0; v < visited.length; v++) {
			visited[v] = false;
		}		
		int n = 1;
		for (int v=0; v < visited.length; v++) {
			if (visited[v]==false && (adjLists[v].school.equals(school))){{
				String ret= "";
				ArrayList<String> names = new ArrayList<String>();
				ArrayList<String> connection = new ArrayList<String>();
				String title = "Clique " + n +":";
				ret += title;
				dfs(v, visited, school, adjLists, names, connection);
				print1(names, connection, ret);
				n++;

			}
			}
		}

	}

	// recursive dfs
	private void dfs(int v, boolean[] visited, String school, Vertex[] adjLists, ArrayList<String> names, ArrayList<String> connection) {
		visited[v] = true;
		String name ="\n"+ adjLists[v].name + "|y|" + adjLists[v].school;
		nameCollector(names, name);	
		for (Neighbor e=adjLists[v].adjList; e != null; e=e.next) {
			if (!visited[e.vertexNum] && adjLists[e.vertexNum].school.equals(school)) {
				dfs(e.vertexNum, visited, school, adjLists, names, connection);
				String link = ("\n"+adjLists[v].name + "|" + adjLists[e.vertexNum].name);
				connectionCollector(connection, link);
			}
		}
	}

	// collects the names and the edges into separate arraylists and
	// prints them out
	private ArrayList<String> nameCollector(ArrayList<String> names, String name){
		names.add(name);
		return names;		
	}

	private ArrayList<String> connectionCollector(ArrayList<String> connection, String link){
		if(connection.size() == 0){
			connection.add(link);
		}else{
			connection.add(0,link);
		}
		return connection;
	}

	private Vertex[] print1(ArrayList<String>names, ArrayList<String> connection, String ret) throws FileNotFoundException{
		int n = names.size();
		String size = "\n"+n;
		ret += size;;
		for(String s : names){
			ret += s;
			//System.out.println(s);
		}
		for(String x: connection){
			ret += x;
		}
		ret += "\n";
		Friends friends = new Friends();
		System.out.println(ret);
		return friends.build(ret);
		//System.out.println();	
	}

	ArrayList<String> connectors = new ArrayList<String>();

	//needed for dfs search for connecters 
	int dfsnumber = 0;
	int sat = 0;

	private void dfs(Vertex v, Vertex[] adjLists) {

		dfsnumber++;
		v.dfsnumber = dfsnumber;
		v.previous = dfsnumber;
		v.visited = true;
		int tmp = 0;

		//searchs for where adjLists[x] equals the vertex parameter 
		// saves the index location in tmp

		for (int x = 0; x < adjLists.length; x++) {
			if (adjLists[x] == v) {
				tmp = x;
			}
		}


		//cycles through the neighors at the index of vertex x which is saved in tmp
		for (Neighbor e = adjLists[tmp].adjList; e != null; e = e.next) {

			Vertex w = adjLists[e.vertexNum];
			if (!w.visited) {
				dfs(w, adjLists);

				//saves a new vertex index number in tmp
				for (int x = 0; x < adjLists.length - 1; x++) {
					if (adjLists[x] == v) {
						tmp = x;
					}
				}

				// accorrding to the dfs connectors algorithm 
				// if w of the previous vertex is greater than or equal to the current vertex dfs num then it is a connector

				if (tmp != sat && v.dfsnumber <= w.previous) {
					if (!connectors.contains(v.name))
						connectors.add(v.name);
				}

				// on the other hand if is the dfsnum of vertex is greater than the previoius w then v.previous is the lowest value of v.previous and w.previous
				if (v.dfsnumber > w.previous)
					v.previous = Math.min(v.previous, w.previous);
			} else {
				v.previous = Math.min(v.previous, w.dfsnumber);
			}
		}
	}

	public String connectors(Vertex[] adjLists) {

		// cycles through each index of adjLists
		// sets v = to each index
		// v is later used as an input in the dsf search
		for (int i = 0; i < adjLists.length; i++) {
			Vertex v = adjLists[i];
			int count = 0;

			//counts the neighbors of v
			for (Neighbor e = adjLists[i].adjList; e != null; e = e.next) {
				count++;
			}

			//starts new dfs 
			if (!v.visited && count == 1) {
				dfsnumber = 0;
				sat = i;
				dfs(v, adjLists);
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append(connectors.get(0));
		for (int i = 1; i < connectors.size(); i++)
			sb.insert(0, connectors.get(i) + ", ");
		sb.insert(0, "Connectors: ");
		
		System.out.println(sb);
		return sb.toString();
	}

}


