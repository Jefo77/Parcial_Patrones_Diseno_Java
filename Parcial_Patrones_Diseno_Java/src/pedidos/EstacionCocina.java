package pedidos;

public class EstacionCocina extends EstacionRol {
    public EstacionCocina() {
        super("Cocina");
    }

    @Override
    public void actualizar(Pedido pedido) {
        if (pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
            System.out.println(getNombreEstacion() + " Preparar pedido #"
                    + pedido.getIdPedido() + " (" + pedido.getProductos().size() + " ítems)");
        }
    }
}
