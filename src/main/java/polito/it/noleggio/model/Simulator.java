package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;
import java.util.Random;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {

	// CODA EVENTI
	private PriorityQueue<Event> queue = new PriorityQueue<Event>();

	// PARAMETRI DI SIMULAZIONE (IMPOSTA VALORI DEFAULT)
	private int NC = 10; // numero macchine totali
	private Duration T_IN = Duration.of(10, ChronoUnit.MINUTES); // intervallo tra clienti
	private final LocalTime oraApertura = LocalTime.of(8, 00);
	private final LocalTime oraChiusura = LocalTime.of(17, 00);
	private Random r = new Random();

	// MODELLO DEL MONDO
	private int nAuto; // auto disponibili in deposito (0 -> NC)

	// VALORI DA CALCOLARE
	private int clienti;
	private int insoddisfatti;
	private int guadagno;

	// METODI PER IMPOSTARE I PARAMETRI

	public void setNumCars(int n) {
		this.NC = n;
	}

	public void setClientFrequency(Duration d) {
		this.T_IN = d;
	}

	// METODI PER RESTITUIRE I RISULTATI

	/**
	 * @return the clienti
	 */
	public int getTotClients() {
		return clienti;
	}

	/**
	 * @return the insoddisfatti
	 */
	public int getDissatisfied() {
		return insoddisfatti;
	}
	
	/**
	 * @return the guadagno
	 */
	public int getGuadagno() {
		return guadagno;
	}

	// SIMULAZIONE
	public void run() {
		// PREPARAZIONE INIZIALE (SETUP: MONDO + CODA EVENTI)
		this.nAuto = NC;
		this.clienti = 0;
		this.insoddisfatti = 0;
		this.queue.clear();

		LocalTime oraArrivoCliente = this.oraApertura;
		do {
			queue.add(new Event(oraArrivoCliente, EventType.NEW_CLIENT));
			oraArrivoCliente = oraArrivoCliente.plus(this.T_IN);
		} while (oraArrivoCliente.isBefore(this.oraChiusura));

		// ESECUZIONE DEL CICLO DI SIMULAZIONE
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			//System.out.println(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {
		case NEW_CLIENT:
			this.clienti++;

			if (this.nAuto > 0) {
				this.nAuto--;
				int h = (int) Math.round(r.nextDouble() * 2) + 1;
				guadagno += h * 10;
				queue.add(new Event(e.getTime().plus(Duration.of(h, ChronoUnit.HOURS)), EventType.CAR_RETURNED));
			} else {
				this.insoddisfatti++;
			}

			break;
		case CAR_RETURNED:
			this.nAuto++;
			break;
		}
	}

	public void randomOutput() {
		for (int i = 0; i < 20; i++) {
			int h = (int) Math.round(r.nextDouble() * 2) + 1;
			System.out.println(h);
		}
	}

}
