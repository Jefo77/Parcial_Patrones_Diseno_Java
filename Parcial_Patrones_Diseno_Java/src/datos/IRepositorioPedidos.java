package datos;

import java.time.LocalDate;
import java.util.List;
import pedidos.Pedido;

public interface IRepositorioPedidos {

    int siguienteId();

    void guardarPedido(Pedido pedido);

    List<Pedido> obtenerPedidos();

    /** Pedidos cobrados (cerrados) en la fecha indicada. */
    List<Pedido> obtenerVentasDelDia(LocalDate fecha);
}
