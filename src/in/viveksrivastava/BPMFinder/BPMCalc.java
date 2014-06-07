package in.viveksrivastava.BPMFinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Vivek
 */
class BPMCalc implements SoundProcessor<Long> {

	private Queue<Long> energyBuffer = new LinkedList<Long>();

	private int bufferLength = 43;

	private long sampleSize = 1024;
	private long frequency = 44100;
	private long samples = 0;
	private long beats = 0;

	private static int beatThreshold = 3;
	private int beatTriggers = 0;

	private List<Integer> bpmList = new LinkedList<Integer>();

	public void start(Long[] sample) {
		energyBuffer.offer(sample[0]);
		samples++;
		if (energyBuffer.size() > bufferLength) {
			energyBuffer.poll();
			double averageEnergy = 0;
			for (long l : energyBuffer)
				averageEnergy += l;
			averageEnergy /= bufferLength;

			double C = 1.3; //a * variance + b;
			boolean beat = sample[0] > C * averageEnergy;
			if (beat) {
				if (++beatTriggers == beatThreshold)
					beats++;
			} else {
				beatTriggers = 0;
			}

			if (samples >= bufferLength) {
				bpmList.add(getInstantBPM());
				beats = 0;
				samples = 0;
			}
		}
	}

	public void init(int freq, int channels) {
		frequency = freq;
	}

	public int getInstantBPM() {
		return (int) ((beats * frequency * 60) / (samples * sampleSize));
	}

	public List<Integer> getBPM() {
		return bpmList;
	}

	public long getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(long sampleSize) {
		this.sampleSize = sampleSize;
	}

}
