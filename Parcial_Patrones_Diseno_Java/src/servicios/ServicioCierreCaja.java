package servicios;

import datos.IRepositorioPedidos;
import java.util.List;
import pedidos.Pedido;

public class ServicioCierreCaja {
    private IRepositorioPedidos repositorio;

    public ServicioCierreCaja(IRepositorioPedidos repositorio) {
        this.repositorio = repositorio;
    }

    public String generarReporteCaja() {
        return "";
    }

    public List<Pedido> obtenerPedidosPagados() {
        return null;
    }

    public double calcularTotalVentas() {
        return 0.0;
    }
}