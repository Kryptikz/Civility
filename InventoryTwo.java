import java.util.*;
public class InventoryTwo{
    private ArrayList<Item> inven;
    public InventoryTwo(){
        inven = new ArrayList<Item>();
    }
    public void addItem(Item a){
        boolean found = false;
        for(Item b: inven){
            if(a.getName().equals(b.getName())){
                System.out.println("before: " + a.getQuantity());
                b.changeQuantity(a.getQuantity());
                System.out.println("after: " + a.getQuantity());
                found = true;
                break;
            }
        }
        if(!found){
            inven.add(a);
        }
    }
    public ArrayList<Item> getInventory(){
        return inven;
    }
    public void craft(Item i){
        addItem(new Item(i.getName(),i.getQuantity(),i.getCanBePlaced(),null,"",null,i.getDamages(),i.getTechLevel()));
        for(TempItem a: i.getRequired()){
            removeItem(a.getName(),a.getQuantity());
        }
    }
    public void removeItem(String name, int quantity){
        for(int a = 0; a < inven.size();a++){
            if(inven.get(a).getName().equals(name)){
                inven.get(a).changeQuantity(quantity*-1);
                if(inven.get(a).getQuantity() == 0){
                    inven.remove(a);
                }
            }
        }
    }
    public int getQuantityOf(String name){
        for(Item a: inven){
            if(a.getName().equals(name)){
                return a.getQuantity();
            }
        }
        return 0;
    }
    public boolean invenContains(Item a){
        for(Item b: inven){
            if(a.getName().equals(b.getName())){
                return true;
            }
        }
        return false;
    }
}