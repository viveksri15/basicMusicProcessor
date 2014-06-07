package in.viveksrivastava.BPMFinder;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Vivek
 */
class DecibelCalc implements SoundProcessor<Short> {

	private long frequency = 44100;
	private long samples = 0;
	private int channels = 2;
	private double sum = 0d;

	private List<Double> dbList = new LinkedList<Double>();

	public void start(Short[] sample) {
		samples++;
		double d = sample[0] / 32768d;
		sum += d * d;
		if (samples >= frequency * channels) {
			double rms = Math.sqrt(sum / (samples));
			double decibel = 20 * Math.log10(rms / 0.00002d);
			if (Double.isInfinite(decibel))
				decibel = 0;
			dbList.add(decibel);
			samples = 0;
			sum = 0;
		}
	}

	public void init(int freq, int channels) {
		frequency = freq;
		this.channels = channels;
	}

	public List<Double> getDB() {
		return dbList;
	}
}
