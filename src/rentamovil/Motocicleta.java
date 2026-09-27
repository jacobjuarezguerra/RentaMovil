package rentamovil;

/** Motocicleta con un recargo unico cuando su cilindraje supera 250 cc. */
public final class Motocicleta extends Vehiculo {
    private static final int LIMITE_CILINDRAJE = 250;
    private static final double RECARGO_ALTO_CILINDRAJE = 75.0;

    private final int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cilindraje = validarEnteroPositivo(cilindraje, "El cilindraje");
    }

    @Override
    public double calcularCosto(int dias) {
        validarDias(dias);
        double recargo = cilindraje > LIMITE_CILINDRAJE ? RECARGO_ALTO_CILINDRAJE : 0.0;
        return costoBase(dias) + recargo;
    }

    @Override
    public CategoriaVehiculo getCategoria() {
        return CategoriaVehiculo.MOTOCICLETA;
    }

    @Override
    protected String detalleEspecifico() {
        return cilindraje + " cc";
    }

    public int getCilindraje() {
        return cilindraje;
    }
}

