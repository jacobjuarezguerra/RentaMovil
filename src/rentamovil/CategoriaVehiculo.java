package rentamovil;

/** Categorias de vehiculos disponibles en esta version del sistema. */
public enum CategoriaVehiculo {
    AUTOMOVIL("Automovil"),
    MOTOCICLETA("Motocicleta"),
    CAMION_CARGA("Camioneta de carga");

    private final String nombre;

    CategoriaVehiculo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

