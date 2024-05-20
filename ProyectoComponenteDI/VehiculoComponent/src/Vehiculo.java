import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Vehiculo extends JLabel implements Serializable {
    private String nombre; // Nombre del vehículo
    private int velocidad; // Velocidad del vehículo
    private String imagenPath; // Ruta de la imagen del vehículo
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this); // Inicializa el soporte para cambios de propiedad
    private int posicion;
    private boolean finalizado; // Flag para saber si el vehículo ha llegado a la meta
    private long tiempoInicio; // Tiempo en el que el vehículo inició la carrera
    private long tiempoLlegada; // Tiempo en el que el vehículo llegó a la meta

    public Vehiculo(String nombre) {
        super(); // Llama al constructor de JLabel primero
        this.nombre = nombre;
        this.velocidad = (int) (Math.random() * 15 + 12); // Velocidad aleatoria entre 12 y 27
        this.finalizado = false; // Inicializa el flag como false
        this.setIcon(new ImageIcon(getClass().getResource("/resources/" + nombre + ".png").getPath())); // Asigna la imagen
        this.tiempoInicio = 0; // Inicializar el tiempo de inicio como 0
        this.tiempoLlegada = 0; // Inicializar el tiempo de llegada como 0

    }

    public void iniciarTiempo() {
        // Método para iniciar el tiempo cuando el vehículo comienza la carrera
        this.tiempoInicio = System.currentTimeMillis();
    }

    public void registrarTiempo() {
        // Método para registrar el tiempo cuando el vehículo llega a la meta
        this.tiempoLlegada = System.currentTimeMillis();
    }

    public long obtenerTiempo() {
        // Método para obtener el tiempo total que tardó el vehículo en la carrera
        return tiempoLlegada - tiempoInicio;
    }
    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int nuevaVelocidad) {
        int viejaVelocidad = this.velocidad;
        this.velocidad = nuevaVelocidad;
        System.out.println("Cambio de velocidad en " + this.nombre + ": " + viejaVelocidad + " -> " + nuevaVelocidad);
        changeSupport.firePropertyChange("velocidad", viejaVelocidad, nuevaVelocidad);
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        String viejoImagenPath = this.imagenPath;
        this.imagenPath = imagenPath;
        System.out.println("Cambio de imagen en " + this.nombre + ": " + viejoImagenPath + " -> " + imagenPath);
        changeSupport.firePropertyChange("imagenPath", viejoImagenPath, imagenPath);
        this.setIcon(new ImageIcon(imagenPath)); // Actualiza la imagen del JLabel
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            System.err.println("Error: changeSupport is null when adding listener!");
        } else {
            this.changeSupport.addPropertyChangeListener(listener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            System.err.println("Error: changeSupport is null when removing listener!");
        } else {
            this.changeSupport.removePropertyChangeListener(listener);
        }
    }

    public void modificarVelocidadAleatoria() {
        int nuevaVelocidad = (int) (Math.random() * 11) + 10; // Genera un número aleatorio entre 10 y 20
        int viejaVelocidad = this.velocidad;
        this.velocidad = nuevaVelocidad;
        System.out.println("Cambio de velocidad en " + this.nombre + ": " + viejaVelocidad + " -> " + nuevaVelocidad);
        changeSupport.firePropertyChange("velocidad", viejaVelocidad, nuevaVelocidad);
    }
}
