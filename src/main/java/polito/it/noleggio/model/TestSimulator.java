package polito.it.noleggio.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TestSimulator {

	public static void main(String args[]) {
		int maxAuto = 25;
		int costoAuto = 10;
		double ciclo = 10000.0;

		for (int i = 0; i < maxAuto; i++) {
			int sommaNegativi = 0;
			int sommaGuadagno = 0;
			for (int j = 0; j < ciclo; j++) {

				Simulator sim = new Simulator();

				sim.setNumCars(i);
				sim.setClientFrequency(Duration.of(10, ChronoUnit.MINUTES));

				sim.run();

				int totClients = sim.getTotClients();
				int dissatisfied = sim.getDissatisfied();
				int guadagno = sim.getGuadagno();

				sommaNegativi += dissatisfied;
				sommaGuadagno += guadagno - (i * costoAuto);

				// System.out.format("Arrived %d clients, %d were dissatisfied\n", totClients,
				// dissatisfied);
			}
			System.out.println(String.format("%d auto - %f Insoddisfatti - %f guadagno", i, sommaNegativi / ciclo,
					sommaGuadagno / ciclo));
		}
	}

}
