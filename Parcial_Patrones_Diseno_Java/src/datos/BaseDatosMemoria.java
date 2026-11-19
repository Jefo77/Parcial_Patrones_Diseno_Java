package datos;

import pedidos.Pedido;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosMemoria implements IRepositorioPedidos {
    private List<Pedido> historialPedidos;

    public BaseDatosMemoria() {
        this.historialPedidos = new ArrayList<>();
    }

    @Override
    public void guardarPedido(Pedido pedido) {

    }

    @Override
    public List<Pedido> obtenerVentasDelDia() {
        return null;
    }
}