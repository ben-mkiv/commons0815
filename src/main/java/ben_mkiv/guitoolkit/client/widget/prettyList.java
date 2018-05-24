package ben_mkiv.guitoolkit.client.widget;

import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class prettyList {
    public ArrayList<ArrayList<prettyButton>> elements = new ArrayList<>();

    public int scrollValue = 0, y = 0, displayElements = 5;

    public prettyList(){}

    public void add(ArrayList<prettyButton> entry){
        for(prettyButton button : entry) {

            this.y = Math.max(0, Math.min(this.y, button.y));
        }

        elements.add(entry);
    }

    public void update(int x, int y){
        scrollValue-=Integer.signum(Mouse.getDWheel());
        scrollValue = Math.max(0, Math.min(scrollValue, (elements.size()-displayElements)));

        for(int i=0; i < scrollValue; i++)
            for(prettyButton button : elements.get(i))
                button.visible = false;

        for(int i=scrollValue+displayElements-1; i < elements.size(); i++)
            for(prettyButton button : elements.get(i))
                button.visible = false;


        for(int i=scrollValue, s=0; i < (elements.size()) && i < (scrollValue + displayElements); i++, s++){
            for(prettyButton button : elements.get(i)){
                button.y= y + (button.height * s);
                button.x+= x;
                button.visible = true;
            }
        }
    }

    public int scrollUp(){
        return --scrollValue;
    }

    public int scrollDown(){
        return ++scrollValue;
    }

    public boolean canScroll(){
        return this.elements.size() > this.displayElements;
    }

    public boolean canScrollUp(){
        return this.scrollValue > 0;
    }

    public boolean canScrollDown(){
        return this.scrollValue < (this.elements.size() - this.displayElements);
    }



}
