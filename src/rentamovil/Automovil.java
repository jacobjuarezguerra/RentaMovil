package rentamovil;

/** Automovil de pasajeros, con recargo diario cuando es automatico. */
public final class Automovil extends Vehiculo {
    private static final double RECARGO_AUTOMATICO_DIARIO = 50.0;

    private final int cantidadPasajeros;
    private final boolean automatico;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria,
                     int cantidadPasajeros, boolean automatico) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cantidadPasajeros = validarEnteroPositivo(cantidadPasajeros, "La cantidad de pasajeros");
        this.automatico = automatico;
    }

    @Override
    public double calcularCosto(int dias) {
        validarDias(dias);
        double recargo = automatico ? RECARGO_AUTOMATICO_DIARIO * dias : 0.0;
        return costoBase(dias) + recargo;
    }

    @Override
    public CategoriaVehiculo getCategoria() {
        return CategoriaVehiculo.AUTOMOVIL;
    }

    @Override
    protected String detalleEspecifico() {
        return cantidadPasajeros + " pasajeros, transmision " + (automatico ? "automatica" : "manual");
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public boolean isAutomatico() {
        return automatico;
    }
}

