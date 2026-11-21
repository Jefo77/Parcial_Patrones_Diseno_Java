package pedidos;

public class EstacionCocina extends EstacionRol {

    public EstacionCocina() {
        super("Cocina");
    }

    @Override
    public void actualizar(Pedido p) {
        if (p.getEstado() == EstadoPedido.PENDIENTE) {
            emitir("NUEVO pedido #" + p.getIdPedido() + ": " + p.getCantidadTotalItems() + " unidad(es) por preparar");
        } else {
            emitir("Pedido #" + p.getIdPedido() + " cambió a " + p.getEstado());
        }
    }

    @Override
    public boolean puedeCrearPedidos() {
        return false;
    }

    @Override
    public boolean puedeAvanzarA(EstadoPedido nuevo) {
        return nuevo == EstadoPedido.EN_PREPARACION || nuevo == EstadoPedido.LISTO;
    }

    @Override
    public boolean puedeAdministrar() {
        return false;
    }
}
