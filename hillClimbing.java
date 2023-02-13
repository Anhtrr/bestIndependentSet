import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.util.Scanner;
import java.lang.Math;

/**
 *  
 *  Artificial Inteligence - Programming Assignment #1 - Hill Climbing Implementation
 *  Professor Ernest Davis
 *  @author Anh Tran - 2/12/2023
 *  Due: 2/13/2023 - 11AM (EST)
 *  
 */
public class hillClimbing {
    public static void main(String args[]){
        // data structure to construct graph and vertex
        HashMap<Character, List<Character>> graph = new HashMap<>();
        HashMap<Character, Integer> vertices = new HashMap<>();
        // store command
        String[] commandInfo = new String[]{"","",""};
        String flag = "";
        int targetValue = 0;
        int restarts = 0;
        try{
            // EXTRACT INPUT
            readInput(graph, vertices, commandInfo);
            flag = commandInfo[0];
            targetValue = Integer.parseInt(commandInfo[1]);
            restarts = Integer.parseInt(commandInfo[2]);      

        } catch (FileNotFoundException e){
            System.err.println("File input.txt was not found!" + 
            " Please make sure it is in the same folder as the source code" +
            " and that you are running the source code from this" +
            " 'hillClimbing' folder in your terminal!");
            System.exit(0);
        }
        // Verbose Option
        if (flag.equals("V")){
            verbose(graph, vertices, targetValue, restarts);
        }
        // Compact Option
        else if(flag.equals("C")){
            compact(graph, vertices, targetValue, restarts);
        }
    }
    
    // EXTRACT INPUT AND CONSTRUCT GRAPH
    public static void readInput(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, String[] commandInfo) throws FileNotFoundException{
        // construct default empty graph 
        File graphFile = new File("input.txt");
        try{
            // 1) Extract command print style and targetValue
            Scanner inFile = new Scanner(graphFile);
            String firstLine = inFile.nextLine().trim();
            try{
                String restartString = firstLine.split(" ", -1)[2];
                String typeOfResult = firstLine.split(" ", -1)[1];
                String targetString = firstLine.split(" ", -1)[0];
                commandInfo[0] = typeOfResult;
                commandInfo[1] = targetString;
                commandInfo[2] = restartString;
            } catch(ArrayIndexOutOfBoundsException e){
                System.err.println("First Line in input.txt is " +
                "not correct! This is Hill Climbing, not Iterative "
                + "Deepening! Please make sure it is correct!");
                System.exit(0);
            }          
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
            }
            // 3) Read edges and add edges to adj list of both vertices(bi directional)
            while (inFile.hasNextLine()){
                String currentLine = inFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                }
                Character vertex1 = currentLine.split(" ", -1)[0].charAt(0);
                Character vertex2 = currentLine.split(" ", -1)[1].charAt(0);
                graph.get(vertex1).add(vertex2);
                graph.get(vertex2).add(vertex1);
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("File input.txt was not found!" + 
            " Please make sure it is in the same folder as the source code" +
            " and that you are running the source code from this" +
            " 'hillClimbing' folder in your terminal!");
            System.exit(0);
        }
        return;
    }

    // VERBOSE PRINTING
    public static void verbose(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal, int randomCount){   
        // Iterate for a user-specified max number of times
        for (int i = 0 ; i < randomCount; i++){
            String randomState = "";
            randomState = getRandomState(vertices);
            // START STATE FORMATTING 
            String formatStartState = formatStateForPrint(randomState);
            int startValue = calcValue(vertices, randomState);
            int startError = calcError(graph, vertices, randomState, goal);
            String formatStartLine = formatStartState + "Value=" + startValue 
            + ". " + "Error=" + startError + ".";
            // PRINT START STATES
            System.out.println("\nRandomly chosen start state: " + 
            formatStartState.trim());
            System.out.println(formatStartLine);
            // if random state is a solution
            if(startError == 0){
                String startAnswerLine = formatStartState + "Value=" ;
                System.out.println("\nFound solution " + startAnswerLine + startValue + ".");
                break;
            }
            String ans = hc(graph, vertices, goal, randomCount, randomState, 'V');
            // if error value = 0
            int finalError = calcError(graph, vertices, ans, goal);
            if(finalError == 0){
                String formatAnswer = formatStateForPrint(ans);
                String formatAnswerLine = formatAnswer + "Value=" ;
                int answerValue = calcValue(vertices, ans);
                System.out.println("\nFound solution " + formatAnswerLine + answerValue + ".");
                break;
            }
            else{
                System.out.println("\nSearch failed");
                continue;
            }
        }
        return;   
    }

    // COMPACT PRINTING
    public static void compact(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal, int randomCount){
        // Iterate for a user-specified max number of times
        for (int i = 0 ; i < randomCount; i++){
            String randomState = "";
            randomState = getRandomState(vertices);
            // START STATE FORMATTING 
            String formatStartState = formatStateForPrint(randomState);
            int startValue = calcValue(vertices, randomState);
            int startError = calcError(graph, vertices, randomState, goal);
            String formatStartLine = formatStartState + "Value=" + startValue 
            + ". " + "Error=" + startError + ".";
            // PRINT START STATES
            System.out.println("\nRandomly chosen start state: " + 
            formatStartState.trim());
            System.out.println(formatStartLine);
            String ans = hc(graph, vertices, goal, randomCount, randomState, 'C');
            // if error value = 0
            int finalError = calcError(graph, vertices, ans, goal);
            if(finalError == 0){
                String formatAnswer = formatStateForPrint(ans);
                String formatAnswerLine = formatAnswer + "Value=" ;
                int answerValue = calcValue(vertices, ans);
                System.out.println("\nFound solution " + formatAnswerLine + answerValue + ".");
                break;
            }
            else{
                System.out.println("\nSearch failed");
                continue;
            }
        }
        return; 
    }

    // HILL CLIMBING ALGO
    public static String hc(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal, int randomCount, 
    String startingState, Character flag){
        String state = new String(startingState);
        while (true){
            List<String> neighbors = new ArrayList<>();
            neighbors = getNeighbors(state, vertices);
            // choose optimal N out of all Ns based on error calculation
            String chosenN = chooseN(graph, vertices, neighbors, goal);
            int chosenError = calcError(graph, vertices, chosenN, goal);
            int startingError = calcError(graph, vertices, state, goal);
            // Verbose printing
            if(flag.equals('V')){
                System.out.println("Neighbors:");
                // loop through all neighbor collected
                for (String neighbor : neighbors){
                    // neighbor data
                    String formatStartState = formatStateForPrint(neighbor);
                    int startValue = calcValue(vertices, neighbor);
                    int startError = calcError(graph, vertices, neighbor, goal);
                    String formatPrintLine = formatStartState + "Value=" + startValue 
                    + ". Error=" + startError + ".";
                    // print neighbor
                    System.out.println(formatPrintLine);
                    if(startError == 0){
                        return neighbor;
                    }
                }
            } 
            // BC - if chosen neighbor is not better than current state
            if (chosenError >= startingError){
                return state;
            }
            state = chosenN;
            // Verbose printing - if chosen neighbor is a better choice than original state
            if(flag.equals('V')){
                String formatNextState = formatStateForPrint(state).trim();
                int nextStateValue = calcValue(vertices, state);
                int nextStateError = calcError(graph, vertices, state, goal);
                String formatNextPrintLine = "\nMove to " + formatNextState + 
                ". Value=" + nextStateValue + ". Error=" + nextStateError + ".";
                System.out.println(formatNextPrintLine);
            }
            if(chosenError == 0){
                return state;
            }
        }
    }

    // HELPER 1 - choose best Neighbor
    public static String chooseN(HashMap<Character, List<Character>> graph,
    HashMap<Character, Integer> vertices, List<String> neighbors, int goal){
        List<Integer> errorValues = new ArrayList<>();
        String chosen = "";
        int chosenIndex = 0;
        // calculate all error values
        for (String neighbor : neighbors){
            int errorValue = calcError(graph, vertices, neighbor, goal);
            errorValues.add(errorValue);
        }
        // compare all errorValues
        for (int i = 1; i < errorValues.size(); i++){
            if(errorValues.get(i) < errorValues.get(chosenIndex)){
                chosenIndex = i;
            }
            else{
                continue;
            }
        }
        chosen = neighbors.get(chosenIndex);
        return chosen;
    }

    // HELPER 2 - calculate error of a state/string
    public static int calcError(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, String state, int T){
        // 1) TOTAL VALUE OF VERTICES IN STATE
        int valueTotal = calcValue(vertices, state);
        // empty state
        if(state.length() == 0){
            return T;
        }
        // 1 vertex in state
        else if(state.length() == 1){
            return (T - vertices.get(state.charAt(0)));
        }
        // more than 1 vertex
        else{       
            // 2) COST OF EDGES
            int edgeCostSum = 0;
            // get all edges in state (no duplicates)
            HashSet<String> edges = new HashSet<>();
            char[] verticesInState = state.toCharArray();
            for(int i = 0; i < verticesInState.length; i++){
                for(int j = 0; j < verticesInState.length; j++){
                    if(i == j){
                        continue;
                    }
                    else{
                        // no duplicate edges
                        char[] edge = {verticesInState[i], verticesInState[j]};
                        Arrays.sort(edge);
                        if(isEdge(graph, new String(edge))){
                            edges.add(new String(edge));
                        }
                    }
                }
            }
            // for each edge in state 
            for (String edge : edges){
                int cost = calcEdgeCost(vertices, edge);
                edgeCostSum += cost;
            }
            // 4) Compare to get max value
            int cmpSum = T - valueTotal;
            // 5) return max(cmpSum, 0)
            if (cmpSum>0){
                return cmpSum + edgeCostSum;
            }
            else{
                return 0 + edgeCostSum;
            }
        }
    }

    // HELPER 3 - calculate the cost of an edge
    public static int calcEdgeCost(HashMap<Character, Integer> vertices, String edge){
        Character vertex1 = edge.charAt(0);
        Character vertex2 = edge.charAt(1);
        int cost1 = vertices.get(vertex1);
        int cost2 = vertices.get(vertex2);
        if(cost1<=cost2){
            return cost1;
        }
        else{
            return cost2;
        }
    }

    // HELPER 4 - checks if this edge exists in graph
    public static boolean isEdge(HashMap<Character, List<Character>> graph, 
    String edge){
        Character vertex1 = edge.charAt(0);
        Character vertex2 = edge.charAt(1);
        for(Character adj : graph.get(vertex1)){
            if(adj.equals(vertex2)){
                return true;
            }
        }
        return false;
    }

    // HELPER 5 - produces a random starting state with 50% chance for each vertex in graph
    public static String getRandomState(HashMap<Character, Integer> vertices){
        // to construct state
        String randomState = "";
        // for all vertices in graph
        for (Character vertex : vertices.keySet()){
            // Math.random() produces values from 0.0 - 1.0
            if(Math.random() < 0.5){
                randomState += vertex;
            }
            // reject vertex
            else{
                continue;
            }
        }
        return randomState;
    }

    // HELPER 6 - return a list of neighbors to a given state
    public static List<String> getNeighbors(String state, 
    HashMap<Character, Integer> vertices){
        List<String> neighbors = new ArrayList<>();
        // if state is empty set
        if (state.trim().length() == 0){
            for(Character c : vertices.keySet()){
                neighbors.add(c.toString().trim());
            }
        }
        // if state has a chain of one vertex or more
        else{
            // all options to add a vertex not in state already
            for(Character c : vertices.keySet()){
                // if vertex c not already in state
                if(state.indexOf(c)==-1){
                    String neighbor = state + c.toString().trim();
                    neighbors.add(neighbor);
                }
            }
            // all options to remove a vertex
            for(int i = 0; i < state.length(); i++){
                Character vertex = state.charAt(i);
                String neighbor = state.replace(vertex.toString().trim(), "").trim();
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // HELPER 7 - calculate total vertices value of state
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

    // HELPER 6 - format a state (string) for printing
    public static String formatStateForPrint(String state){
        if (state.length() == 0){
            return "{} ";
        }
        String formatState = "";
        for (int i = 0; i < state.length(); i++){
            formatState += state.charAt(i) + " ";
        }
        return formatState;
    }
}

// CITATIONS: 
//      https://www.geeksforgeeks.org/java-math-random-method-examples/