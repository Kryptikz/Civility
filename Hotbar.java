public class Hotbar{
    private Item[] items;
    private int itemSelected;
    public Hotbar(){
        items = new Item[10];
        itemSelected = 0;
    }
    public Item[] getHotbar(){
        return items;
    }
    public void selectItem(int S){
        itemSelected = S;
    }
    public int getItemSelected(){
        return itemSelected;
    }
    public void setHotbarItem(Item i){
        items[itemSelected] = i;
    }
    public void addItemToHotBar(Item i){
        items[itemSelected] = i;
    }
    public void check(){
        for(int i = 0; i < items.length;i++){
            if(items[i] != null && items[i].getQuantity() <= 0){
                items[itemSelected] = null;
            }
        }
    }
}