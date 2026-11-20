package pedidos;

public class EstacionCaja extends EstacionRol {

    public EstacionCaja() {
        super("Caja");
    }

    @Override
    public void actualizar(Pedido pedido) {
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            System.out.println(getNombreEstacion() + " Pedido #"
                    + pedido.getIdPedido() + " entregado - Subtotal: $" + pedido.calcularSubtotal());
        }
    }
}