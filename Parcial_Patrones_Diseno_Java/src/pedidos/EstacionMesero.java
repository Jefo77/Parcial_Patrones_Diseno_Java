package pedidos;

public class EstacionMesero extends EstacionRol {

    public EstacionMesero() {
        super("Mesero");
    }

    @Override
    public void actualizar(Pedido p) {
        if (p.getEstado() == EstadoPedido.LISTO) {
            emitir("Pedido #" + p.getIdPedido() + " LISTO: recógelo y llévalo a la mesa");
        } else {
            emitir("Pedido #" + p.getIdPedido() + " cambió a " + p.getEstado());
        }
    }

    @Override
    public boolean puedeCrearPedidos() {
        return true;
    }

    @Override
    public boolean puedeAvanzarA(EstadoPedido nuevo) {
        return nuevo == EstadoPedido.ENTREGADO;
    }

    @Override
    public boolean puedeAdministrar() {
        return false;
    }
}
