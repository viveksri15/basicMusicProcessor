package in.viveksrivastava.BPMFinder;

/**
 * @author Vivek
 */
interface SoundProcessor<T> {

	void start(T[] sample);

	public void init(int freq, int channels);

}