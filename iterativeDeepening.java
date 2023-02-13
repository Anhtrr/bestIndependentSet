import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;

/**
 *  
 *  Artificial Inteligence - Programming Assignment #1 - Iterative Deepening Implementation
 *  Professor Ernest Davis
 *  @author Anh Tran - 2/12/2023
 *  Due: 2/13/2023 - 11AM (EST)
 *  
 */
public class iterativeDeepening {
    public static void main(String args[]){
        // data structure to construct graph and vertex
        HashMap<Character, List<Character>> graph = new HashMap<>();
        HashMap<Character, Integer> vertices = new HashMap<>();
        // store command
        String[] commandInfo = new String[]{"",""};
        String flag = "";
        int targetValue = 0;
        try{
            // EXTRACT INPUT
            readInput(graph, vertices, commandInfo);
            flag = commandInfo[0];
            targetValue = Integer.parseInt(commandInfo[1]);
        } catch (FileNotFoundException e){
            System.err.println("File input.txt was not found!" + 
            " Please make sure it is in the same folder as the source code" +
            " and that you are running the source code from this" +
            " 'iterativeDeepening' folder in your terminal!");
            System.exit(0);
        }
        // Verbose Option
        if (flag.equals("V")){
            verbose(graph, vertices, targetValue);
        }
        // Compact Option
        else if(flag.equals("C")){
            compact(graph, vertices, targetValue);
        }
    }
    
    // EXTRACT INPUT AND CONSTRUCT GRAPH
    public static void readInput(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, String[] commandInfo) throws FileNotFoundException{
        // construct default empty graph 
        List<Character> allVertices = new ArrayList<>();
        File graphFile = new File("input.txt");
        try{
            // 1) Extract command print style and targetValue
            Scanner inFile = new Scanner(graphFile);
            String firstLine = inFile.nextLine().trim();
            String typeOfResult = firstLine.split(" ", -1)[1];
            String targetString = firstLine.split(" ", -1)[0];
            commandInfo[0] = typeOfResult;
            commandInfo[1] = targetString;
            // 2) Add vertices to vertices list and graph
            while (inFile.hasNextLine()){
                String currentLine = inFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                }
                Character label = currentLine.split(" ", -1)[0].charAt(0);
                List<Character> adjList = new ArrayList<>();
                String valueInString  = currentLine.split(" ", -1)[1];
                int value = Integer.parseInt(valueInString);
                // add vertex to list
                vertices.put(label, value);
                // add vertex to graph
                graph.put(label, adjList);
                allVertices.add(label);
            }
            // 3) Add all vertices to each vertex adj list
            for (Character c : graph.keySet()){
                for (Character a : allVertices){
                    if (!a.equals(c)){
                        graph.get(c).add(a);
                    }
                }
            }
            // 4) Read edges and remove ones with edges from adj list
            while (inFile.hasNextLine()){
                String currentLine = inFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                }
                Character vertex1 = currentLine.split(" ", -1)[0].charAt(0);
                Character vertex2 = currentLine.split(" ", -1)[1].charAt(0);
                graph.get(vertex1).remove(vertex2);
                graph.get(vertex2).remove(vertex1);
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("File input.txt was not found!" + 
            " Please make sure it is in the same folder as the source code" +
            " and that you are running the source code from this" +
            " 'iterativeDeepening' folder in your terminal!");
            System.exit(0);
        }
        return;
    }

    // VERBOSE PRINTING
    public static void verbose(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal){   
        // depth is bounded by n (for n vertices) - From hwk
        int maximalDepth = vertices.size();
        boolean foundSolution = false;
        // loop through each depth (skip 0 --> based on sample output)
        for (int currentDepth = 1; currentDepth <= maximalDepth; currentDepth++){
            // print depth level
            System.out.println("\nDepth=" + currentDepth + ".");
            String answer = "";
            answer = dfs( answer, graph, vertices, goal, currentDepth, 'V');
            // IF SOLUTION FOUND
            if(calcValue(vertices, answer)>=goal){
                // format print
                String formatAnswer = formatStateForPrint(answer);
                String formatAnswerLine = formatAnswer + "Value=" 
                + calcValue(vertices, answer) + ".";
                System.out.println("\nFound solution " + formatAnswerLine + "\n");
                foundSolution = true;
                break;
            }
        }
        // NO SOLUTION FOUND
        if (foundSolution == false){
            System.out.println("\nNo solution found\n");
        }
        return;   
    }

    // COMPACT PRINTING
    public static void compact(HashMap<Character, List<Character>> graph, HashMap<Character, Integer> vertices, int goal){
        // depth is bounded by n (for n vertices) - From hwk
        int maximalDepth = vertices.size();
        boolean foundSolution = false;
        // loop through each depth (skip 0 --> based on sample output)
        for (int currentDepth = 1; currentDepth <= maximalDepth; currentDepth++){
            // store answer
            String answer = "";
            answer = dfs( answer, graph, vertices, goal, currentDepth, 'C');
            // IF SOLUTION FOUND
            if(calcValue(vertices, answer)>=goal){
                // format print
                String formatAnswer = formatStateForPrint(answer);
                String formatAnswerLine = formatAnswer + "Value=" 
                + calcValue(vertices, answer) + ".";
                System.out.println("\nFound solution " + formatAnswerLine + "\n");
                foundSolution = true;
                break;
            }
        }
        // NO SOLUTION FOUND
        if (foundSolution == false){
            System.out.println("\nNo solution found\n");
        }
        return;
    }

    // DFS ALGO
    public static String dfs(String state, HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal, int maxDepth, Character flag){
        // if verbose printing declared - print out each state
        if (flag.equals('V') && state.length() > 0){
            String formatState = formatStateForPrint(state);
            String formatStateLine = formatState + "Value=" + calcValue(vertices, state) + ".";
            System.out.println(formatStateLine);
        }
        // BC1 - check for goal state
        if(calcValue(vertices, state)>= goal){
            return state;
        }
        // find successors to current state for # of depths iterations
        for (int i = 0; i < maxDepth; i++){
            List<String> successors = new ArrayList<>();
            successors = getSuccessors(state, graph);
            // BC2 - if no successors
            if (successors.size() == 0){
                return null;
            }
            for (String successor : successors){
                //dfs1(successor)
                String ans = dfs(successor, graph, vertices, goal, maxDepth-1, flag);
                // if ans meets goal
                if (calcValue(vertices, ans) >= goal){
                    return ans;
                }
            }
            // FAIL
            return null;
        }
        // FAIL
        return null;
    }

    // HELPER 1 - return a list of successors
    public static List<String> getSuccessors(String state, HashMap<Character, List<Character>> graph){
        List<String> successors = new ArrayList<>();
        // if starting state
        if (state.length() == 0){
            // for each vertex/state
            for (Character c : graph.keySet()){
                String singleV = c.toString();
                successors.add(singleV);
            }
        }
        // if not starting state
        else{
            // for each vertex adjacent to current state's last vertex 
            for(Character c : graph.get(state.charAt(state.length()-1))){
                // if vertex isnt already in chain
                if(!state.contains(c.toString().trim())){
                    // add to successors
                    String successor = state + c.toString().trim();
                    successors.add(successor);
                }
            }
        }
        return successors;
    }

    // HELPER 2 - calculate value of state
    public static int calcValue(HashMap<Character, Integer> vertices, String state){
        if(state == null){
            return 0;
        }
        if(state.length() == 0 || state == ""){
            return 0;
        }
        else{
            int total = 0;
            // iterate over each vertex in state
            for (int i = 0; i < state.length(); i++){
                total += vertices.get(state.charAt(i));
            }
            return total;
        }
    }

    // HELPER 3 - format a state (string) for printing
    public static String formatStateForPrint(String state){
        String formatState = "";
        for (int i = 0; i < state.length(); i++){
            formatState += state.charAt(i) + " ";
        }
        return formatState;
    }
}
