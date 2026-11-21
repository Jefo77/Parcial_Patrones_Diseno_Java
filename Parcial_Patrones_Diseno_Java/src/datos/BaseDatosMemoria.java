package datos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pedidos.Pedido;
import productos.decorator.BebidaBase;
import productos.decorator.IProducto;
import productos.decorator.PlatoBase;

public class BaseDatosMemoria implements IRepositorioProductos, IRepositorioPedidos {

    private final Map<String, IProducto> productos = new LinkedHashMap<>();
    private final List<Pedido> pedidos = new ArrayList<>();

    public BaseDatosMemoria() {
        cargarMenuBase();
    }

    private void cargarMenuBase() {
   
        registrar(new BebidaBase("B01", "Café americano", 5000));
        registrar(new BebidaBase("B02", "Capuchino", 6500));
        registrar(new BebidaBase("B03", "Limonada natural", 4500));
        registrar(new BebidaBase("B04", "Jugo de naranja", 5500));
        registrar(new BebidaBase("B05", "Gaseosa", 3500));
      
        registrar(new PlatoBase("P01", IProducto.PLATO, "Hamburguesa clásica", 18000));
        registrar(new PlatoBase("P02", IProducto.PLATO, "Pizza personal", 16000));
        registrar(new PlatoBase("P03", IProducto.PLATO, "Sándwich de pollo", 14000));
        registrar(new PlatoBase("P04", IProducto.PLATO, "Ensalada César", 13000));
        
        registrar(new PlatoBase("A01", IProducto.ACOMPANAMIENTO, "Papas fritas", 5000));
        registrar(new PlatoBase("A02", IProducto.ACOMPANAMIENTO, "Aros de cebolla", 5500));
        registrar(new PlatoBase("A03", IProducto.ACOMPANAMIENTO, "Ensalada pequeña", 4000));
        
        registrar(new PlatoBase("D01", IProducto.POSTRE, "Brownie", 6000));
        registrar(new PlatoBase("D02", IProducto.POSTRE, "Cheesecake", 7500));
        registrar(new PlatoBase("D03", IProducto.POSTRE, "Helado", 4500));
    }

    private void registrar(IProducto producto) {
        productos.put(producto.getId().toUpperCase(), producto);
    }


    @Override
    public List<IProducto> obtenerTodos() {
        return new ArrayList<>(productos.values());
    }

    @Override
    public List<IProducto> obtenerPorCategoria(String categoria) {
        if (categoria == null || categoria.equalsIgnoreCase("TODAS")) {
            return obtenerTodos();
        }
        List<IProducto> resultado = new ArrayList<>();
        for (IProducto p : productos.values()) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public IProducto buscarPorId(String id) {
        return id == null ? null : productos.get(id.trim().toUpperCase());
    }

    @Override
    public int siguienteId() {
        return pedidos.size() + 1;
    }

    @Override
    public void guardarPedido(Pedido pedido) {
        if (!pedidos.contains(pedido)) {
            pedidos.add(pedido);
        }
    }

    @Override
    public List<Pedido> obtenerPedidos() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public List<Pedido> obtenerVentasDelDia(LocalDate fecha) {
        List<Pedido> ventas = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.isCerrado() && p.getFechaPago().toLocalDate().equals(fecha)) {
                ventas.add(p);
            }
        }
        return ventas;
    }
}
