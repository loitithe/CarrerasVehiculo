import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private Vehiculo[] vehiculos;
    private JButton startButton;
    private JButton restartButton;
    private JButton chooseButton;
    private CarrilPanel centerPanel; // Cambiar JPanel a CarrilPanel
    private JPanel bottomPanel;
    private JPanel leftPanel;
    private JPanel topPanel;
    private int usuarioEleccion;
    private int numVehiculos = 6;
    private List<Vehiculo> ranking;
    private JLabel[] positionLabels; // Array para almacenar los JLabel de posición
    private JLabel rankingLabel;

    public Main() {
        this.setResizable(false);
        vehiculos = new Vehiculo[numVehiculos];
        ranking = new ArrayList<>();
        positionLabels = new JLabel[numVehiculos]; // Inicializar el array

        for (int i = 0; i < numVehiculos; i++) {
            vehiculos[i] = new Vehiculo("Coche" + (i + 1));
            vehiculos[i].addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("imagenPath".equals(evt.getPropertyName())) {
                        int index = Integer.parseInt(evt.getPropertyName().substring(6)) - 1;
                        vehiculos[index].setIcon(new ImageIcon(vehiculos[index].getImagenPath()));
                    }
                }
            });

            vehiculos[i].modificarVelocidadAleatoria();
        }

        setTitle("Juego de Carreras");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        centerPanel = new CarrilPanel();
        centerPanel.setLayout(null); // Usar layout nulo para controlar la posición de los JLabels
        add(centerPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        startButton = new JButton("Empezar");
        restartButton = new JButton("Reiniciar");
        chooseButton = new JButton("Elegir coche");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarCarrera();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarCarrera();
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elegirCoche();
            }
        });
        // Cambiar la fuente y el tamaño de texto de los botones
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        startButton.setFont(buttonFont);
        restartButton.setFont(buttonFont);
        chooseButton.setFont(buttonFont);

        startButton.setBackground(Color.BLUE);
        startButton.setForeground(Color.WHITE);
        restartButton.setBackground(Color.GREEN);
        restartButton.setForeground(Color.WHITE);
        chooseButton.setBackground(Color.ORANGE);
        chooseButton.setForeground(Color.WHITE);

        bottomPanel.add(startButton);
        bottomPanel.add(restartButton);
        bottomPanel.add(chooseButton);
        add(bottomPanel, BorderLayout.SOUTH);

        topPanel = new JPanel();

        rankingLabel = new JLabel("HAS APOSTADO POR EL : " + vehiculos[usuarioEleccion].getNombre());
        topPanel.add(rankingLabel);
        add(topPanel, BorderLayout.NORTH);
        Font labelFont = new Font("Arial", Font.BOLD, 10);
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(numVehiculos, 1));
        for (int i = 0; i < numVehiculos; i++) {
            positionLabels[i] = new JLabel(vehiculos[i].getNombre() + ": " + vehiculos[i].getPosicion() + " PUESTO");
            positionLabels[i].setFont(labelFont);
            positionLabels[i].setForeground(Color.WHITE);
            leftPanel.add(positionLabels[i]);
        }
        // Establecer el color de fondo del panel central y de la ventana principal
        centerPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setBackground(Color.GRAY);
        topPanel.setBackground(Color.WHITE);
        add(leftPanel, BorderLayout.WEST);
        elegirCoche();
        inicializarPosicionVehiculos();
    }

    private void elegirCoche() {
        // Crear un panel personalizado para el diálogo de selección de vehículo
        JPanel panel = new JPanel(new GridLayout(numVehiculos, 2));
        panel.setBackground(new Color(52, 73, 94)); // Establecer el color de fondo a azul oscuro

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[numVehiculos];

        // Crear y añadir elementos al panel personalizado
        for (int i = 0; i < numVehiculos; i++) {
            JLabel labelCoche = new JLabel("Coche " + (i + 1), vehiculos[i].getIcon(), JLabel.CENTER);
            labelCoche.setForeground(Color.WHITE); // Establecer el color del texto a blanco
            panel.add(labelCoche);

            radioButtons[i] = new JRadioButton();
            group.add(radioButtons[i]);
            panel.add(radioButtons[i]);
        }

        radioButtons[0].setSelected(true);

        // Crear un diálogo JOptionPane personalizado con el panel personalizado
        int result = JOptionPane.showConfirmDialog(this, panel, "APUESTA POR UN COCHE",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Obtener la selección del usuario
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < numVehiculos; i++) {
                if (radioButtons[i].isSelected()) {
                    usuarioEleccion = i;
                    break;
                }
            }
        } else {
            System.exit(0);
        }
    }

    /*
        Esta funcion le da un tamano a los carriles y centra los vehiculos en funcion del array de numVehiculos.
     */
    private void inicializarPosicionVehiculos() {
        int carrilAltura = centerPanel.getHeight() / numVehiculos; // Altura de cada carril basada en el tamaño actual del panel
        for (int i = 0; i < numVehiculos; i++) {
            // Centrar cada coche dentro de su carril
            int yPos = (i * carrilAltura) + (carrilAltura / 2 - 25);
            vehiculos[i].setBounds(50, yPos, 100, 50); // Ajustar la posición vertical
            centerPanel.add(vehiculos[i]); // Añadir los vehículos al panel
        }
        centerPanel.repaint(); // Asegurar que se repinte el panel después de añadir los vehículos
    }

    /**
     *
     */
    private void actualizarPosiciones() {
        List<Vehiculo> vehiculosPosiciones = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            vehiculosPosiciones.add(vehiculo);
        }
        // Ordenar los vehículos según su posición en la pantalla
        vehiculosPosiciones.sort((v1, v2) -> Integer.compare(v2.getBounds().x, v1.getBounds().x));

        // Actualizar los JLabel con las posiciones de los vehículos
        for (int i = 0; i < vehiculosPosiciones.size(); i++) {
            vehiculosPosiciones.get(i).setPosicion(i + 1); // Actualizar la posición del vehículo
            positionLabels[vehiculosPosiciones.get(i).getNombre().charAt(5) - '1'].setText(
                    vehiculosPosiciones.get(i).getNombre() + ": " + vehiculosPosiciones.get(i).getPosicion() + " PUESTO   " + vehiculosPosiciones.get(i).getVelocidad());
        }
    }

    private void iniciarCarrera() {
        reiniciarCarrera();
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean carreraTerminada = true;
                // Llamada al método modificarVelocidadAleatoria() para cada vehículo
                for (int i = 0; i < numVehiculos; i++) {
                    vehiculos[i].modificarVelocidadAleatoria();
                }
                for (int i = 0; i < numVehiculos; i++) {
                    if (!vehiculos[i].isFinalizado()) {
                        vehiculos[i].setBounds(vehiculos[i].getBounds().x + vehiculos[i].getVelocidad(),
                                vehiculos[i].getBounds().y, 100, 50);
                        if (vehiculos[i].getBounds().x >= getWidth() - 100) {
                            vehiculos[i].setFinalizado(true);
                            ranking.add(vehiculos[i]);
                            vehiculos[i].registrarTiempo(); // Registrar el tiempo al llegar a la meta

                        } else {
                            carreraTerminada = false;
                        }
                    }
                }

                actualizarPosiciones(); // Actualizar las posiciones de los coches en cada iteración

                if (carreraTerminada) {
                    ((Timer) e.getSource()).stop();
                    determinarGanador();
                    startButton.setEnabled(true);
                    chooseButton.setEnabled(true);
                }
            }
        });
        timer.start();
        startButton.setEnabled(false);
        chooseButton.setEnabled(false);
        for (int i = 0; i < numVehiculos; i++) {
            vehiculos[i].iniciarTiempo();
        }
    }

    private void reiniciarCarrera() {
        rankingLabel.setText("HAS APOSTADO POR EL : " + vehiculos[usuarioEleccion].getNombre());
        ranking.clear();
        int carrilAltura = centerPanel.getHeight() / numVehiculos; // Altura de cada carril basada en el tamaño actual del panel
        for (int i = 0; i < numVehiculos; i++) {
            // Centrar cada coche dentro de su carril
            int yPos = (i * carrilAltura) + (carrilAltura / 2 - 25);
            vehiculos[i].modificarVelocidadAleatoria();
            vehiculos[i].setBounds(50, yPos, 100, 50); // Ajustar la posición vertical
            vehiculos[i].setFinalizado(false);
        }
        centerPanel.repaint(); // Asegurar que se repinte el panel después de reiniciar
        reiniciarLabels();
    }

    private void reiniciarLabels() {
        for (int i = 0; i < numVehiculos; i++) {
            positionLabels[i].setText(vehiculos[i].getNombre() + ": " + 0 + " PUESTO");
        }
    }

    private void determinarGanador() {
        StringBuilder rankingStr = new StringBuilder();
        for (int i = 0; i < ranking.size(); i++) {
            Vehiculo vehiculo = ranking.get(i);
            long tiempo = vehiculo.obtenerTiempo(); // Obtener el tiempo del vehículo
            rankingStr.append(i + 1).append(". ").append(vehiculo.getNombre()).append(": ").append(tiempo).append(" ms\n");
            // Actualizar la etiqueta en el panel izquierdo con el tiempo del vehículo
            positionLabels[i].setText(vehiculo.getNombre() + ": " + vehiculo.getPosicion() + " PUESTO (" + tiempo + " ms)");
        }

        String mensaje;
        if (ranking.get(0).equals(vehiculos[usuarioEleccion])) {
            mensaje = "<html><font color='#4CAF50'>¡Felicidades! Acertaste sobre el coche ganador.</font></html>";
        } else {
            mensaje = "<html><font color='#F44336'>Lo siento, el coche ganador no fue el que elegiste.</font></html>";
        }

        // Crear un panel personalizado para el mensaje
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(52, 73, 94)); // Color de fondo azul oscuro

        // Agregar el ranking y el mensaje al panel
        JTextArea rankingArea = new JTextArea("Ranking final:\n" + rankingStr.toString());
        rankingArea.setEditable(false);
        rankingArea.setOpaque(false);
        rankingArea.setForeground(Color.WHITE);
        rankingArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(rankingArea, BorderLayout.CENTER);

        JLabel mensajeLabel = new JLabel(mensaje);
        panel.add(mensajeLabel, BorderLayout.SOUTH);

        // Mostrar el diálogo JOptionPane con el panel personalizado
        JOptionPane.showMessageDialog(this, panel, "Resultados de la Carrera", JOptionPane.PLAIN_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}

class CarrilPanel extends JPanel {
    private int numCarriles = 6;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int carrilAltura = getHeight() / numCarriles;

        for (int i = 0; i < numCarriles; i++) {
            // Dibujar el fondo del carril
            g.setColor(Color.GRAY);
            g.fillRect(0, i * carrilAltura, getWidth(), carrilAltura);

            // Dibujar líneas blancas del carril
            g.setColor(Color.WHITE);
            for (int j = 0; j < getWidth(); j += 40) {
                g.fillRect(j, i * carrilAltura + carrilAltura / 2 - 5, 20, 10);
            }
        }
    }
}
