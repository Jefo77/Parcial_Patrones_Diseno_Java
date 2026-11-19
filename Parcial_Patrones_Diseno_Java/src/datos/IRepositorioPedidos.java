package datos;

import pedidos.Pedido;
import java.util.List;

public interface IRepositorioPedidos {
    void guardarPedido(Pedido pedido);

    List<Pedido> obtenerVentasDelDia();
}
