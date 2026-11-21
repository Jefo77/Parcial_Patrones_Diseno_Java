package pedidos;

import productos.decorator.IProducto;

public class ItemPedido {

    private final IProducto producto;
    private int cantidad;

    public ItemPedido(IProducto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public IProducto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}

