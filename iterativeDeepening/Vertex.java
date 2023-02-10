package iterativeDeepening;

public class Vertex {
    
    // Label
    private String label;
    // Value
    private int value;
    
    // default Vertex constructor 
    public Vertex(){
        this.label = "";
        this.value = 0;
    }

    // getter for label
    public String getLabel(){
        return label;
    }

    // setter for label
    public void setLabel(String label){
        this.label = label;
    }

    // getter for value
    public int getValue(){
        return value;
    }

    // setter for value
    public void setValue(int value){
        this.value = value;
    }
}
