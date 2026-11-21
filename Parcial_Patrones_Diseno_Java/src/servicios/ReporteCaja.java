package servicios;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import pedidos.Pedido;

/** Resultado del cierre de caja. */
public class ReporteCaja {

    private final LocalDate fecha;
    private final int totalPedidos;
    private final double totalRecaudado;
    private final Map<String, Integer> masVendidos; // ya ordenado de mayor a menor

    public ReporteCaja(LocalDate fecha, int totalPedidos, double totalRecaudado, Map<String, Integer> masVendidos) {
        this.fecha = fecha;
        this.totalPedidos = totalPedidos;
        this.totalRecaudado = totalRecaudado;
        this.masVendidos = Collections.unmodifiableMap(masVendidos);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getTotalPedidos() {
        return totalPedidos;
    }

    public double getTotalRecaudado() {
        return totalRecaudado;
    }

    public Map<String, Integer> getMasVendidos() {
        return masVendidos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== CIERRE DE CAJA - ").append(fecha).append(" =====\n");
        sb.append("Pedidos cobrados : ").append(totalPedidos).append("\n");
        sb.append("Dinero recaudado : ").append(Pedido.dinero(totalRecaudado)).append("\n\n");
        sb.append("Productos más vendidos:\n");
        if (masVendidos.isEmpty()) {
            sb.append("  (sin ventas registradas)\n");
        }
        int puesto = 1;
        for (Map.Entry<String, Integer> e : masVendidos.entrySet()) {
            sb.append("  ").append(puesto++).append(". ").append(e.getKey())
              .append("  x").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}
