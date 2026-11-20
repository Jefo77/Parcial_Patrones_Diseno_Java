package pedidos;

public class EstacionMesero extends EstacionRol {
    public EstacionMesero() {
        super("Mesero");
    }

    @Override
    public void actualizar(Pedido pedido) {
        if (pedido.getEstado() == EstadoPedido.LISTO) {
            System.out.println(getNombreEstacion() + " Pedido #"
                    + pedido.getIdPedido() + " listo para llevar a la mesa/entrega");
        }
    }
}
