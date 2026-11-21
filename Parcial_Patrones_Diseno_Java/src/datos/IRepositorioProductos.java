package datos;

import java.util.List;
import productos.decorator.IProducto;

public interface IRepositorioProductos {
    List<IProducto> obtenerTodos();
    List<IProducto> obtenerPorCategoria(String categoria);
    IProducto buscarPorId(String id);
}
