package in.viveksrivastava.BPMFinder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDeviceBase;

/**
 *
 * @author Vivek Srivastava
 */
abstract class OutputAudioDevice<T1, T2> extends AudioDeviceBase {

	protected int position = 0;
	protected int freq;
	protected int channels;
	protected int samplesPerMillisecond;
	protected boolean init = false;
	protected SoundProcessor<T1> processor1;
	protected SoundProcessor<T2> processor2;

	public OutputAudioDevice(SoundProcessor<T1> processor,
			SoundProcessor<T2> processor2) {
		this.processor1 = processor;
		this.processor2 = processor2;
	}

	@Override
	protected void openImpl() throws JavaLayerException {
		super.openImpl();
	}

	@Override
	protected void writeImpl(short[] samples, int offs, int len)
			throws JavaLayerException {
		if (!init) {
			freq = getDecoder().getOutputFrequency();
			channels = getDecoder().getOutputChannels();
			samplesPerMillisecond = (freq * channels) / 1000;
			if (processor1 != null)
				processor1.init(freq, channels);
			init = true;
		}
		position += len / samplesPerMillisecond;

		outputImpl(samples, offs, len);
	}

	protected abstract void outputImpl(short[] samples, int offs, int len)
			throws JavaLayerException;

	/**
	 * Retrieves the current playback position in milliseconds.
	*/
	public int getPosition() {
		return position;
	}

	public SoundProcessor<T1> getProcessor1() {
		return processor1;
	}
	public SoundProcessor<T2> getProcessor2() {
		return processor2;
	}
}
