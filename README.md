# Parcial_Patrones_Diseno_Java
1. Alcance del Sistema 

Funcionalidades que se implementarán: 

Personalización dinámica de productos (bebidas y platos) mediante la suma de adiciones que alteran el precio final y la descripción del pedido. 

Creación de combos complejos paso a paso (ej. Selección de plato principal, bebida, acompañamiento y postre). 

Sistema de notificaciones internas de cambio de estado de los pedidos (Pendiente, En Preparación, Listo, Entregado) para alertar a los roles correspondientes (Cocina, Caja, Mesero). 

Funcionalidades que se dejarán fuera (y la razón): 

Control estricto de inventario y stock de ingredientes: Se deja fuera porque requeriría una base de datos compleja y validaciones transaccionales profundas, lo cual desvía el tiempo y el objetivo principal, que es evaluar la aplicación de patrones de diseño orientados a objetos. 

Pasarelas de pago reales o facturación fiscal: Se realizará un cierre de caja simulado. Integrar APIs bancarias externas y añadiría una capa de complejidad técnica innecesaria para el alcance académico. 

Tres áreas con variación o cambio frecuente en el sistema: 

La estructura de los productos: Las combinaciones de ingredientes extras o adiciones que un cliente puede pedir son prácticamente infinitas y cambian constantemente. 

La construcción de los menús/combos: Armar un combo requiere muchos pasos y validaciones, variando según el tipo de combo. 

Los canales de aviso: Hoy el sistema avisa a Caja, Cocina y Mesero, pero mañana el restaurante podría habilitar una pantalla para "Domiciliarios" o "Clientes esperando", lo que exige añadir nuevos receptores de alertas frecuentemente. 

2. Requisitos 

Requisitos Funcionales (RF): 

RF-01: El sistema debe permitir seleccionar un producto base y agregarle múltiples adiciones opcionales, actualizando automáticamente el precio total y la descripción final del ítem. 

RF-02: El sistema debe permitir la configuración y venta de combos estructurados paso a paso, garantizando que no falten los elementos obligatorios del combo. 

RF-03: El sistema debe notificar de manera automática e instantánea a las vistas de "Cocina", "Caja" y "Mesero" cada vez que un pedido avance a un nuevo estado. 

RF-04: El sistema calculará el valor final de la cuenta evaluando y aplicando promociones dinámicas según la hora del día o el día de la semana. 

RF-05: El sistema debe generar un reporte de cierre de caja básico al final de la jornada sumando todos los pedidos completados. 

Requisitos No Funcionales (RNF): 

RNF-01 (Arquitectura): El sistema se construirá como un Monolito estructurado en capas (Presentación, Lógica de Negocio y Acceso a Datos), lo cual es ideal para un equipo pequeño de 3 personas que necesita velocidad de desarrollo inicial. 
