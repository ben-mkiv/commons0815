package ben_mkiv.guitoolkit.client.widget;

public class ToggleButton extends prettyButton {
    private boolean val = false;
    private String textLabel = "";

    public ToggleButton(int id, int x, int y, int width, int height, String label, boolean initialVal){
        super(id, x, y, width, height, label + " ("+initialVal+")");
        textLabel = label;
        val = initialVal;
    }

    public void toggle(){
        val = !val;
        displayString = textLabel + " ("+val+")";
    }
}
