import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.List;
import java.util.PriorityQueue;

class GraphEdge {
    private char from;
    private char to;
    private int weight;
    public GraphEdge(char from, char to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
}

public class dijkstra {
    public static void main(String[] args) throws FileNotFoundException {
        String f = "";
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the number of the file to test:\n1 (Case1), 2 (Case2), or 3 (Case3): ");
        int n = in.nextInt(); 
        in.close();

        if      (n == 1) f = "D:\\College\\3rd Quarter\\CSC 421 Applied Algorithms\\Assignment 4 attached files May 27, 2020 859 PM\\Djikstra's Algorithm Input Files\\Case1.txt";
        else if (n == 2) f = "D:\\College\\3rd Quarter\\CSC 421 Applied Algorithms\\Assignment 4 attached files May 27, 2020 859 PM\\Djikstra's Algorithm Input Files\\Case2.txt";
        else if (n == 3) f = "D:\\College\\3rd Quarter\\CSC 421 Applied Algorithms\\Assignment 4 attached files May 27, 2020 859 PM\\Djikstra's Algorithm Input Files\\Case3.txt";
        else    { System.out.println("Not an option. "); return; }

        File file = new File(f);
        Scanner obj = new Scanner(file); 

        char source      = 'A'; 
        char destination = 'B';

        ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>(); 
        HashSet<Character> uniqueVertices = new HashSet<Character>(); 
        ArrayList<Character> allVertices = new ArrayList<Character>();

        int numberOfVertices = Integer.parseInt(obj.nextLine());
        
        while (obj.hasNextLine()) { 
            String line = obj.nextLine();
            String vertex[] = line.split(" "); 

            GraphEdge edge = new GraphEdge(vertex[0].charAt(0), vertex[1].charAt(0), Integer.parseInt(vertex[2]));
            edges.add(edge);

            if (!uniqueVertices.contains(vertex[0].charAt(0))) { 
                uniqueVertices.add(vertex[0].charAt(0));
                allVertices.add(vertex[0].charAt(0));
            }

            if (!uniqueVertices.contains(vertex[1].charAt(0))) { 
                uniqueVertices.add(vertex[1].charAt(0));
                allVertices.add(vertex[1].charAt(0));
            }
        }

        HashMap<Character, Integer> distance = new HashMap<Character, Integer>();

        for (Character ch : allVertices) {
            distance.put(ch, Integer.MAX_VALUE);
        }

        distance.put(source, 0); 

        Comparator<Character> cmp = new Comparator<Character>() {
            public int compare(Character a, Character b) {
                if (distance.get(a) < distance.get(b))
                    return -1;

                else if (distance.get(a) == distance.get(b))
                    return 0;

                else
                    return 1;
            }
        };

        PriorityQueue<Character> pq = new PriorityQueue<Character>(cmp);

        for (Character ch : allVertices)
            pq.add(ch);

        String path = "";

        while (!pq.isEmpty()) { 
            char ch = pq.remove();
            path += ch + " ";
            if (ch == destination)
                break;

            List<GraphEdge> outVertex = getAllOutgoingEdges(edges, ch);

            for (GraphEdge edge : outVertex) { 
                if (pq.contains(edge.getTo())) { 
                    if (distance.get(ch) + edge.getWeight() < distance.get(edge.getTo())) {
                        distance.put(edge.getTo(), distance.get(ch) + edge.getWeight());
                    }

                    pq.remove(edge.getTo());
                    pq.add(edge.getTo());
                }
            }
        }        
        System.out.println(distance.get(destination));
        System.out.print(path);
        obj.close();
    }

    public static List<GraphEdge> getAllOutgoingEdges(ArrayList<GraphEdge> list, char source) {
        List<GraphEdge> result = new ArrayList<GraphEdge>();
        for (GraphEdge edge : list) {
            if (edge.getFrom() == source)
                result.add(edge);
        }
        return result;
    }
}
