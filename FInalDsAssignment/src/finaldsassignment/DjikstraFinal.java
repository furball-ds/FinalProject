/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgtest2;

import java.util.*;
import javax.swing.JFrame;

/**
 *
 * @author Zakkir
 */
public class Djikstra {

    private int distance[];// array for distances for each node
    private int time[];
    private Set<Integer> settled;// a set for all the nodes that have been calculated
    private int V; //Number of nodes/vertices/neuron
    private PriorityQueue<Node> pq;
    List<List<Node>> adj; //list for adjacent nodes

    public Djikstra(int V) {
        this.V = V;
        distance = new int[V];
        time = new int[V];
        settled = new HashSet<Integer>();
        pq = new PriorityQueue<Node>(V, new Node());

    }

    public void calculate(List<List<Node>> adj, int src) {//to calculate shortest distance using djikstra algo
        this.adj = adj;
        for (int i = 0; i < V; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < V; i++) {
            time[i] = Integer.MAX_VALUE;
        }

        pq.add(new Node(src, 0, 0));
        distance[src] = 0;
        time[src] = 0;

        while (settled.size() != V) {

            int u;
            //if(!pq.isEmpty())
            if (pq.peek() != null) {

                u = pq.remove().node;

                settled.add(u);
                neighbours(u);
            } else {
                //{System.out.println("There is no path!");
                break;
            }
        }
    }

    public void neighbours(int u) {
        int edgeDist = -1;
        int edgeTime = -1;
        int newDist = -1;
        int newTime = -1;

        for (int i = 0; i < adj.get(u).size(); i++) {
            Node v = adj.get(u).get(i);

            if (!settled.contains(v.node)) {
                edgeDist = v.costD;
                edgeTime = v.costT;
                newDist = distance[u] + edgeDist;
                newTime = time[u] + edgeTime;
                if (newTime < time[v.node]) {
                    time[v.node] = newTime;
                    distance[v.node] = newDist;
                }
                if (newTime == time[v.node]) {
                    if (newDist < distance[v.node]) {
                        distance[v.node] = newDist;
                        time[v.node] = newTime;
                    }
                }
                pq.add(new Node(v.node, distance[v.node], time[v.node]));
            }
        }
    }

    public static void main(String arg[]) {

        Scanner UserInput = new Scanner(System.in);
        System.out.println("Welcome to Sheldon's Amazing Human Brain Visualizer!");
        System.out.print("How many nodes are in the network? : ");
        int V = UserInput.nextInt(); //input vertices

        List<List<Node>> adj = new ArrayList<List<Node>>();
        for (int i = 0; i < V; i++) {
            List<Node> item = new ArrayList<Node>();
            adj.add(item);
        }

        for (int count1 = 0; count1 < V; count1++) {
            System.out.print("Please insert the node ID, and its number of edges : ");
            int nodeID = UserInput.nextInt();
            int NumOfEdge = UserInput.nextInt();

            for (int count2 = 0; count2 < NumOfEdge; count2++) {
                System.out.print("Please insert the ID of connected neuron, distance of edge & time to pass through : ");
                int connectedID = UserInput.nextInt();
                int edgeDist = UserInput.nextInt();
                int time = UserInput.nextInt();
                adj.get(nodeID).add(new Node(connectedID, edgeDist, time));
            }
        }
        System.out.println(adj.get(0).get(0).costD);

        // PART 2, to get shortest path : 
        ArrayList<String> sp = new ArrayList<>();
        int source;
        int destination;
        System.out.print("How many messages to be passed through?");
        int numOfMessages = UserInput.nextInt();

        for (int count = 0; count < numOfMessages; count++) {
            Djikstra test = new Djikstra(V); //new one has to be created to refresh the list
            System.out.print("Source and destination of the message? ");
            source = UserInput.nextInt();
            destination = UserInput.nextInt();
            test.calculate(adj, source);

            if (test.distance[destination] != Integer.MAX_VALUE && test.time[destination] != Integer.MAX_VALUE) {
                System.out.print("The shortest path from node ");
                String s = source + " to " + destination + " is " + test.distance[destination] + ", " + test.time[destination];
                System.out.println(s);
                sp.add("The shortest path from node : "+s);
            } else {
                System.out.println("There is no path!");
                sp.add("The shortest path from node : There is no path from "+source+" to "+destination);
            }
        }

        JFrame window = new JFrame("Nodes");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setContentPane(new NodesPanel(V,adj,sp));

        window.pack();
        window.setVisible(true);

    }

}

class Node implements Comparator<Node> {

    public int node;
    public int costD; // cost for distance
    public int costT; // cost for time

    public Node() {
    }

    public Node(int node, int distance, int time) {
        this.node = node;
        this.costD = distance;
        this.costT = time;
    }

    @Override
    public int compare(Node node1, Node node2) //comparator to compare 2 nodes
    {
        if (node1.costT < node2.costT) {
            return -1;
        }
        if (node1.costT > node2.costT) {
            return 1;
        }
        if (node1.costT == node2.costT) {
            if (node1.costD < node2.costD) {
                return -1;
            }
            if (node1.costD > node2.costD) {
                return 1;
            }
            return 0;
        }

        return 0;
    }
}
