/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jTextFieldAutoCOmplete;

import javax.swing.JLabel;

/**
 *
 * @author JOSE
 */
public class Item extends JLabel{

    public Item(String text) {
        super(text);
    }
    
    
    
    
    private int selectedIndex;
    private String selectedItem;

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedItem() {
        return selectedItem;
    }
    
    
    
    
    
    
}
