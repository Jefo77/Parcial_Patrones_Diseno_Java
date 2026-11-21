package presentacion;

import datos.BaseDatosMemoria;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import pedidos.ItemPedido;
import pedidos.Pedido;
import productos.decorator.ExtraLeche;
import productos.decorator.ExtraQueso;
import productos.decorator.IProducto;
import servicios.PromocionHorario;
public class VentanaPrincipal extends JFrame {

    private static final Color CAFE = new Color(0x5D4037);
    private static final Color FONDO = new Color(0xFAF6F1);
    private static final Color VERDE = new Color(0x2E7D32);
    private static final Color ROJO = new Color(0xC62828);
    private static final Color NARANJA = new Color(0xC8873A);
    private static final String[] DIAS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    private final ControladorPedidos ctrl;

    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Mesero", "Cocina", "Caja"});
    private final JCheckBox chkEstudiante = new JCheckBox("Estudiante (-10%)");
    private final JCheckBox chkSimular = new JCheckBox("Simular día/hora:");
    private final JComboBox<String> cmbDia = new JComboBox<>(DIAS);
    private final JSpinner spnHora = new JSpinner(new SpinnerNumberModel(15, 0, 23, 1));
    private final JLabel lblEstado = new JLabel("Listo. Selecciona un rol y empieza a tomar pedidos.");

    private final JComboBox<String> cmbCategoria = new JComboBox<>(new String[]{"TODAS", "BEBIDA", "PLATO", "ACOMPANAMIENTO", "POSTRE"});
    private final DefaultTableModel modeloMenu = modelo("ID", "Producto", "Categoría", "Precio base");
    private final JTable tablaMenu = new JTable(modeloMenu);
 
    private final JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, -99, 99, 1));
    private final JSpinner spnLeche = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
    private final JSpinner spnQueso = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
    private final JLabel lblPedidoTitulo = new JLabel();
    private final DefaultTableModel modeloPedido = modelo("Producto", "Cant.", "Precio unit.", "Subtotal");
    private final JTable tablaPedido = new JTable(modeloPedido);
    private final JLabel lblPromo = new JLabel();
    private final JLabel lblSubtotal = new JLabel();
    private final JLabel lblDescuento = new JLabel();
    private final JLabel lblTotal = new JLabel();

    private final JComboBox<String> cmbPlato = new JComboBox<>();
    private final JComboBox<String> cmbBebida = new JComboBox<>();
    private final JComboBox<String> cmbAcomp = new JComboBox<>();
    private final JComboBox<String> cmbPostre = new JComboBox<>();
    private final JSpinner spnCantCombo = new JSpinner(new SpinnerNumberModel(1, -99, 99, 1));
    private final JLabel lblPrecioCombo = new JLabel("Precio del combo: -");

    private final DefaultTableModel modeloPedidos = modelo("ID", "Estado", "Unidades", "Total", "Pago");
    private final JTable tablaPedidos = new JTable(modeloPedidos);
    private final JTextArea txtDetalle = new JTextArea();
    private final JComboBox<String> cmbMetodo = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA"});

    private final DefaultTableModel modeloPromos = modelo("Promoción", "Días", "Horario", "Estado");
    private final JTable tablaPromos = new JTable(modeloPromos);
    private final JTextArea txtReporte = new JTextArea();

    public VentanaPrincipal(ControladorPedidos ctrl) {
        super("Sistema de Pedidos - Cafetería / Restaurante");
        this.ctrl = ctrl;
        configurarEstiloGlobal();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1250, 760);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        VistaNotificaciones avisos = new VistaNotificaciones(ctrl);
        ctrl.getCocina().agregarReceptor(msg -> lblEstado.setText("Último aviso: " + msg));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("1. Tomar pedido", construirTabPedido());
        pestanas.addTab("2. Armar combo", construirTabCombo());
        pestanas.addTab("3. Pedidos y estados", construirTabPedidos());
        pestanas.addTab("4. Avisos (Cocina / Mesero / Caja)", avisos);
        pestanas.addTab("5. Caja y promociones", construirTabCaja());
        pestanas.addChangeListener(e -> refrescarTodo());

        add(construirBarraSuperior(), BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBorder(new EmptyBorder(6, 12, 6, 12));
        barraEstado.setBackground(new Color(0xEFE6DC));
        barraEstado.add(lblEstado, BorderLayout.CENTER);
        add(barraEstado, BorderLayout.SOUTH);

        anchos(tablaMenu, 50, 230, 130, 90);
        anchos(tablaPedido, 330, 50, 90, 90);
        anchos(tablaPedidos, 50, 150, 80, 110, 150);
        anchos(tablaPromos, 220, 200, 140, 90);
        cargarMenu();
        refrescarTodo();
    }

    private JPanel construirBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(CAFE);
        barra.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel titulo = new JLabel("Cafetería · Sistema de Pedidos");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        barra.add(titulo, BorderLayout.WEST);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controles.setOpaque(false);
        JLabel lblRol = new JLabel("Rol activo:");
        lblRol.setForeground(Color.WHITE);
        controles.add(lblRol);
        controles.add(cmbRol);
        for (JCheckBox chk : new JCheckBox[]{chkEstudiante, chkSimular}) {
            chk.setOpaque(false);
            chk.setForeground(Color.WHITE);
            controles.add(chk);
            if (chk == chkSimular) {
                controles.add(cmbDia);
                controles.add(spnHora);
                JLabel h = new JLabel("h");
                h.setForeground(Color.WHITE);
                controles.add(h);
            }
        }
        cmbDia.setEnabled(false);
        spnHora.setEnabled(false);
        barra.add(controles, BorderLayout.EAST);

        cmbRol.setSelectedItem(ctrl.getRolActivo().getNombre());
        cmbRol.addActionListener(e -> {
            ctrl.setRol((String) cmbRol.getSelectedItem());
            estado("Rol activo: " + ctrl.getRolActivo().getNombre());
        });
        chkEstudiante.addActionListener(e -> {
            ctrl.setDescuentoEstudiante(chkEstudiante.isSelected());
            refrescarTodo();
        });
        chkSimular.addActionListener(e -> {
            cmbDia.setEnabled(chkSimular.isSelected());
            spnHora.setEnabled(chkSimular.isSelected());
            aplicarSimulacion();
        });
        cmbDia.addActionListener(e -> aplicarSimulacion());
        spnHora.addChangeListener(e -> aplicarSimulacion());
        return barra;
    }

    private JPanel construirTabPedido() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setBackground(FONDO);

        JPanel izq = new JPanel(new BorderLayout(0, 8));
        izq.setOpaque(false);
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setOpaque(false);
        filtro.add(new JLabel("Paso 1 - Elige un producto del menú.  Categoría:"));
        filtro.add(cmbCategoria);
        cmbCategoria.addActionListener(e -> cargarMenu());
        estilizar(tablaMenu);
        izq.add(filtro, BorderLayout.NORTH);
        izq.add(new JScrollPane(tablaMenu), BorderLayout.CENTER);

        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        opciones.setOpaque(false);
        opciones.add(new JLabel("Cantidad:"));
        opciones.add(spnCantidad);
        opciones.add(new JLabel("Extra leche (+" + Pedido.dinero(ExtraLeche.PRECIO) + " c/u):"));
        opciones.add(spnLeche);
        opciones.add(new JLabel("Extra queso (+" + Pedido.dinero(ExtraQueso.PRECIO) + " c/u):"));
        opciones.add(spnQueso);
        JPanel sur = new JPanel(new BorderLayout(0, 6));
        sur.setOpaque(false);
        sur.add(opciones, BorderLayout.NORTH);
        sur.add(boton("Paso 2 - AGREGAR AL PEDIDO", NARANJA, this::agregarProducto), BorderLayout.CENTER);
        izq.add(sur, BorderLayout.SOUTH);

        JPanel der = new JPanel(new BorderLayout(0, 8));
        der.setOpaque(false);
        lblPedidoTitulo.setFont(lblPedidoTitulo.getFont().deriveFont(Font.BOLD, 16f));
        estilizar(tablaPedido);
        der.add(lblPedidoTitulo, BorderLayout.NORTH);
        der.add(new JScrollPane(tablaPedido), BorderLayout.CENTER);

        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 22f));
        lblPromo.setForeground(new Color(0x1565C0));
        JPanel resumen = new JPanel(new GridLayout(0, 1, 0, 4));
        resumen.setOpaque(false);
        resumen.add(lblPromo);
        resumen.add(lblSubtotal);
        resumen.add(lblDescuento);
        resumen.add(lblTotal);
        JPanel filaBotones = new JPanel(new GridLayout(1, 3, 6, 0));
        filaBotones.setOpaque(false);
        filaBotones.add(boton("Cambiar cantidad", CAFE, this::cambiarCantidad));
        filaBotones.add(boton("Eliminar ítem", CAFE, this::eliminarItem));
        filaBotones.add(boton("Descartar pedido", ROJO, this::descartarPedido));
        resumen.add(filaBotones);
        resumen.add(boton("Paso 3 - CONFIRMAR PEDIDO", VERDE, this::confirmarPedido));
        der.add(resumen, BorderLayout.SOUTH);

        panel.add(izq);
        panel.add(der);
        return panel;
    }

    private JPanel construirTabCombo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBackground(FONDO);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;

        llenarCombo(cmbPlato, IProducto.PLATO, "-- elegir plato principal --");
        llenarCombo(cmbBebida, IProducto.BEBIDA, "-- elegir bebida --");
        llenarCombo(cmbAcomp, IProducto.ACOMPANAMIENTO, "-- sin acompañamiento --");
        llenarCombo(cmbPostre, IProducto.POSTRE, "-- sin postre --");
        for (JComboBox<String> cmb : java.util.List.of(cmbPlato, cmbBebida, cmbAcomp, cmbPostre)) {
            cmb.addActionListener(e -> actualizarPrecioCombo());
        }

        JLabel titulo = new JLabel("Arma tu combo paso a paso");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        int fila = 0;
        c.gridy = fila++;
        panel.add(titulo, c);
        c.gridy = fila++;
        panel.add(fila("Paso 1 - Plato principal (obligatorio)", cmbPlato), c);
        c.gridy = fila++;
        panel.add(fila("Paso 2 - Bebida (obligatoria)", cmbBebida), c);
        c.gridy = fila++;
        panel.add(fila("Paso 3 - Acompañamiento (opcional)", cmbAcomp), c);
        c.gridy = fila++;
        panel.add(fila("Paso 4 - Postre (opcional)", cmbPostre), c);
        c.gridy = fila++;
        panel.add(fila("Cantidad de combos", spnCantCombo), c);
        lblPrecioCombo.setFont(lblPrecioCombo.getFont().deriveFont(Font.BOLD, 18f));
        c.gridy = fila++;
        panel.add(lblPrecioCombo, c);
        c.gridy = fila++;
        panel.add(boton("AGREGAR COMBO AL PEDIDO", NARANJA, this::agregarCombo), c);
        c.gridy = fila;
        c.weighty = 1;
        panel.add(new JLabel(), c);
        return panel;
    }

    private JPanel construirTabPedidos() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setBackground(FONDO);

        estilizar(tablaPedidos);
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtDetalle.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane detalle = new JScrollPane(txtDetalle);
        detalle.setBorder(BorderFactory.createTitledBorder("Detalle del pedido seleccionado"));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tablaPedidos), detalle);
        split.setResizeWeight(0.55);
        panel.add(split, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        botones.setOpaque(false);
        botones.add(boton("Avanzar al siguiente estado", NARANJA, this::avanzarEstado));
        botones.add(new JLabel("   Pago:"));
        botones.add(cmbMetodo);
        botones.add(boton("Cobrar pedido ENTREGADO", VERDE, this::cobrarPedido));
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirTabCaja() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setBackground(FONDO);

        JPanel promos = new JPanel(new BorderLayout(0, 8));
        promos.setOpaque(false);
        promos.setBorder(BorderFactory.createTitledBorder("Promociones por día / franja horaria (solo rol Caja)"));
        estilizar(tablaPromos);
        promos.add(new JScrollPane(tablaPromos), BorderLayout.CENTER);
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        bp.setOpaque(false);
        bp.add(boton("Activar / Desactivar seleccionada", CAFE, this::alternarPromocion));
        bp.add(boton("Crear promoción...", NARANJA, this::crearPromocion));
        promos.add(bp, BorderLayout.SOUTH);

        JPanel cierre = new JPanel(new BorderLayout(0, 8));
        cierre.setOpaque(false);
        cierre.setBorder(BorderFactory.createTitledBorder("Cierre de caja (solo rol Caja)"));
        txtReporte.setEditable(false);
        txtReporte.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        txtReporte.setMargin(new Insets(8, 8, 8, 8));
        cierre.add(new JScrollPane(txtReporte), BorderLayout.CENTER);
        JPanel bc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bc.setOpaque(false);
        bc.add(boton("GENERAR CIERRE DE CAJA", VERDE, this::cierreDeCaja));
        cierre.add(bc, BorderLayout.SOUTH);

        panel.add(promos);
        panel.add(cierre);
        return panel;
    }

    private void agregarProducto() {
        int fila = tablaMenu.getSelectedRow();
        if (fila < 0) {
            advertir("Selecciona un producto de la tabla del menú.");
            return;
        }
        String id = (String) modeloMenu.getValueAt(fila, 0);
        ejecutar(() -> {
            ctrl.agregarProducto(id, (int) spnCantidad.getValue(), (int) spnLeche.getValue(), (int) spnQueso.getValue());
            estado("Producto agregado al pedido.");
            spnLeche.setValue(0);
            spnQueso.setValue(0);
        });
    }

    private void cambiarCantidad() {
        int fila = tablaPedido.getSelectedRow();
        if (fila < 0) {
            advertir("Selecciona un ítem del pedido.");
            return;
        }
        String texto = JOptionPane.showInputDialog(this, "Nueva cantidad:", "Cambiar cantidad", JOptionPane.QUESTION_MESSAGE);
        if (texto == null) {
            return;
        }
        ejecutar(() -> {
            try {
                ctrl.modificarCantidad(fila, Integer.parseInt(texto.trim()));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("La cantidad debe ser un número entero");
            }
            estado("Cantidad actualizada.");
        });
    }

    private void eliminarItem() {
        int fila = tablaPedido.getSelectedRow();
        if (fila < 0) {
            advertir("Selecciona un ítem del pedido.");
            return;
        }
        ejecutar(() -> {
            ctrl.eliminarItem(fila);
            estado("Ítem eliminado.");
        });
    }

    private void descartarPedido() {
        ejecutar(() -> {
            ctrl.descartarPedido();
            estado("Pedido descartado.");
        });
    }

    private void confirmarPedido() {
        ejecutar(() -> {
            Pedido p = ctrl.confirmarPedido();
            estado("Pedido #" + p.getIdPedido() + " confirmado. Total " + Pedido.dinero(p.getTotal()) + ". Revisa la pestaña 4 (Avisos).");
        });
    }

    private void agregarCombo() {
        ejecutar(() -> {
            ctrl.armarCombo(idDe(cmbPlato), idDe(cmbBebida), idDe(cmbAcomp), idDe(cmbPostre), (int) spnCantCombo.getValue());
            estado("Combo agregado al pedido.");
            for (JComboBox<String> cmb : java.util.List.of(cmbPlato, cmbBebida, cmbAcomp, cmbPostre)) {
                cmb.setSelectedIndex(0);
            }
            spnCantCombo.setValue(1);
        });
    }

    private void avanzarEstado() {
        ejecutar(() -> {
            Pedido p = ctrl.avanzarEstado(idPedidoSeleccionado());
            estado("Pedido #" + p.getIdPedido() + " ahora está " + p.getEstado());
        });
    }

    private void cobrarPedido() {
        ejecutar(() -> {
            Pedido p = ctrl.cobrarPedido(idPedidoSeleccionado(), (String) cmbMetodo.getSelectedItem());
            estado("Pedido #" + p.getIdPedido() + " cobrado (" + p.getMetodoPago() + ") y cerrado.");
        });
    }

    private void alternarPromocion() {
        int fila = tablaPromos.getSelectedRow();
        ejecutar(() -> {
            if (fila < 0) {
                throw new IllegalArgumentException("Selecciona una promoción de la tabla");
            }
            PromocionHorario p = ctrl.getPromociones().get(fila);
            ctrl.cambiarEstadoPromocion(fila, !p.isActiva());
            estado("Promoción " + (p.isActiva() ? "activada" : "desactivada") + ".");
        });
    }

    private void crearPromocion() {
        JTextField txtNombre = new JTextField("Nueva promo");
        JTextField txtPct = new JTextField("15");
        JComboBox<String> cmbDias = new JComboBox<>(new String[]{"Todos los días", "Lunes a viernes", "Sábado y domingo"});
        JSpinner ini = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner fin = new JSpinner(new SpinnerNumberModel(24, 1, 24, 1));
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Descuento (%):"));
        form.add(txtPct);
        form.add(new JLabel("Días:"));
        form.add(cmbDias);
        form.add(new JLabel("Hora inicio (0-23):"));
        form.add(ini);
        form.add(new JLabel("Hora fin (1-24):"));
        form.add(fin);
        if (JOptionPane.showConfirmDialog(this, form, "Crear promoción", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }
        ejecutar(() -> {
            double pct;
            try {
                pct = Double.parseDouble(txtPct.getText().trim().replace(',', '.'));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El porcentaje debe ser un número");
            }
            Set<DayOfWeek> dias = switch (cmbDias.getSelectedIndex()) {
                case 1 -> EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
                case 2 -> EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
                default -> EnumSet.allOf(DayOfWeek.class);
            };
            int horaFin = (int) fin.getValue();
            ctrl.crearPromocion(txtNombre.getText(), pct, dias, LocalTime.of((int) ini.getValue(), 0),
                    horaFin == 24 ? LocalTime.MAX : LocalTime.of(horaFin, 0));
            estado("Promoción creada.");
        });
    }

    private void cierreDeCaja() {
        ejecutar(() -> {
            txtReporte.setText(ctrl.cierreDeCaja().toString());
            estado("Cierre de caja generado.");
        });
    }

    private void aplicarSimulacion() {
        if (chkSimular.isSelected()) {
            DayOfWeek dia = DayOfWeek.of(cmbDia.getSelectedIndex() + 1);
            LocalDate fecha = LocalDate.now().with(TemporalAdjusters.nextOrSame(dia));
            ctrl.setFechaSimulada(LocalDateTime.of(fecha, LocalTime.of((int) spnHora.getValue(), 0)));
        } else {
            ctrl.setFechaSimulada(null);
        }
        refrescarTodo();
    }

    private void cargarMenu() {
        modeloMenu.setRowCount(0);
        for (IProducto p : ctrl.listarMenu((String) cmbCategoria.getSelectedItem())) {
            modeloMenu.addRow(new Object[]{p.getId(), p.getDescripcion(), p.getCategoria(), Pedido.dinero(p.getPrecio())});
        }
    }

    private void refrescarTodo() {
        ctrl.refrescarPedidoActual();
        refrescarPedidoActual();
        refrescarPedidos();
        refrescarPromos();
    }

    private void refrescarPedidoActual() {
        modeloPedido.setRowCount(0);
        Pedido p = ctrl.getPedidoActual();
        lblPedidoTitulo.setText(p == null ? "Pedido en curso: (vacío)" : "Pedido en curso #" + p.getIdPedido());
        if (p != null) {
            for (ItemPedido it : p.getItems()) {
                modeloPedido.addRow(new Object[]{it.getProducto().getDescripcion(), it.getCantidad(),
                    Pedido.dinero(it.getProducto().getPrecio()), Pedido.dinero(it.getSubtotal())});
            }
        }
        lblPromo.setText("Promoción aplicada: " + ctrl.getNombrePromocionAplicada());
        lblSubtotal.setText("Subtotal:  " + Pedido.dinero(p == null ? 0 : p.getSubtotal()));
        lblDescuento.setText("Descuento: -" + Pedido.dinero(p == null ? 0 : p.getDescuento()));
        lblTotal.setText("TOTAL A PAGAR:  " + Pedido.dinero(p == null ? 0 : p.getTotal()));
    }

    private void refrescarPedidos() {
        int seleccion = tablaPedidos.getSelectedRow();
        modeloPedidos.setRowCount(0);
        for (Pedido p : ctrl.listarPedidos()) {
            modeloPedidos.addRow(new Object[]{p.getIdPedido(), p.getEstado(), p.getCantidadTotalItems(),
                Pedido.dinero(p.getTotal()), p.isCerrado() ? "Cobrado (" + p.getMetodoPago() + ")" : "Sin cobrar"});
        }
        if (seleccion < 0 && modeloPedidos.getRowCount() > 0) {
            seleccion = modeloPedidos.getRowCount() - 1; // el pedido más reciente
        }
        if (seleccion >= 0 && seleccion < modeloPedidos.getRowCount()) {
            tablaPedidos.setRowSelectionInterval(seleccion, seleccion);
        }
        mostrarDetalle();
    }

    private void refrescarPromos() {
        int seleccion = tablaPromos.getSelectedRow();
        modeloPromos.setRowCount(0);
        for (PromocionHorario p : ctrl.getPromociones()) {
            modeloPromos.addRow(new Object[]{p.getNombre(), p.getDiasTexto(), p.getHorarioTexto(),
                p.isActiva() ? "ACTIVA" : "Inactiva"});
        }
        if (seleccion >= 0 && seleccion < modeloPromos.getRowCount()) {
            tablaPromos.setRowSelectionInterval(seleccion, seleccion);
        }
    }

    private void mostrarDetalle() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            txtDetalle.setText("Selecciona un pedido de la tabla para ver su detalle.");
            return;
        }
        Pedido p = ctrl.buscarPedido((int) modeloPedidos.getValueAt(fila, 0));
        StringBuilder sb = new StringBuilder("Pedido #" + p.getIdPedido() + "  -  " + p.getEstado() + "\n\n");
        for (ItemPedido it : p.getItems()) {
            sb.append(it.getCantidad()).append(" x ").append(it.getProducto().getDescripcion())
              .append("  =  ").append(Pedido.dinero(it.getSubtotal())).append("\n");
        }
        sb.append("\nSubtotal : ").append(Pedido.dinero(p.getSubtotal()))
          .append("\nDescuento: ").append(Pedido.dinero(p.getDescuento()))
          .append("\nTOTAL    : ").append(Pedido.dinero(p.getTotal()));
        txtDetalle.setText(sb.toString());
        txtDetalle.setCaretPosition(0);
    }

    private void actualizarPrecioCombo() {
        double total = 0;
        boolean alguno = false;
        for (JComboBox<String> cmb : java.util.List.of(cmbPlato, cmbBebida, cmbAcomp, cmbPostre)) {
            String id = idDe(cmb);
            if (id != null) {
                total += ctrl.buscarProducto(id).getPrecio();
                alguno = true;
            }
        }
        lblPrecioCombo.setText("Precio del combo: " + (alguno ? Pedido.dinero(total) : "-"));
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException ex) {
            advertir(ex.getMessage());
        } finally {
            refrescarTodo();
        }
    }

    private int idPedidoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            throw new IllegalStateException("Selecciona un pedido de la tabla");
        }
        return (int) modeloPedidos.getValueAt(fila, 0);
    }

    private void llenarCombo(JComboBox<String> cmb, String categoria, String placeholder) {
        cmb.addItem(placeholder);
        for (IProducto p : ctrl.listarMenu(categoria)) {
            cmb.addItem(p.getId() + " · " + p.getDescripcion() + " - " + Pedido.dinero(p.getPrecio()));
        }
    }

    private String idDe(JComboBox<String> cmb) {
        if (cmb.getSelectedIndex() <= 0) {
            return null;
        }
        String texto = (String) cmb.getSelectedItem();
        return texto.substring(0, texto.indexOf(" · "));
    }

    private JPanel fila(String texto, java.awt.Component componente) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(texto);
        l.setPreferredSize(new Dimension(300, 28));
        p.add(l, BorderLayout.WEST);
        p.add(componente, BorderLayout.CENTER);
        return p;
    }

    private void estado(String mensaje) {
        lblEstado.setText(mensaje);
    }

    private void advertir(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
        lblEstado.setText("AVISO: " + mensaje);
    }

    private JButton boton(String texto, Color fondo, Runnable accion) {
        JButton b = new JButton(texto);
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(9, 14, 9, 14));
        b.addActionListener(e -> accion.run());
        return b;
    }

    private static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void estilizar(JTable tabla) {
        tabla.setRowHeight(26);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionBackground(new Color(0xE8C9A0));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setBackground(new Color(0xEFE6DC));
        tabla.getTableHeader().setFont(tabla.getTableHeader().getFont().deriveFont(Font.BOLD));
    }

    private static void anchos(JTable tabla, int... valores) {
        for (int i = 0; i < valores.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(valores[i]);
        }
    }

    private void configurarEstiloGlobal() {
        Font base = new Font("SansSerif", Font.PLAIN, 13);
        for (Object clave : java.util.Collections.list(UIManager.getDefaults().keys())) {
            if (clave.toString().endsWith(".font")) {
                UIManager.put(clave, base);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignorada) {
                // si falla se usa el aspecto por defecto
            }
            BaseDatosMemoria db = new BaseDatosMemoria();
            new VentanaPrincipal(new ControladorPedidos(db, db)).setVisible(true);
        });
    }
}