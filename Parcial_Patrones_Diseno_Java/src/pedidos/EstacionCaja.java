package pedidos;

public class EstacionCaja extends EstacionRol {

    public EstacionCaja() {
        super("Caja");
    }

    @Override
    public void actualizar(Pedido p) {
        if (p.getEstado() == EstadoPedido.ENTREGADO) {
            emitir("Pedido #" + p.getIdPedido() + " ENTREGADO: listo para cobrar " + Pedido.dinero(p.getTotal()));
        } else {
            emitir("Pedido #" + p.getIdPedido() + " cambió a " + p.getEstado() + " (total " + Pedido.dinero(p.getTotal()) + ")");
        }
    }

    @Override
    public boolean puedeCrearPedidos() {
        return true;
    }

    @Override
    public boolean puedeAvanzarA(EstadoPedido nuevo) {
        return true;
    }

    @Override
    public boolean puedeAdministrar() {
        return true;
    }
}
