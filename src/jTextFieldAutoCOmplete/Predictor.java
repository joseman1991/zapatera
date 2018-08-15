/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jTextFieldAutoCOmplete;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author JOSE
 */
public class Predictor {

    private final JTextField jTextField;
    private final String[] fields;
    private final List lista;
    private Element Elemento;
    private final List<Element> listaClase;
    private final JPanel parent;
    private final JPanel panel;

    private final List<Element> elementos;

    private MouseAdapter eventoClick;

    private int index;

    private int ancho;

    private final List<Item> listaItems;

    private JScrollPane scroll;

    public Predictor(List lista, JTextField jTextField) {
        this.panel = new JPanel();
        this.parent = (JPanel) jTextField.getParent();
        this.lista = lista;
        index = 0;
        this.jTextField = jTextField;
        fields = new String[2];
        listaClase = new ArrayList<>();
        elementos = new ArrayList<>();
        listaItems = new ArrayList<>();
        inicializar();
        envento();
    }

    private void desaparece(boolean des) {
        panel.setVisible(!des);
        scroll.setVisible(!des);
    }

    private void inicializar() {
        ancho = jTextField.getWidth();
        panel.setBackground(Color.white);
        panel.setSize(new Dimension(ancho, 215));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerifyInputWhenFocusTarget(false);
        panel.setLayout(null);
        scroll.setViewportView(panel);
        scroll.setLocation(jTextField.getX(), jTextField.getY() + jTextField.getHeight());
        parent.add(scroll, 0);
        parent.repaint();

        desaparece(true);
    }

    public void obtener(Class<?> clase, String key, String descripcion) throws PredictorException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int i = 0;
        for (Field f : clase.getDeclaredFields()) {
            if (f.getName().equals(key) || f.getName().equals(descripcion)) {
                fields[i] = f.getName();
                i++;
            }
        }
        PredictorException.verificarNull(fields);
        letraMayuscula();
        for (int j = 0; j < lista.size(); j++) {
            Object get = lista.get(j);
            Element clas = new Element();
            for (int k = 0; k < fields.length; k++) {
                String propiedad = fields[k];
                Method method = clase.getMethod("get" + propiedad);
                String dato = method.invoke(get).toString();
                if (k == 0) {
                    clas.key = dato;
                } else {
                    clas.descripcion = dato;
                }
            }
            listaClase.add(clas);
        }

        cargarPrediccion("");
    }

    private void cargarPrediccion(String cadena) {
        int alto;
        alto = jTextField.getHeight() + 3;
        elementos.clear();
        for (int i = 0; i < listaClase.size(); i++) {
            Element get = listaClase.get(i);
            if (get.descripcion.toLowerCase().contains(cadena.toLowerCase())) {
                elementos.add(get);
            }
        }
        panel.removeAll();
        panel.setSize(ancho, 5);
        scroll.setSize(ancho, 5);

        listaItems.clear();
        for (int i = 0; i < elementos.size(); i++) {
            Element get = elementos.get(i);
            Item jl = new Item(get.descripcion);
            jl.setOpaque(true);
            jl.setBackground(Color.white);
            jl.setSelectedIndex(i);
            jl.setSelectedItem(get.descripcion);
            eventoSelected(jl);
            jl.setSize(ancho, alto);
            jl.setLocation(0, alto * i);

            eventoSelecciona(jl);
            addItem(jl);

            if (i < 5) {
                scroll.setSize(ancho, scroll.getHeight() + alto);
            }

            panel.setSize(ancho, panel.getHeight() + alto);

            panel.add(jl);
        }
        panel.setPreferredSize(panel.getSize());
        panel.revalidate();
        if (listaItems.size() > 0) {
            setSelected(listaItems.get(index));
        }
    }

    private void letraMayuscula() {
        for (int i = 0; i < fields.length; i++) {
            String string = fields[i];
            fields[i] = Character.toUpperCase(string.charAt(0)) + string.substring(1);
        }
    }

    private void envento() {
        jTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (listaItems.size() > 0) {
                    int move = scroll.getVerticalScrollBar().getValue();
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_DOWN:
                            if (index < listaItems.size() - 1) {
                                index++;
                                if (index > 4) {
                                    scroll.getVerticalScrollBar().setValue(move += 47);
                                }
                            } else {
                                index = 0;
                                scroll.getVerticalScrollBar().setValue(0);
                            }
                            setSelected(listaItems.get(index));
                            break;

                        case KeyEvent.VK_UP:
                            if (index > 0) {
                                index--;
                                if (index < (listaItems.size() - 5)) {
                                    scroll.getVerticalScrollBar().setValue(move -= 47);
                                }
                            } else {
                                index = listaItems.size() - 1;
                                scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
                            }
                            setSelected(listaItems.get(index));
                            break;

                        case KeyEvent.VK_ESCAPE:
                            listaItems.clear();
                            elementos.clear();
                            panel.removeAll();
                            desaparece(true);
                            index = 0;
                            parent.requestFocus();
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                cargarPrediccion(jTextField.getText());
                panel.repaint();
                parent.repaint();
                desaparece(false);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        if (listaItems.size() > 0) {
                            jTextField.setText(listaItems.get(index).getSelectedItem());
                            Elemento = elementos.get(index);
                            desaparece(true);
                            index = 0;                            
                        }
                        listaItems.clear();
                        break;
                }
                parent.repaint();
            }
        });

        parent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                desaparece(true);
            }
        });
        eventoFocus();
    }

    private void eventoSelected(Item j) {
        j.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (index != j.getSelectedIndex()) {
                    listaItems.get(index).setForeground(Color.black);
                    listaItems.get(index).setBackground(Color.white);
                }
                j.setForeground(Color.white);
                j.setBackground(new Color(57, 105, 138));

                index = j.getSelectedIndex();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                j.setForeground(Color.black);
                j.setBackground(Color.white);
            }
        });
    }

    private void setSelected(Item j) {
        for (int i = 0; i < listaItems.size(); i++) {
            Item get = listaItems.get(i);
            if (get.getSelectedIndex() == j.getSelectedIndex()) {
                get.setForeground(Color.white);
                get.setBackground(new Color(57, 105, 138));
            } else {
                get.setForeground(Color.black);
                get.setBackground(Color.white);
            }
        }
    }

    private void eventoSelecciona(Item j) {
        j.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                jTextField.setText(j.getText());
                Elemento = elementos.get(index);
                index = 0;
                desaparece(true);
            }
        });
        j.addMouseListener(eventoClick);
    }

    private void eventoFocus() {
        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                index = 0;
            }
        });
    }

    private void addItem(Item i) {
        listaItems.add(i);
    }

    public Element getElemento() {
        return Elemento;
    }

    public void setEventoClick(MouseAdapter eventoClick) {
        this.eventoClick = eventoClick;
    }

}
