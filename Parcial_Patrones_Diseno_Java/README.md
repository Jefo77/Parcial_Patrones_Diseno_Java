Aplicación de gestión de pedidos en Java 17+ con interfaz gráfica (Swing) y modo consola sobre un mismo núcleo de negocio. Implementa arquitectura en capas + MVC y los patrones de diseño Decorator, Builder, Strategy y Observer. No requiere conexión a internet ni librerías externas.

Requisitos y Ejecución (Visual Studio Code)
Requisitos previos: Tener instalado JDK 17 o superior y la extensión Extension Pack for Java (Microsoft) en VS Code.

Preparación del proyecto:

Abre VS Code, ve a File → Open Folder… y selecciona la carpeta raíz SistemaPedidos (la que contiene src/ y .vscode/, no abras src directamente).

Espera a que Java termine de cargar en la barra inferior derecha ("Java: Ready").

Ejecutar Interfaz Gráfica (JFrame):

Abre src/presentacion/VentanaPrincipal.java y haz clic en Run (el botón ▶ sobre el método main, se encuentra al final del codiga), o ve a Run and Debug (Ctrl+Shift+D) y selecciona Interfaz grafica (VentanaPrincipal).

Ejecutar Menú por Consola (Terminal):

Ve a Run and Debug (Ctrl+Shift+D) y selecciona Consola (MenuConsola) (o abre src/presentacion/MenuConsola.java y pulsa Run). El menú interactivo se ejecutará directamente en la terminal integrada de VS Code.

Solución de problemas: Si el proyecto no compila o no arranca, presiona Ctrl+Shift+P, busca y ejecuta Java: Clean Java Language Server Workspace y selecciona Restart and delete.

Arquitectura y Patrones Aplicados
MVC y Capas: Separa la lógica de negocio (pedidos, productos, servicios) y acceso a datos (datos) de las vistas (VentanaPrincipal, MenuConsola, VistaNotificaciones). Las vistas solo se comunican con ControladorPedidos.

Decorator (productos.decorator): Permite añadir extras (ExtraLeche, ExtraQueso) dinámicamente a productos base (BebidaBase, PlatoBase) sin crear subclases por cada combinación.

Builder (productos.builder): Construye combos paso a paso mediante ComboBuilder y ComboDirector, validando que incluyan obligatoriamente un plato principal y una bebida.

Strategy (servicios): Calcula descuentos sin condicionales rígidos usando DescuentoStrategy (SinDescuento, DescuentoEstudiante, PromocionHorario).

Observer (pedidos): Desacopla los cambios de estado del Pedido notificando automáticamente a las estaciones suscritas (EstacionCocina, EstacionMesero, EstacionCaja).

Guion de Prueba Paso a Paso (Validación de Requerimientos)
Los datos se almacenan en memoria (se reinician al cerrar). Puedes cambiar el Rol Activo (Mesero / Cocina / Caja) en la esquina superior derecha de la ventana.

1. Catálogo y Adiciones (RF-01, RF-04, RF-05, RF-08):

En la pestaña 1. Tomar pedido, filtra por categoría para verificar el menú.

Con rol Mesero, selecciona B01 Café americano, marca Extra leche = 1 y Extra queso = 1, y pulsa Agregar al pedido. Verifica que el precio unitario suba de $5.000 a $7.000.

2. Gestión de Ítems y Validaciones (RF-08, RF-09, RF-10, RFN-04):

Agrega otro producto con Cantidad = 2, luego selecciona un ítem para Cambiar cantidad (ej. a 3) y prueba Eliminar ítem; los totales deben recalcularse al instante.

Intenta agregar un producto con Cantidad = 0 o escribe letras en el cambio de cantidad para confirmar que el sistema muestra alertas claras sin fallar.

3. Construcción de Combos (RF-06, RF-07, RF-08):

Ve a la pestaña 2. Armar combo, elige solo un plato principal y pulsa Agregar combo; debe alertar que falta la bebida.

Selecciona plato + bebida (y opcionalmente acompañamiento/postre) y agrégalo al pedido.

4. Promociones y Descuentos (RF-10, RF-11):

Vuelve a la pestaña 1, activa Simular día/hora y prueba Miércoles, 16 h (aplica Tarde feliz 20%), Sábado, 10 h (aplica Fin de semana 10%), Miércoles, 10 h (Sin descuento) y el descuento manual de Estudiante (-10%).

5. Confirmación y Notificaciones (RF-14, RFN-03, RFN-08):

Pulsa Paso 3 – Confirmar pedido (si está vacío debe mostrar alerta).

Revisa la pestaña 4. Avisos para comprobar que Cocina, Mesero y Caja recibieron la notificación del pedido nuevo.

6. Flujo de Estados y Permisos por Rol (RF-13, RF-14, RFN-05):

Cambia a rol Cocina, ve a la pestaña 3 y avanza el pedido dos veces (PENDIENTE → EN_PREPARACION → LISTO). Un tercer intento debe bloquearse con alerta porque Cocina no puede marcar como ENTREGADO.

Cambia a rol Mesero y avanza el pedido a ENTREGADO. Verifica que Caja reciba el aviso de cobro y que ni Mesero ni Cocina tengan permiso para cobrar o cerrar caja.

7. Cobro, Gestión de Promociones y Cierre de Caja (RF-12, RF-15, RF-16):

Con rol Caja, cobra el pedido entregado (Efectivo o Tarjeta) en la pestaña 3 para marcarlo como cerrado.

En la pestaña 5, desactiva una promoción o crea una nueva, toma otro pedido para comprobar el cambio y pulsa Generar cierre de caja para ver el resumen recaudado y productos más vendidos.

8. Verificación en Consola (RFN-02):

Ejecuta MenuConsola y realiza un pedido rápido para comprobar que comparte exactamente las mismas reglas y cálculos que la interfaz gráfica.

Alcance y Extensibilidad (RFN-06)
Alcance funcional: El menú inicial se carga en código (BaseDatosMemoria.cargarMenuBase()), por lo que la creación/edición de productos por interfaz (RF-02 y RF-03) está fuera de alcance. Las promociones aplican sobre el subtotal general (si coinciden varias, prevalece la de mayor porcentaje).

Cómo extender el sistema sin modificar el núcleo:

Nueva adición: Crea una clase que herede de AdicionDecorator (ej. ExtraSalsa).

Nueva promoción: Créala desde la pestaña 5 en ejecución, regístrala en ControladorPedidos o implementa DescuentoStrategy para reglas personalizadas.

Nuevo módulo receptor de avisos: Crea una subclase de EstacionRol y suscríbela mediante pedido.agregarObservador
