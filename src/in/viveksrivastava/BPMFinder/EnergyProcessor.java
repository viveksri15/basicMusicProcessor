package in.viveksrivastava.BPMFinder;

import java.util.LinkedList;
import java.util.Queue;

import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author Vivek
 */
class EnergyProcessor extends OutputAudioDevice<Long, Short> {
	private int averageLength = 1024; // number of samples over which the average is calculated
	private Queue<Short> instantBuffer = new LinkedList<Short>();

	public EnergyProcessor(SoundProcessor<Long> processor,
                           SoundProcessor<Short> processor2) {
		super(processor, processor2);
	}

	@Override
	protected void outputImpl(short[] samples, int offs, int len)
			throws JavaLayerException {
		SoundProcessor<Short> processor2 = getProcessor2();
		for (int i = 0; i < len; i++) {
			instantBuffer.offer(samples[i]);
			processor2.start(new Short[]{samples[i]});
		}

		SoundProcessor<Long> processor = getProcessor1();
		while (instantBuffer.size() > averageLength * channels) {
			long energy = 0;
			for (int i = 0; i < averageLength * channels; i++)
				energy += Math.pow(instantBuffer.poll(), 2);
			if (processor != null)
				processor.start(new Long[]{energy});
		}
	}

	public int getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(int averageLength) {
		this.averageLength = averageLength;
	}
}
