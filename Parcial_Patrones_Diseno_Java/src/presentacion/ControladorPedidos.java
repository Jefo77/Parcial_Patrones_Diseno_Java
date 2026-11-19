package presentacion;

import datos.IRepositorioPedidos;
import java.util.List;
import pedidos.EstadoPedido;
import pedidos.IObservadorPedido;
import pedidos.Pedido;
import productos.productosDecorator.IProducto;
import servicios.CalculadoraCuenta;
import servicios.ServicioCierreCaja;

public class ControladorPedidos {
    private IRepositorioPedidos repositorio;
    private CalculadoraCuenta calculadora;
    private ServicioCierreCaja servicioCierreCaja;

    public ControladorPedidos(IRepositorioPedidos repositorio) {
        this.repositorio = repositorio;
        this.calculadora = new CalculadoraCuenta();
        this.servicioCierreCaja = new ServicioCierreCaja(repositorio);
    }

    public Pedido crearPedido(int id, String cliente, List<IObservadorPedido> estaciones) {
        return null;
    }

    public void agregarProductoAPedido(Pedido pedido, IProducto producto) {

    }

    public void cambiarEstadoPedido(Pedido pedido, EstadoPedido nuevoEstado) {

    }

    public double cobrarYGuardarPedido(Pedido pedido) {
        return 0.0;
    }

    public String obtenerReporteCierreCaja() {
        return "";
    }
}