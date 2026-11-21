package servicios;

import datos.IRepositorioPedidos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pedidos.EstadoPedido;
import pedidos.ItemPedido;
import pedidos.Pedido;

public class ServicioCierreCaja {

    private static final int TOP_VENDIDOS = 5;

    private final IRepositorioPedidos repositorio;

    public ServicioCierreCaja(IRepositorioPedidos repositorio) {
        this.repositorio = repositorio;
    }

    public ReporteCaja generarReporte(LocalDate fecha) {
        List<Pedido> ventas = repositorio.obtenerVentasDelDia(fecha);
        double recaudado = 0;
        Map<String, Integer> conteo = new HashMap<>();
        for (Pedido p : ventas) {
            recaudado += p.getTotal();
            for (ItemPedido item : p.getItems()) {
                conteo.merge(item.getProducto().getDescripcion(), item.getCantidad(), Integer::sum);
            }
        }
        Map<String, Integer> ordenado = new LinkedHashMap<>();
        conteo.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(TOP_VENDIDOS)
                .forEach(e -> ordenado.put(e.getKey(), e.getValue()));
        return new ReporteCaja(fecha, ventas.size(), recaudado, ordenado);
    }

    /** Pedidos ENTREGADOS que todavía no se han cobrado. */
    public List<Pedido> obtenerPedidosEntregados() {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : repositorio.obtenerPedidos()) {
            if (p.getEstado() == EstadoPedido.ENTREGADO && !p.isCerrado()) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /** Total cobrado en el día de hoy. */
    public double calcularTotalVentas() {
        double total = 0;
        for (Pedido p : repositorio.obtenerVentasDelDia(LocalDate.now())) {
            total += p.getTotal();
        }
        return total;
    }
}
