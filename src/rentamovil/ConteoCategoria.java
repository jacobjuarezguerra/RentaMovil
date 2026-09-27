package rentamovil;

/** Conteos inmutables de disponibilidad para una categoria. */
public final class ConteoCategoria {
    private final int registrados;
    private final int disponibles;
    private final int alquilados;

    public ConteoCategoria(int registrados, int disponibles, int alquilados) {
        this.registrados = registrados;
        this.disponibles = disponibles;
        this.alquilados = alquilados;
    }

    public int getRegistrados() {
        return registrados;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public int getAlquilados() {
        return alquilados;
    }
}

