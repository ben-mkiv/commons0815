package ben_mkiv.guitoolkit.client.widget;

import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class prettyList implements prettyElement {
    public ArrayList<ArrayList<prettyElement>> elements = new ArrayList<>();

    int scrollValue = 0;
    int x = 0, y = 0, displayElements = 5, elementHeight;
    boolean visible = true;
    String name = "";

    public prettyList(String label){
        this.name = label;
    }

    public prettyList(String label, int posX, int posY){
        this(label);
        this.x = posX;
        this.y = posY;
    }

    public void add(ArrayList<prettyElement> entry){
        int maxHeight = 0;

        for(prettyElement element : entry) {
            setX(Math.max(0, Math.min(getX(), element.getX())));
            setY(Math.max(0, Math.min(getY(), element.getY())));

            if(!(element instanceof prettyList))
                maxHeight = Math.max(maxHeight, element.getY() + element.getHeight());

            /*
            if(element instanceof prettyButton) {
                ((prettyButton) element).renderX = getX();
                ((prettyButton) element).renderY = getY();
            }
            */
        }

        elementHeight = Math.max(elementHeight, maxHeight);

        elements.add(entry);
    }

    public prettyList getSubList(String name){
        for(ArrayList<prettyElement> entry : elements)
            if(isList(entry))
                if(getList(entry).name.equals(name))
                    return getList(entry);

        return null;
    }

    @Override // Interface prettyElement
    public int getY(){ return this.y; }

    @Override // Interface prettyElement
    public int getX(){ return this.x; }

    @Override // Interface prettyElement
    public int getWidth() {
        int maxX = Integer.MIN_VALUE;

        for (ArrayList<prettyElement> el : elements)
            for(prettyElement entry : el)
                //if(!(entry instanceof prettyList))
                    maxX = Math.max(maxX, entry.getX() + entry.getWidth());

        return maxX - getX();
    }

    @Override // Interface prettyElement
    public int getHeight(){
        return elementHeight * displayElements;
    }

    @Override // Interface prettyElement
    public void setY(int pos){
        int offset = pos - this.y;

        for (ArrayList<prettyElement> el : elements)
            for(prettyElement entry : el)
                entry.setY(entry.getY() + offset);

        this.y = pos; }

    @Override
    public void setRenderX(int pos){ x = pos; }

    public void setRenderY(int pos){ y = pos; }

    @Override // Interface prettyElement
    public void setX(int pos){
        int offset = pos - this.x;

        for (ArrayList<prettyElement> el : elements)
            for(prettyElement entry : el)
                entry.setX(entry.getX() + offset);

        this.x = pos; }

    @Override // Interface prettyElement
    public void setVisible(boolean isVisible){

        if(!isVisible)
            for (ArrayList<prettyElement> el : elements)
                for(prettyElement entry : el)
                    entry.setVisible(false);

        visible = isVisible;
    }

    @Override // Interface prettyElement
    public boolean getVisible(){ return visible; }

    public void add(prettyElement element){
        ArrayList<prettyElement> list = new ArrayList<>();
        list.add(element);
        this.add(list);
    }

    public boolean isMouseOver(){
        for(ArrayList<prettyElement> entry : elements)
            for(prettyElement element : entry)
                if(element instanceof prettyButton)
                    if(((prettyButton) element).isMouseOver())
                        return true;

        return false;
    }

    public void update(){
        for(ArrayList<prettyElement> el : elements)
            if(isList(el))
                getList(el).update();

        if(!getVisible())
            return;

        if(isMouseOver())
            scrollValue -= Integer.signum(Mouse.getDWheel());

        scrollValue = Math.max(0, Math.min(scrollValue, (elements.size() - displayElements)));

        for(int i=0; i < scrollValue; i++)
            for(prettyElement element : elements.get(i))
                if(element instanceof prettyButton)
                    element.setVisible(false);

        for(int i=scrollValue+displayElements-1; i < elements.size(); i++)
            for(prettyElement element : elements.get(i))
                if(element instanceof prettyButton)
                    element.setVisible(false);

        for(int i=scrollValue, s=0; i < (elements.size()) && i < (scrollValue + displayElements); i++, s++){
            for(prettyElement element : elements.get(i)){
                element.setRenderY(elementHeight * s);
                if(element instanceof prettyButton)
                    element.setVisible(true);
            }
        }

    }

    public boolean isList(ArrayList<prettyElement> listElement){
        return listElement.get(0) instanceof prettyList;
    }

    public prettyList getList(ArrayList<prettyElement> listElement){
        return isList(listElement) ? (prettyList) listElement.get(0) : null;
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

    public void setScrollValue(int value){
        this.scrollValue = value;
    }

    public int getScrollValue(){
        return this.scrollValue;
    }

    public void setDisplayElements(int count){
        this.displayElements = count;
        //Todo: recalc height
    }

    public int getDisplayElements(){
        return this.displayElements;
    }

    public void hideGroups(){
        hideGroups(0);
    }

    public void hideGroups(int maxLevel){
        setVisible(false);

        if(maxLevel >= 0)
            setVisible(true);

        for(ArrayList<prettyElement> entry : elements)
            if(isList(entry))
                getList(entry).hideGroups(maxLevel - 1);

    }

}
