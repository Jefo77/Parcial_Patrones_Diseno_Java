package presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
public class VistaNotificaciones extends JPanel {

    private final JTextArea areaCocina = crearArea("Cocina");
    private final JTextArea areaMesero = crearArea("Mesero");
    private final JTextArea areaCaja = crearArea("Caja");

    public VistaNotificaciones(ControladorPedidos ctrl) {
        setLayout(new GridLayout(1, 3, 12, 0));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setBackground(new Color(0xFAF6F1));
        add(envolver(areaCocina, "Cocina"));
        add(envolver(areaMesero, "Mesero"));
        add(envolver(areaCaja, "Caja"));

        ctrl.getCocina().agregarReceptor(msg -> imprimirAlerta("Cocina", msg));
        ctrl.getMesero().agregarReceptor(msg -> imprimirAlerta("Mesero", msg));
        ctrl.getCaja().agregarReceptor(msg -> imprimirAlerta("Caja", msg));
    }

    public void imprimirAlerta(String estacion, String mensaje) {
        JTextArea area = "Cocina".equals(estacion) ? areaCocina : "Mesero".equals(estacion) ? areaMesero : areaCaja;
        area.append(mensaje + "\n");
        area.setCaretPosition(area.getDocument().getLength());
    }

    private static JTextArea crearArea(String titulo) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setMargin(new java.awt.Insets(8, 8, 8, 8));
        return area;
    }

    private static JScrollPane envolver(JTextArea area, String titulo) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createTitledBorder("Avisos de " + titulo));
        return scroll;
    }
}
