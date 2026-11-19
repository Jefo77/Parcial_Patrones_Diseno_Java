package pedidos;

import productos.productosDecorator.IProducto;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final int idPedido;
    private EstadoPedido estado;
    private final List<IProducto> productos;
    private final List<IObservadorPedido> observadores;

    public Pedido(int idPedido) {
        this.idPedido = idPedido;
        this.estado = EstadoPedido.PENDIENTE;
        this.productos = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    public void agregarProducto(IProducto producto) {
        this.productos.add(producto);
    }

    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (IProducto p : productos) {
            subtotal += p.getPrecio();
        }
        return subtotal;
    }

    public void agregarObservador(IObservadorPedido observador) {
        this.observadores.add(observador);
    }

    public void eliminarObservador(IObservadorPedido observador) {
        this.observadores.remove(observador);
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (this.estado != nuevoEstado) {
            this.estado = nuevoEstado;
            notificarObservadores();
        }
    }

    private void notificarObservadores() {
        for (IObservadorPedido observador : observadores) {
            observador.actualizar(this);
        }
    }

    public int getIdPedido() {
        return idPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<IProducto> getProductos() {
        return productos;
    }
}